package hypolia.fr.rabbitmq

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import io.ktor.server.application.*
import io.ktor.util.*

class RabbitMQ {

    companion object Feature : BaseApplicationPlugin<Application, RabbitMQConfiguration, RabbitMQInstance> {

        val RabbitMQKey = AttributeKey<RabbitMQInstance>("RabbitMQ")

        override val key: AttributeKey<RabbitMQInstance>
            get() = RabbitMQKey

        override fun install(
            pipeline: Application,
            configure: RabbitMQConfiguration.() -> Unit
        ): RabbitMQInstance {
            val configuration = RabbitMQConfiguration.create()
            configuration.apply(configure)

            val rabbit = configuration.rabbitMQInstance ?: RabbitMQInstance(configuration)
            rabbit.apply { initialize() }

            pipeline.attributes.put(key, rabbit)

            return rabbit.apply {
                println("[*] RabbitMQ initialized")
            }
        }
    }
}
