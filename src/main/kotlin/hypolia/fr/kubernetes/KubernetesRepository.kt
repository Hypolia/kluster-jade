package hypolia.fr.kubernetes

import io.kubernetes.client.openapi.models.V1NamespaceList
import io.kubernetes.client.openapi.models.V1PodList

class KubernetesRepository(private val kubernetesClient: KubernetesClient) {
    fun listPods(namespace: String = "default"): V1PodList {
        return kubernetesClient.getApi()
            .listNamespacedPod(
                namespace,
                null, null, null, null, null, null, null, null, null, null
            )
    }

    fun listeNamespaces(): V1NamespaceList {
        return kubernetesClient.listNamespaces()
    }
}
