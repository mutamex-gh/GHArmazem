package me.gharmazem.configuration;

import lombok.Getter;
import me.gharmazem.Main;
import me.gharmazem.manager.enums.BlockDropMapper;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigValues {

    @Getter private static List<Material> allowedItems;

    public static void loadAllowedItems() {
        List<String> itemNames = Main.getInstance().getConfig().getStringList("Materiais");
        allowedItems = new ArrayList<>();

        for (String itemName : itemNames) {
            try {
                Material material = Material.valueOf(itemName.toUpperCase());
                allowedItems.add(material);

                Material blockType = BlockDropMapper.getDrop(material);
                if (blockType != null) {
                    allowedItems.add(blockType);
                }
            } catch (Exception ignore) {}
        }
    }

    public static Map<Material, Double> loadItemPrices() {
        Map<Material, Double> itemPrices = new HashMap<>();
        FileConfiguration config = Main.getInstance().getConfig();

        if (config.contains("Prices")) {
            for (String materialName : config.getConfigurationSection("Prices").getKeys(false)) {
                Material material = Material.getMaterial(materialName);
                if (material != null) {
                    double price = config.getDouble("Prices." + materialName);
                    itemPrices.put(material, price);

                    Material blockType = BlockDropMapper.getDrop(material);
                    if (blockType != null) {
                        itemPrices.put(blockType, price);
                    }
                }
            }
        }
        return itemPrices;
    }

}
