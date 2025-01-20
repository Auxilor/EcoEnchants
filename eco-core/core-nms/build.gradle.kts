plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14" apply false
}

group = "com.willfp"
version = rootProject.version

subprojects {
    dependencies {
        compileOnly(project(":eco-core:core-plugin"))
    }
}
