package io.github.henr1klmb.patches.netmirror

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch

private const val HELPER = "Lio/github/henr1klmb/patches/shared/WebViewHelper;"

@Suppress("unused")
val disableAdsPatch = patch(
    name = "Disable ads",
    description = "Blocks popup windows and ad elements.",
) {
    compatibleWith(NETMIRROR_COMPATIBILITY)

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