package io.github.henr1klmb.patches.dbnavigator

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.patch

@Suppress("unused")
val disableOptimizelyPatch = patch(
    name = "Disable Optimizely A/B testing",
    description = "Prevents the Optimizely SDK from initializing in DB Navigator.",
) {
    compatibleWith(Compatibility(packageName = "de.hafas.android.db", name = "DB Navigator"))

    execute {
        OptimizelyInitFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
