package hypolia.fr.plugins

import hypolia.fr.s3.S3Service
import hypolia.fr.user.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

data class BucketResponse (
    val data: ArrayList<String>
)

fun Application.configureRouting() {

    val userService by inject<UserService>()
    val s3Service by inject<S3Service>()

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
    }
}
