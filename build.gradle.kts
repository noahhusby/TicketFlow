import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.kapt") version "1.6.10"
    kotlin("plugin.lombok") version "1.6.10"
    id("io.freefair.lombok") version "5.3.0"
}

group = "com.noahhusby"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://maven.noahhusby.com/releases")
}

@OptIn(ExperimentalComposeLibrary::class)
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)
                implementation("com.noahhusby.lib:application:$husbyVersion")
                implementation("com.noahhusby.lib:data:$husbyVersion")
                implementation("mysql:mysql-connector-java:$mysqlVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
            }
        }
    }
}

kapt {
    keepJavacAnnotationProcessors = true
}

kotlinLombok {
    lombokConfigurationFile(file("lombok.config"))
}

compose.desktop {
    application {
        mainClass = "com.noahhusby.ticketflow.TicketFlowKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ticketing"
            packageVersion = "1.0.0"
        }
    }
}
