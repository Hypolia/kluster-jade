package hypolia.fr.kubernetes

class KubernetesService(private val kubernetesRepository: KubernetesRepository) {
    fun getNamespaces(): List<String> {
        return kubernetesRepository
            .listeNamespaces()
            .items.map { it.metadata?.name ?: "Unkown" }
    }
}
