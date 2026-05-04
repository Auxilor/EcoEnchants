import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("io.papermc.paperweight.userdev")
}

group = "com.willfp"
version = rootProject.version

dependencies {
    implementation(project(":eco-core:core-nms:v1_21_8", configuration = "shadow"))
    paperweight.paperDevBundle("26.1.1.build.+")
}

tasks {
    shadowJar {
        relocate(
            "com.willfp.ecoenchants.proxy.v1_21_8",
            "com.willfp.ecoenchants.proxy.v26_1_1",
        )

        exclude("com/willfp/ecoenchants/proxy/v1_21_8/ModernEnchantmentRegisterer*.class")
        exclude("com/willfp/ecoenchants/proxy/v1_21_8/registration/VanillaEcoEnchantsEnchantment*.class")

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