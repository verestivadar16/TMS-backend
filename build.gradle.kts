val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val hikari_version: String by project

repositories{
    mavenCentral()
}


 plugins {
    application
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
     //id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.TMS"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
//    val isDevelopment: Boolean = project.ext.has("development")
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.TMS.ApplicationKt"))
        }
    }
}

repositories {
    mavenCentral()
   //jcenter()

}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation ("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation ("io.ktor:ktor-server-core:$ktor_version")
    implementation ("io.ktor:ktor-auth:$ktor_version")
    implementation ("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation ("io.ktor:ktor-server-auth:$ktor_version")
    implementation ("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation ("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:2.2.1")
    testImplementation ("io.ktor:ktor-server-tests:$ktor_version")
    implementation ("io.ktor:ktor-server-sessions:$ktor_version")

    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")

    implementation("io.ktor:ktor-server:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktor_version")


    implementation ("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation ("org.postgresql:postgresql:$postgres_version")
    implementation ("com.zaxxer:HikariCP:$hikari_version")

    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }

    docker {
        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
        localImageName.set("ktor-tms")
        imageTag.set("0.0.1-preview")
        portMappings.set(listOf(
            io.ktor.plugin.features.DockerPortMapping(
                8081,
                8081,
                io.ktor.plugin.features.DockerPortMappingProtocol.TCP
            )
        ))

        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "ktor-tms" },
                username = providers.environmentVariable("tivike16"),
                password = providers.environmentVariable("Tivadar19")
            )
        )
    }
}