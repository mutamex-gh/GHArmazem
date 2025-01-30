package me.gharmazem.utils;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

import java.util.SplittableRandom;

public class UtilClass {

    public static SplittableRandom rand = new SplittableRandom();

    public static void sendSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
    }

    public static int getFortune(Player p) {
        if (p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            return rand.nextInt(p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)) + 1;
        }
        return 1;
    }

    public static boolean isFullyGrown(Block block) {
        Material type = block.getType();
        MaterialData data = block.getState().getData(); // Obtém os dados do bloco

        if (data instanceof Crops) {
            Crops crops = (Crops) data;
            return crops.getState() == CropState.RIPE;
        }

        if (data instanceof NetherWarts) {
            NetherWarts netherWarts = (NetherWarts) data;
            return netherWarts.getState() == NetherWartsState.RIPE;
        }

        if (type == Material.POTATO || type == Material.CARROT) {
            return data.getData() == 7; // estagio maximo de crescimento
        }

        return false;
    }
}
