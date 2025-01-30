package me.gharmazem.listener.PlotSquared;

import com.intellectualcrafters.plot.api.PlotAPI;
import me.gharmazem.Main;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.manager.enums.BlockDropMapper;
import me.gharmazem.utils.ActionBarUtils;
import me.gharmazem.utils.ColorUtil;
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

        if (plotAPI.getPlot(block.getLocation()) == null) return;
        if (!plotAPI.getPlot(block.getLocation()).isOwner(player.getUniqueId())) return;

        String actionBarStore = Main.getInstance().getConfig().getString("Messages.actionbar-store");
        boolean isEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.enable");
        boolean replantEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.replant");
        double multiplier = Main.getInstance().getConfig().getDouble("PlotSquaredSupport.multiplier");

        if (isEnable && Main.getInstance().getAllowedItems().contains(block.getType())) {

            Material blockMapperType = BlockDropMapper.getDrop(block.getType());
            boolean isFullyGrown = UtilClass.isFullyGrown(block);
            double dropsMultiplier = isFullyGrown
                    ? Math.floor(block.getDrops().size() + UtilClass.getFortune(player) * multiplier)
                    : 1;

            if (blockMapperType != null) {
                int dropsAmount = (int) dropsMultiplier;
                if (block.getType() == Material.NETHER_WARTS || block.getType() == Material.COCOA) {
                    int baseDrops = (block.getType() == Material.NETHER_WARTS) ? 1 : 2;
                    dropsAmount = isFullyGrown
                            ? (baseDrops + (UtilClass.getFortune(player)
                            * (int)multiplier)) : 1;
                }
                event.setCancelled(true);

                if (replantEnable) {
                    block.setType(block.getType());

                    if(!isFullyGrown) {
                        dropsAmount = 0;
                    }
                    BaseManager.storeSpecifyItem(player, blockMapperType, dropsAmount);
                    ActionBarUtils.sendActionBar(
                            player,
                            ColorUtil.colored(actionBarStore)
                                    .replace("{quantia}", String.valueOf(dropsAmount))
                    );
                    return;
                }

                block.setType(Material.AIR);
                BaseManager.storeSpecifyItem(player, blockMapperType, dropsAmount);

                ActionBarUtils.sendActionBar(
                        player,
                        ColorUtil.colored(actionBarStore)
                                .replace("{quantia}", String.valueOf(dropsAmount))
                );
            }
        }
    }
}
