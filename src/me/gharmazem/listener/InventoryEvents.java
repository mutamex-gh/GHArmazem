package me.gharmazem.listener;

import me.gharmazem.Main;
import me.gharmazem.configuration.ConfigValues;
import me.gharmazem.inventories.ArmazemItens;
import me.gharmazem.inventories.ArmazemSection;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryEvents implements Listener {

    @EventHandler
    public boolean onClick(InventoryClickEvent event) {

        FileConfiguration config = Main.getInstance().getConfig();
        String materialmenu = config.getString("StorageItem.material");
        String menuinvname = config.getString("Inventory.inventory-name");
        String armazeminvname = config.getString("StorageInventory.inventory-name");
        String noitenstostore = config.getString("Messages.no-itens-to-store");
        String storeitens = config.getString("Messages.store-itens");

        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getTitle().equals(menuinvname)) {
            event.setCancelled(true);

            if(event.getCurrentItem().getType().equals(Material.getMaterial(materialmenu))) {
                player.openInventory(ArmazemSection.getArmazemInventory());

                ArmazemSection.inventory.setItem(11, ArmazemItens.savedItens(player));
                return false;
            }
        }

        if(event.getInventory().getTitle().equals(armazeminvname)) {
            event.setCancelled(true);

            ItemStack currentItem = event.getCurrentItem();

            if (currentItem == null || currentItem.getType() == Material.AIR) return true;
            if (event.getClickedInventory() == null) return true;

            List<Material> allowed = ConfigValues.getAllowedItems();
            if (event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)) {

                boolean hasStoredItems = false;
                for (Material material : allowed) {
                    int totalAmount = 0;

                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item != null && item.getType() == material) {
                            totalAmount += item.getAmount();
                            player.getInventory().remove(item);
                        }
                    }

                    if (totalAmount > 0) {
                        hasStoredItems = true;
                        ItemStack storedItem = new ItemStack(material, totalAmount);
                        BaseManager.save(player, storedItem);

                        player.sendMessage(ColorUtil.colored(storeitens).replace("{itens}", totalAmount + " " + material.name()));
                        UtilClass.sendSound(player, Sound.LEVEL_UP);
                    }
                }
                if (!hasStoredItems) {
                    player.sendMessage(ColorUtil.colored(noitenstostore));
                }

                player.closeInventory();
            }else if(event.getCurrentItem().getItemMeta() == null) return false;
        }
        return false;
    }
}
