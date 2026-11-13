package io.github.henr1klmb.patches.oebb

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.patch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction

@Suppress("unused")
val fixMapLoadingPatch = patch(
    name = "Fix map loading",
    description = "Switches the map engine from Google Maps to OpenStreetMap (HAFAS tiles) to resolve blank maps on re-signed APKs.",
) {
    compatibleWith(OEBB_COMPATIBILITY)

    execute {
        val method = OebbMapStyleFingerprint.method
        val instructions = method.implementation?.instructions?.toList() ?: emptyList()
        val googleIndex = instructions.indexOfFirst {
            (it.opcode == Opcode.CONST_STRING) &&
                ((it as? ReferenceInstruction)?.reference?.toString() == "GOOGLE")
        }
        if (googleIndex != -1) {
            method.replaceInstruction(googleIndex, "const-string v3, \"OSM\"")
        }
    }
}
