package hypolia.fr.s3

import io.minio.MinioClient
import io.minio.Result
import io.minio.errors.MinioException
import io.minio.messages.Bucket
import io.minio.messages.Item
import java.lang.RuntimeException

class S3Client(private val minioConfig: MinioConfig) {
    private val minioClient: MinioClient

    init {
        try {
            minioClient = MinioClient.builder()
                .endpoint(minioConfig.endpoint)
                .credentials(minioConfig.accessKey, minioConfig.secretKey)
                .build()
            checkConnection()
        } catch (e: MinioException) {
            println("Erreur lors de la vérification de la connexion MinIO: ${e.message}")
            throw e
        }
    }

    private fun checkConnection() {
        try {
            minioClient.listBuckets()
        } catch (e: MinioException) {
            throw RuntimeException("Erreur lors de la vérification de la connection MinIO", e)
        }
    }

    public fun getBuckets(): MutableList<Bucket>? {
        return minioClient.listBuckets()
    }
}
