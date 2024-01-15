package hypolia.fr.listeners

import hypolia.fr.CreatePvcMessage
import hypolia.fr.kubernetes.KubernetesService
import hypolia.fr.rabbitmq.consume
import hypolia.fr.rabbitmq.rabbitConsumer
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.createPodListener() {
    val kubernetesService by inject<KubernetesService>()

    rabbitConsumer {
        consume<CreatePvcMessage>("create-pvc") { body ->
            println("[*] Create PVC $body")

            val namespaces = kubernetesService.getNamespaces()

            namespaces.forEach { namespace ->
                println(namespace)
            }

            this.ack()
        }
    }
}


