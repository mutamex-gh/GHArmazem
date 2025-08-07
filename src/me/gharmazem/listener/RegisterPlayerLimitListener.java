package me.gharmazem.listener;

import me.gharmazem.configuration.ConfigLimit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RegisterPlayerLimitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String p1 = "limits." + player.getName() + ".tier";
        ConfigLimit.getLimitConfig().set(p1, "tier1");

        ConfigLimit.saveLimitConfig();
    }
}
