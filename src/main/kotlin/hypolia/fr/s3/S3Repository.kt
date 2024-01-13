package hypolia.fr.s3

import io.minio.messages.Bucket

interface S3Repository {
    fun getBuckets(): MutableList<Bucket>?
}
class S3RepositoryImpl(private val s3Client: S3Client) : S3Repository {
    override fun getBuckets(): MutableList<Bucket>? {
        return s3Client.getBuckets()
    }
}
