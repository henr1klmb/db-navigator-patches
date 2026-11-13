# henr1k patches

Custom [Morphe](https://github.com/MorpheApp) patches for multiple Apps

---

## Included Patches
<!-- PATCHES_START -->
> **[v1.2.0-dev.2](https://github.com/henr1klmb/henr1k-patches/releases/tag/v1.2.0-dev.2)**&nbsp;&nbsp;•&nbsp;&nbsp;`dev`&nbsp;&nbsp;•&nbsp;&nbsp;12 patches total
<details open>
<summary>📦 DB Navigator&nbsp;&nbsp;•&nbsp;&nbsp;3 patches</summary>
<br>

**Package name:** `de.hafas.android.db`

**🎯 Supported versions:**

| 26.20.0 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [AMOLED dark mode](#amoled-dark-mode) | Replaces dark background colors in night mode with pure black (#FF000000). |  |
| [Disable tracking](#disable-tracking) | Disables Tracking and Analytics. |  |
| [Enable screenshots](#enable-screenshots) | Removes the FLAG_SECURE window flag in DB Navigator to allow taking screenshots and screen recordings. |  |

</details>

<details open>
<summary>📦 NetMirror&nbsp;&nbsp;•&nbsp;&nbsp;3 patches</summary>
<br>

**Package name:** `app.netmirror.netmirrornew`

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Custom icon](#custom-icon) | Changes the app name and optionally replaces the launcher icons of NetMirror. | • App name<br>• Custom icon PNG path (optional) |
| [Disable ads](#disable-ads) | Blocks popup windows and ad elements in NetMirror. |  |
| [Disable trackers](#disable-trackers) | Neutralizes analytics, tracking hooks, and device fingerprinting in NetMirror. |  |

</details>

<details open>
<summary>📦 ÖBB Scotty&nbsp;&nbsp;•&nbsp;&nbsp;3 patches</summary>
<br>

**Package name:** `de.hafas.android.oebb`

**🎯 Supported versions:**

| 9.0.5 (72) |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Disable ads](#disable-ads) | Blocks Wegfinder promotion banners, TRM marketing banners, and other ads. |  |
| [Disable tracking](#disable-tracking) | Disables Tracking and Analytics. |  |
| [Fix map loading](#fix-map-loading) | Switches the map engine from Google Maps to OpenStreetMap (HAFAS tiles) to resolve blank maps on re-signed APKs. |  |

</details>

<details open>
<summary>📦 SBB Mobile&nbsp;&nbsp;•&nbsp;&nbsp;3 patches</summary>
<br>

**Package name:** `ch.sbb.mobile.android.b2c`

**🎯 Supported versions:**

| 12.27.0.87.master |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Disable ads](#disable-ads) | Disables multiple ad types. |  |
| [Disable tracking](#disable-tracking) | Disables Tracking and Analytics. |  |
| [Enable screenshots](#enable-screenshots) | Allows taking screenshots and screen recordings. |  |

</details>

<!-- PATCHES_END -->
---

## Installation

1. Add this repository URL as a patch source in **Morphe Manager**:
   ```text
   https://github.com/henr1klmb/henr1k-patches
