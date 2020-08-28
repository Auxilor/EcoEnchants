package com.willfp.ecoenchants.v1_16_R2;

import com.willfp.ecoenchants.API.TargetWrapper;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class Target implements TargetWrapper {

    public final Set<Material> AXE = new HashSet<Material>() {{
        TargetWrapper.AXE.add(Material.WOODEN_AXE);
        TargetWrapper.AXE.add(Material.STONE_AXE);
        TargetWrapper.AXE.add(Material.IRON_AXE);
        TargetWrapper.AXE.add(Material.GOLDEN_AXE);
        TargetWrapper.AXE.add(Material.DIAMOND_AXE);
        TargetWrapper.AXE.add(Material.NETHERITE_AXE);
    }};

    public final Set<Material> BOOK = new HashSet<Material>() {{
        TargetWrapper.BOOK.add(Material.BOOK);
        TargetWrapper.BOOK.add(Material.ENCHANTED_BOOK);
    }};

    public final Set<Material> PICKAXE = new HashSet<Material>() {{
        TargetWrapper.PICKAXE.add(Material.WOODEN_PICKAXE);
        TargetWrapper.PICKAXE.add(Material.STONE_PICKAXE);
        TargetWrapper.PICKAXE.add(Material.IRON_PICKAXE);
        TargetWrapper.PICKAXE.add(Material.GOLDEN_PICKAXE);
        TargetWrapper.PICKAXE.add(Material.DIAMOND_PICKAXE);
        TargetWrapper.PICKAXE.add(Material.NETHERITE_PICKAXE);
    }};

    public final Set<Material> HOE = new HashSet<Material>() {{
        TargetWrapper.HOE.add(Material.WOODEN_HOE);
        TargetWrapper.HOE.add(Material.STONE_HOE);
        TargetWrapper.HOE.add(Material.IRON_HOE);
        TargetWrapper.HOE.add(Material.GOLDEN_HOE);
        TargetWrapper.HOE.add(Material.DIAMOND_HOE);
        TargetWrapper.HOE.add(Material.NETHERITE_HOE);
    }};

    public final Set<Material> SHOVEL = new HashSet<Material>() {{
        TargetWrapper.SHOVEL.add(Material.WOODEN_SHOVEL);
        TargetWrapper.SHOVEL.add(Material.STONE_SHOVEL);
        TargetWrapper.SHOVEL.add(Material.IRON_SHOVEL);
        TargetWrapper.SHOVEL.add(Material.GOLDEN_SHOVEL);
        TargetWrapper.SHOVEL.add(Material.DIAMOND_SHOVEL);
        TargetWrapper.SHOVEL.add(Material.NETHERITE_SHOVEL);
    }};

    public final Set<Material> SWORD = new HashSet<Material>() {{
        TargetWrapper.SWORD.add(Material.WOODEN_SWORD);
        TargetWrapper.SWORD.add(Material.STONE_SWORD);
        TargetWrapper.SWORD.add(Material.IRON_SWORD);
        TargetWrapper.SWORD.add(Material.GOLDEN_SWORD);
        TargetWrapper.SWORD.add(Material.DIAMOND_SWORD);
        TargetWrapper.SWORD.add(Material.NETHERITE_SWORD);
    }};

    public final Set<Material> HELMET = new HashSet<Material>() {{
        TargetWrapper.HELMET.add(Material.TURTLE_HELMET);
        TargetWrapper.HELMET.add(Material.LEATHER_HELMET);
        TargetWrapper.HELMET.add(Material.CHAINMAIL_HELMET);
        TargetWrapper.HELMET.add(Material.IRON_HELMET);
        TargetWrapper.HELMET.add(Material.GOLDEN_HELMET);
        TargetWrapper.HELMET.add(Material.DIAMOND_HELMET);
        TargetWrapper.HELMET.add(Material.NETHERITE_HELMET);
    }};

    public final Set<Material> CHESTPLATE = new HashSet<Material>() {{
        TargetWrapper.CHESTPLATE.add(Material.LEATHER_CHESTPLATE);
        TargetWrapper.CHESTPLATE.add(Material.CHAINMAIL_CHESTPLATE);
        TargetWrapper.CHESTPLATE.add(Material.IRON_CHESTPLATE);
        TargetWrapper.CHESTPLATE.add(Material.GOLDEN_CHESTPLATE);
        TargetWrapper.CHESTPLATE.add(Material.DIAMOND_CHESTPLATE);
        TargetWrapper.CHESTPLATE.add(Material.NETHERITE_CHESTPLATE);
    }};

    public final Set<Material> LEGGINGS = new HashSet<Material>() {{
        TargetWrapper.LEGGINGS.add(Material.LEATHER_LEGGINGS);
        TargetWrapper.LEGGINGS.add(Material.CHAINMAIL_LEGGINGS);
        TargetWrapper.LEGGINGS.add(Material.IRON_LEGGINGS);
        TargetWrapper.LEGGINGS.add(Material.GOLDEN_LEGGINGS);
        TargetWrapper.LEGGINGS.add(Material.DIAMOND_LEGGINGS);
        TargetWrapper.LEGGINGS.add(Material.NETHERITE_LEGGINGS);
    }};

    public final Set<Material> BOOTS = new HashSet<Material>() {{
        TargetWrapper.BOOTS.add(Material.LEATHER_BOOTS);
        TargetWrapper.BOOTS.add(Material.CHAINMAIL_BOOTS);
        TargetWrapper.BOOTS.add(Material.IRON_BOOTS);
        TargetWrapper.BOOTS.add(Material.GOLDEN_BOOTS);
        TargetWrapper.BOOTS.add(Material.DIAMOND_BOOTS);
        TargetWrapper.BOOTS.add(Material.NETHERITE_BOOTS);
    }};

    public final Set<Material> ELYTRA = new HashSet<Material>() {{
        TargetWrapper.ELYTRA.add(Material.ELYTRA);
    }};

    public final Set<Material> BOW = new HashSet<Material>() {{
        TargetWrapper.BOW.add(Material.BOW);
    }};

    public final Set<Material> CROSSBOW = new HashSet<Material>() {{
        TargetWrapper.CROSSBOW.add(Material.CROSSBOW);
    }};

    public final Set<Material> SHEARS = new HashSet<Material>() {{
        TargetWrapper.SHEARS.add(Material.SHEARS);
    }};

    public final Set<Material> TRIDENT = new HashSet<Material>() {{
        TargetWrapper.TRIDENT.add(Material.TRIDENT);
    }};

    public final Set<Material> SHIELD = new HashSet<Material>() {{
        TargetWrapper.SHIELD.add(Material.SHIELD);
    }};

    public final Set<Material> ROD = new HashSet<Material>() {{
        TargetWrapper.ROD.add(Material.FISHING_ROD);
    }};


    public final Set<Material> TOOL = new HashSet<Material>() {{
        TargetWrapper.TOOL.addAll(TargetWrapper.AXE);
        TargetWrapper.TOOL.addAll(TargetWrapper.PICKAXE);
        TargetWrapper.TOOL.addAll(TargetWrapper.SHOVEL);
        TargetWrapper.TOOL.addAll(TargetWrapper.HOE);
        TargetWrapper.TOOL.addAll(TargetWrapper.SHEARS);
    }};

    public final Set<Material> ARMOR = new HashSet<Material>() {{
        TargetWrapper.ARMOR.addAll(TargetWrapper.HELMET);
        TargetWrapper.ARMOR.addAll(TargetWrapper.CHESTPLATE);
        TargetWrapper.ARMOR.addAll(TargetWrapper.LEGGINGS);
        TargetWrapper.ARMOR.addAll(TargetWrapper.BOOTS);
    }};

    public final Set<Material> ALL = new HashSet<Material>() {{
        TargetWrapper.ALL.addAll(TargetWrapper.TOOL);
        TargetWrapper.ALL.addAll(TargetWrapper.ARMOR);
        TargetWrapper.ALL.addAll(TargetWrapper.TRIDENT);
        TargetWrapper.ALL.addAll(TargetWrapper.SHIELD);
        TargetWrapper.ALL.addAll(TargetWrapper.BOW);
        TargetWrapper.ALL.addAll(TargetWrapper.CROSSBOW);
        TargetWrapper.ALL.addAll(TargetWrapper.ROD);
        TargetWrapper.ALL.addAll(TargetWrapper.BOOK);
        TargetWrapper.ALL.addAll(TargetWrapper.SWORD);
        TargetWrapper.ALL.addAll(TargetWrapper.ELYTRA);
    }};

    public Target() {

    }
}
