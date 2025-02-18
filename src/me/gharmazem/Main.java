package me.gharmazem;

import com.google.common.base.Stopwatch;
import lombok.val;
import me.gharmazem.api.metrics.MetricsProvider;
import me.gharmazem.commands.Commands;
import me.gharmazem.configuration.registry.ConfigRegistry;
import me.gharmazem.hook.EconomyHook;
import me.gharmazem.listener.*;
import me.gharmazem.listener.plotsquared.PSBlockBreak;
import me.gharmazem.listener.plotsquared.PSItemSpawn;
import me.gharmazem.manager.BonusManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

import static me.gharmazem.configuration.ConfigDBase.setupDatabaseFile;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            val loadTime = Stopwatch.createStarted();
            saveDefaultConfig();
            setupDatabaseFile();

            MetricsProvider.of(this).register();
            ConfigRegistry.register();
            BonusManager.register();
            EconomyHook.register();

            loadListener();
            loadCommands();

            loadTime.stop();
            getLogger().info("@Discord: mutamex");
            getLogger().info("@Author: mutamex-gh");
            getLogger().info("@Github: github.com/mutamex-gh/GHArmazem");
            getLogger().log(Level.INFO, "Plugin inicializado com sucesso ({0})", loadTime);
        }catch (Throwable t) {
            getLogger().severe("Plugin nao inicializado devido a um erro!");
            t.printStackTrace();
        }
    }

    public void loadListener() {
        Bukkit.getPluginManager().registerEvents(new InventoriesListener(), Main.this);
        Bukkit.getPluginManager().registerEvents(new SellRecoverListener(), Main.this);
        Bukkit.getPluginManager().registerEvents(new SellAllListener(), Main.this);
        Bukkit.getPluginManager().registerEvents(new PSBlockBreak(), Main.this);
        Bukkit.getPluginManager().registerEvents(new PSItemSpawn(), Main.this);
    }

    public void loadCommands() {
        getCommand("armazem").setExecutor(new Commands());
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }
}