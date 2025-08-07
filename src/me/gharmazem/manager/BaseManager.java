package me.gharmazem.manager;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.configuration.ConfigDBase;
import me.gharmazem.configuration.ConfigValues;
import me.gharmazem.hook.EconomyHook;
import me.gharmazem.inventories.ArmazemInventory;
import me.gharmazem.inventories.ArmazemItens;
import me.gharmazem.parser.ArmazemSection;
import me.gharmazem.utils.ActionBarUtils;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseManager {

    static FileConfiguration config = Main.getInstance().getConfig();

    public static void save(Player player, ItemStack item) {
        String playerUUID = player.getUniqueId().toString();
        String itemType = item.getType().toString();
        int itemAmount = item.getAmount();

        FileConfiguration db = ConfigDBase.getDatabaseConfig();

        String path = "armazem." + playerUUID + "." + itemType;
        db.set(path, db.getInt(path, 0) + itemAmount);

        ConfigDBase.saveDatabaseConfig();
    }

    public static void store(Player player) {
        val storeitens = config.getString("Messages.store-itens");
        val noitenstostore = config.getString("Messages.no-itens-to-store");
        val limitEnable = config.getBoolean("Limit.enable");
        val limitExceeded = config.getString("Messages.limit-exceeded");

        List<Material> allowed = ConfigValues.getAllowedItems();
        LimitManager limitManager = new LimitManager();

        if (limitEnable && BaseManager.getAllStored(player) >= limitManager.getLimit(player)) {
            ActionBarUtils.sendActionBar(
                    player,
                    ColorUtil.colored(limitExceeded)
                            .replace("{amount}", UtilClass.formatNumber(BaseManager.getAllStored(player)))
                            .replace("{limit}", UtilClass.formatNumber(limitManager.getLimit(player)))
            );
            UtilClass.sendSound(player, Sound.VILLAGER_NO);
            return;
        }

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

                player.sendMessage(ColorUtil.colored(storeitens)
                        .replace("{amount}", totalAmount + "")
                        .replace("{item}", DropsNameManager.getName(material)));
            }
        }
        if (!hasStoredItems) {
            player.sendMessage(ColorUtil.colored(noitenstostore));
            return;
        }
        player.closeInventory();
    }

    public static void sell(Player player, ItemStack itemType) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = ConfigDBase.getDatabaseConfig();

        Material material = itemType.getType();
        String itemTypeName = material.name();

        if (db.contains("armazem." + playerUUID + "." + itemTypeName)) {
            db.set("armazem." + playerUUID + "." + itemTypeName, null);
            ConfigDBase.saveDatabaseConfig();
        }
    }

    public static void sellAll(Player player) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = ConfigDBase.getDatabaseConfig();

        String sellItens = config.getString("Messages.sell-itens");
        String noItensToSell = config.getString("Messages.no-itens-to-sell");

        double totalValue = getTotalValue(player);
        int getAllStored = getAllStored(player);

        if (db.contains("armazem." + playerUUID)) {
            db.set("armazem." + playerUUID, null);

            EconomyHook.getEconomy().depositPlayer(player, totalValue);
            ConfigDBase.saveDatabaseConfig();

            player.sendMessage(ColorUtil.colored(sellItens)
                    .replace("{coins}", UtilClass.formatNumber(totalValue))
                    .replace("{amount}", UtilClass.formatNumber(getAllStored)));
            UtilClass.sendSound(player, Sound.LEVEL_UP);
        } else {
            player.sendMessage(ColorUtil.colored(noItensToSell));
            UtilClass.sendSound(player, Sound.VILLAGER_NO);
        }
    }

    public static void storeSpecificItem(Player player, Material material, int quantity) {
        FileConfiguration db = ConfigDBase.getDatabaseConfig();
        String playerUUID = player.getUniqueId().toString();

        String basePath = "armazem." + playerUUID + "." + material.name();

        if (db.contains(basePath)) {
            int currentAmount = db.getInt(basePath);
            db.set(basePath, currentAmount + quantity);
        } else {
            db.set(basePath, quantity);
        }
        ConfigDBase.saveDatabaseConfig();
    }

    public static List<String> storedItens(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();

        String noItens = config.getString("Messages.no-itens-stored");

        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = ConfigDBase.getDatabaseConfig();
        List<String> itensArmazenados = new ArrayList<>();

        if (db.contains("armazem." + playerUUID)) {
            db.getConfigurationSection("armazem." + playerUUID).getKeys(false).forEach(itemType -> {
                int itemAmount = db.getInt("armazem." + playerUUID + "." + itemType);
                itensArmazenados.add(
                        ColorUtil.colored(
                                          " &f" + DropsNameManager.getName(Material.valueOf(itemType))
                                        + " " +
                                          " &e" + UtilClass.formatNumber(itemAmount)));
            });
        } else {
            itensArmazenados.add(ColorUtil.colored(noItens));
        }
        return itensArmazenados;
    }

    public static double getTotalValue(Player player) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = ConfigDBase.getDatabaseConfig();

        Map<Material, Double> itemPrices = ConfigValues.loadItemPrices();

        double totalValue = 0.0;
        if (db.contains("armazem." + playerUUID)) {
            for (String itemName : db.getConfigurationSection("armazem." + playerUUID).getKeys(false)) {
                int itemAmount = db.getInt("armazem." + playerUUID + "." + itemName);

                Material material = Material.getMaterial(itemName);
                if (material != null) {
                    Double itemPrice = itemPrices.get(material);

                    if (itemPrice != null) {
                        totalValue += itemPrice * itemAmount;
                    }
                }
            }
        }
        return totalValue;
    }

    public static void getSpecificItem(Player player, ItemStack itemType, int quantidade) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = ConfigDBase.getDatabaseConfig();

        Material material = itemType.getType();
        String itemTypeName = material.name();

        if (db.contains("armazem." + playerUUID + "." + itemTypeName)) {
            int itemAmount = db.getInt("armazem." + playerUUID + "." + itemTypeName);
            int retirar = Math.min(itemAmount, quantidade);

            ItemStack itemLimpo = new ItemStack(material, retirar);
            HashMap<Integer, ItemStack> naoCouberam = player.getInventory().addItem(itemLimpo);

            int naoAdicionados = 0;
            for (ItemStack sobra : naoCouberam.values()) {
                naoAdicionados += sobra.getAmount();
            }

            int restanteNoArmazem = itemAmount - retirar + naoAdicionados;
            if (restanteNoArmazem > 0) {
                db.set("armazem." + playerUUID + "." + itemTypeName, restanteNoArmazem);
            } else {
                db.set("armazem." + playerUUID + "." + itemTypeName, null);
            }
            ConfigDBase.saveDatabaseConfig();
        }
    }

    public static int getSpecificStored(Player player, ItemStack itemStack) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = ConfigDBase.getDatabaseConfig();
        String itemTypeName = itemStack.getType().name();

        if (db.contains("armazem." + playerUUID + "." + itemTypeName)) {
            return db.getInt("armazem." + playerUUID + "." + itemTypeName);
        }
        return 0;
    }

    public static int getAllStored(Player player) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = ConfigDBase.getDatabaseConfig();

        String basePath = "armazem." + playerUUID;

        int totalQuantity = 0;
        if (db.contains(basePath)) {
            for (String itemName : db.getConfigurationSection(basePath).getKeys(false)) {
                totalQuantity += db.getInt(basePath + "." + itemName);
            }
        }
        return totalQuantity;
    }

    public static void openStorage(Player player) {
        int slot = config.getInt("StorageItem.slot");

        ArmazemSection.updateLore(player);

        ArmazemItens.pessoalArmazemInfoItem(player);
        ArmazemItens.sellAllItem(player);

        if(config.getBoolean("Limit.enable")) ArmazemInventory.getInventory().setItem(16, ArmazemItens.limitItem(player));

        ArmazemInventory.getInventory().setItem(slot, ArmazemItens.armazemItem());
        player.openInventory(ArmazemInventory.getInventory());
    }
}