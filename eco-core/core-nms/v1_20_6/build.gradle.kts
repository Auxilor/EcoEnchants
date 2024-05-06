plugins {
    id("io.papermc.paperweight.userdev")
}

group = "com.willfp"
version = rootProject.version

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(reobfJar)
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        withSourcesJar()
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "21"
        }
    }
}
