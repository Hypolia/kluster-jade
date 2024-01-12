package hypolia.fr

import com.rabbitmq.client.*
import io.ktor.server.application.*
import io.ktor.util.*
import io.ktor.util.logging.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

@OptIn(DelicateCoroutinesApi::class)
class RabbitMqModule (private val username: String, private val password: String) {
    private val factory = ConnectionFactory()
    private val connection: Connection
    private val channel: Channel
    private val queues = listOf("create-pod")

    init {
        factory.host = "localhost"
        factory.username = username
        factory.password = password

        connection = factory.newConnection()
        channel = connection.createChannel()

        println("INIT RABBITMQ MODULE")


        GlobalScope.launch {
            startListening()
        }
    }

    private fun startListening() {
        println("Start listening")

        channel.basicQos(1)

        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray?
            ) {
                val message = body?.toString(Charsets.UTF_8)
                println("Received message on queue $message")

                if (envelope != null) {
                    channel.basicAck(envelope.deliveryTag, false)
                }
            }
        }

        channel.basicConsume("create-pod", false, consumer)
    }

    fun stopListening () {
        connection.close()
    }
}

fun Application.configureRabbitMqModule () {
    val rabbitMqModule = RabbitMqModule("rabbit", "password")


    environment.monitor.subscribe(ApplicationStopping) {
        rabbitMqModule.stopListening()
    }
}
