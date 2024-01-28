package hypolia.fr.kubernetes

import io.kubernetes.client.custom.Quantity
import io.kubernetes.client.openapi.ApiException
import io.kubernetes.client.openapi.models.*

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

    fun createPersistentVolume(namespace: String, name: String,storageSize: String): V1PersistentVolume? {
        val nfsVolumeSource = V1NFSVolumeSource().apply {
            server("nfs-server-address")
            path("/tmp/minecraft")
        }

        val pvSpec = V1PersistentVolumeSpec().apply {
            capacity(mapOf("storage" to Quantity(storageSize))) // Définir la capacité du volume
            accessModes(listOf("ReadWriteMany")) // Définir les modes d'accès
            nfs(nfsVolumeSource)
        }

        val metadata = V1ObjectMeta().name(name)
            .namespace(namespace)

        val pv = V1PersistentVolume().metadata(metadata).spec(pvSpec)

        return try {
            kubernetesClient
                .getApi()
                .createPersistentVolume(pv, null, null, null, null)
            pv
        } catch (e: ApiException) {
            e.printStackTrace()
            null
        }
    }

    fun createPersistentVolumeClaim(namespace: String, claimName: String, storageSize: String) {
        try {
            val accessModes = listOf("ReadWriteOnce")

            val resourceRequirements = V1ResourceRequirements().apply {
                this.requests(mapOf("storage" to Quantity(storageSize)))
            }

            val pvcSpec = V1PersistentVolumeClaimSpec()
                .accessModes(accessModes)
                .resources(resourceRequirements)

            val metadata = V1ObjectMeta().name(claimName)

            val pvc = V1PersistentVolumeClaim().metadata(metadata).spec(pvcSpec)

            kubernetesClient.getApi().createNamespacedPersistentVolumeClaim(namespace, pvc, null, null, null, null)
            println("Persistent Volume Claim $claimName created successfully.")
        } catch (e: ApiException) {
            println("Error creating Persistent Volume Claim: ${e.message}")
        }
    }

    fun createPod(namespace: String, podName: String, imageName: String, envVariables: Map<String, String>) {
        try {
            val container = V1Container()
                .name(podName)
                .image(imageName)
                .env(envVariables.map { V1EnvVar().name(it.key).value(it.value) })

            val podSpec = V1PodSpec().containers(listOf(container))

            val metadata = V1ObjectMeta().name(podName)

            val pod = V1Pod()
                .metadata(metadata)
                .spec(podSpec)

            kubernetesClient.getApi().createNamespacedPod(namespace, pod, null, null, null, null)

            println("Pod $podName created successfully.")
        } catch (e: ApiException) {
            println("Error creating Pod: ${e.message}")
        }
    }
}
