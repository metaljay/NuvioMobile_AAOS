# Nuvio AAOS fork: rules every agent must follow

> Fork-owned file. It does not exist in the parent project, so upstream merges never overwrite it. If reality changes, fix this file in the same change.

## 1. What this repo is

The owner's fork of [Nuvio](https://github.com/NuvioMedia/NuvioMobile), customised so the app is readable and usable on the owner's Polestar 3 Android Automotive OS (AAOS) display. It reaches the car only through Google Play **Internal testing**. The owner has no coding experience, so explain in plain English and never assume the owner can spot a code problem. The sister fork (Flow) follows identical docs.

AAOS is an Android OS built into the car that runs normal Android apps. **Android Auto (phone projection) is out of scope**; do not add Android Auto metadata.

## 1b. The two jobs

Every change to this fork is meant for the owner's car, so there are only two jobs, and both end in a release:
1. **Update from the parent** (`.github/skills/aaos-sync`): merge the parent's changes on a temporary `sync/` branch, re-apply the car customisations, build and test (the owner checks it in the Automotive emulator), merge into `main` only after the owner replies `approve sync`, then run the release stage.
2. **Tweak or fix** (`.github/skills/aaos-tweak`): make the change, check it builds, update the log and the customisation list, push to `main`, then run the release stage.
The release stage (`.github/skills/aaos-release`) always raises the version automatically, builds the bundle, and gives the owner the signing and upload steps. `.github/skills/aaos-uploaded` only records an upload and is optional.

## 2. Terminology (identical in both repos)

| Term | Meaning |
| --- | --- |
| upstream | The parent project (https://github.com/NuvioMedia/NuvioMobile). Read-only to us. Only `upstream/cmp-rewrite` is ever synced; ignore its other branches. |
| origin | The owner's GitHub fork (https://github.com/metaljay/NuvioMobile_AAOS). |
| `main` | Upstream code plus our customisations. The only long-lived branch; always buildable and releasable. |
| sync branch | Temporary `sync/upstream-<date>` branch used to review an upstream update before it reaches `main`. Deleted afterwards. |
| customisation | A deliberate change we keep (sections 5 and Part 2). |
| invariant | A customisation that must survive every upstream merge (section 5). |
| Play version code | The number Play Console requires to rise on every upload. Independent of upstream's version. See `AAOS_RELEASE.md`. |
| release bundle | The signed `.aab` file uploaded to Play. |
| debug build | Emulator-only. Never installed on the car. |

## 3. Golden rules

1. The only target is the Polestar 3 (AAOS), delivered via Play Internal testing.
2. `main` is the only long-lived branch. Upstream updates go through a `sync/` branch first (`AAOS_UPSTREAM_SYNC.md`).
3. Never push to upstream. We only control our fork. Parent changes flow in one way.
4. Port, don't replay: adapt each required behaviour into the current upstream code instead of cherry-picking old commits.
5. Keep edits to upstream-owned files as small as possible (fewer merge conflicts). Fork-only additions go in clearly separate places or files.
6. Every release build raises the version automatically (see `AAOS_RELEASE.md`); the parent's version numbers are ignored.
7. If a product decision is unclear (for example removing a customisation), ask the owner instead of guessing.

## 4. Permissions and safety rails

Agents may edit files, build, run tests, commit, push to `origin/main` once the work is verified, and create and delete `sync/` branches. Agents must **never**:

- force-push or rewrite history on `main`;
- push to upstream or merge anything towards it;
- commit, print, copy or paste `Key.jks`, any keystore, `local.properties` contents or any password;
- delete branches other than `sync/*`;
- change the `applicationId`, namespace or signing setup unless asked;
- say something is "verified" without stating exactly what was run and what was not (compiled / unit-tested / emulator / real car).

Commit with small, clear messages (`type(scope): description`). Stage files by name; do not use `git add -A`.

## 4b. Talking to the owner (the owner is not a coder)

The owner copy-pastes messages between chats and has little or no coding experience. Therefore:
1. Never tell the owner to run a command unless you give the complete command in a copy-paste code block with every value filled in. No placeholders such as <date> or <file>: look the value up yourself first.
2. Run commands yourself wherever you can. Ask the owner to act only for things only they can do: sign the bundle in Android Studio, upload in Play Console, change GitHub settings, or approve a decision.
3. Offer decisions as copy-paste replies, for example: reply `approve sync` or `cancel sync`. One decision at a time.
4. Explain results in plain English. Explain any jargon in one short phrase (for example: "merge means combining the parent's changes with ours").
5. End every task with a report in this order: What I did / What I checked (the exact commands and results) / What I did NOT check / What you need to do next (the exact message to paste, or "nothing").
6. If something fails, stop and say what failed in plain English. Give the owner one message to paste back to you or to another AI assistant. Do not attempt risky fixes.
7. Do not ask the owner to read raw code, diffs or logs; summarise them. Offer the raw output only if asked.

## 5. Invariants: must survive every upstream merge

1. **Play identity**: release `applicationId` `com.JF_Nuvio`; debug `com.JF_Nuvio.debug` (emulator only); namespace and Kotlin packages `com.JF_Nuvio` / `com.JF_Nuvio.android`.
2. **AAOS manifest and runtime**: automotive and portrait/landscape features optional; `distractionOptimized=true` on the application and all launcher activities; launcher activities resizable, `singleTask`, cutout-aware, PiP-capable with automotive config changes handled; Automotive detected at runtime and the platform initialised before UI.
3. **Device-link sign-in**: on Automotive, official-server device-link sign-in starts automatically and shows the phone-based `nuvio.tv/link` instruction (no browser on the car).
4. **Custom-server fallback**: the default Android/Play build offers "Connect to another server"; `AppFeaturePolicy.customServerConnectionsEnabled` is true in the `androidPlaystore` policy; `api.nuvio.tv` is not rejected by `ServerDiscoveryPolicy`; the trust screen is never bypassed; the built-in backend default is `https://api.nuvio.tv`; the auth-observer rebinding and saved-session fixes stay.
5. **AAOS readability (Automotive-gated)**: 1.15 minimum text scale and larger icon tokens; 40 dp bottom-nav icons; 185 x 278 dp default posters with 14 dp corners; 230 dp minimum home tile width (five full posters at 1280 dp); 64 dp search field; Discover capped at four columns; enlarged player, source-selector and details back/play controls. Phone and tablet sizing stays upstream.
5b. **Display safe area (Polestar 3)**: the car screen's rounded corners and bezel cover the outer edge of the app window, so anything tappable must sit well inside it. Keep every touch target (not just the icon) at least 16 dp from the left and right edges of the app window, and icons about 24-28 dp in; check new or moved top bars, side buttons and player controls against this. Same rule as Flow, where the top bar settings cog was only partly tappable at 4 dp from the edge (owner report 2026-10-05); Nuvio has not been audited against it yet.
6. **Playback defaults**: profiles with no saved value default to Reuse last link on and FIRST_STREAM autoplay; saved values always win.
7. **Playback lifecycle**: Android playback pauses when the app leaves the foreground, even if its now-playing media session is active. Picture-in-picture playback may continue; finishing the activity always pauses.
7b. **Car media card after the app is closed**: `NuvioCarMediaBrowserService` stays declared in `androidApp/src/main/AndroidManifest.xml` with the `android.media.browse.MediaBrowserService` intent filter, plus the `NuvioCarMediaArtworkProvider` provider; Do NOT add `androidx.car.app.launchable` to it: on the Polestar launcher (one icon per app) that makes the app icon open the car's media screen ("Continue watching") with no way into the app (owner report 2026-10-05). The player and that service share one `MediaSession` (`NuvioCarMediaSession`); the last item is saved to disk and restored as paused.
8. **Bundle packaging**: ABI splits are disabled when a bundle task is requested (`androidApp/build.gradle.kts`).
9. **Fork docs**: the README banner/contract and the `AAOS_*.md` files.

Details and file locations are in Part 2 below.

## 6. Deliberately NOT customised

- **Upstream store feature policy**: the Play Store build keeps upstream's disabled features, except custom-server connections (invariant 4). Do not delete policy files.
- **Phone/tablet sizing**: stays upstream. AAOS values are gated to Automotive (the shared details-screen buttons and search-field style noted in Part 2 are the exceptions).
- **Saved user preferences**: a saved poster width etc. always wins over our defaults.
- **MPVKit submodule pointer**: MPVKit stays at the upstream-published commit; a stale `d5cf091` checkout was reset to `bb1d0250` on 2026-10-02. The `libass-android` reference is dangling; upstream has the same dangling reference.
- **Android Auto**: out of scope.

## 7. README contract

`README.md` is part of the fork's identity. Every merge or refresh must keep: the `🚗 Android Automotive OS (AAOS) Fork` banner at the top; Nuvio branding and the link to the upstream project; the explanation that this is an AAOS fork for the Polestar 3; an accurate feature summary; the Google Play Internal testing path (no public GitHub downloads); links to `AAOS_FORK.md` and `AAOS_RELEASE.md`; a 'Keeping this fork up to date' section consistent with the AAOS_*.md files and .github/skills. Only use screenshots of this fork that exist and are accurate. On a README conflict, merge deliberately; do not accept either side blindly.

## 8. Keeping these docs current

After every verified change: add a dated entry to `AAOS_LOG.md` (what changed, commands run, what was and was not verified); update Part 2 if behaviour changed; update the release-state table in `AAOS_RELEASE.md` after any version bump or confirmed upload. The `AAOS_*.md` files are the memory that survives between agent chats; if it is not written here, the next agent will not know it. After any change or bug fix, run the `.github/skills/aaos-tweak` recipe, which makes all of these updates.

## 9. Environment notes (one Mac, Android Studio)

- Builds run on the owner's Mac. Both apps sit in the AAOS folder containing both repos; the release key `Key.jks` is in that folder, outside both repos.
- Signed bundles for upload go in the `For upload to Play Console` folder in the AAOS folder, next to both repos (outside git).
- Emulator used for checks: `Automotive_Large_Portrait` (1280 x 1606). It does not prove every Polestar 3 configuration. If `adb` or the emulator is unavailable, say so; never claim device verification from a compile.
- macOS Finder copies create stray duplicates named `* 2.kt`, `* 2.xml` or `* 2` folders (347 had to be removed once). They break builds. Never leave files with a ` 2` suffix; check for them after any copy/merge.
- Common iOS test sources compile, but the iOS simulator tests cannot run on this Mac (simulator SDK not installed).

---

# Part 2: Customisation inventory (detail)

## Required product behavior

### App, store, and platform identity

- Android release/Play application ID: `com.JF_Nuvio` (the existing Play listing to receive Internal testing updates).
- Android debug application ID: `com.JF_Nuvio.debug`; emulator-only, never install on or distribute to the Polestar 3.
- Android namespace and Kotlin packages: `com.JF_Nuvio` / `com.JF_Nuvio.android`.
- Android release signing uses the existing local `Key.jks` configuration. Do not replace the key or expose local passwords.
- Target vehicle: the user's Polestar 3 running Android Automotive OS (AAOS). Delivery path is a signed release AAB uploaded to the existing Play listing's Internal testing track, then installed from Play on the car; do not sideload the debug APK.
- The fork overrides the iOS app/debug and widget identifiers in `Config.xcconfig` and Xcode project settings. Those exact overrides are carried forward; the separate upstream identifiers in untouched Release configurations remain unchanged.

### Android Automotive integration

- Declare Automotive and portrait/landscape screen features as optional so phone/Play compatibility remains possible.
- Keep `distractionOptimized=true` metadata on the application and all launcher activities.
- Keep launcher/main activities resizable, `singleTask`, cutout-aware, PiP-capable, and configured for automotive keyboard, navigation, UI mode, density, and font changes.
- Detect Automotive at runtime and initialize the platform with the Android context before UI creation.
- On Automotive, automatically start official-server device-link sign-in and show the phone-based `nuvio.tv/link` instruction instead of opening a browser on the head unit. Keep the server menu available on the authentication screen so a user can switch to a compatible custom backend if official device-link login does not work.
- Preserve the upstream Play Store feature policy except for custom-server connections, which the user explicitly wants enabled in the default Android build to support that sign-in fallback.

### Device-code login and custom-server fallback

#### Required behavior

- The default Android build must expose **Connect to another server** on the authentication screen. This is needed when Android/the default backend path does not present or complete device-code login; the user enters `api.nuvio.tv`, reviews the discovered server, chooses **I trust this server**, and then device-code login must be available.
- Enable `AppFeaturePolicy.customServerConnectionsEnabled` in `composeApp/src/androidPlaystore/kotlin/com/JF_Nuvio/core/build/AppFeaturePolicy.android.kt`. Keep the existing custom server discovery, trust/review confirmation, persisted selection, server-switch reset, and TV-login capability gating; do not bypass the trust screen or force the server globally.
- The canonical official API (`https://api.nuvio.tv`) is intentionally allowed through server discovery so it can be explicitly reviewed and selected. `ServerDiscoveryPolicy.isOfficial` must not reject this canonical host, even if it is also the configured official backend. Retain rejection of a distinct configured backend URL as appropriate.
- `https://api.nuvio.tv` is also the built-in official backend default when `NUVIO_SUPABASE_URL` is unset. If the build has no publishable key, device-link sign-in resolves it at runtime from the canonical API discovery document and validates the returned backend before use.
- Regression coverage: `composeApp/src/commonTest/kotlin/com/JF_Nuvio/core/network/ServerDiscoveryPolicyTest.kt` asserts that the canonical API discovery URL is not rejected as already-official.

## UI behavior

### Typography and touch targets

- **Player sizing:** The fork makes player controls and labels substantially larger at wide layouts
  (including 768dp, 1024dp, and 1440dp breakpoints) and enlarges slider touch targets. It also
  enlarges header hit areas, progress pills, and player action icons/text. Preserve these changes
  while adapting values to the upstream player layout.
- **Shared AAOS readability:** Apply a 1.15 minimum font scale and larger shared icon tokens to
  Automotive screens without changing phone/tablet sizing.
- **Player-specific controls:** Enlarge player header, seek/skip, play/pause, action-pill, and
  timeline icons/touch targets. Use the dedicated side-icon metric for seek controls rather than
  the play-icon size, and keep trailer playback on the same responsive metrics.
- **Back controls:** Enlarge the player exit arrow beyond adjacent header icons. Use 44dp arrows in
  the Automotive source selector, player toolbar, and opening overlay.

### Home posters and browsing density

- **Defaults:** Restore 185×278dp default poster cards with 14dp corners, 16dp shelf spacing, and
  larger shelf/poster typography (`headlineSmall` shelf headings, `titleMedium` poster titles,
  `bodySmall` detail text). Upstream had reduced these to 126×189dp cards, 10dp spacing, and
  smaller text.
- **Automotive navigation:** Use 40dp home navigation icons and the larger vertical hit area on
  AAOS, including compact layouts. Keep upstream's 24/28dp compact/regular sizing off Automotive.
- **Home density:** Use a 230dp minimum width for AAOS home poster/catalog/folder tiles so a
  1280dp viewport fits five standard tiles instead of six or more. Apply this to poster-style
  Continue Watching cards and loading skeletons, not landscape cards, library/detail rails, global
  poster settings, or non-AAOS layouts.
- **Saved preferences:** Treat 230dp as a minimum and preserve a user's larger saved poster width.
  Keep loading placeholders at the content width to prevent a resize when content loads. Calculate
  landscape Continue Watching metrics from the user's configured poster width so home density
  does not distort those cards.
- **Regression coverage:** `HomePosterCardSizingTest.kt` covers five-versus-six cards at 1280dp
  and non-AAOS behavior.

### Search, details, and implementation map

- **Search and navigation:** Floating navigation icons/labels were increased in
  `core/ui/NavigationBar.kt`, `core/ui/jelly/JellyTabs.kt`, and
  `core/ui/FloatingNavigationBar.android.kt`. The search field is 64dp high with `titleMedium`
  text, and Discover is capped at four columns in `features/search/SearchScreen.kt`. On
  Automotive and tablet layouts, Search includes 68dp of top clearance above the search header so the field sits comfortably below the top floating navigation bar pill, with 30dp of clearance below the field and 64dp at the end of the list.
  Search-result and Discover spacing on non-Automotive layouts remain unchanged.
- **AAOS System Media Card / Now Playing Session:** `PlayerNowPlayingController.android.kt` retains
  the current media item's metadata (title, subtitle, artwork) and transitions `MediaSession` to
  `PlaybackState.STATE_PAUSED` when player views unbind (instead of clearing metadata to `null` and
  setting `STATE_NONE`). `MediaMetadata` publishes rich keys (`METADATA_KEY_ALBUM`, `METADATA_KEY_DISPLAY_DESCRIPTION`, artwork URIs) and allows background artwork loading to update metadata even when exiting the player, ensuring the Polestar 3 / Android Automotive OS system home screen Current-Media Card widget displays the last-watched item details, progress, artwork, and paused play button.
- **Car media card after the app is closed (2026-10-04):** AAOS only treats an app as a media
  source if it exposes a `MediaBrowserService`, and the home screen card reads the session token
  that service hands out. On the emulator's Google car launcher an app that also has a launcher activity
  must opt in with `androidx.car.app.launchable=true` on that service, otherwise the card shows the
  app name with no text. That opt-in was removed on 2026-10-05 because the Polestar launcher then
  opened the car's media screen instead of Nuvio; whether the Polestar card follows the service
  without it is to be confirmed on the car. `features/player/NuvioCarMediaBrowserService.android.kt` (declared in
  `androidApp/src/main/AndroidManifest.xml`) and `features/player/NuvioCarMediaSession.android.kt`
  provide one process-wide `MediaSession` that `PlayerNowPlayingController.android.kt` publishes
  to. The last title, subtitle, artwork URL, artwork image, position and duration are saved
  (shared preferences `nuvio_car_media_card` and `files/car_media_card_artwork.png`). When the car
  binds the service with no player running, the session is refilled from disk as `STATE_PAUSED`
  (never `STATE_NONE`, which hides the card). Video cannot play without the app and Android blocks
  a background app from opening itself, so pressing play while the app is closed shows the AAOS
  error-resolution prompt "Open Nuvio to continue watching" with an "Open Nuvio" button, then
  returns to paused after 15 s. The car's media screen shows a "Continue watching" tab with the
  last item. Artwork: AAOS only shows artwork from a local `content://` URI (Google "Display media
  artwork"), so the saved poster is served read-only by `NuvioCarMediaArtworkProvider`
  (authority `${applicationId}.carmediaart`) and the controller publishes that URI once the poster
  is saved. Side effect: the car app grid lists Nuvio twice (the app and its media entry).
- **Details actions:** Back controls are 48dp with 28dp arrows; play/resume actions are 56dp (60dp
  on tablets) with `titleMedium` text. See `features/details/MetaDetailsScreen.kt`,
  `features/details/components/DetailFloatingHeader.kt`, and
  `features/details/components/DetailActionButtons.kt`. These are shared changes, not
  Automotive-only overrides.
- **Home sizing files:** `HomePosterCardSizing.kt`, `HomePosterCard.kt`,
  `HomeCatalogSection.kt`, `HomeCollectionRowSection.kt`, `HomeContinueWatchingSection.kt`,
  `HomeScreen.kt`, and `HomeSkeletonLoading.kt`.
- **Navigation/player files:** 40dp AAOS bottom-navigation icons are in
  `core/ui/NavigationBar.kt`; player exit controls are in `features/player/PlayerControls.kt`;
  the 68dp source-selector back target with a 44dp arrow is in `features/streams/StreamsScreen.kt`;
  player toolbar/opening-overlay back arrows are in `PlayerControlActions.kt` and
  `OpeningOverlay.kt`. Keep these platform-gated so phone/tablet sizing remains upstream.
- **Saved-width compatibility:** Do not apply the fork's `>=160dp` reload cutoff; it conflicts
  with valid 104–140dp size presets. The larger fork default applies only when no saved preference
  exists.
- The historical custom commit contains other edits entangled with namespace moves. Port only
  behavior changes supported by clear intent; record unresolved product choices here.

## Appendix: iOS (not deployed, kept so merges do not break)

Only Android is deployed (to the Polestar 3). The fork overrides the iOS app, debug and widget identifiers in `Config.xcconfig` and the Xcode project settings; those exact overrides are carried forward, and the separate upstream identifiers in untouched Release configurations stay unchanged. `iosApp/Configuration/Version.xcconfig` is shared with Android (see AAOS_RELEASE.md).
