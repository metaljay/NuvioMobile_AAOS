<!-- AAOS-FORK:START (fork-owned block; keep at the very top; do not edit upstream text below it) -->
# READ FIRST: Nuvio AAOS fork

This repo is the owner's fork of Nuvio, customised for the owner's Polestar 3 (Android Automotive OS). Before ANY code change, upstream merge, build or release:

1. Read `AAOS_FORK.md` (rules, safety rails, and what must survive every merge).
2. Pulling parent (upstream) changes: follow `AAOS_UPSTREAM_SYNC.md`.
3. Building or uploading to Google Play: follow `AAOS_RELEASE.md`. **Every release build automatically raises the version code and name (see `AAOS_RELEASE.md`).**
4. Past validation evidence: `AAOS_LOG.md` (only when needed).
5. Recipes (any agent can follow them as plain instructions): `.github/skills/aaos-sync/SKILL.md` (parent update), `.github/skills/aaos-tweak/SKILL.md` (change or fix the app), `.github/skills/aaos-release/SKILL.md` (release stage, used by both), `.github/skills/aaos-uploaded/SKILL.md` (optional record of an upload).

The owner has no coding experience: explain what you did in plain English and never claim something was verified without saying how.
<!-- AAOS-FORK:END -->
