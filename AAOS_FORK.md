# Nuvio AAOS fork: rules and customisations

> Fork-owned file (the parent project does not have it, so parent updates never overwrite it). Sections marked **(shared)** are word for word the same in the Flow and Nuvio forks: change them in both repos in the same change. Everything else is specific to this app. If reality changes, fix this file in the same change.

## 1. What this repo is

The owner's fork of [Nuvio](https://github.com/NuvioMedia/NuvioMobile), a film and TV streaming app, customised so it is readable and usable on the owner's Polestar 3 running Android Automotive OS (AAOS). It reaches the car only through Google Play **Internal testing**. The sister fork is Flow, next to this repo in the AAOS folder. Only Android is deployed; the iOS app is kept compiling so parent updates merge cleanly.

| Fork fact | Value |
| --- | --- |
| Parent project (upstream) | https://github.com/NuvioMedia/NuvioMobile, branch `cmp-rewrite` (the only parent branch ever synced) |
| Our fork (origin) | https://github.com/metaljay/NuvioMobile_AAOS, branch `main` |
| Play application ID (release) | `com.JF_Nuvio` |
| Code namespace | `com.JF_Nuvio` / `com.JF_Nuvio.android`, renamed from the parent's `com.nuvio.app` (C1) |
| Debug build | `com.JF_Nuvio.debug`: emulator only, never on the car |
| Release facts | `AAOS_RELEASE.md` (module, build variant, version file, last upload) |
| Parent update facts | `AAOS_UPSTREAM_SYNC.md` (last merged parent commit, checks, hotspots) |

## 2. The jobs (shared)

Every change is meant for the owner's car, so there are only these jobs. The owner starts each one by pasting a prompt from `README.md`.

| Job | Recipe | Ends with |
| --- | --- | --- |
| Update from the parent | `.github/skills/aaos-sync/SKILL.md` | the approval report in `AAOS_UPSTREAM_SYNC.md`, the owner's `approve sync`, then a release |
| Tweak or fix for the car | `.github/skills/aaos-tweak/SKILL.md` | a release |
| Release | `.github/skills/aaos-release/SKILL.md` | a bundle the owner signs and uploads (also runs on its own) |
| Record an upload (optional) | `.github/skills/aaos-uploaded/SKILL.md` | "Last uploaded to Play" updated in `AAOS_RELEASE.md` |

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
| C1 | Play identity and package rename | Release application ID `com.JF_Nuvio`; all code renamed from `com.nuvio.app` to `com.JF_Nuvio`. |
| C2 | AAOS manifest and runtime | Automotive features optional, `distractionOptimized`, car-friendly activity settings, Automotive detected at start. |
| C3 | Device-link sign-in | On the car, sign-in starts automatically and is completed on a phone at `nuvio.tv/link`. |
| C4 | Custom-server fallback | "Connect to another server" stays available so `api.nuvio.tv` can be chosen if sign-in fails. |
| C5 | AAOS readability | Larger text, icons, posters, search and player controls, only on Automotive. |
| C6 | Display safe area | Touch targets at least 16 dp from the left and right window edges (not yet audited in Nuvio). |
| C7 | Playback defaults | New profiles default to "Reuse last link" on and first-stream autoplay. |
| C8 | Playback lifecycle | Playback pauses when the app leaves the screen. |
| C9 | Car media card | The car's home screen card shows Nuvio's last item; no `androidx.car.app.launchable`. |
| C10 | Bundle packaging | ABI splits are off for bundle tasks. |
| C11 | Fork docs | README, AGENTS file, agent entry points, recipes and `AAOS_*.md` files stay. |

Each one has a detail block in Part 2: what and why, where it lives, how to redo it after a parent update, and how to check it. Paths in Part 2 are under `composeApp/src/commonMain/kotlin/com/JF_Nuvio/` unless they say otherwise.

## 8. Deliberately NOT customised

- **The parent's Play Store feature policy**: the Play Store build keeps the parent's disabled features, except custom-server connections (C4). Do not delete policy files.
- **Phone and tablet sizing**: stays the parent's. AAOS sizes are gated to Automotive; the shared details-screen buttons and search-field style in C5 are the only exceptions.
- **Saved user preferences**: a saved value (poster width, playback settings) always wins over our defaults.
- **MPVKit submodule**: stays at the parent-published commit (a stale `d5cf091` checkout was reset to `bb1d0250` on 2026-10-02). Its `libass-android` reference is dangling; the parent has the same dangling reference.
- **iOS**: not deployed. The fork's iOS app, debug and widget identifier overrides in `Config.xcconfig` and the Xcode project are carried forward; the parent's identifiers in untouched Release configurations stay unchanged. `iosApp/Configuration/Version.xcconfig` holds the shared version numbers (`AAOS_RELEASE.md`).

## 9. README contract (shared)

`README.md` is written for the owner, a person with no coding experience. Every merge or refresh keeps these sections, in this order:

1. Banner: `🚗 Android Automotive OS (AAOS) Fork`, the app's logo and name, and links to the parent project and the license.
2. **What this is**: an AAOS fork of the parent app for the owner's Polestar 3, not Android Auto.
3. **What's different in the car**: an accurate plain-English summary, one row per customisation, each linking to its Part 2 block in `AAOS_FORK.md`.
4. **Getting it on the car**: Google Play Internal testing only; no public downloads.
5. **What do you want to do?**: one section per job in section 2, each with a copy-paste prompt and what happens next, plus help for when something goes wrong.
6. **Behind the scenes**: which file holds what, for the curious.
7. **License**.

Keep code, file paths and jargon out of sections 1 to 5 apart from the prompts. Only use screenshots of this fork that exist and are accurate. On a README conflict in a parent update, keep ours (the parent's README describes the parent app).

## 10. Keeping these docs current (shared)

- **After every verified change**: add a dated entry to `AAOS_LOG.md`; if a customisation was added, changed or removed, update section 7, its Part 2 block, the hotspot table in `AAOS_UPSTREAM_SYNC.md` and, if the owner would notice it, the README summary; after a confirmed upload, update "Last uploaded to Play" in `AAOS_RELEASE.md`. The recipes make these updates.
- **One home per fact.** Rules and customisations live here; release facts and steps in `AAOS_RELEASE.md`; parent-update steps and the approval report in `AAOS_UPSTREAM_SYNC.md`; car facts in `AAOS_CAR_NOTES.md`; history in `AAOS_LOG.md`. Other files link to these instead of repeating them.
- **Both repos together.** Flow and Nuvio share the same car and the same doc skeleton. Car facts go into `AAOS_CAR_NOTES.md` in **both** repos in the same change (the file is identical in both). A change to a **(shared)** section, the README layout or a recipe is made in both repos too.
- **Keep the log short.** `AAOS_LOG.md` keeps the newest 15 entries. When adding an entry would make more, move the oldest entries (unchanged) to the top of `AAOS_LOG_ARCHIVE.md`. A release-preparation entry is at most three lines.
- **No contradictions, no stale text.** When a number or a rule changes, search `README.md`, `AGENTS.md`, the recipes and every `AAOS_*.md` file for the old wording and update every mention. Delete obsolete text rather than adding a correction next to it.

## 11. Environment notes

Shared (both repos):

- Builds run on the owner's Mac with Android Studio. The AAOS folder holds both repos, the release key `Key.jks` (outside both repos) and the `For upload to Play Console` folder for signed bundles (outside git).
- Emulator used for checks: `Automotive_Large_Portrait` (1280 x 1606). It does not prove Polestar 3 behaviour. If `adb` or the emulator is unavailable, say so; never claim device verification from a compile.
- If tests fail with "Unsupported class file major version 71", the default Java is too new: rerun with Android Studio's bundled Java (`JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"` in front). It is an environment problem, not real failures.

Nuvio only:

- macOS Finder copies create stray duplicates named `* 2.kt`, `* 2.xml` or `* 2` folders (347 had to be removed once). They break builds. Never leave files with a ` 2` suffix; check for them after any copy or merge.
- Common iOS test sources compile, but the iOS simulator tests cannot run on this Mac (simulator SDK not installed).

---

# Part 2: Customisation details

Each block uses the same four headings. **After a parent update** is what the sync recipe must do to keep the customisation working in the parent's new code; **Check** is how to prove it.

### C1. Play identity and package rename

- **What and why:** the car installs Nuvio from our existing Play listing, which only accepts updates carrying the application ID `com.JF_Nuvio`. The fork also renamed the whole Android namespace and Kotlin packages from the parent's `com.nuvio.app` to `com.JF_Nuvio` / `com.JF_Nuvio.android`. The debug build is `com.JF_Nuvio.debug`, for the emulator only.
- **Where:** `androidApp/build.gradle.kts` (application ID, signing properties); every Kotlin package; `iosApp/Configuration/Version.xcconfig` (version numbers, see `AAOS_RELEASE.md`); iOS overrides (section 8).
- **After a parent update:** re-apply the rename to every new or changed parent file. Do not do a blind search-and-replace: check store, signing and iOS configuration files by hand, because some parent identifiers there must stay as they are (section 8). Keep our application ID and version numbers on any conflict.
- **Check:** the merged **release** manifest shows `com.JF_Nuvio`; no `com.nuvio.app` package remains in Kotlin code.

### C2. AAOS manifest and runtime

- **What and why:** lets the car recognise Nuvio as a car app and run it well in the car's window: Automotive and portrait/landscape screen features optional (so Play still allows the install); `distractionOptimized=true` on the application and all launcher activities; launcher and main activities resizable, `singleTask`, cutout-aware, picture-in-picture capable, and handling keyboard, navigation, UI mode, density and font changes themselves; Automotive detected at runtime and the platform initialised with the Android context before any UI.
- **Where:** `androidApp/src/main/AndroidManifest.xml`; `Platform.kt` and `composeApp/src/androidMain/kotlin/com/JF_Nuvio/Platform.android.kt` (Automotive detection).
- **After a parent update:** keep every AAOS entry; give any new parent launcher activity the same settings and metadata.
- **Check:** merged release manifest; the app opens from the emulator's app grid.

### C3. Device-link sign-in

- **What and why:** the car has no browser, so on Automotive the official-server device-link sign-in starts automatically and shows the instruction to finish signing in on a phone at `nuvio.tv/link`. The server menu stays on the sign-in screen so a custom backend can be chosen (C4).
- **Where:** `features/auth/AuthScreen.kt`, `features/auth/DeviceLinkAuthSection.kt`, `core/auth/DeviceLinkAuthRepository.kt`.
- **After a parent update:** keep the automatic start on Automotive and the phone instruction; never open a browser on the car.
- **Check:** emulator: the sign-in screen shows the `nuvio.tv/link` instruction and code without a tap.

### C4. Custom-server fallback

- **What and why:** if the default sign-in path does not offer or complete device-code login, the owner taps **Connect to another server**, enters `api.nuvio.tv`, reviews the discovered server, chooses **I trust this server**, and device-code login becomes available.
- **Where:** `composeApp/src/androidPlaystore/kotlin/com/JF_Nuvio/core/build/AppFeaturePolicy.android.kt` (`customServerConnectionsEnabled = true`); `core/network/ServerDiscovery.kt` (`ServerDiscoveryPolicy.isOfficial` must not reject the canonical `api.nuvio.tv`, while a distinct configured backend URL is still rejected); `core/network/ServerConfiguration.kt` (built-in backend default `https://api.nuvio.tv` when `NUVIO_SUPABASE_URL` is unset; without a publishable key, device-link sign-in reads it from the canonical API discovery document and validates the backend before use); the auth-observer rebinding and saved-session fixes in the auth code.
- **After a parent update:** keep the policy enabled and the `api.nuvio.tv` handling. Keep the parent's server discovery, trust screen, saved selection, server-switch reset and TV-login capability checks; never bypass the trust screen or force the server globally.
- **Check:** `ServerDiscoveryPolicyTest` passes (it asserts the canonical API is not rejected).

### C5. AAOS readability (Automotive only)

- **What and why:** makes Nuvio readable at a glance and easy to tap on the car display, without changing phone or tablet sizing:
  - **Text and icons:** a 1.15 minimum text scale and larger shared icon tokens on Automotive screens.
  - **Navigation:** 40 dp home navigation icons with a larger vertical hit area on Automotive, including compact layouts; larger floating navigation icons and labels. The parent's 24/28 dp sizing stays off Automotive.
  - **Home posters:** 185 x 278 dp default poster cards with 14 dp corners, 16 dp shelf spacing and larger shelf and poster text (`headlineSmall` headings, `titleMedium` titles, `bodySmall` details); the parent had reduced these to 126 x 189 dp, 10 dp and smaller text. A 230 dp minimum width for home poster, catalogue and folder tiles, poster-style Continue Watching cards and loading skeletons, so a 1280 dp screen fits five tiles instead of six (not landscape cards, library or detail rails, or global poster settings). 230 dp is a minimum: a larger saved width wins; placeholders match the content width; landscape Continue Watching sizes come from the user's poster width. Do not bring back the old fork's `>=160dp` reload cutoff (it breaks the valid 104 to 140 dp presets).
  - **Search:** a 64 dp search field with `titleMedium` text; Discover capped at four columns; on Automotive and tablet layouts, 68 dp of top clearance above the search header (below the floating navigation pill), 30 dp below the field and 64 dp at the end of the list. Non-Automotive result spacing is unchanged.
  - **Player:** substantially larger controls and labels at wide layouts (768, 1024 and 1440 dp breakpoints); larger header, seek and skip, play/pause, action-pill and timeline icons and touch targets; seek controls use the dedicated side-icon size, not the play-icon size; trailers use the same sizes.
  - **Back controls:** the player exit arrow larger than the header icons next to it; 44 dp arrows in the source selector (68 dp target), player toolbar and opening overlay.
  - **Details (shared with phones and tablets):** 48 dp back controls with 28 dp arrows; 56 dp play/resume buttons (60 dp on tablets) with `titleMedium` text.
- **Where:** `core/ui/NavigationBar.kt`; `composeApp/src/androidMain/kotlin/com/JF_Nuvio/core/ui/` (`jelly/JellyTabs.kt`, `FloatingNavigationBar.android.kt`); `features/home/HomeScreen.kt` and `features/home/components/` (`HomePosterCardSizing.kt`, `HomePosterCard.kt`, `HomeCatalogSection.kt`, `HomeCollectionRowSection.kt`, `HomeContinueWatchingSection.kt`, `HomeSkeletonLoading.kt`); `features/search/SearchScreen.kt`; `features/player/` (`PlayerControls.kt`, `PlayerControlActions.kt`, `OpeningOverlay.kt`); `features/streams/StreamsScreen.kt`; `features/details/MetaDetailsScreen.kt`, `features/details/components/DetailFloatingHeader.kt`, `DetailActionButtons.kt`.
- **After a parent update:** re-apply our sizes on the parent's new layouts, gated to Automotive (except the details buttons); keep the parent's logic and structure.
- **Check:** `HomePosterCardSizingTest` passes (five versus six cards at 1280 dp, non-AAOS unchanged); emulator: text, posters and controls visibly larger.

### C6. Display safe area

- **What and why:** the Polestar 3 screen's rounded corners and bezel cover the outer edge of the app window (`AAOS_CAR_NOTES.md`). Every touch target (not just its icon) sits at least 16 dp from the left and right window edges, and icons about 24 to 28 dp in. Learned from Flow, whose top bar settings cog was only partly tappable at 4 dp (owner report 2026-10-05). **Nuvio has not been audited against this yet.**
- **Where:** any top bar, side button or player control near the edges.
- **After a parent update:** check new or moved edge controls against the rule.
- **Check:** emulator screenshot of the screen in question, measuring the distance to the edge.

### C7. Playback defaults

- **What and why:** to start playing with as few taps as possible, profiles with no saved value default to "Reuse last link" on and `FIRST_STREAM` autoplay. Saved values always win.
- **Where:** `features/settings/PlaybackSettingsPage.kt`, `features/streams/StreamAutoPlayPolicy.kt`, `StreamAutoPlayModels.kt`.
- **After a parent update:** keep both defaults for unset values.
- **Check:** a fresh profile on the emulator shows both settings with our defaults.

### C8. Playback lifecycle

- **What and why:** Android playback pauses when the app leaves the foreground, even though its now-playing media session is active (otherwise video audio carries on behind other car screens). Picture-in-picture playback may continue; finishing the activity always pauses.
- **Where:** `composeApp/src/androidMain/kotlin/com/JF_Nuvio/features/player/PlayerEngine.android.kt`.
- **After a parent update:** keep the pause rule.
- **Check:** `PlaybackLifecyclePolicyTest` passes.

### C9. Car media card

- **What and why:** the car's home screen card (bottom left) shows Nuvio's last item with title, episode, poster and a play button, even after Nuvio is closed. AAOS only treats an app as a media source if it exposes a `MediaBrowserService`, and the card reads the session that service hands out. The player and that service share one process-wide `MediaSession`. The last title, subtitle, artwork, position and duration are saved; when the car connects with no player running, the session is refilled from disk as paused (never `STATE_NONE`, which hides the card), and when the player closes it keeps the item and switches to paused instead of clearing it. The card only shows artwork from a local `content://` address, so a provider serves the saved poster. Video cannot play without the app and Android blocks a background app from opening itself, so pressing play while Nuvio is closed shows the prompt "Open Nuvio to continue watching" with an "Open Nuvio" button, then returns to paused after 15 s. The car's media screen shows a "Continue watching" tab with the last item.
- **Never** add `androidx.car.app.launchable` to the service: on the Polestar it makes the app icon open the car's media screen instead of Nuvio. Known, accepted limit: after a full car restart the card stays blank (`AAOS_CAR_NOTES.md`).
- **Where:** `composeApp/src/androidMain/kotlin/com/JF_Nuvio/features/player/`: `NuvioCarMediaSession.android.kt`, `NuvioCarMediaBrowserService.android.kt` (declared in `androidApp/src/main/AndroidManifest.xml` with the `android.media.browse.MediaBrowserService` intent filter), `NuvioCarMediaArtworkProvider.android.kt` (authority `${applicationId}.carmediaart`), and `PlayerNowPlayingController.android.kt` (publishes to the shared session, with rich metadata and the `content://` artwork address); saved state in shared preferences `nuvio_car_media_card` and `files/car_media_card_artwork.png`.
- **After a parent update:** keep the controller on `NuvioCarMediaSession` (no private `MediaSession` of its own) and publishing the `content://` artwork; keep the service and the provider in the manifest.
- **Check:** `./gradlew :composeApp:testAndroidHostTest --tests '*PlayerNowPlaying*'` passes; then follow "Testing like the car" in `AAOS_CAR_NOTES.md`: open Nuvio from the app grid (one icon that opens Nuvio), play something, check the card, reboot the emulator, check the icon and the card again.

### C10. Bundle packaging

- **What and why:** ABI splits break App Bundle builds, so they are disabled whenever a bundle task is requested; Play delivers the right ABI from the bundle anyway.
- **Where:** `androidApp/build.gradle.kts`.
- **After a parent update:** keep it; re-test before removing.
- **Check:** `./gradlew :androidApp:bundlePlaystoreRelease` passes.

### C11. Fork docs

- **What and why:** these files are the memory that survives between agent chats and the owner's only way to steer agents.
- **Where:** `README.md` (contract in section 9); `AGENTS.md` (fully fork-owned in this repo); `CLAUDE.md`, `GEMINI.md`; `.claude/skills` (a link to `.github/skills`); `.github/skills/aaos-*`; every `AAOS_*.md` file.
- **After a parent update:** keep ours. If the parent adds its own `AGENTS.md` text, keep our block at the top and the parent's text below it.
- **Check:** the files exist and the README banner is intact.
