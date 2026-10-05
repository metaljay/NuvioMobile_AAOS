# Nuvio AAOS log

Newest entry first. One entry per verified change, upstream sync or upload. Format:

```
## YYYY-MM-DD: short title
- What changed and why
- Commands run and results
- Verified: compile / unit tests / emulator / real car (state which; list anything NOT verified)
```

Agents read this file only when they need evidence. Rules live in `AAOS_FORK.md`; facts about the car live in `AAOS_CAR_NOTES.md`. This file keeps the newest 15 entries; older ones are in `AAOS_LOG_ARCHIVE.md`.

## 2026-10-05: Fork docs tidied for future agents (no app change)
- Added `AAOS_CAR_NOTES.md` (identical in both forks: launcher, media card, safe area, testing like the car, Play), `CLAUDE.md` and `.claude/skills` (a link to `.github/skills`) so Claude Code finds the same rules and recipes.
- Recipes: tweak adds a car-like emulator check (open from the app list, check edges, media card and a reboot when relevant); sync and tweak read the car notes; release asks the owner to reply `Uploaded` and then records it.
- `AAOS_FORK.md`: invariants renumbered in order, stale "app listed twice" text removed, rules for writing car facts into both repos, a 15-entry log limit and keeping docs consistent. `AAOS_UPSTREAM_SYNC.md`: last merged parent commit recorded, duplicate line removed, test commands made foolproof. `AAOS_RELEASE.md`: last upload set from the owner's car report. Older log entries moved to `AAOS_LOG_ARCHIVE.md` unchanged.
- Commands run and results: `git diff --check` passed. Documentation only; no build needed.
- Verified: documentation-only change. NOT verified: nothing in the app changed.

## 2026-10-05: Display safe-area rule added; card-after-restart finding (no app change)
- Added a "Display safe area" invariant (now number 6) to `AAOS_FORK.md` after Flow's top bar cog was found only partly tappable at the Polestar 3 screen edge. Nuvio has not been audited against it yet.
- Car media card after a full restart (owner test on the Polestar 3, reproduced on the emulator): the card works during use (it followed Flow and then Nuvio), but after a full system restart it shows only the Flow or Nuvio icon and stays blank, even after playing in either app. The app's session does hold the right item; the car launcher (car-media-common `MediaSource.isMediaTemplate`, read from the emulator launcher's code) only accepts a media service if it has `androidx.car.app.launchable=true` or the app has no launcher activity, so after a restart it rejects the remembered source ("No opt-in info found ... Skipping MBS"). The opt-in is what broke the app icon on the Polestar, so a card that refills after a restart and an icon that opens the app cannot both be had with a normal app; no code change for this.
- Commands run and results: `git diff --check` passed. Documentation only; no build needed.
- Verified: the owner's car test (photos) and the emulator reproduction. NOT verified: any Nuvio layout against the new rule.

## 2026-10-05: Release prepared for version 146 (0.5.13)
- Contains the app icon fix logged below (car media opt-in removed). Version raised by the release recipe and pushed on its own.
- Commands run and results: `./gradlew :androidApp:bundlePlaystoreRelease` passed.
- Merged release manifest checked: application ID `com.JF_Nuvio`, version code 146, name 0.5.13, min SDK 24, target SDK 36, automotive feature optional; car media service and artwork provider present, without `androidx.car.app.launchable`.
- NOT verified: signing (done by the owner in Android Studio), Play upload, the real car.

## 2026-10-05: App icon opens Nuvio again (car media opt-in removed)
- Removed `androidx.car.app.launchable` from `NuvioCarMediaBrowserService` in the manifest. Why: on the Polestar 3 (one icon per app) the app icon opened the car's media screen ("Continue watching") instead of the app, with no way in; the owner could only open the app from the Play Store's Open button. The car launcher source (AOSP `AppGridRepository`) adds a separate media entry for any opted-in media service; the Polestar keeps one entry per app and picks that one. The rest of the media card code (shared session, saved last item, artwork provider) is kept.
- Commands run and results: `./gradlew :composeApp:compileAndroidMain`, `:androidApp:assembleDebug -Pnuvio.android.distribution=playstore`, `:androidApp:assembleDebug -Pnuvio.android.distribution=full`, `git diff --check` and `:composeApp:testAndroidHostTest --tests '*PlayerNowPlaying*'` passed.
- Verified (emulator `Automotive_Large_Portrait`, playstore debug build): the app grid shows one Nuvio Debug icon and tapping it opens Nuvio. After a full reboot the emulator launcher logged "Skipping MBS ... non media template app" for Nuvio, so on that launcher the card does not refill itself after a restart. Card during playback not re-checked for Nuvio.
- NOT verified: the real Polestar 3 (icon behaviour and whether its card follows the service after a restart).

## 2026-10-04: Release prepared for version 145 (0.5.12)
- Contains the car media card change logged below. Version raised by the release recipe and pushed on its own.
- Commands run and results: `./gradlew :androidApp:bundlePlaystoreRelease` passed.
- Merged release manifest checked: application ID `com.JF_Nuvio`, version code 145, name 0.5.12, min SDK 24, target SDK 36, automotive feature optional; `NuvioCarMediaBrowserService` (with `androidx.car.app.launchable`) and the `com.JF_Nuvio.carmediaart` provider present.
- NOT verified: signing (done by the owner in Android Studio), Play upload, the real car.

## 2026-10-04: Car media card keeps showing the last item after Nuvio is closed

- Why: on AAOS the home screen media card only follows apps that expose a `MediaBrowserService`, reads the session handed out by that service, and (on the Google car launcher) skips apps with a launcher activity unless the service opts in with `androidx.car.app.launchable`. Nuvio had no such service, so the card emptied when the app closed. Sources: Google "Build media apps for cars", "Configure manifest", "Media controls / playback resumption" pages; AOSP `CarMediaService.java` and car-media-common `PlaybackViewModel.java`; emulator launcher log ("Skipping MBS ... belonging to non media template app").
- Added `NuvioCarMediaBrowserService` + `NuvioCarMediaSession` (one shared session, last item saved to disk, restored as paused, "Open Nuvio" prompt on play while closed, "Continue watching" browse tab) and `NuvioCarMediaArtworkProvider` (serves the saved poster as a content:// URI, which AAOS requires for artwork). `PlayerNowPlayingController` now uses the shared session and publishes the content:// artwork URI. Manifest declares the service (with the opt-in meta-data) and the provider.
- Commands run and results: `./gradlew :composeApp:compileAndroidMain` passed; `./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=playstore` passed; `./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=full` passed; `./gradlew :composeApp:testAndroidHostTest --tests '*PlayerNowPlaying*'` passed; `git diff --check` passed.
- Verified (emulator `Automotive_Large_Portrait`, Android 15, playstore debug build): with a hand-seeded saved item and the app force-stopped, the home card showed the title, subtitle and play button; after an emulator reboot with Nuvio as the last media source the session was restored as paused; pressing play showed the "Open Nuvio" prompt in the car media screen and its button opened Nuvio (which then auto-resumed a real episode). That real playback replaced the saved item (title, episode, poster, position); after force-stopping Nuvio the home card showed that episode with its poster. Before the opt-in meta-data the card showed only "Nuvio Debug" with no text; before the content:// artwork it showed a music-note placeholder.
- NOT verified: the real Polestar 3 (its launcher may differ from the emulator's Google car launcher); a real car restart.

## 2026-10-04: Workflow simplified to two jobs (parent update, tweak), both ending in a release that raises the version automatically; aaos-log-change replaced by aaos-tweak

- Updated AAOS documentation (README, AGENTS, AAOS_FORK, AAOS_RELEASE, AAOS_UPSTREAM_SYNC) to document the simplified two-job workflow and automatic version bump.
- Commands run and results: `git diff --check` passed.
- Verified: documentation-only update; no build or app code touched.

## 2026-10-03: Confirmed upload to Play for version 144 (0.5.11)

- Owner confirmed Google Play Internal testing accepted version code 144 and version name 0.5.11 for the `com.JF_Nuvio` app.
- The release-state table in `AAOS_RELEASE.md` was updated to record the confirmed Play upload and the repo's next release candidate must be raised above 144 before the next upload.
- Commands run and results:
  - `git --no-pager status --short` -> clean working tree before the documentation update.
  - `git add AAOS_RELEASE.md AAOS_LOG.md` -> docs staged for the release record.
  - `git commit -m "docs(release): record confirmed Play upload"` -> committed.
  - `git push origin main` -> pushed.
- Verified: documentation-only update only. No emulator, build, or real-car verification was claimed.

## 2026-10-03: Raised the Play upload candidate for a safety buffer

- Increased the repo’s release candidate from 143 / 0.5.10 to 144 / 0.5.11 to keep a one-code safety buffer above the owner-confirmed Play upload of 142 / 0.5.9.
- This is a conservative release-prep step before the next bundle upload and does not change product behavior.
- Commands run and results:
  - `./gradlew --no-daemon --console=plain :composeApp:compileAndroidMain` -> passed.
  - `./gradlew --no-daemon --console=plain :composeApp:testAndroidHostTest --tests "com.JF_Nuvio.features.home.HomePosterCardSizingTest" --tests "com.JF_Nuvio.core.network.ServerDiscoveryPolicyTest"` -> passed.
  - `git --no-pager diff --check` -> passed.
- Verified: Android compilation and the focused regression tests for Search and media-card behavior remained green.
- NOT verified: emulator visual checks and real-car validation were not run.

## 2026-10-03: Logged the current AAOS verification state for Search and media-card changes

- Reviewed the recent AAOS changes in the repo: the Search screen now keeps extra top clearance on Automotive/tablet layouts, and the media-card flow retains session metadata while pausing playback when the player leaves the foreground.
- This log entry records the repo status and confirms the version-code rule is still satisfied: code 143 / 0.5.10 is higher than the owner-confirmed Play upload of 142 / 0.5.9.
- Commands run and results:
  - `git --no-pager status --short` -> clean working tree.
  - `git --no-pager log -5 --oneline` -> recent AAOS commits confirmed the current state.
  - `git --no-pager diff --stat` -> no additional app code changes pending.
  - `./gradlew --no-daemon --console=plain :composeApp:compileAndroidMain` -> passed.
  - `./gradlew --no-daemon --console=plain :androidApp:assembleDebug -Pnuvio.android.distribution=playstore` -> passed.
  - `./gradlew --no-daemon --console=plain :androidApp:assembleDebug -Pnuvio.android.distribution=full` -> passed.
  - `./gradlew --no-daemon --console=plain :composeApp:testAndroidHostTest --tests "com.JF_Nuvio.features.home.HomePosterCardSizingTest" --tests "com.JF_Nuvio.core.network.ServerDiscoveryPolicyTest"` -> passed.
  - `git --no-pager diff --check` -> passed.
- Verified: Android compilation, the Play Store/full debug assembly checks, and the focused AAOS regression tests.
- NOT verified: emulator visual checks and real-car validation were not run in this session.

## 2026-10-03: Prepared release bundle candidate version 143 (0.5.10)

- Built and verified release App Bundle task `:androidApp:bundlePlaystoreRelease` for application ID `com.JF_Nuvio` with version code 143 and version name 0.5.10.
- Merged manifest verified: min SDK 24, target SDK 36, automotive/portrait/landscape features optional, distractionOptimized enabled.
- Commands run and results: `./gradlew :androidApp:bundlePlaystoreRelease` passed.
- Verified: Gradle release bundle build and merged manifest inspection. No emulator or real-car check.

## 2026-10-03: Added top navigation clearance for Search screen and enriched AAOS System Media Card metadata

- Added 68dp top clearance above the SearchScreen header on tablet and Automotive layouts (`isTabletLayout || isAutomotive`) so the search header and search bar sit comfortably below the top floating navigation pill bar.
- Enriched `PlayerNowPlayingController.android.kt` MediaMetadata keys (`METADATA_KEY_ALBUM`, `METADATA_KEY_DISPLAY_DESCRIPTION`, artwork URIs) and fixed artwork updates so downloading artwork completes and updates `MediaMetadata` even when exiting the player screen. This ensures the Polestar 3 AAOS home screen media widget renders title, album, and artwork properly.
- Commands run and results: `./gradlew :composeApp:compileAndroidMain` passed.
- Verified: Android compilation. No emulator visual check or real-car check.

## 2026-10-03: Added the aaos-log-change recipe (record changes and bug fixes) and README step 4

- Documented the `aaos-log-change` recipe across README.md, AGENTS.md, AAOS_FORK.md, and AAOS_LOG.md.
- Commands run and results: `git diff --check` passed.
- Verified: documentation-only update; no build or app code touched.

## 2026-10-03: Retained AAOS system media card session on player exit

- Updated `PlayerNowPlayingController.android.kt` so that when player controls unbind (e.g. user exits the player screen to return to the home screen), `MediaSession` retains the current item's metadata (`title`, `subtitle`, `artwork`) and transitions to `PlaybackState.STATE_PAUSED` at the current progress position instead of clearing metadata to `null` and setting `STATE_NONE`.
- This ensures the Polestar 3 / Android Automotive OS system home screen Current-Media Card widget displays the last-watched item details, progress, and paused play button instead of turning blank when returning to the home screen.
- Commands run and results: `./gradlew :composeApp:compileAndroidMain` passed; `./gradlew :composeApp:testAndroidHostTest --tests com.JF_Nuvio.features.player.PlayerNowPlayingServiceTest` passed (100%); `./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=playstore` passed; `./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=full` passed.
- Verified: Android compilation, unit tests, and Play Store + Full debug APK assembly. No emulator visual check or real-car check.

## 2026-10-03: Increased AAOS search clearance and paused playback when leaving the app

- Increased Automotive search-field spacing to 30dp and added 64dp of end-of-list clearance on both Discover and active search results. Non-Automotive spacing stays unchanged.
- Android playback now pauses when the app leaves the foreground instead of continuing because its now-playing media session is active. Playback in picture-in-picture may continue; finishing the activity always pauses. Advanced the next release candidate to 143 / 0.5.10 because 142 / 0.5.9 was confirmed uploaded.
- Investigated the CarPlay-style AAOS home media card: the app already publishes an Android `MediaSession` with title, artwork and playback state, but the Play build does not provide a `MediaBrowserService`/`MediaLibraryService`. Android Automotive renders the media-browser content in its own UI; Media3 `MediaSessionService` is the documented route for background playback. Video apps have separate parked-video and audio-while-driving requirements. Official guidance: `https://developer.android.com/training/cars/media/automotive-os`, `https://developer.android.com/training/cars/parked/video`, and `https://developer.android.com/media/media3/session/background-playback`. A native AAOS home-card result still needs emulator/car verification; no media-service integration was added.
- Commands run and results: `./gradlew :composeApp:compileAndroidMain :composeApp:testAndroidHostTest --tests com.JF_Nuvio.features.player.PlaybackLifecyclePolicyTest` passed; `git diff --check` passed. Android SDK device tools were unavailable, so no emulator visual check was possible.
- Verified: Android source compilation and the focused lifecycle-policy test only. No emulator or real-car check.
