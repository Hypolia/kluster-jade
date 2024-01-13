package hypolia.fr

import com.rabbitmq.client.*
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class RabbitMqModule (
    private val username: String,
    private val password: String,
    private val queueHandlers: Map<String, (String) -> Unit>
) {
    private val factory = ConnectionFactory()
    private val connection: Connection
    private val channel: Channel

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
        for ((queue, handler) in queueHandlers) {
            val channel = connection.createChannel()
            channel.queueDeclare(queue, true, false, false, null)

            val consumer = object : DefaultConsumer(channel) {
                override fun handleDelivery(
                    consumerTag: String?,
                    envelope: Envelope?,
                    properties: AMQP.BasicProperties?,
                    body: ByteArray?
                ) {
                    val message = body?.toString(Charsets.UTF_8)
                    handler.invoke(message.orEmpty())
                }
            }

            channel.basicConsume(queue, true, consumer)
        }
    }

    fun stopListening () {
        connection.close()
    }
}

fun Application.configureRabbitMqModule () {
    val queueHandlers = mapOf<String, (String) -> Unit>(
        "create-pod" to { message -> handleCreatePod(message) },
        "create-pvc" to { message -> handleCreatePVC(message) }
    )

    val rabbitMqModule = RabbitMqModule("rabbit", "password", queueHandlers)

    environment.monitor.subscribe(ApplicationStopping) {
        rabbitMqModule.stopListening()
    }
}

fun handleCreatePod(message: String) {
    println(message)
}

fun handleCreatePVC(message: String) {

}
