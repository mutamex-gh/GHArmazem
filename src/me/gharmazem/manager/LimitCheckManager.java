package me.gharmazem.manager;

import lombok.val;
import me.gharmazem.configuration.ConfigLimit;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.ItemBuilder;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class LimitCheckManager {

    public static ItemStack createLimitCheck(Integer value) {
        return new ItemBuilder(Material.PAPER)
                .changeItemMeta(itemMeta -> {
                    itemMeta.setDisplayName(ColorUtil.colored("&eCheque de Limite(s)"));
                    itemMeta.setLore(Arrays.asList(ColorUtil.colored(
                            "&7Ganhe limites adicionais",
                            "",
                            "  &fEste item vale como adicional",
                            "  &fadicional de limite para seu",
                            "  &farmazém de itens!",
                            "",
                            "&7Este cheque vale &f" + UtilClass.formatNumber(value) + " &7limite(s)",
                            "",
                            "&aClique para ativar!"
                    )));
                    itemMeta.addEnchant(Enchantment.OXYGEN, 10, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                })
                .setNBTTag("cheque_limite", String.valueOf(value))
                .wrap();
    }

    public static void activateAdditionalLimit(Player player, int value) {
        val limit = ConfigLimit.getLimitConfig().getConfigurationSection("limits");

        val path = "limits." + player.getName() + ".additional-limit";
        val actualLimit = limit.getInt(player.getName() + ".additional-limit");

        ConfigLimit.getLimitConfig().set(path, value + actualLimit);

        ConfigLimit.saveLimitConfig();
    }

    public static Integer getAdditionalLimit(Player player) {
        val limit = ConfigLimit.getLimitConfig().getConfigurationSection("limits");

        return limit.getInt(player.getName() + ".additional-limit");
    }
}
