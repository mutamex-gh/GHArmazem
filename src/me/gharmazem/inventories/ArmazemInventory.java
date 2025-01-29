package me.gharmazem.inventories;

import me.gharmazem.Main;
import me.gharmazem.utils.some.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ArmazemInventory {

    public static final Inventory inventory;

    static {
        String invname = Main.getInstance().getConfig().getString("Inventory.inventory-name");
        int invsize = Main.getInstance().getConfig().getInt("Inventory.inventory-size");

        inventory = Bukkit.createInventory(null, invsize, ColorUtil.colored(invname));
    }

    public static Inventory getInventory() {
        return inventory;
    }
}
