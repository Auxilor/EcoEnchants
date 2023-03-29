plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("com.willfp.libreforge-gradle-plugin") version "1.0.0"
}

group = "com.willfp"
version = findProperty("version")!!
val libreforgeVersion = findProperty("libreforge-version")

base {
    archivesName.set(project.name)
}

dependencies {
    project(":eco-core").dependencyProject.subprojects {
        implementation(this)
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io")
        maven("https://repo.auxilor.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.codemc.org/repository/nms/")
        maven("https://repo.essentialsx.net/releases/")
    }

    dependencies {
        compileOnly("com.willfp:eco:6.53.0")
        compileOnly("org.jetbrains:annotations:23.0.0")
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
        compileOnly("com.github.ben-manes.caffeine:caffeine:3.1.0")
    }

    java {
        withSourcesJar()
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    tasks {
        shadowJar {
            relocate("com.willfp.libreforge.loader", "com.willfp.ecoenchants.libreforge.loader")
        }

        compileKotlin {
            kotlinOptions {
                jvmTarget = "17"
            }
        }

        compileJava {
            options.isDeprecation = true
            options.encoding = "UTF-8"

            dependsOn(clean)
        }

        processResources {
            filesMatching(listOf("**plugin.yml", "**eco.yml")) {
                expand(
                    "version" to project.version,
                    "libreforgeVersion" to libreforgeVersion,
                    "pluginName" to rootProject.name
                )
            }
        }

        build {
            dependsOn(shadowJar)
        }
    }
}
