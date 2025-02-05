package me.gharmazem.manager;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.utils.ActionBarUtils;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BonusManager {

    private final Main plugin;

    public BonusManager(Main plugin) {
        this.plugin = plugin;
    }

    public void register() {
        loadBonusPermissions();
    }

    public void setBonus(Player player, Material material, int quantity) {
        val getPerm = loadBonusPermissions();
        String actionBarStore = Main.getInstance().getConfig().getString("Messages.actionbar-store");

        for (Map.Entry<String, Double> entry : getPerm.entrySet()) {
            String permission = entry.getKey();
            double bonus = entry.getValue();

            if (player.hasPermission(permission)) {
                int nowQuantity = (int) (quantity * bonus);
                BaseManager.storeSpecifyItem(player, material, nowQuantity);

                if(material == Material.CACTUS) return;

                ActionBarUtils.sendActionBar(
                        player,
                        ColorUtil.colored(actionBarStore)
                                .replace("{quantia}", UtilClass.formatNumber(nowQuantity))
                );
            }
        }
    }

    public HashMap<String, Double> loadBonusPermissions() {
        HashMap<String, Double> mapPerm = new HashMap<>();
        ConfigurationSection config = plugin.getConfig();

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
