package ink.ptms.chemdah.core.quest.objective.other

import ink.ptms.chemdah.core.quest.objective.ObjectiveCountableI
import org.bukkit.event.Event

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objective.other.ICustomLevel
 *
 * task:0:
 *   objective: player data
 *   goal:
 *      key: def
 *      value: 10
 *
 * @author sky
 * @since 2021/3/2 5:09 下午
 */
object IPlayerData : ObjectiveCountableI<Event>() {

    override val name = "player data"
    override val event = Event::class
    override val isListener = false

    init {
        addGoal { profile, task ->
            profile.persistentDataContainer[task.goal["key"].toString()].toString() == task.goal["value"].toString()
        }
    }
}