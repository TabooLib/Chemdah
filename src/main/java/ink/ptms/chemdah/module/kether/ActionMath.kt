package ink.ptms.chemdah.module.kether

import ink.ptms.chemdah.util.switch
import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.loader.types.ArgTypes
import io.izzel.taboolib.util.Coerce

/**
 * Chemdah
 * ink.ptms.chemdah.module.kether.ActionMath
 *
 * @author sky
 * @since 2021/6/14 2:59 下午
 */
class ActionMath {

    companion object {

        @KetherParser(["max"], namespace = "chemdah")
        fun max() = ScriptParser.parser {
            it.switch {
                val n1 = it.next(ArgTypes.ACTION)
                val n2 = it.next(ArgTypes.ACTION)
                actionFuture {
                    newFrame(n1).run<Any>().thenAccept { n1 ->
                        newFrame(n2).run<Any>().thenAccept { n2 ->
                            it.complete(kotlin.math.max(Coerce.toDouble(n1), Coerce.toDouble(n2)))
                        }
                    }
                }
            }
        }

        @KetherParser(["ceil"], namespace = "chemdah")
        fun ceil() = ScriptParser.parser {
            it.switch {
                val n1 = it.next(ArgTypes.ACTION)
                actionFuture {
                    newFrame(n1).run<Any>().thenAccept { n1 ->
                        it.complete(kotlin.math.ceil(Coerce.toDouble(n1)))
                    }
                }
            }
        }
    }
}