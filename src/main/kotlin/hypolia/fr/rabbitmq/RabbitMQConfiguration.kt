package hypolia.fr.rabbitmq

import com.rabbitmq.client.Channel
import io.ktor.server.application.*
import org.slf4j.Logger
import kotlin.reflect.KClass

class RabbitMQConfiguration () {
    lateinit var uri: String
    var rabbitMQInstance: RabbitMQInstance? = null
    var connectionName: String? = null
    var logger: Logger? = null
        private set

    internal lateinit var initializeBlock: (Channel.() -> Unit)

    lateinit var serializeBlock:  (Any) -> ByteArray
    lateinit var deserializeBlock: (ByteArray, KClass<*>) -> Any

    fun Application.enableLogging() {
        logger = log
    }

    fun initialize(block: (Channel.() -> Unit)) {
        initializeBlock = block
    }

    fun serialize(block: (Any) -> ByteArray) {
        serializeBlock = block
    }

    fun deserialize(block: (ByteArray, KClass<*>) -> Any) {
        deserializeBlock = block
    }

    companion object {
        fun create(): RabbitMQConfiguration {
            return RabbitMQConfiguration()
        }
    }

}
