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

This fork follows [NuvioMobile](https://github.com/NuvioMedia/NuvioMobile). `main` holds the parent's code plus the AAOS customisations. Parent updates are never merged straight into `main`: an AI agent first applies them on a temporary `sync/` branch, re-applies the car customisations, runs checks, and waits for the owner's approval. Nothing is ever pushed to the parent. No git knowledge is needed: you paste short messages to an AI agent (GitHub Copilot, Codex or Gemini) opened in this repository, and it does the work and reports back in plain English.

### 1. Check for and review a parent update
Paste this to your agent:
```
Follow the instructions in .github/skills/aaos-sync/SKILL.md exactly.
```
In GitHub Copilot you can type `/aaos-sync` instead. The agent tells you whether anything is new. If it is, it prepares and checks the update, then **stops and explains the changes in plain English**. Nothing reaches `main` until you reply `approve sync`. Reply `cancel sync` to discard the update safely. To see more detail first, ask: "Explain the three biggest changes and whether any touch the car customisations."

### 2. Prepare a release
```
Follow the instructions in .github/skills/aaos-release/SKILL.md exactly.
```
The agent builds and checks the release bundle, then gives you the exact clicks to sign it in Android Studio and upload it to Google Play Internal testing. Every Play upload needs a higher version code than the last one, whatever the parent's version says; the agent handles this.

### 3. Record the upload
After Play accepts the upload, paste this (replace 33 with the version code you uploaded):
```
Follow the instructions in .github/skills/aaos-uploaded/SKILL.md exactly. The version code uploaded was 33.
```

### What protects the car customisations
- `AAOS_FORK.md`: rules, safety rails and the customisations that must survive every merge.
- `AAOS_UPSTREAM_SYNC.md`: the step-by-step procedure, the files most likely to conflict, and how to roll back.
- `AAOS_RELEASE.md`: release steps and the version-code rule. `AAOS_LOG.md`: dated history of what changed and what was verified.
- `AGENTS.md` is the entry point for AI agents; the recipes live in `.github/skills/`.

## 📚 Project references

- **[AAOS fork rules, invariants and inventory](AAOS_FORK.md)** — the required AAOS behavior,
  product rules, and implementation inventory for this fork.
- **[AAOS release steps](AAOS_RELEASE.md)** — the versioning and Play release workflow for this fork.
- **[Upstream NuvioMobile](https://github.com/NuvioMedia/NuvioMobile)** — the parent project this
  fork is based on.

## 📄 License

Nuvio is distributed under the [GNU General Public License v3.0](LICENSE). See the license file
for the terms that apply to this fork and its upstream project.
