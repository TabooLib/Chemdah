package ink.ptms.chemdah.core.quest.objective.sandalphon

import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import ink.ptms.sandalphon.module.impl.blockmine.event.BlockBreakEvent

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objective.sandalphon.SBlockBreak
 *
 * @author sky
 * @since 2021/3/2 5:09 下午
 */
@Dependency("Sandalphon")
object SBlockBreak : ObjectiveCountableI<BlockBreakEvent>() {

    override val name = "sandalphon block break"
    override val event = BlockBreakEvent::class

    init {
        handler {
            player
        }
        addCondition("position") {
            toPosition().inside(it.bukkitEvent.block.location)
        }
        addCondition("material") {
            toInferBlock().isBlock(it.bukkitEvent.block)
        }
        addCondition("id") {
            toString().equals(it.blockData.id, true)
        }
        addConditionVariable("id") {
            it.blockData.id
        }
    }
}