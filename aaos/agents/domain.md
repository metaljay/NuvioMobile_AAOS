# Domain docs

How the engineering skills use this fork's domain documentation. Single-context: one glossary and one folder of decision records for the whole fork.

## Before exploring, read these

- **The glossary: `AAOS_FORK.md` section 3 (Terminology).** It does the job the skills give `CONTEXT.md`; this fork keeps its terms there instead, so each term has one home.
- **`aaos/adr/`**: read the decision records that touch the area you're about to work in.

Where either is thin or `aaos/adr/` doesn't exist yet, carry on without it. `/domain-modeling` (reached via `/grill-with-docs` and `/improve-codebase-architecture`) adds to them only when a term or decision actually gets settled.

## Where new entries go

- **A term:** a row in the `AAOS_FORK.md` section 3 table. That section is shared with the sister fork, so the same change goes into Flow too (`AAOS_FORK.md` section 10).
- **A decision record:** `aaos/adr/NNNN-<slug>.md`, numbered from `0001`. Everything under `aaos/` is fork-only, so parent updates never touch it. Keep these files under `aaos/`: the parent's `.gitignore` hides `Docs`, which on the owner's Mac also hides `docs/`.

## Use the glossary's vocabulary

When your output names a domain concept (in a ticket title, a refactor proposal, a hypothesis, a test name), use the term as the glossary defines it. Don't drift to synonyms the glossary explicitly avoids.

If the concept you need isn't in the glossary yet, that's a signal: either you're inventing language the project doesn't use (reconsider) or there's a real gap (note it for `/domain-modeling`).

## Flag ADR conflicts

If your output contradicts an existing decision record, surface it explicitly rather than silently overriding:

> _Contradicts ADR-0001 (…), but worth reopening because…_
