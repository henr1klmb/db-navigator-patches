package io.github.henr1klmb.patches.shared

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.classDefForEach
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.patch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction3rc
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference
import io.github.henr1klmb.patches.oebb.OEBB_COMPATIBILITY
import java.security.cert.X509Certificate

private const val HELPER_CLASS_DESC = "Lio/github/henr1klmb/patches/shared/SpoofSignatureHelper;"

private val LEGACY_SPOOF_METHOD_REF = ImmutableMethodReference(
    HELPER_CLASS_DESC,
    "getPackageInfo",
    listOf("Landroid/content/pm/PackageManager;", "Ljava/lang/String;", "I"),
    "Landroid/content/pm/PackageInfo;",
)

private val FLAGS_SPOOF_METHOD_REF = ImmutableMethodReference(
    HELPER_CLASS_DESC,
    "getPackageInfo",
    listOf(
        "Landroid/content/pm/PackageManager;",
        "Ljava/lang/String;",
        "Landroid/content/pm/PackageManager\$PackageInfoFlags;",
    ),
    "Landroid/content/pm/PackageInfo;",
)

@Suppress("unused")
val spoofSignaturePatch = patch(
    name = "Spoof signature",
    description = "Spoofs the original package signature so Google Maps and signature checks succeed on re-signed APKs.",
) {
    compatibleWith(OEBB_COMPATIBILITY)
    extendWith("extensions/extension.mpe")

    execute {
        executeSpoofSignature()
    }
}

/**
 * Injects the original signing certificate into [io.github.henr1klmb.patches.shared.SpoofSignatureHelper]
 * and redirects all [android.content.pm.PackageManager.getPackageInfo] call sites across the APK to the helper.
 */
fun executeSpoofSignature(targetPackageName: String = packageMetadata.packageName) {
    val cert = packageMetadata.signingCertificates.values.flatten().firstOrNull()
        ?: throw PatchException("No original signing certificate found in unpatched APK.")
    val certHex = cert.encoded.joinToString("") { "%02x".format(it) }

    // Configure SpoofSignatureHelper with the original app package name and signature hex
    val helperClass = mutableClassDefBy(HELPER_CLASS_DESC)

    val getPkgMethod = helperClass.methods.first { it.name == "getAppPackageName" }
    getPkgMethod.clearBody()
    getPkgMethod.ensureRegisters(1)
    getPkgMethod.addInstructions(
        0,
        """
        const-string v0, "$targetPackageName"
        return-object v0
        """.trimIndent(),
    )

    val getSigMethod = helperClass.methods.first { it.name == "getOriginalSignatureHex" }
    getSigMethod.clearBody()
    getSigMethod.ensureRegisters(1)
    getSigMethod.addInstructions(
        0,
        """
        const-string v0, "$certHex"
        return-object v0
        """.trimIndent(),
    )

    // Find all classes that contain calls to PackageManager.getPackageInfo
    val classesToPatch = mutableListOf<ClassDef>()
    classDefForEach { classDef ->
        if (classDef.type.startsWith("Lio/github/henr1klmb/")) return@classDefForEach

        val hasCall = classDef.methods.any { method ->
            method.implementation?.instructions?.any { inst ->
                if (inst.opcode != Opcode.INVOKE_VIRTUAL && inst.opcode != Opcode.INVOKE_VIRTUAL_RANGE) return@any false
                val ref = (inst as? ReferenceInstruction)?.reference as? MethodReference ?: return@any false
                ref.definingClass == "Landroid/content/pm/PackageManager;" &&
                    ref.name == "getPackageInfo" &&
                    (ref.parameterTypes == listOf("Ljava/lang/String;", "I") ||
                        ref.parameterTypes == listOf("Ljava/lang/String;", "Landroid/content/pm/PackageManager\$PackageInfoFlags;"))
            } == true
        }
        if (hasCall) {
            classesToPatch.add(classDef)
        }
    }

    // Redirect invoke-virtual calls to invoke-static on SpoofSignatureHelper
    for (classDef in classesToPatch) {
        if (classDef.type.startsWith("Lio/github/henr1klmb/")) continue
        val mutableClass = mutableClassDefBy(classDef)
        for (method in mutableClass.methods) {
            val impl = method.implementation ?: continue
            val instructions = impl.instructions.toList()
            for ((index, inst) in instructions.withIndex()) {
                if (inst.opcode != Opcode.INVOKE_VIRTUAL && inst.opcode != Opcode.INVOKE_VIRTUAL_RANGE) continue
                val ref = (inst as? ReferenceInstruction)?.reference as? MethodReference ?: continue
                if (ref.definingClass != "Landroid/content/pm/PackageManager;" || ref.name != "getPackageInfo") continue

                val isLegacy = ref.parameterTypes == listOf("Ljava/lang/String;", "I")
                val isFlags = ref.parameterTypes == listOf("Ljava/lang/String;", "Landroid/content/pm/PackageManager\$PackageInfoFlags;")
                if (!isLegacy && !isFlags) continue

                val targetRef = if (isLegacy) LEGACY_SPOOF_METHOD_REF else FLAGS_SPOOF_METHOD_REF
                when (inst) {
                    is Instruction35c -> {
                        val replacement = BuilderInstruction35c(
                            Opcode.INVOKE_STATIC,
                            inst.registerCount,
                            inst.registerC,
                            inst.registerD,
                            inst.registerE,
                            inst.registerF,
                            inst.registerG,
                            targetRef,
                        )
                        method.replaceInstruction(index, replacement)
                    }
                    is Instruction3rc -> {
                        val replacement = BuilderInstruction3rc(
                            Opcode.INVOKE_STATIC_RANGE,
                            inst.startRegister,
                            inst.registerCount,
                            targetRef,
                        )
                        method.replaceInstruction(index, replacement)
                    }
                }
            }
        }
    }
}
