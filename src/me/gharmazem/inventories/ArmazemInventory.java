package me.gharmazem.inventories;

import lombok.Getter;
import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ArmazemInventory {

    @Getter public static final Inventory inventory;

    static {
        val inventoryName = Main.getInstance().getConfig().getString("Inventory.inventory-name");
        val inventorySize = Main.getInstance().getConfig().getInt("Inventory.inventory-size");

        inventory = Bukkit.createInventory(
                null,
                inventorySize,
                ColorUtil.colored(inventoryName)
        );
    }

}
