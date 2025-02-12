package me.gharmazem.listener;

import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemInventory;
import me.gharmazem.manager.BaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SellAllListener implements Listener {

    @EventHandler
    public boolean sellAllItems(InventoryClickEvent event) {
        if (event.getInventory().equals(ArmazemInventory.getInventory())) {
            Player player = (Player) event.getWhoClicked();

            String materialName = Main.getInstance().getConfig().getString("SellAllItem.material");
            Material sellAllMaterial = Material.getMaterial(materialName);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() != sellAllMaterial) return false;

            BaseManager.sellAll(player);
            player.closeInventory();
            return true;
        }
        return false;
    }
}
