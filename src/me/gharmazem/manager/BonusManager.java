package me.gharmazem.manager;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.utils.ActionBarUtils;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BonusManager {

    public static void register() {
        loadBonusPermissions();
    }

    public static void setBonus(Player player, Material material, double amount) {
        val getPerm = loadBonusPermissions();
        val actionBarStore = Main.getInstance().getConfig().getString("Messages.actionbar-store");

        for (Map.Entry<String, Double> entry : getPerm.entrySet()) {
            String permission = entry.getKey();
            double bonus = entry.getValue();

            if (player.hasPermission(permission)) {
                double quantity = amount * bonus;
                BaseManager.storeSpecificItem(player, material, (int) quantity);

                if(material == Material.CACTUS) return;

                ActionBarUtils.sendActionBar(
                        player,
                        ColorUtil.colored(actionBarStore)
                                .replace("{amount}", UtilClass.formatNumber(quantity))
                                .replace("{dropname}", DropsNameManager.getName(material) != null ? DropsNameManager.getName(material) : "Melancia")
                                .replace("{fortune}", String.valueOf(UtilClass.itemFortune(player)))
                                .replace("{multiplier}", String.valueOf(bonus))
                );
            }
        }
    }

    public static HashMap<String, Double> loadBonusPermissions() {
        val mapPerm = new HashMap<String, Double>();
        val config = Main.getInstance().getConfig();

        if (config.contains("PlotSquaredSupport.bonus")) {
            for (String permission : config.getConfigurationSection("PlotSquaredSupport.bonus").getKeys(true)) {
                if (permission != null) {
                    double bonus = config.getDouble("PlotSquaredSupport.bonus." + permission);
                    mapPerm.put(permission, bonus);
                }
            }
        }
        return mapPerm;
    }
}
