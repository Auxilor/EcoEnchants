import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.0"
    id("java")
    id("java-library")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.3.1"
    id("com.willfp.libreforge-gradle-plugin") version "2.1.0"
}

group = "com.willfp"
version = findProperty("version")!!
// useGradleVersions=true (set by release workflows) pins dependencies to the
// versions in gradle.properties; otherwise dev builds track the latest master snapshot.
val useGradleVersions = findProperty("useGradleVersions") == "true"
val libreforgeVersion = if (useGradleVersions) findProperty("libreforge-version") else "dev-SNAPSHOT"
val ecoVersion = if (useGradleVersions) findProperty("eco-version") else "dev-SNAPSHOT"

val embeddedLibreforge by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false
}

base {
    archivesName.set(project.name)
}

dependencies {
    implementation(project(":eco-core:core-plugin"))
    implementation(project(":eco-core:core-nms:v1_21_8", "reobf"))
    implementation(project(":eco-core:core-nms:v1_21_10", "reobf"))
    implementation(project(":eco-core:core-nms:v1_21_11", "reobf"))
    implementation(project(":eco-core:core-nms:v26_1_1", "shadow"))
    implementation(project(":eco-core:core-nms:v26_1_2", "shadow"))
    implementation(project(":eco-core:core-nms:v26_2", "shadow"))

    embeddedLibreforge("com.willfp:libreforge:${libreforgeVersion!!}:shadow@jar")
}

tasks {
    shadowJar {
        from(embeddedLibreforge) {
            rename { "libreforge-$libreforgeVersion-shadow.jar" }
        }
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "com.gradleup.shadow")

    repositories {
        mavenLocal {
            content {
                excludeGroup("com.willfp")
                excludeGroup("com.auxilor")
                excludeGroup("com.exanthiax")
            }
        }
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

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }

    dependencies {
        compileOnly("com.willfp:eco:$ecoVersion")
        compileOnly("org.jetbrains:annotations:26.0.2")
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.3.0")
        compileOnly("com.github.ben-manes.caffeine:caffeine:3.2.3")
    }

    tasks {
        shadowJar {
            exclude("META-INF/**")
            relocate("com.willfp.libreforge.loader", "com.willfp.ecoenchants.libreforge.loader")
            relocate("kotlin", "com.willfp.eco.libs.kotlin")
            relocate("kotlin.jvm", "com.willfp.eco.libs.kotlin.jvm")
            relocate("kotlin.coroutines", "com.willfp.eco.libs.kotlin.coroutines")
            relocate("kotlin.reflect", "com.willfp.eco.libs.kotlin.reflect")
        }

        compileKotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
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
                    "libreforgeVersion" to libreforgeVersion!!,
                    "pluginName" to rootProject.name
                )
            }
        }

        build {
            dependsOn(shadowJar)
        }

        withType<JavaCompile>().configureEach {
            options.release = 21
        }
    }

    java {
        withSourcesJar()
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }
}
