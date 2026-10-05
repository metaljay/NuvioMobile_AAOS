# Nuvio: updating from the parent (upstream sync)

> Fork-owned file. "Parent facts", "Restore after every merge", "Checks to run" and "Hotspots" are specific to this app; every other section is word for word the same in the Flow and Nuvio forks.

The owner has no coding experience and relies on the agent to bring every customisation (`AAOS_FORK.md` section 7) across into the parent's new code, and to say plainly what changed. Do this only when the owner asks, or before a release that needs parent fixes. `main` is not touched until the owner approves. Parent changes only flow in; nothing is ever pushed to the parent. The recipe that drives this file is `.github/skills/aaos-sync/SKILL.md`.

## Parent facts

| Item | Value |
| --- | --- |
| Parent repository | https://github.com/NuvioMedia/NuvioMobile (git remote `upstream`) |
| Parent branch synced | `cmp-rewrite` |
| **Last merged parent commit** | `d667f4324b5f8fbcb5954dae6ee6b82885f9a9c4` (2026-09-30). Step 1 lists everything newer; step 11 updates this row. |

## Steps

In the commands below, `<branch>` is the parent branch in "Parent facts" and `<date>` is today's date; fill in the real values.

0. **Safety.** `git status` must be clean. Then `git switch main && git pull origin main` and mark the last good state with `git tag pre-sync-$(date +%F)`.
1. **Find what is new.** `git fetch upstream`, then `git log --oneline main..upstream/<branch>`. If nothing is listed, stop and tell the owner there is nothing new. Note the full SHA with `git rev-parse upstream/<branch>`.
2. **Assess before merging.** Read the new parent commits and `git diff --stat main...upstream/<branch>`. Using the Hotspots table (each row names its customisation), note which customisations' areas the parent touched and what the parent added or changed that the owner would notice. This feeds the approval report.
3. **Create the sync branch.** `git switch -c sync/upstream-$(date +%F) main`
4. **Merge.** `git merge upstream/<branch>`. Use a merge, not a rebase or reset, so our history stays intact.
5. **Redo the customisations.** Resolve conflicts using the Hotspots table. Rule: take the parent's new code, then re-apply our customisation on top; never choose "ours" or "theirs" wholesale on a hotspot file unless the table says "keep ours". Then go through **every** customisation in `AAOS_FORK.md` section 7 and follow "After a parent update" in its Part 2 block, including those whose files merged without conflict: the parent can change behaviour near our code silently. If a customisation cannot be redone safely, mark it "At risk" in the report; never drop it silently.
6. **Restore fork-owned values** (list below).
7. **Verify.** Run "Checks to run", do each customisation's "Check" from Part 2, and inspect the merged **release** manifest. If the emulator is available, do the car-like check in `AAOS_CAR_NOTES.md` ("Testing like the car").
8. **Approval gate.** Build the debug app, write the approval report (below) and stop. Do not merge into `main`. The owner replies `approve sync`, `cancel sync` or `problem: <what you saw>`. On `cancel sync`, delete the sync branch (see "Abort or roll back"); `main` stays untouched. On `problem`, fix it on the sync branch, re-verify and report again. If the owner returns in a new chat, find the open `sync/` branch, re-run the checks quickly, then continue.
9. **Land it.** `git switch main && git merge --ff-only sync/upstream-<date>`. If that refuses because `main` moved, merge `main` into the sync branch, re-verify, retry. Then `git push origin main`.
10. **Clean up.** `git branch -d sync/upstream-<date>`; if it was pushed, `git push origin --delete sync/upstream-<date>`. Never leave sync branches behind.
11. **Record.** Update "Last merged parent commit" above and add an `AAOS_LOG.md` entry: parent SHA, number of parent commits, conflicts, the status of each customisation, what was and was not verified. If a customisation's code moved, update its Part 2 block and the Hotspots table. Then continue with the release (`AAOS_RELEASE.md`).

## The approval report

The owner cannot read code, so this report is how they decide. Write it in plain English (`AAOS_FORK.md` section 6), using this layout:

> **Parent update for [app]: [number] new parent changes, [first date] to [last date]**
>
> **1. What's new for you.** The parent's new features and fixes, as the owner would notice them in the car (3 to 8 bullets). Say "nothing you would notice" if that is the case.
>
> **2. Your customisations.** A table with **every** ID from `AAOS_FORK.md` section 7, none skipped:
>
> | ID | Customisation | Status | What I did |
> | --- | --- | --- | --- |
>
> Status is one of: **Unaffected** (the parent did not touch that area), **Kept** (the parent touched the area; ours re-applied unchanged), **Adapted** (the parent changed the code, so ours was redone in a new form; say how), **At risk** (could not fully redo or check it; say why and what could go wrong), **Removed** (only if the owner decided so).
>
> **3. Risks and recommendation.** Anything risky: large rewrites, new permissions, sign-in, account or server changes, new network or tracking code, removed features, build or SDK changes. End with "Recommendation: approve" or "Recommendation: wait, because ...".
>
> **4. What to test.** First on the emulator (open Android Studio, choose the emulator `Automotive_Large_Portrait` in the device list at the top (if it is missing: Tools, Device Manager, Create Device, Automotive), click the green Run button, then open the app from the car's app list), then on the car after the release. List the areas this update touched in everyday words, plus the usual checks: the app opens from its icon, text and buttons are large, something plays, and the home screen media card shows it.
>
> **5. Checks I ran.** Each check: passed, failed or not run. Say "emulator" or "real car" explicitly; nothing on the car is verified until the owner tests it.
>
> Reply with exactly one of: `approve sync`, `cancel sync`, or `problem: <what you saw>`.

## Abort or roll back

- Before step 9: `git switch main && git branch -D sync/upstream-<date>`. `main` was never touched.
- After step 9: create a revert commit (`git revert -m 1 <merge commit>`). Do not reset or force-push. The `pre-sync-<date>` tag marks the last good state.

## Restore after every merge

- Keep OUR version code and name in `iosApp/Configuration/Version.xcconfig` (take ours on any conflict); never copy the parent's. The release stage raises them.
- Release `applicationId` is exactly `com.JF_Nuvio`, and the `com.JF_Nuvio` package rename is applied to every new or changed parent file, without a blind search-and-replace (C1).
- The iOS identifier overrides are intact (`AAOS_FORK.md` section 8).
- `AppFeaturePolicy.customServerConnectionsEnabled` is true for the Play Store build (C4).
- The merged release manifest still has the AAOS features and activity settings, the car media service without `androidx.car.app.launchable`, and the artwork provider (C2, C9).
- No stray ` 2` duplicate files (`AAOS_FORK.md` section 11).
- MPVKit stays at the parent-published commit (`AAOS_FORK.md` section 8).
- `README.md` and `AGENTS.md` are ours (C11).

## Checks to run

```bash
./gradlew :composeApp:compileAndroidMain
./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=playstore
./gradlew :androidApp:assembleDebug -Pnuvio.android.distribution=full
./gradlew :composeApp:testAndroidHostTest --tests '*HomePosterCardSizing*' --tests '*ServerDiscoveryPolicy*' --tests '*PlayerNowPlaying*' --tests '*PlaybackLifecyclePolicy*'
git diff --check
```

Run the lines one at a time. Also run focused tests for any other area touched. If a test run reports "Unsupported class file major version 71", rerun it with `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"` in front (wrong Java, not real failures).

## Hotspots: files where our customisations live

Paths are under `composeApp/src/commonMain/kotlin/com/JF_Nuvio/` unless they say otherwise.

| File / area | Customisation | What we changed | On conflict |
| --- | --- | --- | --- |
| All Kotlin packages | C1 | the parent's `com.nuvio.app` renamed to `com.JF_Nuvio` | Re-apply the rename to new or changed parent files; check store, signing and iOS config by hand |
| `androidApp/build.gradle.kts` | C1, C10 | application ID, signing properties, ABI-split workaround | Keep ours for those; take the parent's for the rest |
| `iosApp/Configuration/Version.xcconfig` | C1 | Play version code and name | Keep ours |
| iOS `Config.xcconfig` and Xcode project | C1 | the fork's iOS identifier overrides | Keep the overrides |
| `androidApp/src/main/AndroidManifest.xml` | C2, C9 | AAOS features, activity settings, car media service and provider | Keep every AAOS entry |
| `Platform.kt`, `composeApp/src/androidMain/.../Platform.android.kt` | C2 | Automotive detection and early platform start | Keep |
| `features/auth/AuthScreen.kt`, `DeviceLinkAuthSection.kt`, `core/auth/DeviceLinkAuthRepository.kt` | C3, C4 | automatic device-link sign-in on Automotive; server menu | Keep; re-run the sign-in check |
| `composeApp/src/androidPlaystore/.../AppFeaturePolicy.android.kt` | C4 | custom server connections enabled | Keep enabled |
| `core/network/ServerDiscovery.kt`, `ServerConfiguration.kt`, auth and official-config code | C4 | `api.nuvio.tv` handling, session fixes | Keep; re-run `ServerDiscoveryPolicyTest` |
| `core/ui/NavigationBar.kt`, `androidMain/.../core/ui/jelly/JellyTabs.kt`, `FloatingNavigationBar.android.kt` | C5 | larger navigation icons and labels | Re-apply Automotive-gated sizes |
| `features/home/` (`HomeScreen.kt`, `components/Home*`) | C5 | poster size, density and typography | Re-apply; re-run `HomePosterCardSizingTest` |
| `features/search/SearchScreen.kt`, `features/details/`, `features/player/*`, `features/streams/StreamsScreen.kt` | C5 | larger controls and spacing | Re-apply Automotive-gated sizes (details buttons are shared) |
| `features/settings/PlaybackSettingsPage.kt`, `features/streams/StreamAutoPlay*` | C7 | playback defaults | Keep our defaults for unset values |
| `androidMain/.../features/player/PlayerEngine.android.kt` | C8 | pause when leaving the foreground | Keep; re-run `PlaybackLifecyclePolicyTest` |
| `androidMain/.../features/player/PlayerNowPlayingController.android.kt`, `NuvioCarMediaSession.android.kt`, `NuvioCarMediaBrowserService.android.kt`, `NuvioCarMediaArtworkProvider.android.kt` | C9 | shared session, card restore, `content://` artwork | Keep the controller on `NuvioCarMediaSession` (no private `MediaSession`) and publishing the `content://` artwork |
| `README.md`, `AGENTS.md` | C11 | owner README; fork-owned AGENTS | Keep ours |
| `CLAUDE.md`, `GEMINI.md`, `.claude/skills`, `.github/skills/aaos-*`, `AAOS_*.md` | C11 | fork-owned agent entry points, recipes and docs | Keep ours |
