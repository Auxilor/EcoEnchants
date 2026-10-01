import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("io.papermc.paperweight.userdev")
}

group = "com.willfp"
version = rootProject.version

dependencies {
    implementation(project(":eco-core:core-nms:v26_2", configuration = "shadow"))
    paperweight.paperDevBundle("26.3.build.+")
}

tasks {
    shadowJar {
        relocate(
            "com.willfp.ecoenchants.proxy.v26_2",
            "com.willfp.ecoenchants.proxy.v26_3",
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
