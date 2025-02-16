package me.gharmazem.manager;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;

public class DropsNameManager {

    private static final Map<Material, String> blockNames = new HashMap<>();

    static {
        blockNames.put(Material.WHEAT, "Trigo(s)");
        blockNames.put(Material.NETHER_STALK, "Fungo(s) do nether");
        blockNames.put(Material.SUGAR_CANE_BLOCK, "Cana(s) de açúcar");
        blockNames.put(Material.CARROT_ITEM, "Cenoura(s)");
        blockNames.put(Material.POTATO_ITEM, "Batata(s)");
        blockNames.put(Material.MELON_BLOCK, "Melancia(s)");
    }

    public static String getName(Material material) {
        return blockNames.getOrDefault(material, null);
    }
}