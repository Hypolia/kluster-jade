package hypolia.fr.rabbitmq

import com.rabbitmq.client.ConnectionFactory
import org.koin.core.qualifier.named
import org.koin.core.scope.get
import org.koin.dsl.module
import org.koin.dsl.single

/*
val RabbitMQModule = module {
    single {
        ConnectionFactory().apply {
            host = getProperty("rabbitmq.host", "localhost")
            port = getProperty("rabbitmq.port", 5672)
            username = getProperty("rabbitmq.username", "rabbit")
            password = getProperty("rabbitmq.password", "password")
        }
    }

    single { RabbitMQ(get()) }
    println("TESTTTTT")
    //single { CreatePodListener() }
}
*/
