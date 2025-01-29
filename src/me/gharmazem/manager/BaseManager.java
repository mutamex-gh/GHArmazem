package me.gharmazem.manager;

import me.gharmazem.Main;
import me.gharmazem.inventories.ArmazemInventory;
import me.gharmazem.inventories.ArmazemItens;
import me.gharmazem.utils.some.ColorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.gharmazem.Main.loadItemPrices;

public class BaseManager {

    static FileConfiguration config = Main.getInstance().getConfig();

    public static void saveItem(Player player, ItemStack item) {
        String playerUUID = player.getUniqueId().toString();
        String itemType = item.getType().toString();
        int itemAmount = item.getAmount();

        FileConfiguration db = Main.getInstance().getDatabaseConfig();

        String path = "armazem." + playerUUID + "." + itemType;
        db.set(path, db.getInt(path, 0) + itemAmount);

        Main.getInstance().saveDatabaseConfig();
    }

    public static void getSpecificItem(Player player, ItemStack itemType, int quantidade) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = Main.getInstance().getDatabaseConfig();



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

            Main.getInstance().saveDatabaseConfig();

            if (naoAdicionados > 0) {
                player.sendMessage("§cNem todos os itens cabem no inventário! Restantes: " + naoAdicionados);
            }
        }
    }


    public static void sell(Player player, ItemStack itemType) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = Main.getInstance().getDatabaseConfig();

        Material material = itemType.getType();
        String itemTypeName = material.name();

        if (db.contains("armazem." + playerUUID + "." + itemTypeName)) {
            db.set("armazem." + playerUUID + "." + itemTypeName, null);
            Main.getInstance().saveDatabaseConfig();
        }
    }

    public static void sellAll(Player player) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = Main.getInstance().getDatabaseConfig();

        if (db.contains("armazem." + playerUUID)) {
            db.set("armazem." + playerUUID, null);

            Main.getInstance().saveDatabaseConfig();
        } else {
            player.sendMessage(ColorUtil.colored("&cNão há itens no seu armazém para remover."));
        }
    }


    public static double getTotalValue(Player player) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = Main.getInstance().getDatabaseConfig();
        double totalValue = 0.0;

        Map<Material, Double> itemPrices = loadItemPrices();

        if (db.contains("armazem." + playerUUID)) {
            for (String itemName : db.getConfigurationSection("armazem." + playerUUID).getKeys(false)) {
                int itemAmount = db.getInt("armazem." + playerUUID + "." + itemName);

                Material material = Material.getMaterial(itemName);
                if (material != null) {
                    Double itemPrice = itemPrices.get(material);

                    if (itemPrice != null) {
                        totalValue += itemPrice * itemAmount;
                    } else {
                        System.out.println("Preco nao encontrado para: " + itemName);
                    }
                } else {
                    System.out.println("Material invalido: " + itemName);
                }
            }
        }

        return totalValue;
    }

    public static int getStored(Player player, ItemStack itemStack) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = Main.getInstance().getDatabaseConfig();
        String itemTypeName = itemStack.getType().name();
        int itemAmount = 0;

        if (db.contains("armazem." + playerUUID + "." + itemTypeName)) {
            itemAmount = db.getInt("armazem." + playerUUID + "." + itemTypeName);
        }
        return itemAmount;
    }

    public static int getAllStored(Player player) {
        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = Main.getInstance().getDatabaseConfig();
        int totalQuantity = 0;

        String basePath = "armazem." + playerUUID;

        if (db.contains(basePath)) {
            for (String itemName : db.getConfigurationSection(basePath).getKeys(false)) {
                totalQuantity += db.getInt(basePath + "." + itemName);
            }
        }

        return totalQuantity;
    }

    public static List<String> storeItens(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();

        String noitens = config.getString("Messages.no-itens-stored");

        String playerUUID = player.getUniqueId().toString();
        FileConfiguration db = Main.getInstance().getDatabaseConfig();
        List<String> itensArmazenados = new ArrayList<>();

        if (db.contains("armazem." + playerUUID)) {
            db.getConfigurationSection("armazem." + playerUUID).getKeys(false).forEach(itemType -> {
                int itemAmount = db.getInt("armazem." + playerUUID + "." + itemType);
                itensArmazenados.add(ColorUtil.colored(itemType + " " + itemAmount));
            });
        } else {
            itensArmazenados.add(ColorUtil.colored(noitens));
        }
        return itensArmazenados;
    }

    public static void storeSpecifyItem(Player player, Material material, int quantity) {
        FileConfiguration db = Main.getInstance().getDatabaseConfig();
        String playerUUID = player.getUniqueId().toString();

        String basePath = "armazem." + playerUUID + "." + material.name();

        if (db.contains(basePath)) {
            int currentAmount = db.getInt(basePath);
            db.set(basePath, currentAmount + quantity);
        } else {
            db.set(basePath, quantity);
        }
        Main.getInstance().saveDatabaseConfig();
    }

    public static void openStorage(Player player) {
        int slot = config.getInt("StorageItem.slot");

        Inventory armazem = ArmazemInventory.getInventory();
        ItemStack item = ArmazemItens.armazemItem();

        armazem.setItem(slot, item);
        player.openInventory(armazem);
    }
}
