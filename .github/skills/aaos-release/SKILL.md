---
name: aaos-release
description: Use to prepare a Google Play release bundle for the Polestar 3. This is the last stage of aaos-sync and aaos-tweak and can also be run alone.
---
The owner has no coding experience. Follow `AAOS_FORK.md` section 6 (talking to the owner) for everything you say.

Read `AAOS_RELEASE.md` (its "Release facts" table holds every app-specific value you need), then:

1. Do step 1 of `AAOS_RELEASE.md`: `git pull`, then ALWAYS raise the version by the version rule, and commit and push that bump on its own before building.
2. Do step 2: run the build command from the table and inspect the merged release manifest. Never touch any key, keystore or password.
3. Add a short note to this chat's entry in `handover/SESSION-LOG.md` (at most three lines: version, what it contains, what you did NOT verify). It is not committed.
4. Do step 3 of `AAOS_RELEASE.md`: on the owner's go, run the release script from the table (it signs with key details the owner stored in the Keychain and releases to Internal testing only). Report in plain English what it printed: version, tests, signed, uploaded, the saved file name, the `play-<code>` tag; if it stopped, why and what is needed.
5. If the script cannot be used (the owner has not run `aaos/setup-release.sh`, or Play refuses), use the fallback in step 3 of `AAOS_RELEASE.md`: give the owner steps 3a as a numbered click-by-click list with every value filled in (new version code and name, module and build variant from the table, the destination folder) ending with `Bundle built`; when they reply, do 3b and give them 3c, ending with `Uploaded`.
6. When the release is done (the script reports it, or the owner replies `Uploaded`, now or in a later chat), follow `.github/skills/aaos-uploaded/SKILL.md`. If nothing is recorded, nothing breaks: the version rule does not depend on it.

Follow the safety rails in `AAOS_FORK.md` section 5.
