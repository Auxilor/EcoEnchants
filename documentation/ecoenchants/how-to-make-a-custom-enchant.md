---
title: "How to Make an Enchantment"
sidebar_position: 2
---

EcoEnchants lets you build your own enchantments entirely from config, with no code and no compiling. Every enchantment is a single YAML file describing **how it looks**, **how it's obtained**, and **what it does**. This page walks you through creating one from scratch and explains every option you can set.

## Quick start

1. Open the `EcoEnchants/enchants/` folder in your server files.
2. Create a new file, e.g. `lifesteal.yml`. **The file name becomes the enchantment's ID.**
3. Paste in an enchantment config (copy `_example.yml` as a starting point).
4. Run `/ecoenchants reload`, then **re-log**. Adding *new* enchantments requires reconnecting; editing existing ones only needs a reload.
5. Give it to yourself with `/enchant lifesteal 1` while holding a valid item.
6. Run `/enchantinfo lifesteal` to confirm the name, description, and placeholders render correctly.

:::tip
`_example.yml` is included as a reference and is **never loaded**, so rename or copy it to make a real enchantment. You can also organise enchantments into subfolders inside `enchants/`, and they'll still load.
:::

## Naming and IDs

The **file name (without `.yml`) is the enchantment's ID**. So `razor.yml` has the ID `razor`.

That ID is what you use in commands (`/enchant razor 3`) and in the [Item Lookup System](https://hub.auxilor.io/wiki/eco/the-item-lookup-system-the-item-lookup-system).

:::warning ID rules
IDs may only contain **lowercase letters, numbers, and underscores** (`a-z`, `0-9`, `_`). No spaces, capitals, or hyphens.
:::

## The structure of an enchantment

A config has four logical parts, top to bottom:

| Part | What it controls |
| --- | --- |
| **Display** | The name, description, and type shown to players |
| **Mechanics** | What it goes on, conflicts, requirements, rarity, max level |
| **Obtaining** | Whether players can trade, find, or enchant for it |
| **Effects** | The actual functionality, i.e. what the enchantment *does* |

The rest of this page covers each part in detail. Here's a complete example with everything in place:

```yaml
# === Display: what the player sees ===
display-name: "Example" # In-game name of the enchantment
description: # Lore shown under the enchantment
  - "Gives a &a%placeholder%%&8 bonus to damage"
placeholder: "%level% * 20" # Value injected wherever %placeholder% appears
placeholders: # Extra named placeholders (optional)
  example: "%level% * 800" # Used as %example% in the description
type: normal # Enchantment type, from types.yml

# === Mechanics: where it goes and how it relates to others ===
targets: # Item groups it can apply to, from targets.yml
  - sword
conflicts: # Enchantments it can't coexist with (optional)
  - sharpness
required: # Enchantments that must be present first (optional)
  - unbreaking
rarity: common # Rarity, from rarity.yml
max-level: 4 # Highest obtainable level

# === Obtaining: how players can get it naturally ===
tradeable: true # Buyable from villagers
discoverable: true # Generates in loot chests
# To toggle individual discovery methods instead, use a map:
# discoverable:
#   chests: true
#   fishing: true
#   mob-drops: true
#   raids: true
enchantable: true # Rolls from the enchanting table
hide-from-enchantgui: false # If true, hides from the enchants GUI and /enchantinfo

# === Drag and drop: applying via an enchanted book (optional) ===
drag-and-drop:
  enabled: false # Lets players apply this enchantment by holding an enchanted book on their cursor and clicking an eligible item
  price: # Same format as any other eco price
    value: "100"
    type: coins
    display: "&a%value% coins"
  price-level-multiplier: "%level%" # Optional; %level% is the book's stored level, used as the price multiplier

# === Effects: what the enchantment actually does ===
effects:
  - id: damage_multiplier # The effect to run
    args:
      multiplier: "1 + 0.2 * %level%" # Effect strength, scaling with level
    triggers:
      - melee_attack # When it fires

conditions: [ ] # When the enchantment may activate ([ ] = always)
```

### Display

This is everything the player sees on the item and in menus.

```yaml
display-name: "Example" # In-game name; supports color codes like &a and &8
description: # Lore shown under the enchant; one string or a list of lines, color codes and placeholders work
  - "Gives a &a%placeholder%%&8 bonus to damage"
placeholder: "%level% * 20" # Optional; replaces %placeholder% in the description, good for scaling numbers
placeholders: # Optional; define extra named placeholders when one isn't enough
  example: "%level% * 800" # Referenced as %example% in the description
type: normal # Enchantment type from types.yml; controls coloring and grouping (e.g. normal, curse, special)
```

:::info Math in placeholders
Placeholder values are **expressions**, not plain text. `%level% * 20` is evaluated, so a level-3 enchantment shows `60`. You can use `%level%` and any other available placeholders inside them.
:::

### Mechanics

This part defines where the enchantment can go and how it relates to others.

```yaml
targets: # Item groups it applies to (sword, axe, bow, armor...) from targets.yml; list as many as you like
  - sword
conflicts: # Optional; IDs of enchantments that can't share an item with this one
  - sharpness
required: # Optional; IDs that must already be on the item before this can apply
  - unbreaking
rarity: common # Rarity from rarity.yml; affects coloring and how likely it rolls randomly
max-level: 4 # Highest level players can reach; effects scale with %level% up to here
```

### Obtaining

These flags control how players can get the enchantment naturally and what they can see.

```yaml
tradeable: true # Can be bought from villagers
discoverable: true # Can generate in loot chests
enchantable: true # Can roll from the enchanting table
hide-from-enchantgui: false # If true, hides from the enchants GUI and /enchantinfo for players without ecoenchants.seehidden
```

:::tip Admin-only enchantments
Set all three obtain flags to `false` so the enchantment can only be given via `/enchant` or `/ecoenchants giverandombook`. Combine with the `ecoenchants.fromtable.<id>` permission to gate enchanting-table access per enchantment.

Set `hide-from-enchantgui: true` to hide a WIP or staff-only enchantment from players entirely. Players with the `ecoenchants.seehidden` permission (operators by default) can still see and look up hidden enchantments.
:::

`discoverable` can also be a map, to toggle individual discovery methods instead of all of them at once:

```yaml
discoverable:
  chests: true # Loot chests
  fishing: true # Fishing rewards
  mob-drops: true # Mob drop tables
  raids: true # Raid rewards
```

Missing sub-keys default to `true`. If `discoverable` is `false` (the plain boolean form), every method is disabled regardless of the map.

### Drag and drop

Optionally, players can apply the enchantment by holding an enchanted book on their cursor and clicking an eligible item in their own inventory, in exchange for a price:

```yaml
drag-and-drop:
  enabled: false # Off by default
  price: # Same format as any other eco price - see the price lookup system docs
    value: "100"
    type: coins
    display: "&a%value% coins"
  price-level-multiplier: "%level%" # Optional expression; %level% is the book's stored level, used as the price multiplier
```

If the item already has this enchantment, applying another book of the same level bumps it by one level (up to `max-level`); a higher-level book always takes the higher level. The price is charged once per application, using the multiplier evaluated against the book's level *before* any bump.

### Effects

This is the heart of the enchantment, i.e. what it actually does.

```yaml
effects:
  - id: damage_multiplier # Which effect to run
    args:
      multiplier: "1 + 0.2 * %level%" # Effect strength; scales with level
    triggers:
      - melee_attack # Event that fires the effect

conditions: [ ] # Restrict when the enchant works ([ ] = always active)
```

Each effect has an **`id`** (what it does), **`args`** (how strongly), and **`triggers`** (when it fires). The example multiplies melee damage by `1 + 0.2 * level` every time the player lands a melee hit.

`conditions` restrict *when* the enchantment is allowed to work at all (for example, only at night, or only while sneaking). An empty list `[ ]` means "always active".

:::danger The Effects section is its own system
Effects, triggers, conditions, filters, and mutators are a shared system across all Eco plugins, with hundreds of options. They are **not** documented here, so see the dedicated guides:

- [Configuring an Effect](https://hub.auxilor.io/wiki/libreforge/configuring-an-effect) is the full effect/trigger/condition reference.
- [Configuring an Effect Chain](https://hub.auxilor.io/wiki/libreforge/configuring-a-chain) lets you string multiple effects under one trigger for advanced enchantments.

Use `%level%` as a placeholder anywhere in this section to scale with enchantment level.
:::

## Internal placeholders

These placeholders are provided by EcoEnchants and can be used in the description, `placeholder`/`placeholders`, effect args, and conditions:

| Placeholder | Value |
| --- | --- |
| `%level%` | The current level of the enchantment. Use it to make enchantments scale as the level increases. |

### `/enchantinfo` placeholders

These are separate from the placeholders above - they're only available in `enchantinfo.item.lore` in `config.yml`, not in an enchant's own `description`:

| Placeholder | Value |
| --- | --- |
| `%tradeable%` | Whether the enchantment is obtainable from villagers |
| `%discoverable%` | Whether the enchantment is obtainable through discovery (any method) |
| `%discoverable_chests%` | Whether the enchantment can be found in loot chests |
| `%discoverable_fishing%` | Whether the enchantment can be found through fishing |
| `%discoverable_mob_drops%` | Whether the enchantment can be found from mob drops |
| `%discoverable_raids%` | Whether the enchantment can be found from raids |
| `%enchantable%` | Whether the enchantment is obtainable from the enchanting table |
| `%drag_and_drop%` | Whether the enchantment can be applied by dragging an enchanted book onto an item |

:::tip Troubleshooting
- **Enchantment doesn't appear after reload?** It's new, so you must re-log, not just reload.
- **`%placeholder%` shows literally instead of a number?** Make sure the `placeholder` field is defined and the name matches exactly.
- **Can't apply it to your item?** Check `targets`, as the item must belong to a listed target group.
- **Effect does nothing?** Verify the effect `id` and `trigger` names against the [effects documentation](https://hub.auxilor.io/wiki/libreforge/configuring-an-effect).
:::

<hr/>

## Where to go next

- **Default configs:** study the [built-in enchantments](https://github.com/Auxilor/EcoEnchants/tree/master/eco-core/core-plugin/src/main/resources/enchants) for real, working examples.
- **Community configs:** browse and import user-created enchantments from [lrcdb](https://lrcdb.auxilor.io/) (`/ecoenchants import <id>`).
- **Effects reference:** the [Configuring an Effect](https://hub.auxilor.io/wiki/libreforge/configuring-an-effect) guide for everything the effects section can do.