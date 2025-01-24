package me.gharmazem.events;

import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemInventory;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtils;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class SellAllEvent implements Listener {

    @EventHandler
    public boolean sellAllItems(InventoryClickEvent event) {
        if (event.getInventory().equals(ArmazemInventory.getInventory())) {
            FileConfiguration config = Main.getInstance().getConfig();

            String materialName = Main.getInstance().getConfig().getString("SellAllItem.material");
            String sellItens = config.getString("Messages.sell-itens");
            String noItensToSell = config.getString("Messages.no-itens-to-sell");

            Material sellAllMaterial = Material.getMaterial(materialName);

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() != sellAllMaterial) return false;

            DecimalFormat df = new DecimalFormat("#,###,###,##0.##");
            int totalQuantia = BaseManager.getTotalQuantity(player);
            if (totalQuantia > 0) {
                double totalRendimento = BaseManager.getTotalValue(player);
                Main.getEconomy().depositPlayer(player, totalRendimento);

                BaseManager.sellAllItems(player);

                player.sendMessage(ColorUtils.colored(sellItens)
                        .replace("{rendimento}", df.format(totalRendimento))
                        .replace("{itens}", String.valueOf(totalQuantia)));

                UtilClass.sendSound(player, Sound.LEVEL_UP);
            }else {
                player.sendMessage(ColorUtils.colored(noItensToSell));
            }
            player.closeInventory();
            return true;
        }
        return false;
    }
}
