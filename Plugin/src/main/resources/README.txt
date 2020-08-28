Thanks for purchasing EcoEnchants!

Some of the config can get quite confusing, so here's a little guide.

Enchantment-Specific config files are in their respective folders related to their type.
Or, more simply: curses are stored in /curse, artifacts are stored in /artifact

If you want to disable an enchantment, set everything in obtaining to false, like this:
obtaining:
  table: true
  villager: true
  loot: true
  rarity: rare

You can set the rarity to any valid rarity, but you must still specify one.

---------------- TARGETS ----------------

Some enchantments support modifying/refining their targets.
Targets are the items that the enchantment can be applied to.

You can specify a target like this:

general-config:
  target:
    - material
    - material2

A list of available materials can be found here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html

Most users won't need to modify targets, however it is recommended that the modified target is a subset of the original target.
For example, a helmet enchantment can be refined to only gold helmets, a bow enchantment can be specified to work on a crossbow, and a sword enchantment can be specified to work on axes.

** IMPORTANT **
Piece-specific armor enchants will only work on their default piece.
An example of this is sating, a helmet enchantment.
Specifying sating to work on boots will allow boots to be enchanted, however the sating effect WILL NOT WORK.
The only exception to this rule is magnetic, which can be specified to work on any piece of armor.

Putting this into context:
Dexterous can, by default, be applied to all swords and axes.
However, maybe you want it to only be applied to diamond and netherite swords.
So, add this to general-config:

target:
  - diamond_sword
  - netherite_sword

Another example:
Hook, by default, can only be applied to bows.
If you want it to be applicable to crossbows as well, simply set the target to this:

target:
  - bow
  - crossbow

It is worth mentioning that bow/crossbow enchantments will not work on tridents.``