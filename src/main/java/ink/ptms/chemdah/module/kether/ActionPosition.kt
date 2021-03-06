package ink.ptms.chemdah.module.kether

import ink.ptms.chemdah.core.quest.selector.InferArea
import ink.ptms.chemdah.core.quest.selector.InferArea.Companion.toInferArea
import ink.ptms.chemdah.util.getPlayer
import io.izzel.taboolib.kotlin.kether.Kether.expects
import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.QuestAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import java.util.concurrent.CompletableFuture

/**
 * Chemdah
 * ink.ptms.chemdah.module.kether.ConditionPosition
 *
 * @author sky
 * @since 2021/2/10 6:39 下午
 */
class ActionPosition(val area: InferArea) : QuestAction<Boolean>() {

    override fun process(frame: QuestContext.Frame): CompletableFuture<Boolean> {
        return CompletableFuture.completedFuture(area.inside(frame.getPlayer().location))
    }

    override fun toString(): String {
        return "ActionPosition(area=$area)"
    }

    companion object {

        /**
         * position inside "world 0 0 0 > 10 10 10"
         */
        @KetherParser(["position"])
        fun parser() = ScriptParser.parser {
            it.expects("is", "in", "inside")
            ActionPosition(it.nextToken().toInferArea())
        }
    }
}