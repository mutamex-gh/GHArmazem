package me.gharmazem.listener;

import lombok.val;
import me.gharmazem.inventories.ArmazemInventory;
import me.gharmazem.manager.LimitManager;
import me.gharmazem.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TierUpgradeListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        val player = (Player) event.getWhoClicked();
        LimitManager limitManager = new LimitManager();

        if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;

        if(event.getInventory().equals(ArmazemInventory.getInventory())
        && event.getCurrentItem().getItemMeta().getDisplayName().equals(ColorUtil.colored("&aGerenciador de Limites"))) {

            limitManager.updateTier(player);
        }
    }
}
