package me.gharmazem.inventories;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.hook.EconomyHook;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.manager.LimitCheckManager;
import me.gharmazem.manager.LimitManager;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.ItemBuilderGB;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmazemItens {

    public static void pessoalArmazemInfoItem(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();
        LimitManager limitManager = new LimitManager();

        val title = config.getString("PessoalArmazemItem.title");
        val playerInfoLore = config.getStringList("PessoalArmazemItem.lore");
        val slot = config.getInt("PessoalArmazemItem.slot");
        val limitEnable = config.getBoolean("Limit.enable");

        double balance = EconomyHook.getEconomy().getBalance(player);
        int allStored = BaseManager.getAllStored(player);

        List<String> lore = new ArrayList<>();
        for (String line : playerInfoLore) {
            String updatedLine = ColorUtil.colored(line)
                            .replace("{coins}", UtilClass.formatNumber(balance))
                            .replace("{amount}", UtilClass.formatNumber(allStored))
                            .replace("{baselimit}", UtilClass.formatNumber(limitManager.getBaseLimit(player)))
                            .replace("{adtlimit}", UtilClass.formatNumber(LimitCheckManager.getAdditionalLimit(player)))
                            .replace("{limit}", UtilClass.formatNumber(limitManager.getLimit(player)));
            lore.add(updatedLine);
        }
        lore.add(UtilClass.getStorageBar(BaseManager.getAllStored(player), limitManager.getLimit(player)));

        ItemStack PessoalArmazemInfo = new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtil.colored(title).replace("{player}", player.getName()))
                .lore(ColorUtil.colored(lore))
                .durability((short)3)
                .skullOwner(player.getName())
                .build();

        ArmazemInventory.getInventory().setItem(slot, PessoalArmazemInfo);
    }

    public static ItemStack limitItem(Player player) {
        LimitManager limitManager = new LimitManager();
        List<String> lore;

        if(limitManager.getNextTier(player) == null || limitManager.getNextTier(player).equalsIgnoreCase("fim")) {
            lore = Arrays.asList(ColorUtil.colored(
                    "&7Clique para atualizar seu limite",
                    "",
                    " &fLimite base atual: &e" + UtilClass.formatNumber(limitManager.getBaseLimit(player)),
                    "",
                    UtilClass.getStorageBar(BaseManager.getAllStored(player), limitManager.getLimit(player)),
                    "",
                    "&cVocê está no último nivel!",
                    ""
            ));
        }else {
            lore = Arrays.asList(ColorUtil.colored(
                    "&7Clique para atualizar seu limite",
                    "",
                    " &fLimite base atual: &e" + UtilClass.formatNumber(limitManager.getBaseLimit(player)),
                    "",
                    " &7Preço para upgrade: &a" + UtilClass.formatNumber(limitManager.getNextTierPrice(player)) + " &2Coin(s)",
                    " &7Próximo limite: &c" + UtilClass.formatNumber(limitManager.getNextTierLimit(player)),
                    "",
                    UtilClass.getStorageBar(BaseManager.getAllStored(player), limitManager.getLimit(player)),
                    "",
                    "&aClique para evoluir!",
                    ""
            ));
        }

        return new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtil.colored("&aGerenciador de Limites"))
                .lore(ColorUtil.colored(lore))
                .durability((short) 3)
                .skullTexture("76a5734eaed02907408dff3e3f2c6efcd35a546c8f0af6c5e952d8cb8a516e82")
                .build();
    }

    public static ItemStack armazemItem() {
        FileConfiguration config = Main.getInstance().getConfig();

        List<String> lore = config.getStringList("StorageItem.lore");
        String title = config.getString("StorageItem.title");

        return new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtil.colored(title))
                .lore(ColorUtil.colored(lore))
                .durability((short) 3)
                .skullTexture("275bcff2e74deed37a319a1f404e70d06a5f360cacee99c71346f38560cbd72a")
                .build();
    }

    public static ItemStack storageDropsItem() {
        return new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtil.colored("&aGuardar Drops"))
                .lore(ColorUtil.colored(
                        "&7Clique para guardar os drops",
                        "",
                        " &fAo clicar, você irá armazenar",
                        " &ftodos os drops de seu inventario",
                        ""
                     ))
                .durability((short)3)
                .skullTexture("bb141f56cc5147fe410348e94345d148e7c679b3212335cb3e88fd9d6f840806")
                .build();
    }

    public static ItemStack savedItens(Player player) {
        List<String> itensArmazenados = BaseManager.storedItens(player);

        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.colored("&7Veja seus itens armazenados"));
        lore.add(" ");

        for (String item : itensArmazenados) {
            lore.add(ColorUtil.colored("  &f" + item));
        }

        return new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtil.colored("&aItens Armazenados"))
                .lore(ColorUtil.colored(lore))
                .durability((short) 3)
                .skullTexture("ace0c3da31714dd537da6a0c6e9b3a46f9aa95a1a8d7975448a1ba8990fe541f")
                .build();
    }

    public static ItemStack sellAllItem(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();

        int slot = config.getInt("SellAllItem.slot");
        String title = config.getString("SellAllItem.title");
        List<String> lore = config.getStringList("SellAllItem.lore");

        String URL;
        if(BaseManager.getAllStored(player) >= 1) {
            URL = "67f8d8c39f9921a64d7b755de322e441fded4b775a7657e0b3196b6fda863646";
        }else {
            URL = "ad2eb52a60c12ed2fb4a2b912d4420fe1eb7c69821d9e868d0655b29737e2a2c";
        }

        ItemStack sellAllItemBuilder = new ItemBuilderGB(Material.SKULL_ITEM)
                .name(ColorUtil.colored(title))
                .lore(ColorUtil.colored(lore))
                .durability((short) 3)
                .skullTexture(URL)
                .enchantment(Enchantment.ARROW_INFINITE, 1)
                .hideEnchantments()
                .build();

        ArmazemInventory.getInventory().setItem(slot, sellAllItemBuilder);

        return sellAllItemBuilder;
    }

}
