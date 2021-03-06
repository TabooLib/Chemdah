package ink.ptms.chemdah.core.conversation

import ink.ptms.chemdah.util.colored
import ink.ptms.chemdah.util.extend
import ink.ptms.chemdah.util.namespaceConversationPlayer
import ink.ptms.chemdah.util.print
import io.izzel.taboolib.kotlin.kether.KetherFunction
import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.util.Coerce
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Chemdah
 * ink.ptms.chemdah.core.conversation.Reply
 *
 * @author sky
 * @since 2021/2/9 6:23 下午
 */
data class PlayerReply(
    val root: MutableMap<String, Any>,
    var condition: String?,
    var text: String,
    val action: MutableList<String>,
    val uuid: UUID = UUID.randomUUID()
) {

    fun build(session: Session): String {
        return try {
            KetherFunction.parse(text, namespace = namespaceConversationPlayer) {
                extend(session.variables)
            }.colored()
        } catch (e: Throwable) {
            e.print()
            e.localizedMessage
        }
    }

    fun check(session: Session): CompletableFuture<Boolean> {
        return if (condition == null) {
            CompletableFuture.completedFuture(true)
        } else {
            try {
                KetherShell.eval(condition!!, namespace = namespaceConversationPlayer) {
                    extend(session.variables)
                }.thenApply {
                    Coerce.toBoolean(it)
                }
            } catch (e: Throwable) {
                e.print()
                CompletableFuture.completedFuture(false)
            }
        }
    }

    fun select(session: Session): CompletableFuture<Void> {
        return try {
            KetherShell.eval(action, namespace = namespaceConversationPlayer) {
                extend(session.variables)
            }.thenAccept {
                if (session.isNext) {
                    session.isNext = false
                } else {
                    session.close()
                }
            }
        } catch (e: Throwable) {
            e.print()
            session.close()
            CompletableFuture.completedFuture(null)
        }
    }
}