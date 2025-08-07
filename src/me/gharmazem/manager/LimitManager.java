package me.gharmazem.manager;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.configuration.ConfigLimit;
import me.gharmazem.hook.EconomyHook;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LimitManager {

    FileConfiguration limit = ConfigLimit.getLimitConfig();
    FileConfiguration config = Main.getInstance().getConfig();

    public void updateTier(Player player) {
        val itemSection = config.getConfigurationSection("Limit.tiers");

        LimitManager limitManager = new LimitManager();
        List<String> tiers = new ArrayList<>(itemSection.getKeys(false));

        val actualTier = limitManager.getTier(player);
        val lastTier = itemSection.getString(actualTier + ".next");

        if(lastTier == null || lastTier.equalsIgnoreCase("fim")) {
            player.sendMessage(ColorUtil.colored(config.getString("Messages.last-limit-tier")));
            UtilClass.sendSound(player, Sound.VILLAGER_NO);
            player.closeInventory();
            return;
        }

        val priceToUpgrade = getNextTierPrice(player);
        val index = tiers.indexOf(actualTier);

        if(EconomyHook.getEconomy().getBalance(player) >= priceToUpgrade) {
            if (index != -1) {
                val nextTier = (index + 1 < tiers.size()) ? tiers.get(index + 1) : null;
                val path = "limits." + player.getName() + ".tier";

                EconomyHook.getEconomy().withdrawPlayer(player, priceToUpgrade);
                ConfigLimit.getLimitConfig().set(path, nextTier);
                ConfigLimit.saveLimitConfig();

                player.closeInventory();

                UtilClass.sendSound(player, Sound.LEVEL_UP);
                player.sendMessage(ColorUtil.colored(config.getString("Messages.limit-upgrade")));
            }
        }else {
            UtilClass.sendSound(player, Sound.VILLAGER_NO);
            player.sendMessage(ColorUtil.colored(config.getString("Messages.limit-upgrade-fail")
                    .replace("{amount}", UtilClass.formatNumber(priceToUpgrade))));
        }
    }

    public Integer getNextTierPrice(Player player) {
        val itemSection = config.getConfigurationSection("Limit.tiers");

        LimitManager limitManager = new LimitManager();
        List<String> tiers = new ArrayList<>(itemSection.getKeys(false));

        val actualTier = limitManager.getTier(player);
        val index = tiers.indexOf(actualTier);

        if (index != -1) {
            val nextTier = (index + 1 < tiers.size()) ? tiers.get(index + 1) : null;

            return itemSection.getInt(nextTier + ".price");
        }
        return 0;
    }

    public Integer getNextTierLimit(Player player) {
        val itemSection = config.getConfigurationSection("Limit.tiers");

        LimitManager limitManager = new LimitManager();
        List<String> tiers = new ArrayList<>(itemSection.getKeys(false));

        val actualTier = limitManager.getTier(player);
        val index = tiers.indexOf(actualTier);

        if (index != -1) {
            val nextTier = (index + 1 < tiers.size()) ? tiers.get(index + 1) : null;

            return itemSection.getInt(nextTier + ".limit");
        }
        return 0;
    }

    public String getTier(Player player) {
        return (String) limit.get("limits." + player.getName() + ".tier");
    }

    public Integer getLimit(Player player) {
        ConfigurationSection itemSection = config.getConfigurationSection("Limit.tiers");

        return itemSection.getInt(getTier(player) + ".limit");
    }

    public String getNextTier(Player player) {
        ConfigurationSection itemSection = config.getConfigurationSection("Limit.tiers");
        val actualTier = getTier(player);

        return itemSection.getString(actualTier + ".next");
    }
}
