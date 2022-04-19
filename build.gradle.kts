import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.kapt") version "1.6.10"
}

group = "com.noahhusby"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://maven.noahhusby.com/releases")
}

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
                implementation("com.noahhusby.lib:application:$husbyVersion")
                implementation("com.noahhusby.lib:data:$husbyVersion")
                implementation("mysql:mysql-connector-java:$mysqlVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                compileOnly("org.projectlombok:lombok:$lombokVersion")
                /*
                configurations.get("kapt").dependencies.add(
                    org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency(
                        "org.projectlombok",
                        "lombok",
                        lombokVersion
                    )
                )
                */
            }

        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ticketing"
            packageVersion = "1.0.0"
        }
    }
}
