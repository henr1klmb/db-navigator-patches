package io.github.henr1klmb.patches.oebb

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction

private const val HELPER = "Lio/github/henr1klmb/patches/shared/WebViewHelper;"

@Suppress("unused")
val disableAdsPatch = patch(
    name = "Disable ads",
    description = "Blocks Wegfinder promotion banners, TRM marketing banners, and other ads.",
) {
    compatibleWith(OEBB_COMPATIBILITY)

    execute {
        // Inject ad blocking script into WebViews
        HafasWebViewClientFingerprint.methodOrNull?.addInstructions(
            0,
            """
                invoke-static {p1}, $HELPER->injectAdBlock(Ljava/lang/Object;)V
            """.trimIndent(),
        )

        // Collapse Wegfinder promo card in connection overview list to 0dp and View.GONE
        OebbConnectionOverviewAdFingerprint.methodOrNull?.let { method ->
            val instructions = method.implementation?.instructions?.toList() ?: emptyList()
            val oebbAdViewIndex = instructions.indexOfFirst {
                (it.opcode == Opcode.CONST_STRING) &&
                    ((it as? ReferenceInstruction)?.reference?.toString() == "oebbAdView")
            }
            if (oebbAdViewIndex != -1) {
                method.addInstructions(
                    oebbAdViewIndex + 2,
                    """
                        invoke-virtual {p1}, Landroid/view/View;->getLayoutParams()Landroid/view/ViewGroup${'$'}LayoutParams;
                        move-result-object v0
                        if-eqz v0, :cond_skip_lp
                        const/4 v1, 0x0
                        iput v1, v0, Landroid/view/ViewGroup${'$'}LayoutParams;->width:I
                        iput v1, v0, Landroid/view/ViewGroup${'$'}LayoutParams;->height:I
                        :cond_skip_lp
                        const/16 v0, 0x8
                        invoke-virtual {p1, v0}, Landroid/view/View;->setVisibility(I)V
                        return-object p2
                    """.trimIndent(),
                )
            }
        }

        // Collapse Wegfinder ad banner in the navigation drawer to 0dp and View.GONE
        OebbDrawerAdFingerprint.methodOrNull?.addInstructions(
            0,
            """
                iget-object v0, p0, Lde/hafas/app/menu/HafasDrawerProvider;->oebbAdView:Landroid/view/View;
                if-nez v0, :cond_skip_hide
                return-void
                :cond_skip_hide
                const/16 v1, 0x8
                invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V
                invoke-virtual {v0}, Landroid/view/View;->getLayoutParams()Landroid/view/ViewGroup${'$'}LayoutParams;
                move-result-object v1
                if-eqz v1, :cond_skip_drawer_lp
                const/4 v2, 0x0
                iput v2, v1, Landroid/view/ViewGroup${'$'}LayoutParams;->width:I
                iput v2, v1, Landroid/view/ViewGroup${'$'}LayoutParams;->height:I
                :cond_skip_drawer_lp
                return-void
            """.trimIndent(),
        )

        // Collapse TRM (Targeted Relationship Marketing) placeholders in drawer to 0dp and View.GONE
        ShowTrmBannersFingerprint.methodOrNull?.addInstructions(
            0,
            """
                iget-object v0, p0, Lde/hafas/app/menu/HafasDrawerProvider;->trmTopPlaceholder:Landroid/view/ViewGroup;
                if-eqz v0, :cond_skip_top
                const/16 v3, 0x8
                invoke-virtual {v0, v3}, Landroid/view/View;->setVisibility(I)V
                invoke-virtual {v0}, Landroid/view/View;->getLayoutParams()Landroid/view/ViewGroup${'$'}LayoutParams;
                move-result-object v3
                if-eqz v3, :cond_skip_top
                const/4 v4, 0x0
                iput v4, v3, Landroid/view/ViewGroup${'$'}LayoutParams;->width:I
                iput v4, v3, Landroid/view/ViewGroup${'$'}LayoutParams;->height:I
                :cond_skip_top
                iget-object v1, p0, Lde/hafas/app/menu/HafasDrawerProvider;->trmBottomPlaceholder:Landroid/view/ViewGroup;
                if-eqz v1, :cond_skip_bottom
                const/16 v3, 0x8
                invoke-virtual {v1, v3}, Landroid/view/View;->setVisibility(I)V
                invoke-virtual {v1}, Landroid/view/View;->getLayoutParams()Landroid/view/ViewGroup${'$'}LayoutParams;
                move-result-object v3
                if-eqz v3, :cond_skip_bottom
                const/4 v4, 0x0
                iput v4, v3, Landroid/view/ViewGroup${'$'}LayoutParams;->width:I
                iput v4, v3, Landroid/view/ViewGroup${'$'}LayoutParams;->height:I
                :cond_skip_bottom
                iget-object v2, p0, Lde/hafas/app/menu/HafasDrawerProvider;->trmLoyaltyPlaceholder:Landroid/view/ViewGroup;
                if-eqz v2, :cond_skip_loyalty
                const/16 v3, 0x8
                invoke-virtual {v2, v3}, Landroid/view/View;->setVisibility(I)V
                invoke-virtual {v2}, Landroid/view/View;->getLayoutParams()Landroid/view/ViewGroup${'$'}LayoutParams;
                move-result-object v3
                if-eqz v3, :cond_skip_loyalty
                const/4 v4, 0x0
                iput v4, v3, Landroid/view/ViewGroup${'$'}LayoutParams;->width:I
                iput v4, v3, Landroid/view/ViewGroup${'$'}LayoutParams;->height:I
                :cond_skip_loyalty
                return-void
            """.trimIndent(),
        )

        // Neutralize Wegfinder ad click handler
        OebbAdClickHandlerFingerprint.methodOrNull?.addInstructions(
            0,
            "return-void",
        )
    }
}
