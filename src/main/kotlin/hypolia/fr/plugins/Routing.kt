package hypolia.fr.plugins

import hypolia.fr.kubernetes.KubernetesService
import hypolia.fr.s3.S3Service
import hypolia.fr.user.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

data class BucketResponse (
    val data: ArrayList<String>
)

data class NamespaceResponse (
    val data: List<String>
)

fun Application.configureRouting() {

    val userService by inject<UserService>()
    val s3Service by inject<S3Service>()
    val kubernetesService by inject<KubernetesService>()

    routing {
        get("/") {
            val buckets = s3Service.listBuckets()
            val items = arrayListOf<String>()
            buckets?.forEach { bucket ->
                run {
                    println(bucket.name())
                    items.add(bucket.name())
                }
            }
            call.respond(
                BucketResponse(items)
            )
        }

        get("/namespaces") {
            val namespaces = kubernetesService.getNamespaces()
            call.respond(
                NamespaceResponse(namespaces)
            )
        }
    }
}
