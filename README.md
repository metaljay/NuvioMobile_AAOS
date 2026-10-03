<div align="center">

<strong>🚗 Android Automotive OS (AAOS) Fork</strong>

<br><br>

<img src="composeApp/src/commonMain/composeResources/drawable/app_icon_original.png" alt="Nuvio app icon" width="112">

# Nuvio for Android Automotive

### An AAOS-focused adaptation of Nuvio, designed with the Polestar 3 in mind

Larger, more readable screens and comfortable controls for Nuvio on a vehicle display.

<br>

[![Android Automotive OS](https://img.shields.io/badge/Platform-Android_Automotive_OS-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/training/cars)
[![Forked from Nuvio](https://img.shields.io/badge/Forked_from-Nuvio-4285F4?style=for-the-badge&logo=github&logoColor=white)](https://github.com/NuvioMedia/NuvioMobile)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-orange?style=for-the-badge&logo=gnu&logoColor=white)](LICENSE)

<br>

[AAOS fork rules, invariants and inventory](AAOS_FORK.md) · [Release steps](AAOS_RELEASE.md) · [Upstream Nuvio](https://github.com/NuvioMedia/NuvioMobile) · [License](LICENSE)

</div>

---

## 🚘 About this fork

This is an independent fork of [NuvioMobile](https://github.com/NuvioMedia/NuvioMobile), adapted
for **Android Automotive OS (AAOS)** with the user's Polestar 3 as its primary target. It preserves
Nuvio's upstream feature policies while carrying focused changes for the vehicle display,
automotive sign-in, and the fork's existing Google Play app identity.

AAOS is the operating system built into compatible vehicles; Android Auto projection is a
different platform.

## ✨ What’s different

| | Automotive-focused changes |
| --- | --- |
| 👀 | **Easier to read at a glance**, with larger typography and shared icons on AAOS screens. |
| 👆 | **More comfortable touch controls**, including enlarged navigation, player, seek, and back-button targets. |
| 🖼️ | **Roomier home browsing**, with larger posters, clearer shelf labels, and spacing tuned for the car display. |
| 🧭 | **A more focused browsing layout**, with larger search controls and Discover limited to four columns. |
| 🔐 | **Device-code sign-in for the head unit**, with an optional trusted-server flow—including `api.nuvio.tv`—if the default sign-in path needs a fallback. |
| 📱 | **Dedicated Android app identity** (`com.JF_Nuvio`) to update the existing fork listing. |

The AAOS-specific sizing is intended to improve legibility and reachability in the vehicle; it does
not imply that every screen or action is appropriate while driving. For implementation details,
the rationale behind each customization, and requirements to preserve during upstream updates, see
the **[AAOS fork rules, invariants and inventory](AAOS_FORK.md)**.

## 🧭 Internal testing and installation

**This fork does not publish public GitHub release downloads.** Its intended distribution path is a
signed Android release App Bundle uploaded to the existing Google Play **Internal testing** track.
Invited testers can then install or update the app from Google Play on a compatible AAOS vehicle.
It is not a public Play Store listing.

The Android debug build uses the separate application ID `com.JF_Nuvio.debug` and is for emulator
development and testing only. It is not the release package for the Polestar 3.

## 🛠️ Building from source

To build the Play Store release bundle locally:

```bash
./gradlew :androidApp:bundlePlaystoreRelease
```

A successful local build is not a published release. Uploading to Play requires the authorized
release-signing configuration and access to the Play Console internal testing track. Use the
signed `com.JF_Nuvio` release variant for that workflow.

## 🔄 Keeping this fork up to date with its parent

This fork follows [NuvioMobile](https://github.com/NuvioMedia/NuvioMobile). `main` holds the parent's code plus the AAOS customisations. Parent updates are never merged straight into `main`: they are reviewed on a temporary `sync/` branch first, and nothing is ever pushed to the parent. The procedure is written down for people **and** AI agents, so the changes this fork needs for the car survive every update.

### Quick how-to

1. **See what is new.** Run `git fetch upstream`, then `git log --oneline main..upstream/cmp-rewrite`. No output means there is nothing to sync.
2. **Pull the update safely.** Ask your AI agent to run the `aaos-sync` recipe. GitHub Copilot lists it as `/aaos-sync`; with any other agent say: "Follow the instructions in .github/skills/aaos-sync/SKILL.md exactly." It merges the parent into a `sync/` branch, re-applies the AAOS customisations, restores the fork-only values (application ID, Play version code, README banner), runs the checks, and only then merges into `main`.
3. **Inspect the changes.** To review before anything reaches `main`, add "stop after the checks and before merging into main" to your request, then look at `git diff --stat main..sync/<date>` (what changed) and `git diff main..sync/<date> -- <file>` for any file in the hotspot table of `AAOS_UPSTREAM_SYNC.md`. After it has landed, `git diff --stat pre-sync-<date> main` shows the same thing (the procedure tags the last good state before it starts). Then read the newest entry in `AAOS_LOG.md` (conflicts, how they were resolved, what was verified and what was not) and tick through the invariants in `AAOS_FORK.md` section 5.
4. **Release.** The `aaos-release` recipe (`/aaos-release`) prepares and checks a release bundle. Sign it in Android Studio (Build, then Generate Signed App Bundle), upload it to Google Play Internal testing, then run `aaos-uploaded <version code>` (`/aaos-uploaded <code>`) to record it. Every upload needs a higher version code than the last one, whatever the parent's version says.

### What protects the customisations
- `AAOS_FORK.md`: rules, safety rails (no force-push, never push to the parent, never commit keys), and the list of customisations that must survive every merge.
- `AAOS_UPSTREAM_SYNC.md`: the step-by-step procedure, the files most likely to conflict, and how to roll back.
- `AAOS_RELEASE.md`: release steps and the version-code rule. `AAOS_LOG.md`: dated history of what changed and what was verified.
- The recipes live in `.github/skills/` and `AGENTS.md` is the entry point for AI agents.

## 📚 Project references

- **[AAOS fork rules, invariants and inventory](AAOS_FORK.md)** — the required AAOS behavior,
  product rules, and implementation inventory for this fork.
- **[AAOS release steps](AAOS_RELEASE.md)** — the versioning and Play release workflow for this fork.
- **[Upstream NuvioMobile](https://github.com/NuvioMedia/NuvioMobile)** — the parent project this
  fork is based on.

## 📄 License

Nuvio is distributed under the [GNU General Public License v3.0](LICENSE). See the license file
for the terms that apply to this fork and its upstream project.
