# Upstream sync: how to pull Nuvio parent updates safely

Do this only when the owner asks, or before a release that needs parent fixes. `main` is not touched until the sync branch is verified. Parent changes only flow in; nothing is ever pushed to upstream.

## Steps

0. **Safety.** `git status` must be clean (apart from known items in `AAOS_FORK.md` section 6). Then `git switch main && git pull origin main` and mark the last good state with `git tag pre-sync-$(date +%F)`.
1. **Find what is new.** `git fetch upstream`. The target is always `upstream/cmp-rewrite`. `git log --oneline main..upstream/cmp-rewrite`: if nothing is listed, stop. Note the full SHA with `git rev-parse upstream/cmp-rewrite`.
2. **Create the sync branch.** `git switch -c sync/upstream-$(date +%F) main`
3. **Merge.** `git merge upstream/cmp-rewrite`. Use a merge, not a rebase or reset, so our history stays intact.
4. **Resolve conflicts** using the hotspot table below. Rule: take upstream's new code, then re-apply our customisation on top. Never choose "ours" or "theirs" wholesale on a hotspot file. Also review hotspot files that merged *without* conflict, because upstream can change behaviour near our changes silently.
5. **Restore fork-owned values** (list below).
6. **Verify.** Run the checks below, walk the invariants in `AAOS_FORK.md` section 5, and inspect the merged **release** manifest (application ID, version, min/target SDK, automotive and camera features, every launcher activity/alias, MediaBrowser entry). Use the emulator if available.
7. **Land it.** `git switch main && git merge --ff-only sync/upstream-<date>`. If that refuses because `main` moved, merge `main` into the sync branch, re-verify, retry. Then `git push origin main`.
8. **Clean up.** `git branch -d sync/upstream-<date>`; if it was pushed, `git push origin --delete sync/upstream-<date>`. Never leave sync branches behind.
9. **Record.** Add an `AAOS_LOG.md` entry (upstream SHA, conflicts, verification, limits). If releasing, continue with `AAOS_RELEASE.md`.

## Abort or roll back

- Before step 7: `git switch main && git branch -D sync/upstream-<date>`. `main` was never touched.
- After step 7: create a revert commit (`git revert -m 1 <merge commit>`). Do not reset or force-push. The `pre-sync-<date>` tag marks the last good state.

**Nuvio-only checks during the merge**
- Re-apply the `com.nuvio.app` to `com.JF_Nuvio` rename to every new or changed upstream file. Do not do a blind search-and-replace; the Part 2 notes list store-config exceptions.
- Check for stray ` 2` duplicate files (see AAOS_FORK.md section 9).
- Leave the `MPVKit` pointer alone unless the build needs it.

## Restore after every merge

- The Play version code/name in `iosApp/Configuration/Version.xcconfig` (`CURRENT_PROJECT_VERSION` is the Android version code, `MARKETING_VERSION` the name; Android reads this file) must be **above the last uploaded code** in `AAOS_RELEASE.md`. Upstream's value is irrelevant; never keep a lower number.
- Release `applicationId` is exactly `com.JF_Nuvio`.
- Package namespace `com.JF_Nuvio` everywhere; iOS identifier overrides.
- `AppFeaturePolicy.customServerConnectionsEnabled` is true for the Play Store policy.
- README banner/contract and the AGENTS AAOS block are intact.

## Checks to run

```bash
./gradlew :composeApp:compileAndroidMain
./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=playstore
./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=full
git diff --check
```
Run the focused tests for the area touched (for example `HomePosterCardSizingTest`, `ServerDiscoveryPolicyTest`).

## Hotspots: files where our changes live

| File / area | What we changed | On conflict |
| --- | --- | --- |
| All Kotlin packages | upstream `com.nuvio.app` renamed to `com.JF_Nuvio` | Re-apply the rename to new/changed upstream files; respect the store-config exceptions in Part 2 |
| `androidApp/build.gradle.kts` | applicationId, signing properties, ABI-split workaround | Keep ours for those; take upstream for the rest |
| `iosApp/Configuration/Version.xcconfig` | Play version code/name | After the merge set the code above the last uploaded one (AAOS_RELEASE.md) |
| iOS `Config.xcconfig` and Xcode project | fork's iOS identifier overrides | Keep the overrides (Appendix) |
| Android manifest(s) | AAOS flags, activity settings | Keep every AAOS entry |
| `composeApp/src/androidPlaystore/.../AppFeaturePolicy.android.kt` | custom server connections enabled | Keep enabled |
| `core/network/ServerDiscoveryPolicy*`, auth / official-config code | `api.nuvio.tv` handling, session fixes | Keep; re-run `ServerDiscoveryPolicyTest` |
| `core/ui/NavigationBar.kt`, `jelly/JellyTabs.kt`, `FloatingNavigationBar.android.kt` | larger nav icons/labels | Re-apply Automotive-gated sizes |
| `Home*` files (`HomePosterCardSizing.kt`, `HomePosterCard.kt`, `HomeCatalogSection.kt`, ...) | poster density and typography | Re-apply; re-run `HomePosterCardSizingTest` |
| `features/search/SearchScreen.kt`, details, `features/player/*`, `features/streams/StreamsScreen.kt` | larger controls and spacing | Re-apply Automotive-gated sizes |
| `README.md`, `AGENTS.md` | fork banner/contract; AGENTS file | Keep ours |
