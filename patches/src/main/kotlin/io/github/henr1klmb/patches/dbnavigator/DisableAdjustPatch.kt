package io.github.henr1klmb.patches.dbnavigator

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.patch

@Suppress("unused")
val disableAdjustPatch = patch(
    name = "Disable Adjust tracking",
    description = "Prevents the Adjust analytics SDK from initializing in DB Navigator.",
) {
    compatibleWith(Compatibility(packageName = "de.hafas.android.db", name = "DB Navigator"))

    execute {
        AdjustInitFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
