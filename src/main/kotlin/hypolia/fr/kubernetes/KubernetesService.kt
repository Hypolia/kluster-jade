package hypolia.fr.kubernetes

class KubernetesService(private val kubernetesRepository: KubernetesRepository) {
    fun getNamespaces(): List<String> {
        return kubernetesRepository
            .listeNamespaces()
            .items.map { it.metadata?.name ?: "Unkown" }
    }

    fun createPvc(namespace: String, claimName: String, storageSize: String) {
        kubernetesRepository
            .createPersistentVolumeClaim(namespace, claimName, storageSize)
    }

    fun createPv(namespace: String, pvName: String, storageSize: String) {
        kubernetesRepository
            .createPersistentVolume(namespace, pvName, storageSize)
    }

    fun createMinecraftServer(name: String, version: String) {
        val mapData = mapOf("EULA" to "TRUE", "TYPE" to "SPIGOT", "VERSION" to version)
        kubernetesRepository.createPod("minecraft", name, "itzg/minecraft-server", mapData)
    }
}
