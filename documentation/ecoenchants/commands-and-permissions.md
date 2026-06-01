---
title: "Commands and Permissions"
sidebar_position: 5
---

Every command and its permission node is listed below. Permissions follow the `ecoenchants.command.<name>` pattern and are granted to operators by default.

| Command                                                          | Description                                                             | Permission                               |
|------------------------------------------------------------------|-------------------------------------------------------------------------|------------------------------------------|
| `/ecoenchants reload`                                            | Reload the plugin configs (adding new enchantments requires re-logging) | `ecoenchants.command.reload`             |
| `/enchant <enchant> <level>`                                     | Enchant the held item                                                   | `ecoenchants.command.enchant`            |
| `/enchantinfo <enchant> [level]`                                 | Open the enchant info GUI for the specified enchantment at an optional level | `ecoenchants.command.enchantinfo`   |
| `/ecoenchants gui`                                               | Open the enchantment GUI                                                | `ecoenchants.command.gui`                |
| `/ecoenchants giverandombook <player> [type/rarity] [min] [max]` | Give a player a random enchanted book                                   | `ecoenchants.command.giverandombook`     |
| `/ecoenchants import <id>`                                       | Import an enchant from [lrcdb](https://lrcdb.auxilor.io/)               | `ecoenchants.command.import`             |
| `/ecoenchants export <id>`                                       | Export an enchant to [lrcdb](https://lrcdb.auxilor.io/)                 | `ecoenchants.command.export`             | 
| `/ecoenchants toggledescriptions`                                | Let players toggle enchantment descriptions                             | `ecoenchants.command.toggledescriptions` |

### Additional Permissions

| Permission                   | Description                                                                                   |
|------------------------------|-----------------------------------------------------------------------------------------------|
| `ecoenchants.fromtable.<id>` | Permission to allow an enchantment to be obtained from an enchanting table (given by default) |

<hr/>

## Where to go next

- **Make an enchantment to use these on:** the [How to Make an Enchantment](how-to-make-a-custom-enchant) guide.
- **Configure the plugin:** every option in the [Plugin Config](plugin-config).