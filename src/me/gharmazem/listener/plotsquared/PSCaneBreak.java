package me.gharmazem.listener.plotsquared;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import me.gharmazem.Main;
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

public class PSCaneBreak implements Listener {

    boolean psEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.enable");

    @EventHandler
    public void onCaneBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockDropMapper = BlockDropMapper.getDrop(block.getType());

        if (!psEnable) return;
        if (block.getType() != Material.SUGAR_CANE_BLOCK) return;

        final Plot plotAPI = new PlotAPI().getPlot(block.getLocation());
        if (plotAPI == null || !plotAPI.hasOwner() || !plotAPI.isOwner(player.getUniqueId())) return;

        boolean isFullyGrown = UtilClass.isFullyGrown(block);
        int dropsMultiplier = isFullyGrown ? 1 + UtilClass.getFortune(player) : 1;

        event.setCancelled(true);

        Block aboveBlock = block.getRelative(0, 1, 0);
        if (aboveBlock.getType() == Material.SUGAR_CANE_BLOCK) {
            aboveBlock.setType(Material.AIR);

            BonusManager.setBonus(player, blockDropMapper, dropsMultiplier * 2);
            RewardsManager.sendReward(player);
            block.setType(Material.AIR);

            return;
        }
        BonusManager.setBonus(player, blockDropMapper, dropsMultiplier);
        RewardsManager.sendReward(player);
        block.setType(Material.AIR);
    }
}
