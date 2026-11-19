#!/usr/bin/env bash
# AAOS fork-owned (C13). One command from the Mac to Play internal testing:
# tests -> signed release bundle -> upload and release.
#   aaos/release.sh             full run
#   aaos/release.sh --check     sign in to Play and look, change nothing (skips the build)
#   aaos/release.sh --dry-run   build, upload to a throwaway edit, validate, discard (releases nothing)
# It never raises the version: that is a separate committed step (AAOS_RELEASE.md, "The version rule").
set -euo pipefail
cd "$(dirname "$0")/.."
START=$SECONDS
MODE=""
case "${1:-}" in
  "") ;;
  --check|--dry-run) MODE="$1" ;;
  *) echo "Unknown option: $1 (use --check or --dry-run)"; exit 2 ;;
esac
die() { echo "Stopped: $*" >&2; exit 1; }
trap 'echo "Elapsed: $((SECONDS - START))s"' EXIT

APP_NAME="Nuvio"
APP_ID="com.JF_Nuvio"
KEYCHAIN_SERVICE="nuvio-release"
VERSION_FILE="iosApp/Configuration/Version.xcconfig"

if [[ "$MODE" == "--check" ]]; then
  node aaos/play-upload.mjs --check
  exit 0
fi

# What goes up must be a commit.
if [[ -n "$(git status --porcelain --untracked-files=no)" ]]; then
  git status --short --untracked-files=no
  die "there are uncommitted changes. Commit them first so the upload matches a commit."
fi
echo "Commit: $(git log -1 --format='%h %s')"

for a in store-file key-alias store-password key-password; do
  security find-generic-password -s "$KEYCHAIN_SERVICE" -a "$a" >/dev/null 2>&1 || die "signing details not in the Keychain. Run aaos/setup-release.sh."
done

export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
[[ -x "$JAVA_HOME/bin/java" ]] || die "Android Studio's Java not found at $JAVA_HOME."
export PATH="$JAVA_HOME/bin:$PATH"

VERSION_CODE="$(sed -n 's/^[[:space:]]*CURRENT_PROJECT_VERSION[[:space:]]*=[[:space:]]*\([0-9][0-9]*\).*/\1/p' "$VERSION_FILE" | head -1)"
VERSION_NAME="$(sed -n 's/^[[:space:]]*MARKETING_VERSION[[:space:]]*=[[:space:]]*\([^[:space:]]*\).*/\1/p' "$VERSION_FILE" | head -1)"
[[ -n "$VERSION_CODE" ]] || die "couldn't read CURRENT_PROJECT_VERSION from $VERSION_FILE."
[[ -n "$VERSION_NAME" ]] || die "couldn't read MARKETING_VERSION from $VERSION_FILE."
echo "Releasing $APP_NAME $VERSION_NAME (version code $VERSION_CODE)"

# The fork's own tests only: the host tests named in aaos/customisations.txt. (The parent's full suite has a
# known failing test, so it is not used as a gate.)
TEST_ARGS=()
while IFS= read -r cls; do
  [[ -n "$cls" ]] && TEST_ARGS+=(--tests "*$cls")
done < <(grep -v -E '^[[:space:]]*(#|$)' aaos/customisations.txt | awk '{print $2}' | grep -E '/(androidHostTest|commonTest)/.*Test\.kt$' | sed -E 's#.*/([^/]+)\.kt$#\1#' | sort -u)
[[ ${#TEST_ARGS[@]} -gt 0 ]] || die "found no tests in aaos/customisations.txt."
./gradlew :composeApp:testAndroidHostTest "${TEST_ARGS[@]}"

# Every fork change must be labelled with a customisation.
REPORT="$(aaos/report.sh)"
if grep -q "NOT LABELLED" <<<"$REPORT"; then
  grep -A20 "NOT LABELLED" <<<"$REPORT"
  die "some changed files are not labelled in aaos/customisations.txt."
fi
echo "Every fork file is labelled."

./gradlew :androidApp:bundlePlaystoreRelease

AAB_DIR="androidApp/build/outputs/bundle/playstoreRelease"
AAB="$(ls -t "$AAB_DIR"/*.aab 2>/dev/null | head -1 || true)"
[[ -n "$AAB" && -f "$AAB" ]] || die "the bundle wasn't built (looked in $AAB_DIR)."

# The Keychain password is only needed for building; verifying a signature needs none.
# jarsigner exits 0 even for an unsigned jar, so read what it says.
VERIFY="$(jarsigner -verify "$AAB" 2>&1 || true)"
if grep -qi "jar is unsigned" <<<"$VERIFY" || ! grep -q "jar verified" <<<"$VERIFY"; then
  die "the bundle is not signed. Check the Keychain items (aaos/setup-release.sh)."
fi
echo "Bundle is signed."

# Check the bundle's application ID (read from its manifest). The version code is checked by play-upload.mjs:
# it stops, releasing nothing, if Play reports a different code from the one above.
MANIFEST_TEXT="$(unzip -p "$AAB" base/manifest/AndroidManifest.xml | strings || true)"
grep -q "$APP_ID" <<<"$MANIFEST_TEXT" || die "the bundle's application ID is not $APP_ID."
grep -q "$APP_ID\.debug" <<<"$MANIFEST_TEXT" && die "this is a debug bundle ($APP_ID.debug)."
echo "Bundle application ID is $APP_ID."

node aaos/play-upload.mjs ${MODE:+"$MODE"} --bundle "$AAB" --version-code "$VERSION_CODE"

# After a real release only: keep the file as the owner's folder expects, and mark the commit (never pushed from here).
if [[ -z "$MODE" ]]; then
  PLAY_DIR=""
  for d in "../../Play Console" "../Play Console"; do [[ -d "$d" ]] && { PLAY_DIR="$d/$APP_NAME"; break; }; done
  if [[ -n "$PLAY_DIR" ]]; then
    mkdir -p "$PLAY_DIR"
    NAME="$APP_NAME-v$VERSION_NAME-build$VERSION_CODE"
    cp "$AAB" "$PLAY_DIR/$NAME.aab"
    MAPPING="androidApp/build/outputs/mapping/playstoreRelease/mapping.txt"
    [[ -f "$MAPPING" ]] && cp "$MAPPING" "$PLAY_DIR/$NAME-mapping.txt"
    echo "Saved $NAME.aab in the Play Console folder."
  else
    echo "Note: the Play Console folder was not found, so the bundle was not copied (it is in $AAB_DIR)."
  fi
  git tag "play-$VERSION_CODE" 2>/dev/null && echo "Tagged play-$VERSION_CODE (not pushed)." || echo "Note: tag play-$VERSION_CODE already exists; left alone."
fi
