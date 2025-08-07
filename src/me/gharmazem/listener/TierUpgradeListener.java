package me.gharmazem.listener;

import lombok.val;
import me.gharmazem.inventories.ArmazemInventory;
import me.gharmazem.manager.LimitManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TierUpgradeListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        val player = (Player) event.getWhoClicked();
        LimitManager limitManager = new LimitManager();

        if(event.getInventory().equals(ArmazemInventory.getInventory())
        && event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_KNOCKBACK)) {

            limitManager.updateTier(player);
        }
    }
}
