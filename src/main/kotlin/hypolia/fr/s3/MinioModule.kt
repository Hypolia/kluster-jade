package hypolia.fr.s3

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MinioModule = module {
    single {
        MinioConfig(
            endpoint = "http://localhost:9000",
            accessKey = "U1jmiLZUFW1fX3NlA1sE",
            secretKey = "ocD3ArDwQyqRSep9DV08KlFoHPqqRKerpy3Wmyhw",
            bucketName = "development"
        )
    }

    single { S3Client(get<MinioConfig>()) }

    singleOf(::S3RepositoryImpl) { bind<S3Repository>() }
    singleOf(::S3Service)
}

data class MinioConfig(
    val endpoint: String,
    val accessKey: String,
    val secretKey: String,
    val bucketName: String
)
