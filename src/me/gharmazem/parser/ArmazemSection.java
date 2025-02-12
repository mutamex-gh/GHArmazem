package me.gharmazem.parser;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemInventory;
import me.gharmazem.inventories.ArmazemItens;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.api.InventoryButton;
import me.gharmazem.utils.ItemBuilder;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArmazemSection {

    public static final Map<String, InventoryButton> inventoryButtons = new LinkedHashMap<>();
    public static final Inventory inventory;

    static {
        FileConfiguration config = Main.getInstance().getConfig();
        inventory = Bukkit.createInventory(
                null,
                config.getInt("StorageInventory.inventory-size"),
                ColorUtil.colored(config.getString("StorageInventory.inventory-name")));

        ConfigurationSection itemSection = config.getConfigurationSection("items");
        if (itemSection != null) {
            for (String key : itemSection.getKeys(false)) {
                Material material = Material.valueOf(itemSection.getString(key + ".material"));
                int slot = itemSection.getInt(key + ".slot");
                int price = itemSection.getInt(key + ".price");

                ItemStack itemStack = new ItemBuilder(material)
                        .changeItemMeta(itemMeta -> {
                            itemMeta.setDisplayName(ColorUtil.colored(itemSection.getString(key + ".name")));
                            itemMeta.setLore(ColorUtil.colored(itemSection.getStringList(key + ".lore")));
                        })
                        .setNBTTag("itemsnbt", key)
                        .wrap();

                inventoryButtons.put(key, new InventoryButton(key, itemStack, itemSection.getStringList(key + ".lore"), slot, price));
                inventory.setItem(slot, itemStack);
            }
        }

        inventory.setItem(10, ArmazemItens.storageDropsItem());
    }

    public static void updateLore(Player player) {
        for (String key : inventoryButtons.keySet()) {
            InventoryButton button = inventoryButtons.get(key);
            List<String> updatedLore = new ArrayList<>();

            val stored = BaseManager.getSpecificStored(player, button.getItemStack());
            val value = stored * button.getPrice();

            for (String line : button.getLore()) {
                String newLore = line
                        .replace("{allvalue}", UtilClass.formatNumber(value))
                        .replace("{allstored}", UtilClass.formatNumber(stored))
                        .replace("{player}", player.getName());

                updatedLore.add(newLore);
            }

            ItemStack item = button.getItemStack();
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setLore(ColorUtil.colored(updatedLore));
                item.setItemMeta(meta);
            }
            inventory.setItem(button.getSlot(), item);
        }
    }

    public static Inventory getArmazemInventory() {
        return inventory;
    }
}