package io.github.henr1klmb.patches.dbnavigator

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.patch

@Suppress("unused")
val disableQualtricsPatch = patch(
    name = "Disable Qualtrics tracking",
    description = "Prevents the Qualtrics SDK from initializing in DB Navigator.",
) {
    compatibleWith(Compatibility(packageName = "de.hafas.android.db", name = "DB Navigator"))

    execute {
        QualtricsInitFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
