package hypolia.fr.listeners

import hypolia.fr.CreatePvcMessage
import hypolia.fr.kubernetes.KubernetesService
import hypolia.fr.rabbitmq.consume
import hypolia.fr.rabbitmq.rabbitConsumer
import hypolia.fr.s3.S3Service
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.createPvcListener() {
    val kubernetesService by inject<KubernetesService>()
    val s3Service by inject<S3Service>()

    rabbitConsumer {
        consume<CreatePvcMessage>("create-pvc", true) {
            println("[*] create pvc $it")
            kubernetesService.createPv(it.namespace, it.claimName, "2Gi")

            val objects = s3Service.listObjects(it.key, "development")?.forEach {
                println(it.get().objectName())
            }

        }
    }
}
