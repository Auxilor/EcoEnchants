plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21" apply false
}

group = "com.willfp"
version = rootProject.version

val paperAdventureAnsiVersion = "4.26.1"

subprojects {
    dependencies {
        compileOnly(project(":eco-core:core-plugin"))
    }
}

// Paper's current dev bundle metadata omits the version for
// net.kyori:adventure-text-serializer-ansi in newer mappings, so we pin it here
// for the affected modules to keep CI and local builds reproducible.
listOf(
    project(":eco-core:core-nms:v1_21_11"),
    project(":eco-core:core-nms:v26_1_1"),
    project(":eco-core:core-nms:v26_1_2"),
).forEach { nmsProject ->
    nmsProject.dependencies {
        add("compileOnly", "net.kyori:adventure-text-serializer-ansi:$paperAdventureAnsiVersion")
    }
}
