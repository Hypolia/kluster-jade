package hypolia.fr.kubernetes
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.models.V1NamespaceList

class KubernetesClient(private val coreV1Api: CoreV1Api) {
    fun getApi(): CoreV1Api {
        return coreV1Api
    }

    fun listNamespaces(): V1NamespaceList {
        return coreV1Api.listNamespace(null, null, null, null, null, null, null, null, null, null)
    }
}
