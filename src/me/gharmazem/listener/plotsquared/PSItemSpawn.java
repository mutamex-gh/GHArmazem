package me.gharmazem.listener.plotsquared;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import lombok.val;
import me.gharmazem.Main;
import me.gharmazem.manager.BaseManager;
import me.gharmazem.manager.BonusManager;
import me.gharmazem.manager.LimitManager;
import me.gharmazem.utils.ActionBarUtils;
import me.gharmazem.utils.ColorUtil;
import me.gharmazem.utils.UtilClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class PSItemSpawn implements Listener {

    @EventHandler
    public void onGrowth(ItemSpawnEvent event) {
        val config = Main.getInstance().getConfig();
        val isPSSupportEnable = config.getBoolean("PlotSquaredSupport.enable");
        val isCactusFarmEnable = config.getBoolean("PlotSquaredSupport.enable-cactus");

        Item entity = event.getEntity();
        LimitManager limitManager = new LimitManager();

        if (entity.getItemStack().getType() != Material.CACTUS) return;

        if (isPSSupportEnable && isCactusFarmEnable) {
            final Plot plotAPI = new PlotAPI().getPlot(entity.getLocation());
            if (plotAPI == null) return;
            final OfflinePlayer player = Bukkit.getOfflinePlayer(plotAPI.getOwners().stream().findFirst().get());

            if (BaseManager.getAllStored((Player) player) >= limitManager.getLimit((Player) player)) {
                event.getEntity().remove();
                return;
            }

            if (!player.isOnline() || !plotAPI.hasOwner() || !plotAPI.isOwner(player.getUniqueId())) return;

            BonusManager.setBonus((Player) player, Material.CACTUS, entity.getItemStack().getAmount());
            event.getEntity().remove();
        }
    }
}
