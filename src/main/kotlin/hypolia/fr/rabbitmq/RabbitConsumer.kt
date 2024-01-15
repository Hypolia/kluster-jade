package hypolia.fr.rabbitmq

import com.rabbitmq.client.*
import io.ktor.server.application.Application
import io.ktor.server.application.plugin
import java.io.IOException


@RabbitMQMarker
fun Application.rabbitConsumer(configuration: RabbitMQInstance.() -> Unit): RabbitMQInstance =
    plugin(RabbitMQ).apply(configuration)

@RabbitMQMarker
inline fun <reified T> RabbitMQInstance.consume(
    queue: String,
    autoAck: Boolean = true,
    basicQos: Int? = null,
    crossinline rabbitDeliverCallback: ConsumerScope.(body: T) -> Unit,
) {
    withChannel {
        basicQos?.let { this.basicQos(it) }
        val consumer = object : DefaultConsumer(this) {
            @Throws(IOException::class)
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope,
                properties: AMQP.BasicProperties?,
                body: ByteArray
            ) {
                val mappedEntity = deserialize<T>(body)

                val scope = ConsumerScope(
                    channel = this.channel,
                    envelope = envelope
                )

                rabbitDeliverCallback.invoke(scope, mappedEntity)
            }
        }

        channel.basicConsume(queue, autoAck, consumer)
    }
}

class ConsumerScope(
    private val channel: Channel,
    private val envelope: Envelope,
) {

    fun ack(multiple: Boolean = false) {
        channel.basicAck(envelope.deliveryTag, multiple)
    }

    fun nack(multiple: Boolean = false, requeue: Boolean = false) {
        channel.basicNack(envelope.deliveryTag, multiple, requeue)
    }

    fun reject(requeue: Boolean = false) {
        channel.basicReject(envelope.deliveryTag, requeue)
    }
}
