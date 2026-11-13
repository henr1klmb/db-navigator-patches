# DB Navigator Patches

Custom [Morphe](https://github.com/MorpheApp) patches for **DB Navigator** (`de.hafas.android.db`) focused on privacy, AMOLED Integration and Screenshot Fix

---

## Included Patches
<!-- PATCHES_START -->
> **[v1.0.1](https://github.com/henr1klmb/db-navigator-patches/releases/tag/v1.0.1)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;7 patches total
<details open>
<summary>📦 DB Navigator&nbsp;&nbsp;•&nbsp;&nbsp;7 patches</summary>
<br>

**Package name:** `de.hafas.android.db`

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [AMOLED dark mode](#amoled-dark-mode) | Replaces dark background colors in night mode with pure black (#FF000000). |  |
| [Disable Adjust tracking](#disable-adjust-tracking) | Prevents the Adjust analytics SDK from initializing in DB Navigator. |  |
| [Disable Instana tracking](#disable-instana-tracking) | Prevents the Instana monitoring and telemetry SDK from initializing in DB Navigator. |  |
| [Disable Optimizely A/B testing](#disable-optimizely-a-b-testing) | Prevents the Optimizely SDK from initializing in DB Navigator. |  |
| [Disable Qualtrics tracking](#disable-qualtrics-tracking) | Prevents the Qualtrics SDK from initializing in DB Navigator. |  |
| [Disable Tealium tracking](#disable-tealium-tracking) | Prevents the Tealium analytics SDK from initializing in DB Navigator. |  |
| [Enable screenshots](#enable-screenshots) | Removes the FLAG_SECURE window flag in DB Navigator to allow taking screenshots and screen recordings. |  |

</details>

<!-- PATCHES_END -->
---

## Installation

1. Add this repository URL as a patch source in **Morphe Manager**:
   ```text
   https://github.com/henr1klmb/db-navigator-patches
