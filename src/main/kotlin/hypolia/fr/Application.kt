package hypolia.fr

import hypolia.fr.plugins.*
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

fun Application.module() {
    configureRouting()
    configureRabbitMqModule()

    install(Koin) {
        slf4jLogger()
        modules(userModule, MinioModule)
    }

    install(ContentNegotiation) {
        gson()
    }
}
