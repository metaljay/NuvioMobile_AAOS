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

## 🔄 Keeping this fork up to date and improving it

This fork follows [NuvioMobile](https://github.com/NuvioMedia/NuvioMobile). `main` holds the parent's code plus the AAOS customisations. There are only two jobs, and you do both by pasting one short message to an AI agent (Claude Code, GitHub Copilot, Codex or Gemini) opened in this repository. The agent does the work, reports in plain English, and finishes by preparing the Google Play release. No git knowledge is needed, and nothing is ever pushed to the parent.

### Job 1: update from the parent
```
Follow the instructions in .github/skills/aaos-sync/SKILL.md exactly.
```
The agent merges the parent's changes on a temporary branch, re-applies the car customisations, builds a test version, and tells you how to try it in the Automotive emulator. It **stops until you reply** `approve sync` (or `cancel sync`, or `problem: ...`). Only then does it update `main` and prepare the release.

### Job 2: tweak or fix something
```
Follow the instructions in .github/skills/aaos-tweak/SKILL.md exactly. The tweak: [describe what you want changed, in plain English].
```
The agent makes the change, checks the app builds, records it (log and customisation list), pushes it, then prepares the release.

### The release (both jobs end here)
The agent raises the version number automatically (version numbers only have to go up; they drift from the parent's and that is fine), builds the bundle, and gives you the exact clicks to sign it in Android Studio and upload it to Google Play Internal testing. Say `Bundle built` when it is signed and the agent collects the file into a folder called For upload to Play Console. To run only this stage: `Follow the instructions in .github/skills/aaos-release/SKILL.md exactly.`

In Claude Code or GitHub Copilot you can type `/aaos-sync`, `/aaos-tweak` or `/aaos-release` instead. An optional `/aaos-uploaded` records what you uploaded.

### What protects the car customisations
- `AAOS_FORK.md`: rules, safety rails and the customisations that must survive every merge.
- `AAOS_UPSTREAM_SYNC.md`: the step-by-step procedure, the files most likely to conflict, and how to roll back.
- `AAOS_CAR_NOTES.md`: facts learned on the real car (app icon, media card, screen edges, Play), identical in both forks.
- `AAOS_RELEASE.md`: release steps and the version rule. `AAOS_LOG.md`: dated history of what changed and what was verified (older entries in `AAOS_LOG_ARCHIVE.md`).
- `AGENTS.md` is the entry point for AI agents (`CLAUDE.md` and `GEMINI.md` point to it); the recipes live in `.github/skills/`.

## 📚 Project references

- **[AAOS fork rules, invariants and inventory](AAOS_FORK.md)** — the required AAOS behavior,
  product rules, and implementation inventory for this fork.
- **[AAOS release steps](AAOS_RELEASE.md)** — the versioning and Play release workflow for this fork.
- **[Upstream NuvioMobile](https://github.com/NuvioMedia/NuvioMobile)** — the parent project this
  fork is based on.

## 📄 License

Nuvio is distributed under the [GNU General Public License v3.0](LICENSE). See the license file
for the terms that apply to this fork and its upstream project.
