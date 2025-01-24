package me.gharmazem.events;

import me.gharmazem.inventories.ArmazemInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BackMenu implements Listener {

    @EventHandler
    public void arrowBackEvent(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();

        if(event.getCurrentItem().getType() == Material.ARROW) {
            player.openInventory(ArmazemInventory.getInventory());
        }
    }
}
