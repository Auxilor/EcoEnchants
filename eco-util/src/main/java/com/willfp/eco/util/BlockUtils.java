package com.willfp.eco.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class BlockUtils {
    private Set<Block> getNearbyBlocks(@NotNull final Block start,
                                       @NotNull final List<Material> allowedMaterials,
                                       @NotNull final HashSet<Block> blocks,
                                       final int limit) {
        for (BlockFace face : BlockFace.values()) {
            Block block = start.getRelative(face);
            if (!blocks.contains(block) && allowedMaterials.contains(block.getType())) {
                blocks.add(block);
                if (blocks.size() > limit || blocks.size() > 2500) {
                    return blocks;
                }
                blocks.addAll(getNearbyBlocks(block, allowedMaterials, blocks, limit));
            }
        }
        return blocks;
    }


    /**
     * Get a set of all blocks in contact with each other of a specific type.
     *
     * @param start            The initial block.
     * @param allowedMaterials A list of all valid {@link Material}s.
     * @param limit            The maximum size of vein to return.
     *
     * @return A set of all {@link Block}s.
     */
    public Set<Block> getVein(@NotNull final Block start,
                              @NotNull final List<Material> allowedMaterials,
                              final int limit) {
        return getNearbyBlocks(start, allowedMaterials, new HashSet<>(), limit);
    }
}
