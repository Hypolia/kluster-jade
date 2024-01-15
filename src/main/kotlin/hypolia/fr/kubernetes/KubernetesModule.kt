package hypolia.fr.kubernetes

import io.ktor.serialization.*
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.util.Config
import org.koin.dsl.module
import io.kubernetes.client.util.KubeConfig


val KubernetesModule = module {
    single {
        KubernetesConfig(
            kubeConfigPath = "/Users/nathaelbonnal/.kube/config",
            context = "docker-desktop"
        )
    }

    single {
        val config = get<KubernetesConfig>().toKubernetesConfig()
        val apiClient = Config.fromConfig(config).apply { isDebugging = true }
        Configuration.setDefaultApiClient(apiClient)

        CoreV1Api(apiClient)
    }

    single { KubernetesClient(get()) }
    single { KubernetesRepository(get()) }
    single { KubernetesService(get()) }
}
