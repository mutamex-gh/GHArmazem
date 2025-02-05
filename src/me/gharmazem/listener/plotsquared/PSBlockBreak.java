package me.gharmazem.listener.plotsquared;

import com.intellectualcrafters.plot.api.PlotAPI;
import me.gharmazem.Main;
import me.gharmazem.configuration.ConfigValues;
import me.gharmazem.manager.BonusManager;
import me.gharmazem.manager.enums.BlockDropMapper;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class PSBlockBreak implements Listener {

    @EventHandler
    public void onBreakBlocksInPlot(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        PlotAPI plotAPI = new PlotAPI();

        boolean isEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.enable");
        boolean replantEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.replant");
        List<String> toolToBreak = Main.getInstance().getConfig().getStringList("PlotSquaredSupport.tool-to-break");

        if (!isEnable) return;
        if (plotAPI.getPlot(block.getLocation()) == null) return;
        if (!plotAPI.getPlot(block.getLocation()).isOwner(player.getUniqueId())) return;
        if (block.getType() == Material.CACTUS) event.setCancelled(true);

        if (toolToBreak.contains(player.getItemInHand().getType().name()) &&
                ConfigValues.getAllowedItems().contains(block.getType())) {

            Material blockMapperType = BlockDropMapper.getDrop(block.getType());
            boolean isFullyGrown = UtilClass.isFullyGrown(block);
            int dropsMultiplier = isFullyGrown ? block.getDrops().size() + UtilClass.getFortune(player) : 1;

            if (blockMapperType != null) {
                if (block.getType() == Material.NETHER_WARTS) {
                    int baseDrops = 1;
                    dropsMultiplier = isFullyGrown ? baseDrops + UtilClass.getFortune(player) : 1;
                }
                event.setCancelled(true);

                if (replantEnable) {
                    block.setType(block.getType());

                    if (!isFullyGrown) {
                        return;
                    }
                    BonusManager.setBonus(player, blockMapperType, dropsMultiplier);
                    return;
                }
                BonusManager.setBonus(player, blockMapperType, dropsMultiplier);
                block.setType(Material.AIR);
            }
        }
    }
}

