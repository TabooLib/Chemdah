package ink.ptms.chemdah.core.quest.objective.bukkit

import ink.ptms.chemdah.core.PlayerProfile
import ink.ptms.chemdah.core.quest.Task
import ink.ptms.chemdah.core.quest.objective.Dependency
import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import org.bukkit.event.player.PlayerItemMendEvent

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objective.bukkit.IItemMend
 *
 * @author sky
 * @since 2021/3/2 5:09 下午
 */
@Dependency("minecraft")
object IItemMend : ObjectiveCountableI<PlayerItemMendEvent>() {

    override val name = "item mend"
    override val event = PlayerItemMendEvent::class

    init {
        handler {
            player
        }
        addCondition("position") { e ->
            toPosition().inside(e.player.location)
        }
        addCondition("item") { e ->
            toInferItem().isItem(e.item)
        }
        addCondition("amount") { e ->
            toInt() <= e.repairAmount
        }
        addConditionVariable("amount") {
            it.repairAmount
        }
    }

    override fun getCount(profile: PlayerProfile, task: Task, event: PlayerItemMendEvent): Int {
        return event.repairAmount
    }
}