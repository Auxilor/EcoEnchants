import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "2.1.0"
    id("com.gradleup.shadow") version "8.3.5"
    id("com.willfp.libreforge-gradle-plugin") version "1.0.0"
}

group = "com.willfp"
version = findProperty("version")!!
val libreforgeVersion = findProperty("libreforge-version")

base {
    archivesName.set(project.name)
}

dependencies {
    implementation(project(":eco-core:core-plugin"))
    implementation(project(":eco-core:core-nms:v1_17_R1"))
    implementation(project(":eco-core:core-nms:v1_18_R1"))
    implementation(project(":eco-core:core-nms:v1_18_R2"))
    implementation(project(":eco-core:core-nms:v1_19_R1"))
    implementation(project(":eco-core:core-nms:v1_19_R2"))
    implementation(project(":eco-core:core-nms:v1_19_R3"))
    implementation(project(":eco-core:core-nms:v1_20_R1"))
    implementation(project(":eco-core:core-nms:v1_20_R2"))
    implementation(project(":eco-core:core-nms:v1_20_R3", configuration = "reobf"))
    implementation(project(":eco-core:core-nms:v1_21", configuration = "reobf"))
    implementation(project(":eco-core:core-nms:v1_21_3", configuration = "reobf"))
    implementation(project(":eco-core:core-nms:v1_21_4", configuration = "reobf"))
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "com.gradleup.shadow")

    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.auxilor.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.codemc.org/repository/nms/")
        maven("https://repo.essentialsx.net/releases/")

        maven("https://jitpack.io") {
            content { includeGroupByRegex("com\\.github\\..*") }
        }
    }

    dependencies {
        compileOnly("com.willfp:eco:6.75.0")
        compileOnly("org.jetbrains:annotations:23.0.0")
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
        compileOnly("com.github.ben-manes.caffeine:caffeine:3.1.5")
    }

    tasks {
        shadowJar {
            relocate("com.willfp.libreforge.loader", "com.willfp.ecoenchants.libreforge.loader")
        }

        compileKotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
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

        withType<JavaCompile>().configureEach {
            options.release = 17
        }
    }

    java {
        withSourcesJar()
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}
