package hypolia.fr.kubernetes

import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.util.KubeConfig
import java.io.File
import java.io.FileReader

data class KubernetesConfig(
    val kubeConfigPath: String = getDefaultKubeConfigPath(),
    val context: String? = null
)

fun getDefaultKubeConfigPath(): String {
    val kubeConfigEnv = System.getenv("KUBECONFIG")
    return if (!kubeConfigEnv.isNullOrBlank()) {
        kubeConfigEnv
    } else {
        val homeDir = System.getProperty("user.home")
        println(homeDir)
        "$homeDir/.kube/config"
    }
}

fun KubernetesConfig.toKubernetesConfig(): KubeConfig? {
    val kubeConfig = KubeConfig.loadKubeConfig(FileReader(kubeConfigPath))
    kubeConfig.setContext(context)

    return kubeConfig
}
