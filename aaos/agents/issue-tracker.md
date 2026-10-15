# Issue tracker: private files in the handover

Tickets and specs for this fork live as Markdown files in `handover/issues/` on the owner's Mac. Git ignores `handover/`, so they never reach GitHub. GitHub Issues stay switched off: the fork has one user, the open items in the owner's handover already track the work, and the parent's issue robots (copied into the fork) would close new issues.

This repo is public, but tickets may hold personal details: copy only the technical facts from a ticket into code, commits or repo docs.

An agent that can't see `handover/` (another tool, a cloud chat) puts its ticket in its final report instead.

## Conventions

- One piece of work per folder: `handover/issues/<feature-slug>/`
- The spec is `handover/issues/<feature-slug>/spec.md`
- Implementation tickets are one file each at `handover/issues/<feature-slug>/issues/<NN>-<slug>.md`, numbered from `01`, never a single combined tickets file
- Triage state is a `Status:` line near the top of each ticket (the role strings are in `triage-labels.md`)
- Comments and conversation history go at the bottom of the file under a `## Comments` heading
- Each folder is linked from its open item (`OI-nn`) in `handover/HANDOVER.md` section B2; add the open item if there isn't one

## When a skill says "publish to the issue tracker"

Create a new file under `handover/issues/<feature-slug>/` (creating the folder if needed), and link the folder from its open item.

## When a skill says "fetch the relevant ticket"

Read the file at the referenced path. The owner normally gives the path, the open item (`OI-nn`) or the ticket number.

## Wayfinding operations

Used by `/wayfinder`. The **map** is a file with one **child** file per ticket.

- **Map**: `handover/issues/<effort>/map.md` (the Notes / Decisions-so-far / Fog body).
- **Child ticket**: `handover/issues/<effort>/issues/NN-<slug>.md`, numbered from `01`, with the question in the body. A `Type:` line records the ticket type (`research`/`prototype`/`grilling`/`task`); a `Status:` line records `claimed`/`resolved`.
- **Blocking**: a `Blocked by: NN, NN` line near the top. A ticket is unblocked when every file it lists is `resolved`.
- **Frontier**: scan `handover/issues/<effort>/issues/` for files that are open, unblocked and unclaimed; first by number wins.
- **Claim**: set `Status: claimed` and save before any work.
- **Resolve**: append the answer under an `## Answer` heading, set `Status: resolved`, then append a context pointer (gist + link) to the map's Decisions-so-far in `map.md`.
