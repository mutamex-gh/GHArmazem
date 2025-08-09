package me.gharmazem.listener;

import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemInventory;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SellAllListener implements Listener {

    @EventHandler
    public void sellAllItems(InventoryClickEvent event) {
        if (event.getInventory().equals(ArmazemInventory.getInventory())) {
            Player player = (Player) event.getWhoClicked();
            String title = Main.getInstance().getConfig().getString("SellAllItem.title");

            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || !event.getCurrentItem().getItemMeta().getDisplayName().equals(ColorUtil.colored(title))) return;

            BaseManager.sellAll(player);
            player.closeInventory();
        }
    }
}
