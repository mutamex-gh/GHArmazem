package me.gharmazem.listener;

import me.gharmazem.Main;
import me.gharmazem.economy.EconomyHook;
import me.gharmazem.inventories.ArmazemSection;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.UtilClass;
import me.gharmazem.utils.ColorUtil;
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

            if (item == null) item = event.getCursor();
            if (item == null || item.getType() == Material.AIR) return false;

            String nbtTag = ItemBuilder.getNBTTag(item, "itemsnbt");
            if (ArmazemSection.inventoryButtons.get(nbtTag) == null) return false;
            InventoryButton button = ArmazemSection.inventoryButtons.get(nbtTag);

            ItemStack itemStack = button.getItemStack();
            Material material = itemStack.getType();
            int quantity = itemStack.getAmount();

            ItemStack itemFinal = new ItemStack(material, quantity);

            int quantia = BaseManager.getStored(player, button.getItemStack());
            double rendimento = button.getPrice() * quantia;

            // vender/recolher por inventario :(
            if (event.getClick().isLeftClick()) {
                if (quantia > 0) {
                    EconomyHook.getEconomy().depositPlayer(player, rendimento);
                    BaseManager.sell(player, itemFinal);

                    player.closeInventory();
                    UtilClass.sendSound(player, Sound.LEVEL_UP);
                    player.sendMessage(ColorUtil.colored(sellitens)
                            .replace("{rendimento}", UtilClass.formatNumber(rendimento))
                            .replace("{itens}", UtilClass.formatNumber(quantia)));
                    return true;
                } else {
                    player.sendMessage(ColorUtil.colored(noitenstosell));
                }
            } else if (event.getClick().isRightClick() && quantia > 0) {

                int freeSlots = 0;
                for (ItemStack drop : player.getInventory().getContents()) {
                    if (drop == null || drop.getType() == Material.AIR) {
                        freeSlots++;
                    }
                }
                int itemsPerSlot = itemFinal.getMaxStackSize();
                int maxItems = freeSlots * itemsPerSlot;
                int itemsToCollect = Math.min(quantia, maxItems);
                if (maxItems > 0) {
                    BaseManager.getSpecificItem(player, itemFinal, itemsToCollect);
                    player.closeInventory();

                    UtilClass.sendSound(player, Sound.CLICK);
                    player.sendMessage(ColorUtil.colored(itensrecuperados)
                            .replace("{itens}", String.valueOf(itemsToCollect)));
                } else {
                    player.sendMessage(ColorUtil.colored(nospaceinventory));
                }
            } else {
                player.sendMessage(ColorUtil.colored(noitenstorecover));
            }

        }
        return false;
    }
}
