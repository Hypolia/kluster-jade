package hypolia.fr.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            println("PROUT")
            call.respondText("Hello World!")
        }
    }
}
