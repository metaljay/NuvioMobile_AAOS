# Nuvio AAOS log

Newest entry first. One entry per verified change, upstream sync or upload. Format:

```
## YYYY-MM-DD: short title
- What changed and why
- Commands run and results
- Verified: compile / unit tests / emulator / real car (state which; list anything NOT verified)
```

Agents read this file only when they need evidence. Rules live in `AAOS_FORK.md`.

## 2026-10-02: Docs anonymised: removed personal identifiers from fork-owned docs.

## 2026-10-02: Search spacing compile fix

- Commit `61cf0ec1` (search results spacing for AAOS, also bumped the version to 142 / 0.5.9) introduced a syntax error in `SearchScreen.kt`; commit `a1cfd08c` fixed it by moving the 16 dp bottom spacer outside the `when` expression. The 16 dp gap now applies in all search states. Version 142 has not been uploaded to Play.
- Verified: `./gradlew --stop`; `./gradlew --status` (no Gradle daemons were running); `./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=playstore` (passed). Emulator and real-car behaviour NOT verified.

## 2026-10-02: Reset stale MPVKit checkout

- Android build search found no MPVKit use in `androidApp/`, `composeApp/src/androidMain`, `composeApp/src/androidPlaystore`, or any `build.gradle.kts` / `settings.gradle.kts` file.
- MPVKit stays at the upstream-published commit; a stale `d5cf091` checkout was reset to `bb1d0250` on 2026-10-02.
- The first `:composeApp:compileAndroidMain` attempt exposed an existing Kotlin syntax error in `SearchScreen.kt`; moved the active-search trailing spacer outside the `when` expression. The rerun succeeded.
- Verified: `git submodule update --init MPVKit`; `./gradlew :composeApp:compileAndroidMain` succeeded. No emulator or vehicle verification.

## 2026-10-02: Docs restructure (no app code changed)

- Replaced `CUSTOM_FEATURES_MIGRATION.md` with `AAOS_FORK.md`, `AAOS_UPSTREAM_SYNC.md`, `AAOS_RELEASE.md` and this log, identical in both fork repos.
- Owner-confirmed from Play Console: last uploaded 141 (0.5.8); the repo is at 142 (0.5.9) for the next upload.
- Owner-confirmed: the app runs on the Polestar 3 from Play Internal testing. Not re-verified by an agent.
- Branch policy: `main` only, with temporary `sync/upstream-<date>` branches for upstream updates.
- The superseded cmp-rewrite branch (one old README commit) was deleted on 2026-10-03; main is the only branch.
- Finding: `libass-android` is a dangling submodule reference that upstream has too; harmless.

## Archived history

## Historical refs and source points — verify before use

- The refs and versions below describe the recorded migration checkpoint; they are not authoritative
  for the current checkout. Check live refs and version configuration before acting on them.
- Custom reference branch: `my-custom-features` at `5ec5890760127394f755f50cc9febbbde5d59321` (left intact).
- Fork commit's parent/common base: `68337ffac8578b986d0c3f6e432abf75f4a33521`.
- Upstream remote: `https://github.com/NuvioMedia/NuvioMobile`.
- Fetched upstream release branch: `upstream/cmp-rewrite` at `d667f432` (0.5.5).
- Migration branch: `migration/upstream-0.5.5`, based on that upstream tip.
- The custom branch contains one broad commit, mostly a `com.nuvio.app` to `com.JF_Nuvio` namespace migration across Kotlin source sets, plus product and AAOS changes. Reapply the namespace mechanically to upstream files while retaining upstream code and package-specific store configuration.

## Emulator and release validation history (2026-10-01)

**Initial server fallback**

- The live `https://api.nuvio.tv/.well-known/nuvio` document was checked on 2026-10-01 and reported service `nuvio`, version 1, `self_hosted: true`, backend `https://api.nuvio.tv`, and both `email_password_auth` and `tv_login` capabilities enabled. Do not record or copy its publishable key into notes.
- Validation on 2026-10-01: the resulting Play Store debug APK was installed on the freshly wiped `Automotive_Large_Portrait` AVD and launched. This was an emulator-only debug install, not a Play release install.
- On that clean emulator, automatic official-server device-link sign-in initially failed because the built app attempted `https://localhost/rest/v1/rpc/start_device_login_session`. The authentication menu exposed the custom-server connection option; entering `api.nuvio.tv` successfully discovered `https://api.nuvio.tv`, displayed the expected trust/review dialog, and accepting it generated a device code and reached “Waiting for approval”. The code itself is intentionally not recorded.
- Follow-up default-server fix: when `NUVIO_SUPABASE_URL` is not configured, generated runtime config and `officialConfiguration()` now default to `https://api.nuvio.tv` rather than an empty URL (which Supabase interpreted as localhost). If the publishable key is absent, device-link sign-in loads it from the canonical server's discovery document and verifies the returned backend before using it. The emulator's next direct request targeted `https://api.nuvio.tv/rest/v1/rpc/start_device_login_session`, confirming the default URL is fixed; code generation could not be reverified on that boot because emulator networking was unavailable (`Network is unreachable` / DNS failure). The prior manual trusted-server test successfully generated a code.
- Fresh-launch recheck (2026-10-01): Android compilation and Play Store debug APK assembly succeeded. After the `Automotive_Large_Portrait` emulator resumed an old playback session, the Play Store debug app's data was cleared for emulator user 10 and the app was launched again. The login screen then automatically generated a device code and showed “Waiting for approval” without manually adding a server. The code is not recorded. This confirms automatic device-link generation through the default `api.nuvio.tv` configuration when emulator networking is available.

**Approved login and returning sessions**

- Approved-login hang investigation (2026-10-01): installed the locally signed Play Store release APK on the Automotive emulator (`com.JF_Nuvio`, version code 139 / version 0.5.6), confirming the issue was not exclusive to the debug-suffixed package. Release logcat showed an initial avatar catalog request before official API-key discovery. Resolving the discovered official key resets the cached Supabase client, but `AuthRepository` had still been observing the old client's session flow. The device-link exchange could import a session into the new client while the UI remained on “Signing in”. Fix: reinitialize the auth observer after resolving official configuration, wait for authenticated app state after importing the session, and bound session exchange/auth validation so transient network delays cannot leave an indefinite spinner. Verified with the signed release: logcat recorded approval, exchange, and completed sign-in, and the emulator reached the authenticated home screen.
- Returning-session follow-up (2026-10-01): after relaunching that release install, the Supabase client loaded the saved session before discovering its missing API key; the resulting “No API key found in request” 401 was misclassified as a revoked account and cleared the session. Fix: resolve official configuration before `AuthRepository` attaches to the session flow, and do not invalidate a saved session solely because its verification request lacked an API key. Verified the rebuilt signed release retained the approved session through app relaunch/update and opened the authenticated home screen. No release APK has been uploaded to Play.

**Automotive layout checks**

- AAOS readability visual check (2026-10-01): installed the latest Play Store debug APK on the Automotive emulator, cleared only its debug-app data, and confirmed the app opened without a crash, displayed larger sign-in text, and generated a device code with “Waiting for approval”.
- AAOS home/player navigation sizing (2026-10-01): set Automotive bottom navigation icons to 40dp and enlarged player/source-screen back buttons and arrows. Set home catalog/folder and poster-style continue-watching cards to a 230dp minimum; added a regression test verifying five cards fit and six do not in a 1280dp viewport. A sizing follow-up ensures this home width does not alter landscape Continue Watching card proportions. The signed release APK was installed and launched on the Automotive emulator; visual capture showed five full catalog posters with the next card peeking into view. Android host tests, compile, signed release assembly, and `git diff --check` passed. This was emulator-only validation: nothing has been uploaded to Play or installed on the Polestar.

## Build and source cleanup

- Duplicate-source cleanup (2026-10-01): the default agent identified and removed 347 duplicate `* 2.kt` files, duplicate `* 2` directories, and duplicate release mapping outputs. They are now absent from the source sets. A normal build no longer needs temporary source-file moves; this session independently verified `:composeApp:compileAndroidMain`, `:androidApp:assembleDebug -Pnuvio.android.distribution=full`, and `:androidApp:assembleDebug -Pnuvio.android.distribution=playstore` all succeed.
- AAOS readability restoration: reset poster defaults to the fork's 185×278dp with 14dp corners; restored 16dp shelf spacing and larger poster/shelf text; restored 32dp navigation icons and full vertical padding on Automotive, including compact layouts. Preserved upstream compact/regular navigation sizing and valid saved poster-width choices on non-automotive platforms. Verified `:composeApp:compileAndroidMain` and Play Store debug assembly succeeded.
- Broader AAOS readability pass: added a 1.15 minimum text scale and larger shared icon tokens for Automotive; enlarged player header/control icons, action pills, seek hit areas, and timeline thickness. Fixed seek buttons using the play-icon measurement instead of their dedicated side-icon measurement. Kept all these overrides gated to Automotive. Added a metric regression test; Android compilation and Play Store debug assembly pass.
- Search/navigation/details readability pass (2026-10-01): enlarged floating navigation icons and labels, increased the search field height and text size, capped Discover at four poster tiles per row, and enlarged the details back button plus play/resume action. The shared search input accepts an optional text style so other screens retain their existing sizing. Verified with `:composeApp:compileAndroidMain`; this was compile validation only, not an emulator visual check.
- Search results spacing and upload version (2026-10-02): added 8dp of additional spacing under the search field and a 16dp trailing spacer for active queries, giving the result list clearer separation from the field and floating bottom navigation without changing Discover layout. Bumped the fork's Play upload candidate from code 141 / name 0.5.8 to code 142 / name 0.5.9. `git diff --check` passed. Android compilation remains unverified: the normal compile failed on generated `ic_launcher_background 2.xml` resource-name validation, and retrying with that validation task excluded did not complete within the available run.
- Resume/source-selection behavior check (2026-10-01): a resume action passes its saved position into playback, but opens stream/source selection when autoplay is in `MANUAL` mode and **Reuse last link** is disabled. Those were the old defaults; they are now enabled for profiles without saved values. Existing saved per-profile values still win, so a profile that already stored `MANUAL`/disabled may continue to show source selection until those settings are changed.
- Playback defaults and upload version (2026-10-01): profiles without saved values enable **Reuse last link** and use `FIRST_STREAM` autoplay across platforms. These are defaults, not forced overrides: any saved per-profile values (including an older `false`/`MANUAL`) continue to take precedence and must be changed in Playback settings if the user wants to override them. This also means the default stream selector can still appear when no reusable cached link exists and autoplay cannot select a playable stream. At that time, the Android upload candidate was code 141 / name 0.5.8; it was superseded by 142 / 0.5.9 on 2026-10-02. `:composeApp:compileAndroidMain` and all 14 focused autoplay-policy tests passed with these defaults. The Play Console maximum still needs confirming before upload.
- Common test sources compile with `:composeApp:compileTestKotlinIosSimulatorArm64`. Running the iOS simulator test task itself remains unavailable because the required Xcode simulator SDK is not installed.
- The debug app was installed only on the Automotive emulator for these checks. It is not the release artifact and must not be installed on the Polestar 3.

## Historical release version and distribution state

- Android version code and name are sourced from `iosApp/Configuration/Version.xcconfig`.
- Fork reference version: code 131 / name 0.4.15.
- Upstream 0.5.5 uses code 137 / name 0.5.5, but that code belongs to upstream's different Android application ID and does not establish the highest code used by the fork's Play listing.
- User-confirmed Play Store version for `com.JF_Nuvio`: code 139 / name 0.5.6. Current fork upload candidate: code 142 / name 0.5.9, greater than the confirmed live code. Recheck Play Console before each upload in case a newer release was submitted. Code 138 was rejected before packaging because of the bundle split conflict below; no artifact was produced at 138.

## Decisions and open questions

- Store distribution feature policy: preserve upstream restrictions by default because the Play Store flavor intentionally disables features. The fork's deletion of policy files is not carried forward absent confirmation that those restrictions were meant to be removed.
- Explicit exception: custom-server connection is enabled in the default Android/Play Store policy because the user requires the trust-and-connect path to recover device-code login through `api.nuvio.tv`.
- Release target: update the existing Play listing ("custom features" build) by uploading the compatible, signed `com.JF_Nuvio` release AAB to Internal testing. The Play application ID must not change.
- Branch strategy: `main` is the single active maintained branch on `origin/main`. Future parent updates from `upstream/cmp-rewrite` will be pulled into a temporary migration branch, verified against all custom AAOS invariants, applied to `main`, and pushed to `origin/main`. Temporary migration branches will be cleaned up after verification.
- iOS bundle IDs: carry forward the fork's exact overridden identifiers and keep untouched upstream Release IDs as they were.
- Play Store baseline confirmed by the user: `com.JF_Nuvio` code 139 / name 0.5.6. Current candidate 142 / 0.5.9 is above that baseline. Before each upload, check that no newer version has been submitted and keep the candidate code greater than the latest Play Console code.

## Migration progress and validation checklist

Archived 2026-10-02: every item here is superseded by the main-only branch policy; unchecked boxes are NOT tasks.

- [x] Inspect remotes, branches, repository guidance, and version configuration.
- [x] Review the README against the AAOS fork-identity and distribution contract; retain its banner,
  Nuvio branding, accurate AAOS summary, Internal testing guidance, and migration-guide link.
- [x] Fetch latest upstream refs; `upstream/cmp-rewrite` is at `d667f432`.
- [x] Create upstream-based migration branch without changing the custom branch.
- [x] Port Android app identity, namespace, and AAOS manifest/runtime behavior.
- [x] Port Automotive device-link sign-in and touch-sized player controls.
- [x] Preserve upstream store feature policy and port the fork's iOS identifier overrides.
- [x] Preserve the authentication-screen custom-server trust flow in the default Android build so device-code login can use `api.nuvio.tv`.
- [x] Set `https://api.nuvio.tv` as the built-in official backend default and add runtime discovery/validation for its publishable key when build configuration leaves it unset.
- [x] Build/install a Play Store **debug-only** APK on the wiped Automotive emulator and verify custom-server discovery/trust plus device-code generation. This debug package is for emulator use only.
- [x] Recheck fresh-launch automatic device-code generation using the default `api.nuvio.tv` configuration after clearing only the emulator's debug-app data.
- [x] Build and install the signed Play Store release APK (`com.JF_Nuvio`) on the Automotive emulator to compare release behavior with debug.
- [x] Rebind the app's auth-session observer after runtime API-key discovery replaces the Supabase client; add bounded handling for stalled session exchange and auth-state validation.
- [x] Verify on the signed release that an approved code reaches the home screen and the session remains valid after app relaunch/update.
- [x] Remove duplicate `* 2.kt` source files/directories and verify normal Android compilation plus Full and Play Store debug assembly without temporary file moves.
- [x] Restore the fork's AAOS readability improvements for poster dimensions, shelf spacing/typography, and navigation icon sizing; validate Android compilation and Play Store debug assembly.
- [x] Review overall AAOS text/icon sizing and enlarge shared typography/tokens plus player-specific visual and touch targets; verify Android compilation and Play Store debug assembly.
- [x] Increase AAOS home poster density to five full cards per 1280dp viewport, enlarge home navigation and player/source back controls, preserve non-AAOS and landscape-card sizing, and verify with the focused sizing test plus signed release assembly/emulator launch.
- [x] Advance version code to 139 and marketing version to 0.5.6 before release work (Play Console maximum remains unconfirmed).
- [x] Advance version code to 140 and marketing version to 0.5.7 for the next upload candidate; Play Console maximum remains unconfirmed.
- [x] Advance version code to 141 and marketing version to 0.5.8 for the next upload candidate; Play Console maximum remains unconfirmed.
- [x] Advance version code to 142 and marketing version to 0.5.9 for the search-results spacing update; this supersedes candidate 141 / 0.5.8, and the Play Console maximum remains unconfirmed.
- [x] Record the user-confirmed live Play Store baseline, code 139 / name 0.5.6; current candidate 142 is higher.
- [x] Add active-query-only vertical spacing below the search field and after search results to separate them from the floating Home/Search/Profile navigation; Discover spacing remains unchanged.
- [ ] Before upload, check Play Console for any version newer than 139 and increase code 142 if needed.
- [ ] Build the signed Play Store **release** AAB for `com.JF_Nuvio` (not debug), then inspect the merged manifest, package/version, signature, and bundle contents.
- [ ] Upload the release AAB to the existing Play Internal testing track and install it from Play on the Polestar 3.
- [ ] Verify first-run sign-in/code generation and core AAOS behavior on the real car; record results before replacing the current custom-features release workflow.
- [ ] After acceptance, designate this upstream-based fork as the maintained branch for future parent updates while retaining `my-custom-features` as the rollback/reference.

## Existing workspace state to preserve

- The workspace contains pre-existing files under `androidApp/playstore/release/` (including an AAB and mapping files); leave them untouched. The ignore rules above keep these build outputs out of status.
- The existing `androidApp/playstore/release/androidApp-playstore-release.aab` is a pre-existing artifact dated 2026-09-09. Do not treat it or the debug APK as a newly validated release candidate; build the final release AAB from accepted source and verify its signature before upload.
- MPVKit stays at the upstream-published commit; a stale `d5cf091` checkout was reset to `bb1d0250` on 2026-10-02.
- A `:androidApp:assembleFullDebug` APK was built and launched on `emulator-5554` before migration work began. It showed Nuvio's welcome/sign-in screen; device-link sign-in reported a network error in that emulator session.
- The first release build compiled, passed release lint, and reached R8, but the combined bundle+APK invocation failed because ABI splits produced multiple shrunk-resource files for the AAB task. `androidApp/build.gradle.kts` now disables release ABI splits when a bundle task is requested; APK-only release builds keep their ABI splits. The version advanced from 138 to 139 before retrying, and no artifact was produced by the failed attempt.
