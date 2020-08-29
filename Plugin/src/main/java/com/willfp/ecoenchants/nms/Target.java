package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.API.TargetWrapper;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Set;

public class Target {
    private static final EcoEnchantsPlugin PLUGIN = EcoEnchantsPlugin.getInstance();
    private static TargetWrapper target;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + version + ".Target");
            if(TargetWrapper.class.isAssignableFrom(class2)) {
                target = (TargetWrapper) class2.getConstructor().newInstance();
            }
        } catch(Exception e) {
            PLUGIN.getLogger().info(e.getCause() + "");
            e.printStackTrace();
            target = null;
        }
    }

    public enum Applicable {
        TOOL(target.TOOL),
        ARMOR(target.ARMOR),
        ALL(target.ALL),
        AXE(target.AXE),
        BOOK(target.BOOK),
        PICKAXE(target.PICKAXE),
        HOE(target.HOE),
        SHOVEL(target.SHOVEL),
        SWORD(target.SWORD),
        HELMET(target.HELMET),
        CHESTPLATE(target.CHESTPLATE),
        LEGGINGS(target.LEGGINGS),
        BOOTS(target.BOOTS),
        ELYTRA(target.ELYTRA),
        BOW(target.BOW),
        CROSSBOW(target.CROSSBOW),
        SHEARS(target.SHEARS),
        TRIDENT(target.TRIDENT),
        SHIELD(target.SHIELD),
        ROD(target.ROD);

        private final Set<Material> materials;

        Applicable(Set<Material> materials) {
            this.materials = materials;
        }

        public Set<Material> getMaterials() {
            return this.materials;
        }
    }
}
