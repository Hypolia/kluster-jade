package hypolia.fr

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import hypolia.fr.kubernetes.KubernetesModule
import hypolia.fr.listeners.createMinecraftServerListener
import hypolia.fr.listeners.createPodListener
import hypolia.fr.listeners.createPvcListener
import hypolia.fr.plugins.*
import hypolia.fr.rabbitmq.*
import hypolia.fr.s3.MinioModule
import hypolia.fr.user.userModule
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 9999, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

data class Test (
    val id: String,
    val name: String
)

data class CreatePvcMessage (
    val key: String,
    val namespace: String,
    val claimName: String
)

fun Application.module() {
    configureRouting()

    install(Koin) {
        slf4jLogger()
        modules(userModule, MinioModule, KubernetesModule)
    }

    install(ContentNegotiation) {
        gson()
    }

    install(RabbitMQ) {
        rabbitMQInstance = RabbitMQInstance(RabbitMQConfiguration.create()
            .apply {
                uri = "amqp://rabbit:password@localhost:5672"
                connectionName = "Connection name"

                serialize { jacksonObjectMapper().writeValueAsBytes(it) }
                deserialize { bytes, type -> jacksonObjectMapper().readValue(bytes, type.javaObjectType) }

                initialize {
                    exchangeDeclare(/* exchange = */ "exchange", /* type = */ "direct", /* durable = */ true)
                    queueDeclare(
                        /* queue = */ "create-pod",
                        /* durable = */true,
                        /* exclusive = */false,
                        /* autoDelete = */false,
                        /* arguments = */emptyMap()
                    )
                    queueBind(/* queue = */ "create-pod", /* exchange = */ "exchange", /* routingKey = */ "routingKey")
                }
            })
    }



    createPodListener()
    createPvcListener()
    createMinecraftServerListener()
}


