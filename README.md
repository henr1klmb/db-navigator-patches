# DB Navigator Patches

Custom [Morphe](https://github.com/MorpheApp) patches for **DB Navigator** (`de.hafas.android.db`) focused on privacy, AMOLED Integration and Screenshot Fix

---

## Included Patches
<!-- PATCHES_START -->
| Patch Name | Category | Description |
| :--- | :--- | :--- |
| **Disable Tealium tracking** | Privacy | Prevents the Tealium analytics SDK from initializing. |
| **Disable Adjust tracking** | Privacy | Blocks Adjust attribution and device fingerprinting on startup. |
| **Disable Qualtrics tracking** | Privacy | Disables Qualtrics telemetry and in-app feedback survey popups. |
| **Disable Optimizely tracking** | Privacy | Prevents Optimizely A/B testing and experimentation tracking. |
| **Disable Instana tracking** | Privacy | Stops Instana performance and session monitoring. |
| **AMOLED dark mode** | Appearance | Replaces dark gray night-mode surfaces with pure `#000000` black. |
| **Enable screenshots** | Usability | Removes `FLAG_SECURE` restrictions so screenshots work everywhere. |
<!-- PATCHES_END -->
---

## Installation

1. Add this repository URL as a patch source in **Morphe Manager**:
   ```text
   https://github.com/henr1klmb/db-navigator-patches
