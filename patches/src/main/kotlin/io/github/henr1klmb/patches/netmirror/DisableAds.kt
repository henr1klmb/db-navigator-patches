package io.github.henr1klmb.patches.netmirror

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.patch

private const val HELPER = "Lio/github/henr1klmb/patches/shared/WebViewHelper;"

@Suppress("unused")
val disableAdsPatch = patch(
    name = "Disable ads",
    description = "Blocks popup windows and ad elements in NetMirror.",
) {
    compatibleWith(Compatibility(packageName = "app.netmirror.netmirrornew", name = "NetMirror"))

    execute {
        PopupWebViewClientFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent(),
        )

        MainWebViewClientFingerprint.method.addInstructions(
            0,
            """
                invoke-static {p1}, $HELPER->injectAdBlock(Ljava/lang/Object;)V
            """.trimIndent(),
        )
    }
}