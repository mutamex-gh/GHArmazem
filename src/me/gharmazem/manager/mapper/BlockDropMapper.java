package me.gharmazem.manager.mapper;

import org.bukkit.Material;

/**
 * @author Gustavo Henrique
 * @github mutamex-gh
 */

public enum BlockDropMapper {
         //QUEBRAVEL IG       //PARA OG
    WHEAT(Material.CROPS, Material.WHEAT),
    CROPS(Material.WHEAT, Material.CROPS),
    NETHER_WARTS(Material.NETHER_WARTS, Material.NETHER_STALK),
    SUGAR_CANE(Material.SUGAR_CANE_BLOCK, Material.SUGAR_CANE),
    CARROT(Material.CARROT, Material.CARROT_ITEM),
    POTATO(Material.POTATO, Material.POTATO_ITEM),
    MELON(Material.MELON_BLOCK, Material.MELON); // MELON_BLOCK quebrado = Melancia individual (sem ser bloco)

    private final Material blockType;
    private final Material dropType;

    BlockDropMapper(Material blockType, Material dropType) {
        this.blockType = blockType;
        this.dropType = dropType;
    }

    public Material getBlockType() {
        return blockType;
    }

    public Material getDropType() {
        return dropType;
    }

    public static Material getDrop(Material blockType) {
        for (BlockDropMapper mapper : values()) {
            if (mapper.getBlockType() == blockType) {
                return mapper.getDropType();
            }
        }
        return null;
    }
}
