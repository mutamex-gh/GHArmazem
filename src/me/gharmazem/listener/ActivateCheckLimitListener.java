package me.gharmazem.listener;

import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.manager.LimitCheckManager;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.ItemBuilder;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ActivateCheckLimitListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        val player = event.getPlayer();
        val item = player.getItemInHand();

        val success = Main.getInstance().getConfig().getString("Messages.activate-additional-limit");

        if(item == null || item.getType() != Material.PAPER || !item.getItemMeta().hasEnchant(Enchantment.OXYGEN)) return;

        val valorNBT = ItemBuilder.getNBTTag(item, "cheque_limite");
        if(valorNBT == null) return;

        LimitCheckManager.activateAdditionalLimit(player, Integer.parseInt(valorNBT));

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }

        player.sendMessage(ColorUtil.colored(success)
                .replace("{amount}", UtilClass.formatNumber(Integer.parseInt(valorNBT))));
        UtilClass.sendSound(player, Sound.LEVEL_UP);
    }
}
