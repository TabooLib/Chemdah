package ink.ptms.chemdah.core.quest.objective.bukkit

import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import org.bukkit.event.player.PlayerHarvestBlockEvent

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objective.bukkit.IBlockHarvest
 *
 * @author sky
 * @since 2021/3/2 5:09 下午
 */
@Dependency("minecraft")
object IBlockHarvest : ObjectiveCountableI<PlayerHarvestBlockEvent>() {

    override val name = "harvest block"
    override val event = PlayerHarvestBlockEvent::class

    init {
        handler {
            player
        }
        addCondition("position") { e ->
            toPosition().inside(e.harvestedBlock.location)
        }
        addCondition("material") { e ->
            toInferBlock().isBlock(e.harvestedBlock)
        }
        addCondition("item") { e ->
            e.itemsHarvested.any { toInferItem().isItem(it) }
        }
    }
}