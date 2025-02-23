package me.gharmazem.listener.plotsquared;

import com.intellectualcrafters.plot.api.PlotAPI;
import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.configuration.ConfigValues;
import me.gharmazem.manager.BonusManager;
import me.gharmazem.manager.RewardsManager;
import me.gharmazem.manager.enums.BlockDropMapper;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PSBlockBreak implements Listener {

    @EventHandler
    public void onBreakBlocksInPlot(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        PlotAPI plotAPI = new PlotAPI();

        val isEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.enable");
        val replantEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.replant");
        val toolToBreak = Main.getInstance().getConfig().getStringList("PlotSquaredSupport.tool-to-break");

        if (!isEnable) return;
        if (block.getType() == Material.CACTUS) event.setCancelled(true);
        if (block.getType() == Material.SUGAR_CANE_BLOCK) return;
        if (plotAPI.getPlot(block.getLocation()) == null) return;
        if (!plotAPI.getPlot(block.getLocation()).isOwner(player.getUniqueId())) return;

        if (toolToBreak.contains(player.getItemInHand().getType().name()) &&
                ConfigValues.getAllowedItems().contains(block.getType())) {

            Material blockMapperType = BlockDropMapper.getDrop(block.getType());
            boolean isFullyGrown = UtilClass.isFullyGrown(block);
            int dropsMultiplier = isFullyGrown ? block.getDrops().size() + UtilClass.getFortune(player) : 1;

            if (blockMapperType != null) {
                if (block.getType() == Material.NETHER_WARTS) dropsMultiplier = isFullyGrown ? 1 + UtilClass.getFortune(player) : 1;
                if (block.getType() == Material.MELON_BLOCK) dropsMultiplier = isFullyGrown ? 6 + UtilClass.getFortune(player) : 6;

                event.setCancelled(true);

                if (replantEnable && block.getType() != Material.MELON_BLOCK) {
                    block.setType(block.getType());

                    if (!isFullyGrown) return;

                    BonusManager.setBonus(player, blockMapperType, dropsMultiplier);
                    RewardsManager.sendReward(player);
                }else {
                    BonusManager.setBonus(player, blockMapperType, dropsMultiplier);
                    RewardsManager.sendReward(player);

                    block.setType(Material.AIR);
                }
            }
        }
    }
}

