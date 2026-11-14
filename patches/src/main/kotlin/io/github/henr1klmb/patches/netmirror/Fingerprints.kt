package io.github.henr1klmb.patches.netmirror

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.Compatibility

internal val NETMIRROR_COMPATIBILITY = Compatibility(
    packageName = "app.netmirror.netmirrornew",
    name = "NetMirror",
)

private const val WEB_VIEW = "Landroid/webkit/WebView;"
private const val STRING = "Ljava/lang/String;"

internal object WebChromeClientOnCreateWindowFingerprint : Fingerprint(
    name = "onCreateWindow",
    returnType = "Z",
    parameters = listOf(WEB_VIEW, "Z", "Z", "Landroid/os/Message;"),
)

internal object PopupWebViewClientFingerprint : Fingerprint(
    name = "shouldOverrideUrlLoading",
    returnType = "Z",
    parameters = listOf(WEB_VIEW, STRING),
)

internal object MainWebViewClientFingerprint : Fingerprint(
    name = "shouldOverrideUrlLoading",
    returnType = "Z",
    parameters = listOf(WEB_VIEW, STRING),
)

internal object MainWebViewClientOnPageFinishedFingerprint : Fingerprint(
    name = "onPageFinished",
    returnType = "V",
    parameters = listOf(WEB_VIEW, STRING),
)

internal object RNDeviceUniqueIdFingerprint : Fingerprint(
    name = "getUniqueIdSync",
    returnType = "Ljava/lang/String;",
    parameters = emptyList(),
)
