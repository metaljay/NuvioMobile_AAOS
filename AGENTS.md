<!-- AAOS-FORK:START (fork-owned block; keep at the very top; do not edit upstream text below it) -->
# READ FIRST: Nuvio AAOS fork

This repo is the owner's fork of Nuvio, customised for the owner's Polestar 3 (Android Automotive OS). Before ANY code change, upstream merge, build or release:

1. Read `AAOS_FORK.md` (rules, safety rails, and what must survive every merge).
2. Pulling parent (upstream) changes: follow `AAOS_UPSTREAM_SYNC.md`.
3. Building or uploading to Google Play: follow `AAOS_RELEASE.md`. **Every Play upload needs a higher version code than the last one.**
4. Past validation evidence: `AAOS_LOG.md` (only when needed).
5. Common jobs have step-by-step recipes that any agent can follow as plain instructions: `.github/skills/aaos-sync/SKILL.md` (pull parent updates), `.github/skills/aaos-release/SKILL.md` (prepare a Play release), `.github/skills/aaos-uploaded/SKILL.md` (record a confirmed upload), `.github/skills/aaos-log-change/SKILL.md` (record a change or bug fix).

The owner has no coding experience: explain what you did in plain English and never claim something was verified without saying how.
<!-- AAOS-FORK:END -->
