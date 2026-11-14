package io.github.henr1klmb.patches.shared

import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod

fun MutableMethod.clearBody() {
    val impl = implementation ?: return

    // Das interne tryBlocks-Feld von MutableMethodImplementation direkt leeren
    val tryBlocksField = impl.javaClass.getDeclaredField("tryBlocks").apply {
        isAccessible = true
    }
    (tryBlocksField.get(impl) as? MutableList<*>)?.clear()

    val count = impl.instructions.size
    if (count > 0) {
        removeInstructions(0, count)
    }
}

fun MutableMethod.ensureRegisters(needed: Int) {
    val impl = implementation ?: return
    if (impl.registerCount < needed) {
        val field = impl.javaClass.getDeclaredField("registerCount").apply {
            isAccessible = true
        }
        field.setInt(impl, needed)
    }
}