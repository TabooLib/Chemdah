package ink.ptms.chemdah.module.kether

import ink.ptms.chemdah.core.quest.selector.InferItem
import ink.ptms.chemdah.core.quest.selector.InferItem.Companion.toInferItem
import ink.ptms.chemdah.util.getPlayer
import io.izzel.taboolib.kotlin.kether.Kether.expects
import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.QuestAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import io.izzel.taboolib.kotlin.kether.common.loader.QuestReader
import io.izzel.taboolib.util.item.Equipments
import io.izzel.taboolib.util.item.Items
import java.util.concurrent.CompletableFuture

/**
 * Chemdah
 * ink.ptms.chemdah.module.kether.ActionInventory
 *
 * @author sky
 * @since 2021/2/10 6:39 下午
 */
class ActionInventory {

    class InventoryTake(val item: InferItem.Item, val amount: Int) : QuestAction<Boolean>() {

        override fun process(frame: QuestContext.Frame): CompletableFuture<Boolean> {
            return CompletableFuture.completedFuture(item.take(frame.getPlayer().inventory, amount))
        }

        override fun toString(): String {
            return "InventoryTake(item=$item, amount=$amount)"
        }

    }

    class InventoryCheck(val item: InferItem.Item, val amount: Int) : QuestAction<Boolean>() {

        override fun process(frame: QuestContext.Frame): CompletableFuture<Boolean> {
            return CompletableFuture.completedFuture(item.check(frame.getPlayer().inventory, amount))
        }

        override fun toString(): String {
            return "InventoryCheck(item=$item, amount=$amount)"
        }

    }

    class InventorySlot(val slot: Int, val item: InferItem.Item, val amount: Int) : QuestAction<Boolean>() {

        override fun process(frame: QuestContext.Frame): CompletableFuture<Boolean> {
            val equipment = frame.getPlayer().inventory.getItem(slot)
            return if (Items.nonNull(equipment) && item.match(equipment!!)) {
                CompletableFuture.completedFuture(equipment.amount >= amount)
            } else {
                CompletableFuture.completedFuture(false)
            }
        }

        override fun toString(): String {
            return "InventorySlot(slot=$slot, item=$item, amount=$amount)"
        }

    }

    class InventoryEquipment(val equipment: Equipments, val item: InferItem.Item, val amount: Int) : QuestAction<Boolean>() {

        override fun process(frame: QuestContext.Frame): CompletableFuture<Boolean> {
            val equipment = equipment.getItem(frame.getPlayer())
            return if (Items.nonNull(equipment) && item.match(equipment!!)) {
                CompletableFuture.completedFuture(equipment.amount >= amount)
            } else {
                CompletableFuture.completedFuture(false)
            }
        }

        override fun toString(): String {
            return "InventoryEquipment(equipment=$equipment, item=$item, amount=$amount)"
        }

    }

    class InventoryClose() : QuestAction<Void>() {

        override fun process(frame: QuestContext.Frame): CompletableFuture<Void> {
            frame.getPlayer().closeInventory()
            return CompletableFuture.completedFuture(null)
        }

        override fun toString(): String {
            return "InventoryClose()"
        }

    }

    companion object {

        /**
         * inventory close
         * inventory check "minecraft:stone" amount 1
         * inventory take "minecraft:stone" amount 1
         * inventory helmet is "minecraft:stone"
         * inventory slot 9 is "minecraft:stone"
         */
        @KetherParser(["inventory"])
        fun parser() = ScriptParser.parser {
            when (it.expects(
                "has", "have", "check", "take", "remove",
                "hand", "mainhand", "offhand",
                "head", "helmet", "chest", "chestplate", "legs", "leggings", "boots", "feet",
                "slot",
                "close"
            )) {
                "has", "have", "check" -> InventoryCheck(it.nextToken().toInferItem(), matchAmount(it))
                "take", "remove" -> InventoryTake(it.nextToken().toInferItem(), matchAmount(it))
                "hand", "mainhand" -> InventoryEquipment(Equipments.HAND, matchItem(it), matchAmount(it))
                "offhand" -> InventoryEquipment(Equipments.OFF_HAND, matchItem(it), matchAmount(it))
                "head", "helmet" -> InventoryEquipment(Equipments.HEAD, matchItem(it), matchAmount(it))
                "chest", "chestplate" -> InventoryEquipment(Equipments.CHEST, matchItem(it), matchAmount(it))
                "legs", "leggings" -> InventoryEquipment(Equipments.LEGS, matchItem(it), matchAmount(it))
                "boots", "feet" -> InventoryEquipment(Equipments.FEET, matchItem(it), matchAmount(it))
                "slot" -> InventorySlot(it.nextInt(), matchItem(it), matchAmount(it))
                "close" -> InventoryClose()
                else -> error("out of case")
            }
        }

        private fun matchItem(it: QuestReader) = it.run {
            it.expect("is")
            it.nextToken().toInferItem()
        }

        private fun matchAmount(it: QuestReader) = try {
            it.mark()
            it.expect("amount")
            it.nextInt()
        } catch (ex: Throwable) {
            it.reset()
            1
        }
    }
}