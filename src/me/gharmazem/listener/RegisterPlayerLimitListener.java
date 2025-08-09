package me.gharmazem.listener;

import lombok.val;
import me.gharmazem.configuration.ConfigLimit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RegisterPlayerLimitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        val limitSection = ConfigLimit.getLimitConfig().getConfigurationSection("limits");

        if(!limitSection.contains(player.getName())) {
            val p1 = "limits." + player.getName() + ".tier";
            val p2 = "limits." + player.getName() + ".additional-limit";

            ConfigLimit.getLimitConfig().set(p1, "tier1");
            ConfigLimit.getLimitConfig().set(p2, 0);

            ConfigLimit.saveLimitConfig();
        }
    }
}
