package io.github.henr1klmb.patches.oebb

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.patch

private const val HELPER = "Lio/github/henr1klmb/patches/shared/WebViewHelper;"

@Suppress("unused")
val disableAdsPatch = patch(
    name = "Disable ads",
    description = "Blocks Wegfinder promotion banners, TRM marketing banners, and other ads.",
) {
    compatibleWith(Compatibility(packageName = "de.hafas.android.oebb", name = "ÖBB Scotty"))

    execute {
        // Inject ad blocking script into WebViews
        HafasWebViewClientFingerprint.methodOrNull?.addInstructions(
            0,
            """
                invoke-static {p1}, $HELPER->injectAdBlock(Ljava/lang/Object;)V
            """.trimIndent(),
        )

        // Hide Wegfinder ad banner in the navigation drawer
        OebbDrawerAdFingerprint.methodOrNull?.addInstructions(
            0,
            """
                iget-object v0, p0, Lde/hafas/app/menu/HafasDrawerProvider;->oebbAdView:Landroid/view/View;
                if-nez v0, :cond_skip_hide
                return-void
                :cond_skip_hide
                const/16 v1, 0x8
                invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V
                return-void
            """.trimIndent(),
        )

        // Disable TRM (Targeted Relationship Marketing) banners in drawer
        ShowTrmBannersFingerprint.methodOrNull?.addInstructions(
            0,
            "return-void",
        )

        // Neutralize Wegfinder ad click handler
        OebbAdClickHandlerFingerprint.methodOrNull?.addInstructions(
            0,
            "return-void",
        )
    }
}
