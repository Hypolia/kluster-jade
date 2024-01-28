package hypolia.fr.s3

import io.minio.CopySource
import io.minio.ListObjectsArgs
import io.minio.messages.Bucket
import io.minio.messages.Item
import io.minio.Result

interface S3Repository {
    fun getBuckets(): MutableList<Bucket>?
    fun getFiles(key: String, bucket: String): MutableIterable<Result<Item>>?
}
class S3RepositoryImpl(private val s3Client: S3Client) : S3Repository {
    override fun getBuckets(): MutableList<Bucket>? {
        return s3Client.getBuckets()
    }

    override fun getFiles(key: String, bucket: String): MutableIterable<Result<Item>>? {
        return s3Client.getClient().listObjects(
            ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(key)
                .recursive(true)
                .build()
        )
    }
}
