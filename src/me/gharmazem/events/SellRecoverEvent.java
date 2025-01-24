package me.gharmazem.events;

import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemSection;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.UtilClass;
import me.gharmazem.utils.ColorUtils;
import me.gharmazem.manager.InventoryButton;
import me.gharmazem.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class SellRecoverEvent implements Listener {

    @EventHandler
    public boolean sellAndRecover(InventoryClickEvent event) {
        if (event.getInventory().equals(ArmazemSection.getArmazemInventory())) {

            FileConfiguration config = Main.getInstance().getConfig();
            String itensrecuperados = config.getString("Messages.recovery-itens");
            String sellitens = config.getString("Messages.sell-itens");
            String noitenstosell = config.getString("Messages.no-itens-to-sell");
            String noitenstorecover = config.getString("Messages.no-itens-to-recover");
            String nospaceinventory = config.getString("Messages.no-space-inventory");

            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            DecimalFormat df = new DecimalFormat("#,###,###,##0.##");

            if (item == null) item = event.getCursor();
            if (item == null || item.getType() == Material.AIR) return false;

            String nbtTag = ItemBuilder.getNBTTag(item, "itemsnbt");
            if (ArmazemSection.inventoryButtons.get(nbtTag) == null) return false;
            InventoryButton button = ArmazemSection.inventoryButtons.get(nbtTag);

            ItemStack itemStack = button.getItemStack();
            Material material = itemStack.getType();
            int quantity = itemStack.getAmount();

            ItemStack itemFinal = new ItemStack(material, quantity);

            int quantia = BaseManager.getQuantity(player, button.getItemStack());
            double rendimento = button.getPrice() * quantia;

            if (event.getClick().isLeftClick()) {
                if(quantia > 0) {
                    UtilClass.sendSound(player, Sound.LEVEL_UP);
                    player.sendMessage(ColorUtils.colored(sellitens)
                            .replace("{rendimento}", df.format(rendimento))
                            .replace("{itens}", String.valueOf(quantia)));

                    player.closeInventory();

                    Main.getEconomy().depositPlayer(player, rendimento);
                    BaseManager.sellItem(player, itemFinal);
                    return true;
                }else {
                    player.sendMessage(ColorUtils.colored(noitenstosell));
                }
            }else if (event.getClick().isRightClick() && quantia > 0) {
                UtilClass.sendSound(player, Sound.CLICK);
                int freeSlots = 0;

                for (ItemStack drop : player.getInventory().getContents()) {
                    if (drop == null || drop.getType() == Material.AIR) {
                        freeSlots++;
                    }
                }
                int itemsPerSlot = itemFinal.getMaxStackSize();
                int maxItems = freeSlots * itemsPerSlot;

                if (maxItems > 0) {
                    int itemsToCollect = Math.min(quantia, maxItems);

                    player.sendMessage(ColorUtils.colored(itensrecuperados)
                            .replace("{itens}", String.valueOf(itemsToCollect)));

                    BaseManager.recuperarItemEspecificoDoArmazem(player, itemFinal, itemsToCollect);
                    player.closeInventory();
                } else {
                    player.sendMessage(ColorUtils.colored(nospaceinventory));
                }
            } else {
                player.sendMessage(ColorUtils.colored(noitenstorecover));
            }

        }
        return false;
    }
}
