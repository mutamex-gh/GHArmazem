package me.gharmazem.listener.plotsquared;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import me.gharmazem.Main;
import me.gharmazem.manager.BonusManager;
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
        boolean isPSSupportEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.enable");
        boolean isCactusFarmEnable = Main.getInstance().getConfig().getBoolean("PlotSquaredSupport.enable-cactus");

        Item entity = event.getEntity();

        if (entity.getItemStack().getType() != Material.CACTUS) return;

        if (isPSSupportEnable && isCactusFarmEnable) {
            final Plot plotAPI = new PlotAPI().getPlot(entity.getLocation());
            if (plotAPI == null) return;
            final OfflinePlayer player = Bukkit.getOfflinePlayer(plotAPI.getOwners().stream().findFirst().get());

            if (!player.isOnline()) return;
            if (!plotAPI.hasOwner()) return;
            if (!plotAPI.isOwner(player.getUniqueId())) return;

            BonusManager.setBonus((Player) player, Material.CACTUS, 1);
            event.getEntity().remove();
        }
    }
}
