# Nuvio AAOS fork: rules and customisations

> Fork-owned file (the parent project does not have it, so parent updates never overwrite it). Sections marked **(shared)** are word for word the same in the Flow and Nuvio forks: change them in both repos in the same change. Everything else is specific to this app. If reality changes, fix this file in the same change.

## 1. What this repo is

The owner's fork of [Nuvio](https://github.com/NuvioMedia/NuvioMobile), a film and TV streaming app, customised so it is readable and usable on the owner's Polestar 3 running Android Automotive OS (AAOS). It reaches the car only through Google Play **Internal testing**. The sister fork is Flow, next to this repo in the Development folder. Only Android is deployed; the iOS app is kept compiling so parent updates merge cleanly.

| Fork fact | Value |
| --- | --- |
| Parent project (upstream) | https://github.com/NuvioMedia/NuvioMobile, branch `cmp-rewrite` (the only parent branch ever synced) |
| Our fork (origin) | https://github.com/metaljay/NuvioMobile_AAOS, branch `main` |
| Play application ID (release) | `com.JF_Nuvio` |
| Code namespace | the parent's `com.nuvio.app` / `com.nuvio.android`, unchanged: only the application ID is ours (C1) |
| Debug build | `com.JF_Nuvio.debug`: emulator only, never on the car |
| Release facts | `AAOS_RELEASE.md` (module, build variant, version file, last upload) |
| Parent update facts | `AAOS_UPSTREAM_SYNC.md` (last merged parent commit, checks, hotspots) |

## 2. The jobs (shared)

The owner is the only user (Play Internal testing) and every change goes to their car, so the owner has exactly two jobs, both started by pasting a prompt from `README.md`, and both always end in a release:

| Job | Recipe | Ends with |
| --- | --- | --- |
| Update from the parent | `.github/skills/aaos-sync/SKILL.md` | the approval report in `AAOS_UPSTREAM_SYNC.md`, the owner's `approve sync`, then the release stage |
| Improve or fix the app for the car | `.github/skills/aaos-tweak/SKILL.md` | the release stage |

The release stage (`.github/skills/aaos-release/SKILL.md`) and recording the upload (`.github/skills/aaos-uploaded/SKILL.md`) are run by the agent as the last steps of both jobs; the owner never needs to start them. An agent may run the release stage on its own only to finish or retry an interrupted release.

The recipes are plain instructions any AI agent can follow. Claude Code and GitHub Copilot also list them as `/aaos-sync`, `/aaos-tweak`, `/aaos-release` and `/aaos-uploaded`.

## 3. Terminology (shared)

| Term | Meaning |
| --- | --- |
| upstream, parent | The original project this fork follows (section 1). Read-only to us. Only the parent branch named in section 1 is ever synced. |
| origin | The owner's GitHub fork (section 1). |
| `main` | Parent code plus our customisations. The only long-lived branch; always buildable and releasable. |
| sync branch | Temporary `sync/upstream-<date>` branch used to review a parent update before it reaches `main`. Deleted afterwards. |
| customisation | A deliberate change we keep. Each has an ID (C1, C2, ...) in section 7 and a detail block in Part 2. Every customisation must survive every parent update unless the owner decides otherwise. |
| Play version code | The number Play Console requires to rise on every upload. Independent of the parent's version. See `AAOS_RELEASE.md`. |
| release bundle | The signed `.aab` file uploaded to Play. |
| debug build | Emulator-only. Never installed on the car. |

## 4. Golden rules (shared)

1. The only target is the owner's Polestar 3 (AAOS), delivered via Play Internal testing. Android Auto (phone projection) is out of scope; do not add Android Auto metadata.
2. `main` is the only long-lived branch. Parent updates go through a `sync/` branch first (`AAOS_UPSTREAM_SYNC.md`).
3. Never push to the parent. Parent changes flow in one way.
4. Port, don't replay: redo each customisation in the parent's current code instead of cherry-picking old commits.
5. Keep edits to parent-owned files as small as possible (fewer merge conflicts). Fork-only additions go in clearly separate places or files.
6. Every release build raises the version automatically (`AAOS_RELEASE.md`); the parent's version numbers are ignored.
7. Before touching the manifest, the media card, how the app launches, or anything near the screen edges, read `AAOS_CAR_NOTES.md` (facts learned on the real car).
8. If a product decision is unclear (for example removing a customisation), ask the owner instead of guessing.

## 5. Permissions and safety rails (shared)

Agents may edit files, build, run tests, commit, push to `origin/main` once the work is verified, and create and delete `sync/` branches. Agents must **never**:

- force-push or rewrite history on `main`;
- push to the parent or merge anything towards it;
- commit, print, copy or paste `Key.jks`, any keystore, `local.properties` contents or any password;
- delete branches other than `sync/*`;
- change the application ID, namespace or signing setup unless asked;
- say something is "verified" without stating exactly what was run and what was not (compiled / unit-tested / emulator / real car);
- put personal names, usernames, emails or absolute paths into any file, commit message or log.

Commit with small, clear messages (`type(scope): description`). Stage files by name; do not use `git add -A`.

If a pull request is ever merged on GitHub, merge it with `gh pr merge <number> --author-email "$(git config user.email)"`: without that, GitHub records the merge under the account's private email.

## 6. Talking to the owner (shared)

The owner has no coding experience, copy-pastes messages between chats, and depends on the agent to do and check the work. Therefore:

1. Never tell the owner to run a command unless you give the complete command in a copy-paste code block with every value filled in. No placeholders such as <date> or <file>: look the value up yourself first.
2. Run commands yourself wherever you can. Ask the owner to act only for things only they can do: sign the bundle in Android Studio, upload in Play Console, test on the car, change GitHub settings, or approve a decision.
3. Offer decisions as copy-paste replies, for example: reply `approve sync` or `cancel sync`. One decision at a time.
4. Explain results in plain English. Explain any jargon in one short phrase (for example: "merge means combining the parent's changes with ours").
5. End every task with a report in this order: What I did / What I checked (the exact commands and results) / What I did NOT check / What you need to do next (the exact message to paste, or "nothing").
6. If something fails, stop and say what failed in plain English. Give the owner one message to paste back to you or to another AI assistant. Do not attempt risky fixes.
7. Do not ask the owner to read raw code, diffs or logs; summarise them. Offer the raw output only if asked.

## 7. Customisations (must survive every parent update)

| ID | Customisation | In one line |
| --- | --- | --- |
| C1 | Play identity | Release application ID `com.JF_Nuvio`; the code keeps the parent's `com.nuvio.app` names. |
| C2 | AAOS manifest and runtime | Automotive features optional, `distractionOptimized` on every activity, car-friendly activity settings, Automotive detected at start. |
| C3 | Device-link sign-in | On the car, sign-in starts automatically and is completed on a phone at `nuvio.tv/link`. |
| C4 | Custom-server fallback | "Connect to another server" stays available so `api.nuvio.tv` can be chosen if sign-in fails. |
| C5 | AAOS readability | One whole-app 1.5x scale on Automotive (text, buttons, menus, spacing); Home artwork kept at its on-car size. |
| C6 | Display safe area | Touch targets at least 24 px (16 dp under the C5 scale) from the left and right window edges. |
| C7 | Playback defaults | New profiles default to "Reuse last link" on and first-stream autoplay. |
| C8 | Playback lifecycle | Playback pauses when the app leaves the screen. |
| C9 | Car media card | The car's home screen card shows Nuvio's last item; on cars that support it, gated twin media services let it refill after a restart and after a force-stop or update while the icon still opens Nuvio (emulator only so far; the force-stop fix is untested on the car). |
| C10 | Bundle packaging | ABI splits are off for bundle tasks. |
| C11 | Fork docs | README, AGENTS file, agent entry points, recipes, `AAOS_*.md` files and the `aaos/` folder stay. |
| C12 | Build tools | Newer Android Gradle Plugin and Gradle, Java toolchain download, and release files kept out of git. |
| C13 | One-command release | `aaos/release.sh` tests, builds, signs (key details from the owner's Keychain) and releases to Play Internal testing only. |

Each one has a detail block in Part 2: what and why, where it lives, how to redo it after a parent update, and how to check it. Paths in Part 2 are under `composeApp/src/commonMain/kotlin/com/nuvio/app/` unless they say otherwise.

**How our changes are kept apart from the parent's** (so parent updates merge with few conflicts and the fork can be reviewed on its own):

- The code keeps the parent's package names; only the application ID differs (C1). Never rename packages again.
- Car code that can stand alone lives in **fork-only files** next to the code it changes, named `Aaos*.kt` (for example `AaosScale.kt`, `AaosPlayerLayout.kt`) or `NuvioCar*.kt` (the media card). The parent never has these files, so its updates never conflict with them.
- In the parent's files, keep changes to the smallest hook possible; a new hook line ends with `// AAOS Cx` (the customisation ID). Sizes are not edited inside the parent's screens: the C5 scale enlarges them all at once, and the few exceptions live in the fork-only files.
- `aaos/customisations.txt` maps every file that differs from the parent to its customisation ID. **`aaos/report.sh`** lists every difference from the parent grouped by ID (`aaos/report.sh C5` also shows the code for C5; `aaos/report.sh all` for all). It must show no "NOT LABELLED" files: when a change adds, moves or removes a fork change, update the map in the same commit.

## 8. Deliberately NOT customised

- **The parent's Play Store feature policy**: the Play Store build keeps the parent's disabled features, except custom-server connections (C4). Do not delete policy files.
- **Phone and tablet sizing**: stays the parent's, with no exceptions: the C5 scale and every C5/C6 size are gated to Automotive.
- **Saved user preferences**: a saved value (poster width, playback settings) always wins over our defaults.
- **MPVKit submodule**: stays at the parent-published commit (a stale `d5cf091` checkout was reset to `bb1d0250` on 2026-10-02). Its `libass-android` reference is dangling; the parent has the same dangling reference.
- **iOS**: not deployed, so its identifiers are the parent's (the fork's old iOS identifier overrides were dropped on 2026-10-12). Only `iosApp/Configuration/Version.xcconfig` is ours: it holds the shared version numbers (`AAOS_RELEASE.md`).

## 9. README contract (shared)

`README.md` is written for the owner, a person with no coding experience. Every merge or refresh keeps these sections, in this order:

1. Banner: `🚗 Android Automotive OS (AAOS) Fork`, the app's logo and name, and links to the parent project and the license.
2. **What this is**: an AAOS fork of the parent app for the owner's Polestar 3, not Android Auto.
3. **What's different in the car**: an accurate plain-English summary, one row per customisation, each linking to its Part 2 block in `AAOS_FORK.md`.
4. **Getting it on the car**: Google Play Internal testing only; no public downloads.
5. **What do you want to do?**: the two jobs in section 2, each with a copy-paste prompt and what happens next; the release both end with; and help for when something goes wrong.
6. **Behind the scenes**: which file holds what, for the curious.
7. **License**.

Keep code, file paths and jargon out of sections 1 to 5 apart from the prompts. Only use screenshots of this fork that exist and are accurate. On a README conflict in a parent update, keep ours (the parent's README describes the parent app).

## 10. Keeping these docs current (shared)

- **After every verified change**: record it in the change history (`handover/SESSION-LOG.md`); if a customisation was added, changed or removed, update section 7, its Part 2 block, the hotspot table in `AAOS_UPSTREAM_SYNC.md` and, if the owner would notice it, the README summary; after a confirmed upload, update "Last uploaded to Play" in `AAOS_RELEASE.md`. The recipes make these updates.
- **One home per fact.** Rules and customisations live here; release facts and steps in `AAOS_RELEASE.md`; parent-update steps and the approval report in `AAOS_UPSTREAM_SYNC.md`; car facts in `AAOS_CAR_NOTES.md`; current state and history in the owner's private handover. Other files link to these instead of repeating them.
- **Both repos together.** Flow and Nuvio share the same car and the same doc skeleton. Car facts go into `AAOS_CAR_NOTES.md` in **both** repos in the same change (the file is identical in both). A change to a **(shared)** section, the README layout or a recipe is made in both repos too.
- **The handover (Nuvio only, for now).** State and history live in `handover/` on the owner's Mac: `HANDOVER.md` (current state, open items; Part A locked) and `SESSION-LOG.md` (one entry per chat). It is git-ignored and never committed; it replaced `AAOS_LOG.md` on 2026-10-12 (the old log is archived there). Claude Code reads it through the git-ignored `CLAUDE.local.md`. An agent without access to it (another tool, a cloud chat) puts the same evidence in its final report and commit message. A release-preparation note is at most three lines.
- **No contradictions, no stale text.** When a number or a rule changes, search `README.md`, `AGENTS.md`, the recipes and every `AAOS_*.md` file for the old wording and update every mention. Delete obsolete text rather than adding a correction next to it.

## 11. Environment notes

Shared (both repos):

- Builds run on the owner's Mac with Android Studio. Both repos sit side by side in the owner's code folder (outside iCloud), with the `Play Console` folder beside them (outside git): the release key `Key.jks`, the signed bundles (one folder per app) and the store artwork.
- Emulator used for checks: `Automotive_Large_Portrait` (1280 x 1606). It does not prove Polestar 3 behaviour. If `adb` or the emulator is unavailable, say so; never claim device verification from a compile.
- If tests fail with "Unsupported class file major version 71", the default Java is too new: rerun with Android Studio's bundled Java (`JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"` in front). It is an environment problem, not real failures.

Nuvio only:

- macOS Finder copies create stray duplicates named `* 2.kt`, `* 2.xml` or `* 2` folders (347 had to be removed once). They break builds. Never leave files with a ` 2` suffix; check for them after any copy or merge.
- Common iOS test sources compile, but the iOS simulator tests cannot run on this Mac (simulator SDK not installed).

---

# Part 2: Customisation details

Each block uses the same four headings. **After a parent update** is what the sync recipe must do to keep the customisation working in the parent's new code; **Check** is how to prove it.

### C1. Play identity

- **What and why:** the car installs Nuvio from our existing Play listing, which only accepts updates carrying the application ID `com.JF_Nuvio`. Only the application ID changes: the code keeps the parent's namespace and package names (`com.nuvio.app`, `com.nuvio.android`), so parent updates merge without a rename. (Until 2026-10-12 the fork had renamed every package to `com.JF_Nuvio`; that touched about 1,100 files for no benefit and was undone in release 147.) The debug build is `com.JF_Nuvio.debug`, for the emulator only.
- **Where:** `androidApp/build.gradle.kts` (the two application ID lines, marked `// AAOS C1`; signing properties); `iosApp/Configuration/Version.xcconfig` (version numbers, see `AAOS_RELEASE.md`).
- **After a parent update:** keep our two application ID lines and our version numbers on any conflict. Never rename packages. Everything that must match the installed app (provider authorities, file provider) uses `${applicationId}` or `context.packageName`, never a written-out package name.
- **Check:** the merged **release** manifest shows package `com.JF_Nuvio`; `aaos/report.sh` lists only the expected files under C1.

### C2. AAOS manifest and runtime

- **What and why:** lets the car recognise Nuvio as a car app and run it well in the car's window: Automotive and portrait/landscape screen features optional (so Play still allows the install); `distractionOptimized=true` on the application and on **every** activity in our manifest, launcher or not (owner's rule, 2026-11-16: the car blocks an untagged activity while driving, and the parent's link-opening `LinkActivity` had been missed); launcher and main activities resizable, `singleTask`, cutout-aware, picture-in-picture capable, and handling keyboard, navigation, UI mode, density and font changes themselves; Automotive detected at runtime and the platform initialised with the Android context before any UI.
- **Also:** the player must not force landscape on the car. Since 2026-11-09 the parent locks the player to landscape on every screen, including large ones; `PlayerPlatformEffects.android.kt` skips that lock on Automotive (one marked line), so the car window keeps the car's own orientation (Google asks car apps to support portrait and landscape).
- **Where:** `androidApp/src/main/AndroidManifest.xml` (the parent moved its deep-link filters to a separate `LinkActivity`; our settings stay on `MainActivity`); `Platform.kt` and `composeApp/src/androidMain/kotlin/com/nuvio/app/Platform.android.kt` (Automotive detection); `composeApp/src/androidMain/kotlin/com/nuvio/app/features/player/PlayerPlatformEffects.android.kt` (no landscape lock).
- **After a parent update:** keep every AAOS entry; give any new parent launcher activity the same settings and metadata, and give **every** new parent activity of any kind the `distractionOptimized` tag.
- **Check:** merged release manifest; in `androidApp/src/main/AndroidManifest.xml` the number of `distractionOptimized` tags is the number of `<activity` entries plus one (the application); the app opens from the emulator's app grid.

### C3. Device-link sign-in

- **What and why:** the car has no browser, so on Automotive the official-server device-link sign-in starts automatically and shows the instruction to finish signing in on a phone at `nuvio.tv/link`. The server menu stays on the sign-in screen so a custom backend can be chosen (C4).
- **Where:** `features/auth/AuthScreen.kt`, `features/auth/DeviceLinkAuthSection.kt`, `core/auth/DeviceLinkAuthRepository.kt`.
- **After a parent update:** keep the automatic start on Automotive and the phone instruction; never open a browser on the car.
- **Check:** emulator: the sign-in screen shows the `nuvio.tv/link` instruction and code without a tap.

### C4. Custom-server fallback

- **What and why:** if the default sign-in path does not offer or complete device-code login, the owner taps **Connect to another server**, enters `api.nuvio.tv`, reviews the discovered server, chooses **I trust this server**, and device-code login becomes available.
- **Where:** `composeApp/src/androidPlaystore/kotlin/com/nuvio/app/core/build/AppFeaturePolicy.android.kt` (`customServerConnectionsEnabled = true`); `core/network/ServerDiscovery.kt` (`ServerDiscoveryPolicy.isOfficial` must not reject the canonical `api.nuvio.tv`, while a distinct configured backend URL is still rejected); `core/network/ServerConfiguration.kt` (built-in backend default `https://api.nuvio.tv` when `NUVIO_SUPABASE_URL` is unset; without a publishable key, device-link sign-in reads it from the canonical API discovery document and validates the backend before use); the auth-observer rebinding and saved-session fixes in the auth code.
- **After a parent update:** keep the policy enabled and the `api.nuvio.tv` handling. Keep the parent's server discovery, trust screen, saved selection, server-switch reset and TV-login capability checks; never bypass the trust screen or force the server globally.
- **Check:** `ServerDiscoveryPolicyTest` passes (it asserts the canonical API is not rejected).

### C5. AAOS readability (Automotive only)

- **What and why:** makes Nuvio readable at a glance and easy to tap on the car display, without changing phone or tablet sizing. Redone on 2026-11-16: the earlier hand-enlarged sizes (about 30 files) were removed and replaced by one scale, so the parent's own proportions are kept and new parent screens are covered automatically.
  - **The scale:** on Automotive, `MainActivity` raises the screen density its resources report by 1.5 (`AaosUiScale`, applied in `attachBaseContext`), so every dp and sp in the parent's layouts (text, buttons, the top menu pill, settings rows, player panels, spacing) is drawn 1.5 times larger. Dialogs, drop-down menus, bottom sheets and pop-ups open in their own window and read the density from the activity, so they follow it too (a scale set only inside the Compose theme does not reach them). The layouts therefore see a narrower screen (1280 px = about 853 dp) and the parent's breakpoints pick its smaller-tablet layouts. System font scaling stays off, as in the parent. Video subtitles are Android views sized in sp, so they also draw 1.5 times larger than the saved subtitle size did before; picture-in-picture works in real pixels and is unaffected.
  - **Home artwork keeps its on-car size:** Home poster, catalogue and folder tiles, poster-style Continue Watching cards and loading skeletons have a 153 dp minimum width (about 230 px), so a 1280 px row shows five posters and the edge of a sixth, as before the redo. It is a minimum: a larger saved poster width wins. Labels under Home row posters use a 0.875 font scale (about 18 px titles) so long titles are not cut off. Not applied to Search, Library or detail rails, which follow the scale (Discover shows four posters a row).
  - **Player:** the parent's size table under the scale, with Automotive minimums for the sizes that came out too small: play icon 72 dp (was 56; 108 px on screen), skip icons 56 dp (was 44), header icons 40 dp (was 30; their round buttons are 56 dp, from the icon plus 16 dp), the bottom action row and top toolbar buttons 56 dp touch area (was 48) with icons 36 dp (was 24; the next-episode arrow stays 40), the toolbar back arrow icon 32 dp (was 24), the room reserved for the action row 56 dp (was 50), time and metadata text 14 sp, seek bar touch height 32 dp. Trailers use the same table. Widened 2026-11-18 so every button is easy to hit while parked.
  - **Shared car button standard (same in Flow):** every icon button is 56 dp to touch (84 px on screen) with a 32 dp icon. Back arrows on normal screens (details, settings, catalogue, streams, the settings and addons headers, profile picker) were 36 to 48 dp touch with a 24 dp icon; the top navigation pill (icons only) was 48 dp tall with 24 dp icons. Sizes only ever rise on Automotive (never shrink a larger parent size, so the player's 56/40 header button keeps its 40 dp icon). The player already meets the standard: toolbar back arrow 56/32, play 72, skip 56. Chips, list and settings rows, primary buttons, hero dots and Home tiles are unchanged. Not yet hooked (still the parent's 48 dp): the person page and TMDB browse back arrows (`PersonDetailScreen.kt`, `TmdbEntityBrowseScreen.kt`), the Library list header arrow and the arrows inside dialogs and sheets.
  - **Grown with the scale, not held back:** cast photos, trailer and episode thumbnails and Discover posters are 1.5 times their parent size.
- **Where:** `core/ui/AaosScale.kt` (the scale) and `composeApp/src/androidMain/kotlin/com/nuvio/app/core/ui/AaosScale.android.kt` (applies it; one marked line in `MainActivity.kt`, `attachBaseContext`); `features/home/components/HomePosterCardSizing.kt` (poster minimum and Home label scale) with its hooks in `core/ui/ShelfComponents.kt` (`posterWidthDp`), `features/home/HomeScreen.kt` and `features/home/components/` (`HomePosterCard.kt`, `HomeCatalogSection.kt`, `HomeCollectionRowSection.kt`, `HomeContinueWatchingSection.kt`, `HomeSkeletonLoading.kt`); `core/ui/AaosButtonSizes.kt` (shared back-arrow and navigation pill sizes) with marked lines in `core/ui/Components.kt` (`NuvioBackButton`, `NuvioScreenHeader`), `features/settings/SettingsComponents.kt`, `features/streams/StreamsScreen.kt`, `features/catalog/CatalogScreen.kt`, `features/details/components/DetailFloatingHeader.kt`, and in `androidMain/.../core/ui/FloatingNavigationBar.android.kt` and `jelly/JellyTabs.kt` (the navigation pill); `features/player/AaosPlayerLayout.kt` with one marked line each in `PlayerScreenContent.kt`, `PlayerControls.kt` and `features/trailer/TrailerPlayerControls.kt`, and marked lines in `PlayerControlActions.kt` (action and toolbar buttons) and `PlayerScreenSupport.kt` (action row height).
- **After a parent update:** keep the marked `attachBaseContext` line in `MainActivity.kt` and the hooks above; take the parent's version of everything else. Do not re-add per-screen sizes: if a new parent screen looks wrong on the emulator, fix that one thing in a fork-only `Aaos*.kt` file with a one-line hook.
- **Check:** `HomePosterCardSizingTest` (scale is 1.5 and off elsewhere; five posters, not six, on a 1280 px row; saved width wins) and `AaosPlayerLayoutTest` and `AaosButtonSizesTest` pass; emulator: Home posters about 230 px wide, top menu pill 84 px tall with 48 px icons (was 72 px and 36 px), back arrows 84 px touch (was 60 to 72 px), settings rows at least 80 px tall, and a dialog and a bottom sheet (Settings, Appearance, App Icon; Search, the type filter) at the same scale as the screen behind them.

### C6. Display safe area

- **What and why:** the Polestar 3 screen's rounded corners and bezel cover the outer edge of the app window (`AAOS_CAR_NOTES.md`). Every touch target (not just its icon) sits at least 24 px from the left and right window edges: that is 16 dp under the C5 scale. Learned from Flow, whose top bar settings cog was only partly tappable at 4 dp (owner report 2026-10-05). Audited on the emulator on 2026-11-16 (Home, Search, Library, Settings, details, player): the scale brought most gaps to 24 px or more; two needed a hand fix (the settings sidebar rows and the details back arrow, both 18 px before).
- **Where:** `aaosEdgeGap()` in `core/ui/AaosScale.kt`, used by one marked line in `features/settings/SettingsComponents.kt` (sidebar rows) and two in `features/details/MetaDetailsScreen.kt` (back arrows). Any other top bar, side button or player control near the edges follows the rule.
- **After a parent update:** keep the marked lines; check new or moved edge controls against the rule and wrap their edge padding in `aaosEdgeGap()` if they fall short.
- **Check:** emulator: read the control's bounds from the accessibility tree (or measure a screenshot); nothing tappable starts closer than 24 px to the left or right edge, apart from row items scrolling off the edge.

### C7. Playback defaults

- **What and why:** to start playing with as few taps as possible, profiles with no saved value default to "Reuse last link" on and `FIRST_STREAM` autoplay. Saved values always win.
- **Where:** `features/player/PlayerSettingsRepository.kt` (the defaults); test `commonTest/.../features/streams/StreamAutoPlayLoadingPolicyTest.kt`.
- **After a parent update:** keep both defaults for unset values.
- **Check:** a fresh profile on the emulator shows both settings with our defaults.

### C8. Playback lifecycle

- **What and why:** Android playback pauses when the app leaves the foreground, even though its now-playing media session is active (otherwise video audio carries on behind other car screens). Picture-in-picture playback may continue; finishing the activity always pauses.
- **Where:** `composeApp/src/androidMain/kotlin/com/nuvio/app/features/player/PlayerEngine.android.kt`.
- **After a parent update:** keep the pause rule.
- **Check:** `PlaybackLifecyclePolicyTest` passes.

### C9. Car media card

- **What and why:** the car's home screen card (bottom left) shows Nuvio's last item with title, episode, poster and a play button, even after Nuvio is closed. AAOS only treats an app as a media source if it exposes a `MediaBrowserService`, and the card reads the session that service hands out. The player and that service share one process-wide `MediaSession`. The last title, subtitle, artwork, position and duration are saved; when the car connects with no player running, the session is refilled from disk as paused (never `STATE_NONE`, which hides the card), and when the player closes it keeps the item and switches to paused instead of clearing it. The card only shows artwork from a local `content://` address, so a provider serves the saved poster. Video cannot play without the app and Android blocks a background app from opening itself, so pressing play while Nuvio is closed shows the prompt "Open Nuvio to continue watching" with an "Open Nuvio" button, then returns to paused after 15 s. The car's media screen shows a "Continue watching" tab with the last item.
- **Refill after a full restart (added 2026-11-17):** the car's launcher only refills the card after a restart for a media service carrying `androidx.car.app.launchable=true`, and that flag alone makes the app icon open the car's media screen instead of Nuvio (`AAOS_CAR_NOTES.md`). On cars that report the feature `android.software.car.templates_host.media`, Google's car media app (since March 2025) opens the activity named in the service's `androidx.car.app.media.CalMediaActivityComponent` meta-data instead of its own media screen (`MediaDispatcherActivity.maybeLaunchCalComponentForMediaSource`; it reads the value with `ComponentName.unflattenFromString`, so it must be `package/class`, and it switches the activity on itself). So the manifest has a **second** media service, `NuvioCarMediaLaunchableService` (a subclass, same behaviour), switched off, carrying `launchable=true` and `CalMediaActivityComponent` = `${applicationId}/com.nuvio.app.features.player.NuvioCarMediaEntryActivity`, a screenless activity that opens Nuvio's launcher activity and closes (no launcher entry of its own). When Nuvio opens (`initializePlatform`), `NuvioCarMediaSession.selectBrowseService` switches on exactly one service: the opted-in one (and the plain one off) only when the car reports that feature, otherwise the plain one; the session's browse-service extra names the one that is on now and is updated after a switch. It is never called from a service start, because a service the car is binding must not switch itself off. Cars without the feature behave as before. Emulator results (2026-11-17, Google's launcher, car media app 37): the app grid shows a second Nuvio entry once Nuvio has opened (Google's launcher lists the launcher activity and the opted-in service separately); both entries and the card itself open Nuvio through the redirect; after a cold restart, without opening Nuvio, the card shows the last item paused with title and picture. Not yet tried on the car.
- **Card after a force-stop or update (added 2026-11-18, untested on the car):** after a force-stop or an update the launcher's link to the opted-in service is dead and never rebuilt, so the card stayed blank even after playing, until the owner switched to another media app and back. Google's `CarMediaService` only changes the media source (which makes the launcher reconnect the card) when a session that becomes active names, in the `android.media.session.BROWSE_SERVICE` extra, a **different switched-on** service of the app than the current source. So there are now **three** services: the plain one and two identical launchable twins, `NuvioCarMediaLaunchableService` (A) and `NuvioCarMediaLaunchableServiceB`, both off in the manifest with the same meta-data. On cars with the feature, `selectBrowseService` keeps whichever twin is on (neither: A) and never swaps. `NuvioCarMediaBrowserService.onGetRoot` sets a process-wide "car linked" flag when a client other than the app binds. Just before this process first publishes a playing or buffering state (`NuvioCarMediaSession.relinkIfNeeded`, called from one marked line in `PlayerNowPlayingController.publishPlaybackState`), if the car has not linked, it switches on the other twin, names it in the session extras, then switches the old one off. Once per process. A harmless extra swap can happen if Nuvio plays while another app owns the card. Cars without the feature and phones are unchanged.
- **Never** put `androidx.car.app.launchable` on the always-on `NuvioCarMediaBrowserService`: on the Polestar it makes the app icon open the car's media screen instead of Nuvio (2026-10-05). It lives only on the gated second service. Emulator result and car status: `AAOS_CAR_NOTES.md`.
- **Where:** `composeApp/src/androidMain/kotlin/com/nuvio/app/features/player/`: `NuvioCarMediaSession.android.kt` (incl. `selectBrowseService`, `relinkIfNeeded`), `NuvioCarMediaBrowserService.android.kt` (all three services; declared in `androidApp/src/main/AndroidManifest.xml` with the `android.media.browse.MediaBrowserService` intent filter), `NuvioCarMediaEntryActivity.android.kt` (in the manifest, no intent filter), `NuvioCarMediaArtworkProvider.android.kt` (authority `${applicationId}.carmediaart`), and `PlayerNowPlayingController.android.kt` (publishes to the shared session, with rich metadata and the `content://` artwork address; one marked `relinkIfNeeded` line before the first playing state); one marked line in `composeApp/src/androidMain/kotlin/com/nuvio/app/Platform.android.kt` (`initializePlatform` calls `selectBrowseService`, so the switch happens when Nuvio opens); saved state in shared preferences `nuvio_car_media_card` and `files/car_media_card_artwork.png`.
- **After a parent update:** keep the controller on `NuvioCarMediaSession` (no private `MediaSession` of its own) and publishing the `content://` artwork; keep all three services, the entry activity and the `relinkIfNeeded` line and the provider in the manifest, and the marked line in `Platform.android.kt`. **To remove this feature**, ship a build that resets all three services to their manifest default (`COMPONENT_ENABLED_STATE_DEFAULT`) when Nuvio opens: the switched-off state of a service survives app updates, so deleting the code alone would leave Nuvio with no media service at all.
- **Check:** `./gradlew :composeApp:testAndroidHostTest --tests '*PlayerNowPlaying*' --tests '*NuvioCarMediaBrowseServiceSelection*' --tests '*NuvioCarMediaRelink*'` passes; the merged release manifest shows both launchable twins off with both meta-data and the value `com.JF_Nuvio/com.nuvio.app.features.player.NuvioCarMediaEntryActivity`; then follow "Testing like the car" in `AAOS_CAR_NOTES.md`: open Nuvio from the app grid (every Nuvio entry opens Nuvio, never the car's media screen), play something, check the card, reboot the emulator, check the card (last item, paused, with title and picture) and the icon again; then force-stop Nuvio, open it from the grid, play, and check the card shows the item (blank before 2026-11-18).

### C10. Bundle packaging

- **What and why:** ABI splits break App Bundle builds, so they are disabled whenever a bundle task is requested; Play delivers the right ABI from the bundle anyway.
- **Where:** `androidApp/build.gradle.kts`.
- **After a parent update:** keep it; re-test before removing.
- **Check:** `./gradlew :androidApp:bundlePlaystoreRelease` passes.

### C11. Fork docs

- **What and why:** these files are the memory that survives between agent chats and the owner's only way to steer agents.
- **Where:** `README.md` (contract in section 9); `AGENTS.md` (fully fork-owned in this repo); `CLAUDE.md`, `GEMINI.md`; `.claude/skills` (a link to `.github/skills`); `.github/skills/aaos-*`; every `AAOS_*.md` file; the `aaos/` folder (the customisation map and report, section 7). On the owner's Mac only (git-ignored, never committed): `handover/` and `CLAUDE.local.md`.
- **After a parent update:** keep ours. If the parent adds its own `AGENTS.md` text, keep our block at the top and the parent's text below it.
- **Check:** the files exist and the README banner is intact.

### C12. Build tools

- **What and why:** the fork builds with a newer Android Gradle Plugin (9.4.1) and Gradle (9.6.0) than the parent, lets Gradle download a matching Java automatically (the `foojay` plugin and `gradle/gradle-daemon-jvm.properties`), and keeps built `.apk`/`.aab` files, release folders and mapping files out of git. Added with the parent port of 2026-10-01; the reason was not recorded, most likely the owner's Android Studio version.
- **Where:** `gradle/libs.versions.toml` (`agp`), `gradle/wrapper/gradle-wrapper.properties`, `settings.gradle.kts`, `gradle/gradle-daemon-jvm.properties`, `.gitignore`.
- **After a parent update:** if the parent moves to the same or newer versions, take the parent's and drop ours. Otherwise keep ours; never go below the parent's versions.
- **Check:** the "Checks to run" builds pass in `AAOS_UPSTREAM_SYNC.md`.

### C13. One-command release

- **What and why:** the owner no longer signs in Android Studio and uploads in Play Console by hand. `aaos/release.sh` runs the fork's own host tests, checks every fork file is labelled, builds `:androidApp:bundlePlaystoreRelease`, signs it with the upload-key details the owner stored once in the login Keychain (service `nuvio-release`, set by `aaos/setup-release.sh`), checks the signature and application ID, and `aaos/play-upload.mjs` (which stops if Play reports a different version code) releases it to Play **Internal testing only** (only `automotive:internal`, `automotive:qa`, `internal`, `qa` can be written; any other track is refused; a version code not above Play's highest is refused). It never raises the version. Modes: none = real release, `--check` = sign in and look, `--dry-run` = upload to a throwaway edit and discard. After a real release it saves `Nuvio-v<name>-build<code>.aab` (and its mapping file) in the Play Console folder and makes the local tag `play-<code>`. Added 2026-11-19; copied from the car app's script. The Play service-account key is the shared file `~/.config/claude-car-play/key.json` (override: env `CLAUDE_CAR_PLAY_KEY`).
- **Where:** `aaos/release.sh`, `aaos/play-upload.mjs`, `aaos/setup-release.sh`, `aaos/test/play-upload.test.mjs` (fork-only; they live in `aaos/` because the parent ignores `scripts/*`), and one marked block (`// AAOS C13`) in `androidApp/build.gradle.kts` that reads the Keychain first and falls back to `local.properties`.
- **After a parent update:** keep the marked block when the parent changes the signing lines; take the parent's code around it. Never log the values.
- **Check:** `node --test aaos/test/play-upload.test.mjs` passes (14 tests); `bash -n` on the two shell scripts; `aaos/release.sh --bogus` exits 2; `./gradlew :androidApp:bundlePlaystoreRelease` builds and `jarsigner -verify` on the bundle says "jar verified" (with and without the Keychain items).
