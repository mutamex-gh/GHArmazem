package me.gharmazem.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class EconomyHook {

    private static Economy econ = null;

    public static void register() {
        setupEconomy();
    }

    private static void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return;
        econ = rsp.getProvider();
    }

    public static Economy getEconomy() {
        return econ;
    }
}
