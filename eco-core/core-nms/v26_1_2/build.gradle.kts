import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("io.papermc.paperweight.userdev")
}

group = "com.willfp"
version = rootProject.version

dependencies {
    implementation(project(":eco-core:core-nms:v26_1_1", configuration = "shadow"))
    paperweight.paperDevBundle("26.1.2.build.+")
}

tasks {
    shadowJar {
        relocate(
            "com.willfp.ecoenchants.proxy.v26_1_1",
            "com.willfp.ecoenchants.proxy.v26_1_2",
        )

        duplicatesStrategy = DuplicatesStrategy.FAIL
    }

    compileJava {
        options.release.set(25)
    }

    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
        }
    }
}