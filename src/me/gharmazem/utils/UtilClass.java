package me.gharmazem.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

import java.text.DecimalFormat;
import java.util.SplittableRandom;

public class UtilClass {

    public static SplittableRandom rand = new SplittableRandom();

    public static void sendSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
    }

    public static int getFortune(Player p) {
        if (p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            return rand.nextInt(p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));
        }
        return 0;
    }

    public static int itemFortune(Player p) {
        if (p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            return p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        }
        return 0;
    }

    public static boolean isFullyGrown(Block block) {
        Material type = block.getType();
        MaterialData data = block.getState().getData();

        if (data instanceof Crops) {
            Crops crops = (Crops) data;
            return crops.getState() == CropState.RIPE;
        }

        if (data instanceof NetherWarts) {
            NetherWarts netherWarts = (NetherWarts) data;
            return netherWarts.getState() == NetherWartsState.RIPE;
        }

        if (type == Material.MELON_BLOCK || type == Material.SUGAR_CANE_BLOCK) {
            return true;
        }

        if (type == Material.POTATO || type == Material.CARROT) {
            return data.getData() == 7; // estagio maximo de crescimento
        }

        return false;
    }

    public static String formatNumber(double number) {
        if (number < 1000) {
            return String.valueOf((int)number);
        }

        String[] suffixes = {"", "k", "m", "b", "t", "q", "qq", "s", "sp"};
        int magnitude = (int) Math.log10(number);
        int suffixIndex = magnitude / 3;

        double shortenedNumber = number / Math.pow(1000, suffixIndex);

        DecimalFormat df = new DecimalFormat("#0.##");

        return df.format(shortenedNumber) + suffixes[suffixIndex];
    }

    public static String getStorageBar(int used, int max) {
        int totalBars = 10;
        double percentage = (double) used / max;
        int filledBars = (int) (percentage * totalBars);

        StringBuilder bar = new StringBuilder("§fEstoque: ");

        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                bar.append("§a▌");
            } else {
                bar.append("§7▌");
            }
        }

        bar.append(" §7").append((int)(percentage * 100)).append("%");

        return bar.toString();
    }

}
