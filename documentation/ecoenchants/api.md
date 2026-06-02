---
title: "API"
sidebar_position: 8
---

EcoEnchants is fully open-source, and you can build against it from your own plugin. This page shows how to add it as a dependency.

## Source code

The full source is on [GitHub](https://github.com/Auxilor/EcoEnchants).

## Adding the dependency

1. Add the Auxilor repository and the EcoEnchants dependency to your `build.gradle.kts`:

   ```kotlin
   repositories {
       maven("https://repo.auxilor.io/repository/maven-public/")
   }

   dependencies {
       compileOnly("com.willfp:EcoEnchants:<version>")
   }
   ```

The latest version available on the repo can be found [here](https://github.com/Auxilor/EcoEnchants/tags)

<hr/>

## Where to go next

- **The framework:** EcoEnchants is built on [eco](https://github.com/Auxilor/eco), where most shared APIs live.
- **Make an enchantment from config:** the [How to Make an Enchantment](how-to-make-a-custom-enchant) guide.
