package ink.ptms.chemdah.core.quest.objective.bukkit

import ink.ptms.chemdah.core.quest.objective.Dependency
import org.bukkit.event.entity.EntityDeathEvent

/**
 * Chemdah
 * ink.ptms.chemdah.core.quest.objective.bukkit.IPlayerKill
 *
 * @author sky
 * @since 2021/3/2 5:09 下午
 */
@Dependency("minecraft")
object IPlayerKill : AEntityDeath<EntityDeathEvent>() {

    override val name = "player kill"
    override val event = EntityDeathEvent::class

    init {
        handler {
            entity.killer
        }
        addCondition("weapon") { e ->
            toInferItem().isItem(e.entity.killer!!.inventory.itemInMainHand)
        }
        addCondition("victim") { e ->
            toInferEntity().isEntity(e.entity)
        }
    }
}