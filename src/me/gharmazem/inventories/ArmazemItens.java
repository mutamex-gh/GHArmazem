package me.gharmazem.inventories;

import me.gharmazem.Main;
import me.gharmazem.hook.EconomyHook;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.ItemBuilderGB;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArmazemItens {

    public static void pessoalArmazemInfoItem(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();

        String title = config.getString("PessoalArmazemItem.title");
        List<String> playerInfoLore = config.getStringList("PessoalArmazemItem.lore");
        int slot = config.getInt("PessoalArmazemItem.slot");

        double balance = EconomyHook.getEconomy().getBalance(player);
        int allStored = BaseManager.getAllStored(player);

        List<String> lore = new ArrayList<>();
        for (String line : playerInfoLore) {
            String updatedLine = ColorUtil.colored(line)
                            .replace("{coins}", UtilClass.formatNumber(balance))
                            .replace("{amount}", UtilClass.formatNumber(allStored));
            lore.add(updatedLine);
        }

        ItemStack PessoalArmazemInfo = new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtil.colored(title).replace("{player}", player.getName()))
                .lore(ColorUtil.colored(lore))
                .durability((short)3)
                .skullOwner(player.getName())
                .build();

        ArmazemInventory.getInventory().setItem(slot, PessoalArmazemInfo);
    }

    public static ItemStack armazemItem() {
        FileConfiguration config = Main.getInstance().getConfig();

        List<String> lore = config.getStringList("StorageItem.lore");
        String material = config.getString("StorageItem.material");
        String title = config.getString("StorageItem.title");

        ItemStack armazemChest = new ItemBuilderGB(Material.getMaterial(material))
                .name(ColorUtil.colored(title))
                .lore(ColorUtil.colored(lore))
                .build();

        return armazemChest;
    }

    public static ItemStack storageDropsItem() {
        ItemStack storageDrop = new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtil.colored("&aGuardar Drops"))
                .lore(ColorUtil.colored(
                        "&7Clique para guardar os drops",
                        "",
                        " &fAo clicar, você irá armazenar",
                        " &ftodos os drops de seu inventario",
                        ""
                     ))
                .durability((short)3)
                .enchantment(Enchantment.ARROW_DAMAGE, 1)
                .hideEnchantments()
                .skullOwner("Chestt")
                .build();

        return storageDrop;
    }

    public static ItemStack savedItens(Player player) {
        List<String> itensArmazenados = BaseManager.storedItens(player);

        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.colored("&7Veja seus itens armazenados"));
        lore.add(" ");

        for (String item : itensArmazenados) {
            lore.add(ColorUtil.colored("  &f" + item)); // adiciona cada item como uma nova linha
        }

        ItemStack savedItens = new ItemBuilderGB(Material.HOPPER)
                .name(ColorUtil.colored("&aItens Armazenados"))
                .lore(ColorUtil.colored(lore))
                .build();

        return savedItens;
    }

    public static ItemStack sellAllItem() {
        FileConfiguration config = Main.getInstance().getConfig();

        int slot = config.getInt("SellAllItem.slot");
        String material = config.getString("SellAllItem.material");
        String title = config.getString("SellAllItem.title");
        List<String> lore = config.getStringList("SellAllItem.lore");

        ItemStack sellAllItemBuilder = new ItemBuilderGB(Material.getMaterial(material))
                .name(ColorUtil.colored(title))
                .lore(ColorUtil.colored(lore))
                .build();

        ArmazemInventory.getInventory().setItem(slot, sellAllItemBuilder);

        return sellAllItemBuilder;
    }
}
