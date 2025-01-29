package me.gharmazem.listener;

import com.intellectualcrafters.plot.api.PlotAPI;
import me.gharmazem.Main;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.manager.mapper.BlockDropMapper;
import me.gharmazem.utils.some.ActionBarUtils;
import me.gharmazem.utils.some.ColorUtil;
import me.gharmazem.utils.some.UtilClass;
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

        String actionBarStore = Main.getInstance().getConfig().getString("Messages.actionbar-store");
        boolean isEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.enable");
        double multiplier = Main.getInstance().getConfig().getDouble("PlotSquaredSupport.multiplier");

        if (isEnable) {
            if (plotAPI.getPlot(block.getLocation()).isOwner(player.getUniqueId())) {
                if (Main.getInstance().getAllowedItems().contains(block.getType())) {

                    Material blockMapperType = BlockDropMapper.getDrop(block.getType());
                    boolean isFullyGrown = UtilClass.isFullyGrown(block);

                    double dropsMultiplier = isFullyGrown ?
                            Math.floor(block.getDrops().size() * UtilClass.getFortune(player) * multiplier + 1) : 1;

                    if (blockMapperType != null) {
                        BaseManager.storeSpecifyItem(player, blockMapperType, (int)dropsMultiplier);

                        ActionBarUtils.sendActionBar(
                                player,
                                ColorUtil.colored(actionBarStore)
                                        .replace("{quantia}", String.valueOf((int)dropsMultiplier))
                        );

                        event.setCancelled(true);
                        block.setType(Material.AIR);
                        return;
                    }
                    BaseManager.storeSpecifyItem(player, block.getType(), (int)dropsMultiplier);

                    ActionBarUtils.sendActionBar(
                            player,
                            ColorUtil.colored(actionBarStore)
                                    .replace("{quantia}", String.valueOf((int)dropsMultiplier))
                    );

                    event.setCancelled(true);
                    block.setType(Material.AIR);
                }
            }
        }
    }
}
