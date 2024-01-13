package hypolia.fr.s3

import io.minio.messages.Bucket

class S3Service(private val s3Repository: S3Repository) {
    fun listBuckets(): MutableList<Bucket>? {
        return s3Repository.getBuckets()
    }
}
