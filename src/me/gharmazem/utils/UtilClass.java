package me.gharmazem.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class UtilClass {

    public static void sendSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
    }
}
