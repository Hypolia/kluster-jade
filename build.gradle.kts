val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
}

group = "hypolia.fr"
version = "0.0.1"

application {
    mainClass.set("hypolia.fr.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-gson:1.6.8")
    implementation("io.minio:minio:8.5.7")
    implementation("ch.qos.logback:logback-classic")
    implementation("com.rabbitmq:amqp-client:5.13.1")
    implementation("io.insert-koin:koin-ktor:3.5.3")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.3")
    implementation("io.ktor:ktor-serialization:1.6.10")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.kubernetes:client-java-api:15.0.0")
    implementation("io.kubernetes:client-java:15.0.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.+")
    //implementation("io.github.cdimascio:dotenv-kotlin:4.4.0")
}
