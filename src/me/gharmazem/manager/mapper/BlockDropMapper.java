package me.gharmazem.manager.mapper;

import lombok.Getter;
import org.bukkit.Material;

/**
 * @author Gustavo Henrique
 * @github mutamex-gh
 *
 * @use Converter as plantações quebradas
 *      no mesmo itens usado em inventario
 */

public enum BlockDropMapper {

          //IN-GAME       //IN-CONFIG (OG)
    WHEAT(Material.CROPS, Material.WHEAT),
    CROPS(Material.WHEAT, Material.CROPS),
    NETHER_WARTS(Material.NETHER_WARTS, Material.NETHER_STALK),
    SUGAR_CANE(Material.SUGAR_CANE_BLOCK, Material.SUGAR_CANE),
    CARROT(Material.CARROT, Material.CARROT_ITEM),
    POTATO(Material.POTATO, Material.POTATO_ITEM),
    //CACTUS(Material.CACTUS, Material.CACTUS),
    // COCOA(Material.COCOA, Material.COCOA),
    MELON(Material.MELON_BLOCK, Material.MELON); // retorna melancia

    @Getter private final Material blockType;
    @Getter private final Material dropType;

    BlockDropMapper(Material blockType, Material dropType) {
        this.blockType = blockType;
        this.dropType = dropType;
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
