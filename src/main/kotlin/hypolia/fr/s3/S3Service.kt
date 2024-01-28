package hypolia.fr.s3

import io.minio.Result
import io.minio.messages.Bucket
import io.minio.messages.Item

class S3Service(private val s3Repository: S3Repository) {
    fun listBuckets(): MutableList<Bucket>? {
        return s3Repository.getBuckets()
    }

    fun listObjects(key: String, bucket: String): MutableIterable<Result<Item>>? {
        return s3Repository.getFiles(key, bucket)
    }
}
