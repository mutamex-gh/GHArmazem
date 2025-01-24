package me.gharmazem.inventories;

import me.gharmazem.Main;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.utils.ColorUtils;
import me.gharmazem.utils.ItemBuilderGB;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ArmazemItens {

    public static ItemStack pessoalArmazemInfoItem(Player player) {
        DecimalFormat df = new DecimalFormat("#,###,###,##0.##");
        FileConfiguration config = Main.getInstance().getConfig();

        String title = config.getString("PessoalArmazemItem.title");
        List<String> playerinfolore = config.getStringList("PessoalArmazemItem.lore");
        int slot = config.getInt("PessoalArmazemItem.slot");

        double balance = Main.getEconomy().getBalance(player);
        int allStored = BaseManager.getAllStored(player);

        List<String> lore = new ArrayList<>();
        for (String line : playerinfolore) {
            String updatedLine = ColorUtils.colored(line)
                            .replace("{money}", df.format(balance)
                            .replace("{storageitens}", String.valueOf(allStored)));
            lore.add(updatedLine);
        }

        ItemStack PessoalArmazemInfo = new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtils.colored(title).replace("{player}", player.getName()))
                .lore(ColorUtils.colored(lore))
                .durability((short)3)
                .skullOwner(player.getName())
                .build();

        ArmazemInventory.getInventory().setItem(slot, PessoalArmazemInfo);

        return PessoalArmazemInfo;
    }

    public static ItemStack armazemItem() {
        FileConfiguration config = Main.getInstance().getConfig();

        List<String> lore = config.getStringList("StorageItem.lore");
        String material = config.getString("StorageItem.material");
        String title = config.getString("StorageItem.title");

        ItemStack armazemChest = new ItemBuilderGB(Material.getMaterial(material))
                .name(ColorUtils.colored(title))
                .lore(ColorUtils.colored(lore))
                .build();

        return armazemChest;
    }

    public static ItemStack storageDropsItem() {
        ItemStack storageDrop = new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtils.colored("&aGuardar Drops"))
                .lore(ColorUtils.colored(
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

    public static ItemStack arrowBack() {
        ItemStack arrowBack = new ItemBuilderGB(Material.ARROW)
                .name(ColorUtils.colored("&aVoltar&7(Clique)"))
                .lore(ColorUtils.colored(
                        "",
                        " &fClique para voltar ao menu principal"
                ))
                .build();

        return arrowBack;
    }

    public static ItemStack savedItens(Player player) {

        List<String> itensArmazenados = BaseManager.storedItens(player);

        List<String> lore = new ArrayList<>();
        lore.add(ColorUtils.colored("&7Veja seus itens armazenados"));
        lore.add(" ");

        for (String item : itensArmazenados) {
            lore.add(ColorUtils.colored("  &f" + item)); // adiciona cada item como uma nova linha
        }

        ItemStack savedItens = new ItemBuilderGB(Material.HOPPER)
                .name(ColorUtils.colored("&aItens Armazenados"))
                .lore(ColorUtils.colored(lore))
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
                .name(ColorUtils.colored(title))
                .lore(ColorUtils.colored(lore))
                .build();

        ArmazemInventory.getInventory().setItem(slot, sellAllItemBuilder);

        return sellAllItemBuilder;
    }
}
