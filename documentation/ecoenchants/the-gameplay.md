---
title: "The Gameplay"
sidebar_position: 1
---

EcoEnchants is built to feel like a natural extension of vanilla. This page explains the four systems that shape how enchantments behave and how players get them: **types**, **rarity**, **means of obtaining**, and **targets**. Each is fully configurable.

## Types

Vanilla Minecraft has two types: Normal and Curse. EcoEnchants expands on this model to add more to the enchantment metagame.

You can create as many enchantment types as you want, but by default, EcoEnchants adds a third:

- **Normal:** the straightforward majority. They improve the item and make it stronger.
- **Curse:** the opposite of normal enchantments. They make the item worse and weaker.
- **Special:** a more powerful version of normal enchantments. They're very strong, so by default a player can only have one special enchantment on an item at a time. This forces players to specialise their items around a specific trait, which adds a whole new layer to the item metagame.

Types are defined in `types.yml` and control coloring and grouping.

## Rarity

Rarity is mostly hidden from the player, and functions more as a way for you to choose how each enchantment can be obtained. A rarity consists of several values. The minimum xp level required to get the enchantment from an enchanting table - should they be level 1, level 15, level 30? Something else altogether? The percentage chance for the enchantment to be applied to an item every time it is enchanted above that minimum level, the percentage chance for a villager to spawn with a trade for that enchantment, and the percentage chance for an item in a loot chest to spawn with that enchantment. All values are completely configurable, and you can create, edit, and delete as many rarities as you want.

[Check out rarity.yml here](https://github.com/Auxilor/EcoEnchants/blob/master/eco-core/core-plugin/src/main/resources/rarity.yml)

## Means of obtaining

By default, all enchantments are available from Enchanting Tables, Villagers, and Loot Chests. This is completely configurable on a per-enchant basis.

Levels are calculated based on their cost. If you get an enchantment from 1 xp level or 1 emerald, it will probably be a level 1 enchantment. Of course, it is possible to get above this at a low cost but it is rare. This is designed to be as similar to vanilla as possible.

Loot chests will generally contain higher level enchantments. This is also designed to be like vanilla, where enchantments in, for example, an end city will have a relatively high level.

Some enchantment types (special by default) are set up with a bias to make it extremely rare for them to generate or be obtained above level 1 or 2. Like everything else, you can change this.

:::tip
The level bias is controlled per type. See [Advanced Configuration](advanced-configuration) for how the bias curve works and how to tune it.
:::

## Targets

Targets are the items that can be enchanted by any given enchantment. These are things like melee weapons, tools, armor pieces, elytra, fishing rods, etc. You can create your own targets (for example if you want diamond and netherite items to have exclusive enchantments) and edit and delete as many targets as you want.

[Check out targets.yml here](https://github.com/Auxilor/EcoEnchants/blob/master/eco-core/core-plugin/src/main/resources/targets.yml)

<hr/>

## Where to go next

- **Make an enchantment:** put these systems to use in the [How to Make an Enchantment](how-to-make-a-custom-enchant) guide.
- **Tune the balance:** the [Advanced Configuration](advanced-configuration) page covers cost scaling and level bias.
- **Configure obtaining:** the [Plugin Config](plugin-config) controls enchanting tables, villagers, and loot.

