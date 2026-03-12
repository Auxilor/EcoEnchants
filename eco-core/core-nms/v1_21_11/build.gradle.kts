plugins {
    id("io.papermc.paperweight.userdev")
}

group = "com.willfp"
version = rootProject.version

dependencies {
    implementation(project(":eco-core:core-nms:v1_21_4", configuration = "shadow"))
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
            "com.willfp.ecoenchants.proxy.v1_21_4",
            "com.willfp.ecoenchants.proxy.v1_21_11",
        )

        exclude("com/willfp/ecoenchants/proxy/v1_21_4/ModernEnchantmentRegisterer*.class")
        exclude("com/willfp/ecoenchants/proxy/v1_21_4/registration/VanillaEcoEnchantsEnchantment*.class")

        duplicatesStrategy = DuplicatesStrategy.FAIL
    }
}
