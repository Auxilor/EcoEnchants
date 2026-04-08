plugins {
    id("io.papermc.paperweight.userdev")
}

group = "com.willfp"
version = rootProject.version

dependencies {
    implementation(project(":eco-core:core-nms:v1_21_8", configuration = "shadow"))
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(reobfJar)
    }

    reobfJar {
        mustRunAfter(shadowJar)
    }

    shadowJar {
        relocate(
            "com.willfp.ecoenchants.proxy.v1_21_8",
            "com.willfp.ecoenchants.proxy.v1_21_11",
        )

        exclude("com/willfp/ecoenchants/proxy/v1_21_8/ModernEnchantmentRegisterer*.class")
        exclude("com/willfp/ecoenchants/proxy/v1_21_8/registration/VanillaEcoEnchantsEnchantment*.class")

        duplicatesStrategy = DuplicatesStrategy.FAIL
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}