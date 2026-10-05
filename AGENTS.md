<!-- AAOS-FORK:START (fork-owned block; keep at the very top; do not edit parent text below it) -->
# READ FIRST: Nuvio AAOS fork

This repo is the owner's fork of Nuvio, customised for the owner's Polestar 3 (Android Automotive OS). The owner has no coding experience and depends on you: explain in plain English and never claim something was verified without saying how.

Before ANY code change, parent update, build or release, read `AAOS_FORK.md`: rules, safety rails, how to talk to the owner, and the customisations that must survive every parent update. Then open the file for the job:

| Job or question | Read |
| --- | --- |
| The owner pasted a prompt from `README.md` | the recipe it names in `.github/skills/` |
| Update from the parent (upstream) | `.github/skills/aaos-sync/SKILL.md`, which uses `AAOS_UPSTREAM_SYNC.md` |
| Change or fix the app for the car | `.github/skills/aaos-tweak/SKILL.md` |
| Build or upload a release | `.github/skills/aaos-release/SKILL.md`, which uses `AAOS_RELEASE.md` (every release raises the version) |
| Record an upload | `.github/skills/aaos-uploaded/SKILL.md` |
| Manifest, media card, launch behaviour, screen edges, Play | `AAOS_CAR_NOTES.md` first (facts learned on the real car) |
| Past changes and evidence | `AAOS_LOG.md` (older entries in `AAOS_LOG_ARCHIVE.md`), only when needed |

Claude Code and GitHub Copilot also list the recipes as `/aaos-sync`, `/aaos-tweak`, `/aaos-release` and `/aaos-uploaded`. Any text below this block comes from the parent project; follow its coding rules too.
<!-- AAOS-FORK:END -->
