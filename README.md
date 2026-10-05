<div align="center">

<strong>🚗 Android Automotive OS (AAOS) Fork</strong>

<br><br>

<img src="composeApp/src/commonMain/composeResources/drawable/app_icon_original.png" alt="Nuvio logo" width="112">

# Nuvio for AAOS

### Nuvio, the film and TV app, adapted for the Polestar 3's built-in Android Automotive screen

<br>

[![Android Automotive OS](https://img.shields.io/badge/Platform-Android_Automotive_OS-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/training/cars)
[![Forked from Nuvio](https://img.shields.io/badge/Forked_from-Nuvio-4285F4?style=for-the-badge&logo=github&logoColor=white)](https://github.com/NuvioMedia/NuvioMobile)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-orange?style=for-the-badge&logo=gnu&logoColor=white)](LICENSE)

[Original Nuvio project](https://github.com/NuvioMedia/NuvioMobile) · [Car customisations](AAOS_FORK.md#part-2-customisation-details) · [License](LICENSE)

</div>

---

## 🚘 What this is

Nuvio is a film and TV streaming app. This is a personal copy of it (a "fork"), changed to work well on the screen of a Polestar 3, which runs **Android Automotive OS** (AAOS): Android built into the car itself. It is not an Android Auto app (Android Auto mirrors a phone onto the car screen).

Changes from the original Nuvio project (the "parent") are brought into this copy from time to time, and the car changes are redone on top each time, so the app stays up to date without losing them.

## ✨ What's different in the car

| | Change | Details |
| --- | --- | --- |
| 👀 | **Bigger text, icons, posters, search and player controls** on the car, easy to read and tap at a glance. Phones and tablets keep the normal sizes. | [More](AAOS_FORK.md#c5-aaos-readability-automotive-only) |
| 🔐 | **Sign in without a browser**: the car shows a code and you finish signing in on your phone at nuvio.tv/link. | [More](AAOS_FORK.md#c3-device-link-sign-in) |
| 🛟 | **A backup way to sign in** ("Connect to another server", then `api.nuvio.tv`) if the normal one fails. | [More](AAOS_FORK.md#c4-custom-server-fallback) |
| ▶️ | **Starts playing quickly**: new profiles reuse the last link and auto-play the first stream. | [More](AAOS_FORK.md#c7-playback-defaults) |
| ⏸️ | **Pauses when you leave the app**, so sound doesn't carry on behind other screens. | [More](AAOS_FORK.md#c8-playback-lifecycle) |
| 🎵 | **The car's home screen media card** shows what Nuvio last played, even after you close it. | [More](AAOS_FORK.md#c9-car-media-card) |
| 🚗 | **Works as a car app**: it appears in the car's app list and runs well in the car's window. | [More](AAOS_FORK.md#c2-aaos-manifest-and-runtime) |
| 📦 | **Updates come through your own Play Store listing**, so they reach the car. | [More](AAOS_FORK.md#c1-play-identity-and-package-rename) |

**Known limit:** after a full restart of the car, the home screen media card stays blank. That is the price of the app icon opening the app properly; see the [car notes](AAOS_CAR_NOTES.md).

## 📲 Getting it on the car

There are no public downloads. The app reaches the car only through **Google Play Internal testing** (a private test track on your own Play listing):

1. Accept the internal test invitation for Nuvio with the Google account used in the car.
2. Open the Play Store in the car and install or update Nuvio.

The car's Play Store may show a temporary name ending in "(unreviewed)" and a placeholder icon. That is normal for an internal test, and installs and updates still work.

## 🛠️ What do you want to do?

Everything is done by an AI assistant (Claude Code, GitHub Copilot, Codex or Gemini). Open it in the **NuvioMobile** folder (this app's folder), paste the prompt for your job, and follow what it tells you. It explains everything in plain English, runs the commands itself, and only asks you to do the steps only you can do: approving, signing in Android Studio, uploading to Play and testing in the car. Each prompt only affects this app; do the other app separately in its own folder.

In Claude Code or GitHub Copilot you can type the short command shown instead of pasting the prompt.

### 1. Check whether the original app has something new

Nothing gets changed. You get a plain-English summary of what's new and which car changes it would affect.

```
Follow the instructions in .github/skills/aaos-sync/SKILL.md, but only check for parent updates. Do not change anything.
```

### 2. Update from the original app (`/aaos-sync`)

```
Follow the instructions in .github/skills/aaos-sync/SKILL.md exactly.
```

What happens next:

1. The assistant brings in the parent's changes on a temporary copy and redoes every car change in the new code.
2. It builds a test version and sends you a report with four parts: **what's new for you**, **how each of your car changes was kept or adapted** (or if any is at risk), **risks with a recommendation**, and **what to test**.
3. Nothing reaches the real app until you reply with one of these:
   - `approve sync`: it goes ahead and prepares a release (step 4 below).
   - `cancel sync`: everything is thrown away and nothing changes.
   - `problem: ` followed by what you saw: it fixes that and reports again.

### 3. Change or fix something for the car (`/aaos-tweak`)

```
Follow the instructions in .github/skills/aaos-tweak/SKILL.md exactly. The tweak: [describe what you want changed, or what is wrong on the car, in plain English].
```

The assistant makes the change, checks the app still builds, records what it did, and then prepares a release (step 4 below).

### 4. Release it to the car (`/aaos-release`)

Updates and tweaks end here automatically. To run this step on its own:

```
Follow the instructions in .github/skills/aaos-release/SKILL.md exactly.
```

1. The assistant raises the version number (it must go up for every upload) and checks the build.
2. It gives you click-by-click steps to sign the file in Android Studio. When that's done, reply `Bundle built`.
3. It puts the file in the **For upload to Play Console** folder, opens that folder, and gives you the clicks for Google Play Console. When Play accepts it, reply `Uploaded` and it records the upload.
4. Install or update the app from the Play Store in the car.

### 5. Tell the assistant something you learned about the car

```
I learned this about the car: [what you saw]. Add it to AAOS_CAR_NOTES.md in both the Flow and NuvioMobile folders, following AAOS_FORK.md section 10.
```

### If something goes wrong

- **A chat ended halfway through a job:** open a new chat in the same folder and paste:
  ```
  Read AGENTS.md, then check AAOS_LOG.md, git status and any sync/ branch, and tell me in plain English where the last job got to and what's next. Do not change anything yet.
  ```
- **Play says the version code was already used:** tell the assistant `Play says the version code was already used.` It raises the number and you sign and upload again.
- **The assistant stops with an error:** it gives you one message to paste back to it, or to another AI assistant. You never need to fix code yourself.

## 🗂️ Behind the scenes

You don't need to read these files. They are the instructions that keep any AI assistant on track between chats.

| File | What it holds |
| --- | --- |
| `README.md` | This page |
| [`AAOS_FORK.md`](AAOS_FORK.md) | Rules for AI assistants, and the full list of car customisations with technical detail |
| [`AAOS_UPSTREAM_SYNC.md`](AAOS_UPSTREAM_SYNC.md) | How parent updates are done, and the report you get before approving |
| [`AAOS_RELEASE.md`](AAOS_RELEASE.md) | Release steps, the version number rule, and the last version uploaded |
| [`AAOS_CAR_NOTES.md`](AAOS_CAR_NOTES.md) | Things learned on the real Polestar 3 (the same in both apps) |
| [`AAOS_LOG.md`](AAOS_LOG.md) | A diary of every change and what was checked (older entries in `AAOS_LOG_ARCHIVE.md`) |
| `AGENTS.md`, `CLAUDE.md`, `GEMINI.md` | Where AI assistants start reading |
| `.github/skills/` | The step-by-step recipes that the prompts above run |

## 📄 License

Nuvio is distributed under the [GNU General Public License v3.0](LICENSE). The license applies to this fork and to the original project.
