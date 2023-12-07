plugins {
    id("io.papermc.paperweight.userdev") version "1.5.3" apply false
}


group = "com.willfp"
version = rootProject.version

subprojects {
    dependencies {
        compileOnly(project(":eco-core:core-plugin"))
    }
}
