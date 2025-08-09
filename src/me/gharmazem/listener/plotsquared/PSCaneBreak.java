package me.gharmazem.listener.plotsquared;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.manager.BonusManager;
import me.gharmazem.manager.LimitManager;
import me.gharmazem.manager.RewardsManager;
import me.gharmazem.manager.enums.BlockDropMapper;
import me.gharmazem.utils.ActionBarUtils;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PSCaneBreak implements Listener {

    boolean psEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.enable");

    @EventHandler
    public void onCaneBreak(BlockBreakEvent event) {
        val player = event.getPlayer();
        val block = event.getBlock();
        val blockDropMapper = BlockDropMapper.getDrop(block.getType());
        val config = Main.getInstance().getConfig();
        val limitExceeded = config.getString("Messages.limit-exceeded");

        LimitManager limitManager = new LimitManager();

        if (!psEnable) return;
        if (block.getType() != Material.SUGAR_CANE_BLOCK) return;

        final Plot plotAPI = new PlotAPI().getPlot(block.getLocation());
        if (plotAPI == null || !plotAPI.hasOwner() || !plotAPI.isOwner(player.getUniqueId())) return;

        val isFullyGrown = UtilClass.isFullyGrown(block);
        val dropsMultiplier = isFullyGrown ? 1 + UtilClass.getFortune(player) : 1;

        if (BaseManager.getAllStored(player) >= limitManager.getLimit(player)) {
            ActionBarUtils.sendActionBar(
                    player,
                    ColorUtil.colored(limitExceeded)
                            .replace("{amount}", UtilClass.formatNumber(BaseManager.getAllStored(player)))
                            .replace("{limit}", UtilClass.formatNumber(limitManager.getLimit(player)))
            );
            Block aboveBlock = block.getRelative(0, 1, 0);

            aboveBlock.setType(Material.AIR);
            block.setType(Material.AIR);

            UtilClass.sendSound(player, Sound.VILLAGER_NO);
            return;
        }

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
