# Nuvio: releasing to the Polestar 3 via Google Play

## Release state (update after every confirmed upload)

| Item | Value |
| --- | --- |
| Release application ID | `com.JF_Nuvio` |
| **Last uploaded to Play** | **146 (0.5.13)**, reported installed on the car by the owner on 2026-10-05 |
| Code in the repo now | Informational (the release recipe raises it on every release) |
| Version file | `iosApp/Configuration/Version.xcconfig` (`CURRENT_PROJECT_VERSION` is the Android version code, `MARKETING_VERSION` the name; Android reads this file) |
| Release key | `Key.jks` in the AAOS folder containing both repos (outside the repo; keep a backup) |
| Signed bundles for upload | The folder named `For upload to Play Console` in the AAOS folder (next to both repos, outside git). Files are named `<App>-<code>-<name>.aab`, for example `Flow-33-2.2.14.aab`. |
| Who signs and uploads | The owner, with Android Studio and Play Console. Agents prepare and verify. |

## The version rule (hard rule): every release build raises the version

The version numbers do not matter to Play or to the owner. They only have to go **up on every upload**. There is no reliable way to track versions across chats, tools and machines, so **every time a release bundle is prepared, the version is raised automatically, whatever it is now.** The numbers will drift away from the parent project's numbers; that is accepted.

- First run `git pull`. Then set the new version code to **the larger of (the code in the repo now) and ("Last uploaded to Play" in the table above), plus 1**, and raise the version name's patch number by 1. Commit and push this change on its own ("chore(release): bump version for upload") before building.
- Do this on every release preparation, even if the repo already looks higher than the last upload and even if the previous bundle was never uploaded. Gaps are fine; going down or repeating a number is not.
- Debug and test builds used only for checking do not need a bump.
- The parent's version numbers are ignored. After a parent update keep OUR numbers on any conflict.
- If Play says "version code already used", raise the code by one more and rebuild.
- "Last uploaded to Play" is a record and safety net, not something that must be perfect.
- Docs-only changes never need a bump.

## Steps

This is the last stage of both the `aaos-sync` and `aaos-tweak` recipes; it can also be run alone.

1. **Agent: prepare.** On `main`, work verified. Run `git pull`, then apply the version rule above: always raise the version, commit and push the bump on its own, and confirm the new values in the version file named in the table.
2. **Agent: check the build.** `./gradlew :androidApp:bundlePlaystoreRelease`. Nuvio's git-ignored `local.properties` also points Gradle at the key (`NUVIO_RELEASE_STORE_FILE=../Key.jks`), so Gradle can sign too. The wizard is still the standard path so both apps are released the same way. Never print, copy or commit the passwords in `local.properties`. Inspect the merged release manifest: application ID `com.JF_Nuvio`, version code/name, min/target SDK, automotive and camera features optional.
3. **Owner: sign the bundle in Android Studio.** (a) Open this project in Android Studio. (b) In the top menu choose Build, then Generate Signed App Bundle / APK. (c) Choose Android App Bundle and click Next. (d) Set Module to `androidApp`. (e) For Key store path click Choose existing and select the file Key.jks in the AAOS folder containing both repos; enter the key store password, choose the key alias and enter the key password. (f) Click Next, set Destination Folder to the folder named For upload to Play Console in the AAOS folder (create it if it is missing; Android Studio usually remembers it next time), tick the build variant named above (a release variant, never debug), and click Create. (g) Tell the agent: `Bundle built`
4. **Agent: collect and check the file.** When the owner says `Bundle built`, find the newest .aab file under the folder `../For upload to Play Console` (if there is none, look in the module's release build folder). Copy it to the top level of `../For upload to Play Console` named `<App>-<code>-<name>.aab` using the real app name, version code and version name. Check it is signed and, if tools are available, check its version code. Then run `open "../For upload to Play Console"` so the folder opens in Finder, and tell the owner the exact file name to upload. Report in plain English.
5. **Owner: upload in Play Console.** (a) Open Google Play Console and choose the app. (b) In the left menu choose Test and release, then Testing, then Internal testing. (c) Click Create new release. (d) Upload the file the agent named (it is in the folder that just opened in Finder). (e) Click Next, review, then Save and roll out. If Play says the version code was already used, tell the agent: it raises the code by one and you repeat from step 3.
6. **Owner: install on the car** from the Play Store on the Polestar 3 (internal testing invitation). The debug build uses `com.JF_Nuvio.debug`; it is emulator-only and must never be installed on, or distributed to, the car.
7. **Optional housekeeping (the version rule does not depend on it):** Update "Last uploaded to Play" above and add an `AAOS_LOG.md` entry. Optional: `git tag play-<code>` and push the tag.

## Never

- Never upload or install a debug build on the car.
- Never change the application ID; it must match the existing Play listing.
- Never replace the key, or commit/print/share it or its passwords.
- Never put real-vehicle claims in the log unless the owner actually tested on the car.
