package me.gharmazem.listener;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.configuration.ConfigValues;
import me.gharmazem.inventories.ArmazemItens;
import me.gharmazem.manager.DropsNameManager;
import me.gharmazem.manager.LimitManager;
import me.gharmazem.parser.ArmazemSection;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ActionBarUtils;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoriesListener implements Listener {

    @EventHandler
    public boolean onClick(InventoryClickEvent event) {

        FileConfiguration config = Main.getInstance().getConfig();
        String menuinvname = config.getString("Inventory.inventory-name");
        String armazeminvname = config.getString("StorageInventory.inventory-name");
        String title = config.getString("StorageItem.title");

        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getTitle().equals(menuinvname)) {
            event.setCancelled(true);

            if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return true;

            if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ColorUtil.colored(title))) {
                player.openInventory(ArmazemSection.getArmazemInventory());

                ArmazemSection.inventory.setItem(11, ArmazemItens.savedItens(player));
                return false;
            }
        }

        if(event.getInventory().getTitle().equals(armazeminvname)) {
            event.setCancelled(true);

            ItemStack currentItem = event.getCurrentItem();
            LimitManager limitManager = new LimitManager();
            BaseManager baseManager = new BaseManager();

            if (currentItem == null || currentItem.getType() == Material.AIR || event.getClickedInventory() == null) return true;

            val limitExceeded = config.getString("Messages.limit-exceeded");

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ColorUtil.colored("&aGuardar Drops"))) {
                if (BaseManager.getAllStored(player) >= limitManager.getLimit(player)) {
                    ActionBarUtils.sendActionBar(
                            player,
                            ColorUtil.colored(limitExceeded)
                                    .replace("{amount}", UtilClass.formatNumber(BaseManager.getAllStored(player)))
                                    .replace("{limit}", UtilClass.formatNumber(limitManager.getLimit(player)))
                    );
                    UtilClass.sendSound(player, Sound.VILLAGER_NO);
                    return false;
                }
                baseManager.store(player);
            }
        }
        return false;
    }
}
