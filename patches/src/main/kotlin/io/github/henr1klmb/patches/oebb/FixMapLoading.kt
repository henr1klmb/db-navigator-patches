package io.github.henr1klmb.patches.oebb

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.patch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction

private const val OSM_TILE_URL = "https://tile.openstreetmap.org/{z}/{x}/{y}.png"

@Suppress("unused")
val fixMapLoadingPatch = patch(
    name = "Fix map loading",
    description = "Switches the map engine from Google Maps to OpenStreetMap and configures the OSM tile server to resolve blank maps on re-signed APKs.",
) {
    compatibleWith(OEBB_COMPATIBILITY)

    execute {
        // Switch map engine from GOOGLE to OSM
        val styleMethod = OebbMapStyleFingerprint.method
        val styleInstructions = styleMethod.implementation?.instructions?.toList() ?: emptyList()
        val googleIndex = styleInstructions.indexOfFirst {
            (it.opcode == Opcode.CONST_STRING) &&
                ((it as? ReferenceInstruction)?.reference?.toString() == "GOOGLE")
        }
        if (googleIndex != -1) {
            styleMethod.replaceInstruction(googleIndex, "const-string v3, \"OSM\"")
        }

        // Configure OpenStreetMap tile server URL in HAFAS OSM tile provider
        OebbOsmTileFingerprint.methodOrNull?.let { method ->
            val instructions = method.implementation?.instructions?.toList() ?: emptyList()
            val mapServerOsmIndex = instructions.indexOfFirst {
                (it.opcode == Opcode.CONST_STRING) &&
                    ((it as? ReferenceInstruction)?.reference?.toString() == "MAP_SERVER_OSM")
            }
            if (mapServerOsmIndex != -1 && mapServerOsmIndex + 1 < instructions.size) {
                // The next instruction is const/4 v3, 0x0 which provides the default value for both
                // MAP_SERVER_OSM and MAP_SERVER_OSM_RETINA lookups.
                method.replaceInstruction(
                    mapServerOsmIndex + 1,
                    "const-string v3, \"$OSM_TILE_URL\"",
                )
            }
        }
    }
}
