package hypolia.fr.listeners

import hypolia.fr.kubernetes.KubernetesService
import hypolia.fr.rabbitmq.consume
import hypolia.fr.rabbitmq.rabbitConsumer
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

data class CreateMinecraftServerMessage (
    val name: String,
    val type: String,
    val version: String
)

fun Application.createMinecraftServerListener() {
    val kubernetesService by inject<KubernetesService>()

    rabbitConsumer {
        consume<CreateMinecraftServerMessage>("create-minecraft-server") {
            kubernetesService.createMinecraftServer(it.name, it.version)
            println("[*] Create minecraft server ${it.type}")
        }
    }
}
