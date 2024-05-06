plugins {
    id("io.papermc.paperweight.userdev") version "1.6.2" apply false
}


group = "com.willfp"
version = rootProject.version

subprojects {
    dependencies {
        compileOnly(project(":eco-core:core-plugin"))
    }
}
