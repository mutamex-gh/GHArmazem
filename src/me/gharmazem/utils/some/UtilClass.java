package me.gharmazem.utils.some;

import org.bukkit.CropState;
import org.bukkit.NetherWartsState;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.material.Crops;
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
        if (block.getState().getData() instanceof Crops) {
            Crops crops = (Crops) block.getState().getData();
            return crops.getState() == CropState.RIPE;
        }

        if (block.getState().getData() instanceof NetherWarts) {
            NetherWarts netherWarts = (NetherWarts) block.getState().getData();
            return netherWarts.getState() == NetherWartsState.RIPE;
        }

        return true;
    }
}
