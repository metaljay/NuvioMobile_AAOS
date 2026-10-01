# ⚠️ CRITICAL AGENT INSTRUCTION ⚠️
> **IMPORTANT**: Before making any code changes, pulling updates from upstream, or modifying project configuration, you MUST read [`CUSTOM_FEATURES_MIGRATION.md`](file:///Users/jordanfern/Documents/AAOS/NuvioMobile/CUSTOM_FEATURES_MIGRATION.md).

## Upstream Sync & Custom Branch Maintenance

1. **Active Working Branch**: `main` is the single maintained, stable branch on GitHub (`origin/main`).
2. **Upstream Remote**: `upstream` (`https://github.com/NuvioMedia/NuvioMobile`) is the official parent repository (read-only). **NEVER** push directly to `upstream`.
3. **Origin Remote**: `origin` (`https://github.com/metaljay/NuvioMobile_AAOS.git`) is the user's custom fork. Always push custom updates to `origin/main`.
4. **Upstream Update Workflow**:
   - When pulling new updates from parent repo (`upstream/cmp-rewrite`):
     1. Create a temporary migration branch (e.g. `temp/upstream-update-<version>`).
     2. Merge/port the upstream changes into the temporary branch.
     3. Assess and re-verify all custom AAOS features, branding, and invariants against `CUSTOM_FEATURES_MIGRATION.md`.
     4. Build and verify target compilation (`./gradlew :composeApp:compileAndroidMain` / `:androidApp:assembleDebug`).
     5. Once verified stable, update `main` with the clean build and push to `origin/main`.
     6. Delete the temporary migration branch locally and on `origin`. Never leave temporary migration branches behind.
5. **Preserve Custom Invariants**:
   - Application ID & Namespace: `com.JF_Nuvio` / `com.JF_Nuvio.android`
   - Target vehicle: User's Polestar 3 running Android Automotive OS (AAOS)
   - Custom server discovery fallback (`api.nuvio.tv`) for device-code login on authentication screen
   - AAOS touch targets, poster card dimensions, and readability typography scaling
   - `README.md` AAOS branding notice banner
