import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.0"
    id("java")
    id("java-library")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.3.1"
    id("com.willfp.libreforge-gradle-plugin") version "2.0.0"
}

group = "com.willfp"
version = findProperty("version")!!
val libreforgeVersion = findProperty("libreforge-version")
val ecoVersion = findProperty("eco-version")

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

java {
    withJavadocJar()
}

publishing {
    publications {
        // maven-private: only the shaded jar
        create<MavenPublication>("private") {
            artifactId = rootProject.name
        }
        // maven-releases + GitHub: full set (none, all, sources, javadoc)
        create<MavenPublication>("release") {
            artifactId = rootProject.name
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "Auxilor"
            url = uri("https://repo.auxilor.io/repository/maven-private/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
        maven {
            name = "AuxilorReleases"
            url = uri("https://repo.auxilor.io/repository/maven-releases/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

afterEvaluate {
    publishing.publications.named<MavenPublication>("private") {
        artifact(tasks.named("libreforgeJar"))
    }
}

tasks.matching { it.name.startsWith("generatePomFileFor") }.configureEach {
    mustRunAfter(tasks.named("clean"))
}
tasks.register("publishToAuxilor") {
    dependsOn(
        "publishPrivatePublicationToAuxilorRepository",
        "publishReleasePublicationToAuxilorReleasesRepository",
    )
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
