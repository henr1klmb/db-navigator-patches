package io.github.henr1klmb.patches.dbnavigator

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.patch

@Suppress("unused")
val disableInstanaPatch = patch(
    name = "Disable Instana tracking",
    description = "Prevents the Instana monitoring and telemetry SDK from initializing in DB Navigator.",
) {
    compatibleWith(Compatibility(packageName = "de.hafas.android.db", name = "DB Navigator"))

    execute {
        InstanaInitFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}
