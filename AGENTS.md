# ⚠️ CRITICAL AGENT INSTRUCTION ⚠️
> **IMPORTANT**: Before making any code changes, pulling updates from upstream, or modifying project configuration, you MUST read [`CUSTOM_FEATURES_MIGRATION.md`](file:///Users/jordanfern/Documents/AAOS/NuvioMobile/CUSTOM_FEATURES_MIGRATION.md).

## Upstream Sync & Custom Branch Maintenance

1. **Working Branch**: `my-custom-features` is the active maintained branch on GitHub (`origin/my-custom-features`).
2. **Upstream Remote**: `upstream` (`https://github.com/NuvioMedia/NuvioMobile`) is the official parent repository (read-only). Never push directly to `upstream`.
3. **Origin Remote**: `origin` (`https://github.com/metaljay/NuvioMobile_AAOS.git`) is the user's custom fork. Always push custom updates to `origin`.
4. **Preserve Custom Invariants**:
   - Application ID & Namespace: `com.JF_Nuvio`
   - Android Automotive OS (AAOS) optimizations & layout targets
   - Custom server discovery fallback (`api.nuvio.tv`) for device-code login
   - Custom UI & branding overrides
