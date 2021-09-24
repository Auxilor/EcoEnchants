package io.papermc.paper.enchantments;

/**
 * This right here is the worst workaround I have ever seen in my entire life.
 * <p>
 * I hate the fact that I've done it, I hate everything that made me have to
 * do this, and I hate the fact that there's an issue here in the first place.
 * <p>
 * Spigot rules prevent you from having a hard dependency on papermc. Already,
 * that's extremely infuriating, but it is what it is, I guess.
 * <p>
 * However, I want to use the paper API if paper is installed, and also provide
 * paper API support for anyone who uses it.
 * <p>
 * Unfortunately, because EcoEnchant <b>is</b> both an Enchantment and a listener,
 * spigot can't register the listener because it throws a ClassDefNotFound error
 * when getting the methods - the solution to this for me is to load a stub class
 * that exists, the actual method signature shouldn't be any different and I don't
 * depend on this module in core-plugin, so I won't have any bugs on my end.
 * <p>
 * I really hope this solution works because if it doesn't then I have a lot of
 * work to do. A potential (and janky) workaround would be to load asm and then
 * remove this class from existence if the server is running paper.
 * <p>
 * This class here will be loaded <b>after</b> paper so it shouldn't cause any
 * issues there, and the class will always then be present on the classpath.
 * <p>
 * I will write this once and then never look at it again.
 */
public enum EnchantmentRarity {

}
