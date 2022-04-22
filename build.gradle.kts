import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
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
                implementation("com.zaxxer:HikariCP:$hikariVersion")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.noahhusby.ticketflow.TicketFlowKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi)
            packageName = "TicketFlow"
            packageVersion = "1.0.0"
            copyright = "© 2022 Noah Husby. All rights reserved."
            vendor = "Husby Labs"
            licenseFile.set(project.file("LICENSE"))
            macOS {
                iconFile.set(project.file("src/jvmMain/resources/icon.icns"))
            }
            windows {
                iconFile.set(project.file("src/jvmMain/resources/icon.ico"))
                dirChooser = false
                menuGroup = "TicketFlow"
            }
            linux {
                iconFile.set(project.file("src/jvmMain/resources/icon.png"))
            }
        }
    }
}
