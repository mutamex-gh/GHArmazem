package me.gharmazem.manager;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.manager.enums.RewardsMode;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class RewardsManager {

    public static void sendReward(Player player) {
        val config = Main.getInstance().getConfig();

        val possibleRewards = config.getStringList("PlotSquaredSupport.rewards.rewards-command");
        val rewardsMessage = config.getStringList("PlotSquaredSupport.rewards.rewards-message");
        val rewardsEnable = config.getBoolean("PlotSquaredSupport.rewards.enable");
        val psEnable = config.getBoolean("PlotSquaredSupport.enable");
        val chance = config.getDouble("PlotSquaredSupport.rewards.chance");
        val mode = RewardsMode.mode(config.getString("PlotSquaredSupport.rewards.mode", "SORTER"));

        if (!psEnable && !rewardsEnable) return;
        if (possibleRewards.isEmpty()) return;

        if (player.hasPermission("gharmazem.rewards")) {
            val random = new Random();
            val randomValue = random.nextDouble() * 100;

            try {
                if (randomValue <= chance) {
                    switch (mode) {
                        case SORTER:
                            val rewardCommand = possibleRewards.get(random.nextInt(possibleRewards.size()));

                            Bukkit.dispatchCommand(
                                    Bukkit.getConsoleSender(),
                                    rewardCommand
                                            .replace("{player}", player.getName())
                            );
                            break;

                        case ALL_BELOW:
                            for (val command : possibleRewards) {
                                Bukkit.dispatchCommand(
                                        Bukkit.getConsoleSender(),
                                        command
                                                .replace("{player}", player.getName())
                                );
                            }
                            break;
                    }
                    player.sendMessage(ColorUtil.colored(String.join("\n", rewardsMessage)));
                    UtilClass.sendSound(player, Sound.LEVEL_UP);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
