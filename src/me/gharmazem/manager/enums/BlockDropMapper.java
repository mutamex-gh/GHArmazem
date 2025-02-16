package me.gharmazem.manager.enums;

import lombok.Getter;
import org.bukkit.Material;

/**
 * @author Gustavo Henrique
 * @github mutamex-gh
 *
 * @use Converter as plantações quebradas
 *      no mesmo itens usado em inventario
 */

@Getter
public enum BlockDropMapper {

          //IN-GAME       //IN-CONFIG
    WHEAT(Material.CROPS, Material.WHEAT),
    CROPS(Material.WHEAT, Material.CROPS),
    NETHER_WARTS(Material.NETHER_WARTS, Material.NETHER_STALK),
    SUGAR_CANE(Material.SUGAR_CANE_BLOCK, Material.SUGAR_CANE),
    CARROT(Material.CARROT, Material.CARROT_ITEM),
    POTATO(Material.POTATO, Material.POTATO_ITEM),
    MELON(Material.MELON_BLOCK, Material.MELON); // retorna melancia

    private final Material blockType;
    private final Material dropType;

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
