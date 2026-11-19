# Nuvio: releasing to the Polestar 3 via Google Play

> Fork-owned file. The release facts table is specific to this app; every other section is word for word the same in the Flow and Nuvio forks.

## Release facts

| Item | Value |
| --- | --- |
| **Last uploaded to Play** | **151 (0.5.18)**, upload confirmed by the owner on 2026-11-17 |
| App name for bundle files | `Nuvio` |
| Release application ID | `com.JF_Nuvio` |
| Version file | `iosApp/Configuration/Version.xcconfig` (`CURRENT_PROJECT_VERSION` is the Android version code, `MARKETING_VERSION` the version name; Android reads this file) |
| Build command (check) | `./gradlew :androidApp:bundlePlaystoreRelease` |
| Release script | `aaos/release.sh` (modes: none = real release, `--check` = sign in to Play and look, `--dry-run` = build and validate, release nothing). Helpers beside it: `aaos/play-upload.mjs`, `aaos/setup-release.sh` (the owner's one-time Keychain setup), `aaos/test/play-upload.test.mjs`. Customisation C13. |
| Keychain service | `nuvio-release` in the owner's login Keychain (items `store-file`, `key-alias`, `store-password`, `key-password`). Gradle reads it first and falls back to the git-ignored `local.properties` (`NUVIO_RELEASE_*`). Never print, copy or commit the passwords or `local.properties` contents. |
| Gradle-built bundle | `androidApp/build/outputs/bundle/playstoreRelease/*.aab`, signed by Gradle from the Keychain (or `local.properties`); its `mapping.txt` is in `androidApp/build/outputs/mapping/playstoreRelease/`. |
| Signing wizard: module | `androidApp` |
| Signing wizard: build variant | `playstoreRelease` |
| Debug builds | `com.JF_Nuvio.debug`: emulator only |
| Release key | `Key.jks` in the `Play Console` folder next to both repos (outside git; Time Machine keeps a copy off the Mac) |
| Signed bundles for upload | The app's folder in the `Play Console` folder next to both repos (outside git): one file per release, named `<App>-v<name>-build<code>.aab`, for example `Nuvio-v0.5.13-build146.aab`. Android Studio saves into its `Android Studio output` sub-folder first |
| Who signs and uploads | `aaos/release.sh` signs (key details from the owner's Keychain) and uploads to Internal testing only; an agent may run it on the owner's go. Fallback: the owner, with Android Studio and Play Console. |

## The version rule (hard rule): every release build raises the version

The version numbers do not matter to Play or to the owner. They only have to go **up on every upload**. There is no reliable way to track versions across chats, tools and machines, so **every time a release bundle is prepared, the version is raised automatically, whatever it is now.** The numbers will drift away from the parent project's numbers; that is accepted.

- First run `git pull`. Then set the new version code to **the larger of (the code in the version file) and ("Last uploaded to Play" above), plus 1**, and raise the version name's patch number by 1. Commit and push this change on its own ("chore(release): bump version for upload") before building.
- Do this on every release preparation, even if the repo already looks higher than the last upload and even if the previous bundle was never uploaded. Gaps are fine; going down or repeating a number is not.
- Debug and test builds used only for checking do not need a bump. Docs-only changes never need a bump.
- The parent's version numbers are ignored. After a parent update keep OUR numbers on any conflict.
- If Play says "version code already used" (or the script says so), raise the code by one more, commit it and run again.
- "Last uploaded to Play" is a record and safety net, not something that must be perfect.

## Steps

This is the last stage of both the `aaos-sync` and `aaos-tweak` recipes; it can also be run alone (`.github/skills/aaos-release/SKILL.md`).

1. **Agent: prepare.** On `main`, work verified. Run `git pull`, then apply the version rule above: always raise the version in the version file, commit and push the bump on its own.
2. **Agent: check the build.** Run the build command from the table. Read the "Gradle-built bundle" row: never add passwords to `local.properties` or create keystore files in the repo. Inspect the merged release manifest: application ID, version code and name, min and target SDK, automotive and other hardware features optional.
3. **Agent: release with the script (the normal way).** On the owner's go, run the release script from the table. It checks there are no uncommitted changes, runs the fork's own tests, checks every fork file is labelled, builds the bundle, signs it with the key details from the owner's Keychain, checks the signature and application ID, uploads it (stopping if Play reports another version code) and releases it to **Internal testing only** (never any other track). After a real release it saves the bundle as `../Play Console/<App>/<App>-v<name>-build<code>.aab` (with `<App>-v<name>-build<code>-mapping.txt` beside it) and makes the local git tag `play-<code>` (push the tag yourself with the docs). It never raises the version: step 1 does. Optionally run `--check` or `--dry-run` first. You never see, type, copy or move the key, keystore or passwords. Report what it printed in plain English, then follow `.github/skills/aaos-uploaded/SKILL.md`. If it stops, say why in plain English; if it cannot be used (the Keychain is not set up, or Play refuses), use the fallback below.
   **Fallback if the script cannot be used (steps 3a to 3c):**
   - **3a. Owner: sign the bundle in Android Studio.** (a) Open this project in Android Studio. (b) In the top menu choose Build, then Generate Signed App Bundle / APK. (c) Choose Android App Bundle and click Next. (d) Set Module to the module in the table. (e) For Key store path click Choose existing and select the file Key.jks in the Play Console folder next to this project's folder; enter the key store password, choose the key alias and enter the key password. (f) Click Next, set Destination Folder to the folder named Android Studio output inside this app's folder in that Play Console folder (create it if it is missing; Android Studio usually remembers it next time), tick the build variant in the table (a release variant, never debug), and click Create. (g) Tell the agent: `Bundle built`
   - **3b. Agent: collect and check the file.** When the owner says `Bundle built`, find the newest .aab file under `../Play Console/<App>/Android Studio output` (Android Studio saves it in a sub-folder per build variant with a generic name; if there is none, look in the module's release build folder). Check inside it that the application ID, version code and version name are this release's, and that it is signed. Move it up to `../Play Console/<App>/` named `<App>-v<name>-build<code>.aab` with the real app name, version name and version code, so the app's folder holds exactly one clearly named file per release; rename the `mapping.txt` beside it `<App>-v<name>-build<code>-mapping.txt` and leave it in the output folder. Then run `open "../Play Console/<App>"` so the folder opens in Finder, and tell the owner the exact file name to upload.
   - **3c. Owner: upload in Play Console.** (a) Open Google Play Console and choose the app. (b) In the left menu choose Test and release, then Testing, then Internal testing. (c) Click Create new release. (d) Upload the file the agent named (it is in the folder that just opened in Finder). (e) Click Next, review, then Save and roll out. (f) Tell the agent: `Uploaded`. If Play says the version code was already used, tell the agent instead: it raises the code by one and you repeat from step 3a.
4. **Owner: install on the car** from the Play Store on the Polestar 3 (internal testing invitation). The Play Store on the car may show a temporary "(unreviewed)" name and placeholder icon; that is cosmetic (`AAOS_CAR_NOTES.md`).
5. **Agent: record (optional).** When the script reports the release (or the owner says `Uploaded`), update "Last uploaded to Play" above and note it in the change history (`handover/SESSION-LOG.md`) (`.github/skills/aaos-uploaded/SKILL.md`). The version rule does not depend on it.

## Never

- Never upload or install a debug or nightly build on the car.
- Never change the application ID; it must match the existing Play listing.
- Never replace the key, or commit, print or share it or its passwords.
- Never put real-car claims in the log unless the owner actually tested on the car.
