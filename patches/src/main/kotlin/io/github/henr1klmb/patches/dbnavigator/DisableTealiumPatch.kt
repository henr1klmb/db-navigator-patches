package io.github.henr1klmb.patches.dbnavigator

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.patch

@Suppress("unused")
val disableTealiumPatch = patch(
    name = "Disable Tealium tracking",
    description = "Prevents the Tealium analytics SDK from initializing in DB Navigator.",
) {
    compatibleWith(Compatibility(packageName = "de.hafas.android.db", name = "DB Navigator"))

    execute {
        TealiumInitFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}