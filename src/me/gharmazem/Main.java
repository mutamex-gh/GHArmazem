package me.gharmazem;

import me.gharmazem.commands.Commands;
import me.gharmazem.events.BackMenu;
import me.gharmazem.events.InventoryEvents;
import me.gharmazem.events.SellAllEvent;
import me.gharmazem.events.SellRecoverEvent;
import me.gharmazem.utils.ColorUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    private File dbaseFile;
    private FileConfiguration dbaseConfig;
    private List<Material> allowedItems;
    public static Economy econ = null;

    @Override
    public void onEnable() {
        loadAllowedItems();
        loadItemPrices();
        Bukkit.getConsoleSender().sendMessage(ColorUtils.colored("&a[GHArmazem] &7Itens e precos carregados com sucesso!"));
        saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage(ColorUtils.colored("&a[GHArmazem] &7Config carregada com sucesso!"));
        setupDatabaseFile();
        Bukkit.getConsoleSender().sendMessage(ColorUtils.colored("&a[GHArmazem] &7DataBase carregada com sucesso!"));
        setupEconomy();
        Bukkit.getConsoleSender().sendMessage(ColorUtils.colored("&a[GHArmazem] &7Economia carregada com sucesso!"));

        loadListener();
        loadCommands();

        Bukkit.getConsoleSender().sendMessage(ColorUtils.colored("&a[GHArmazem] Carregado comandos e eventos!"));
        Bukkit.getConsoleSender().sendMessage(ColorUtils.colored("&a[GHArmazem] iniciado com sucesso!"));
    }

    @Override
    public void onDisable() {
        saveDatabaseConfig();
    }

    public void setupDatabaseFile() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        dbaseFile = new File(getDataFolder(), "dbase.yml");
        if (!dbaseFile.exists()) {
            try {
                if (dbaseFile.createNewFile()) {
                    getLogger().info("Arquivo dbase.yml criado com sucesso!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dbaseConfig = YamlConfiguration.loadConfiguration(dbaseFile);
    }

    public void reloadDatabaseFile() {
        try{
            dbaseConfig = YamlConfiguration.loadConfiguration(dbaseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getDatabaseConfig() {
        if (dbaseConfig == null) {
            setupDatabaseFile();
        }
        return dbaseConfig;
    }

    public void saveDatabaseConfig() {
        if (dbaseConfig == null || dbaseFile == null) {
            getLogger().severe("Erro: dbase.yml não foi inicializado corretamente!");
            return;
        }
        try {
            dbaseConfig.save(dbaseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return (econ != null);
    }

    public void loadAllowedItems() {
        List<String> itemNames = getConfig().getStringList("Materiais");
        allowedItems = new ArrayList<>();

        for (String itemName : itemNames) {
            try {
                Material material = Material.valueOf(itemName.toUpperCase());
                allowedItems.add(material);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Material inválido no config.yml: " + itemName);
            }
        }
    }

    public static Map<Material, Double> loadItemPrices() {
        Map<Material, Double> itemPrices = new HashMap<>();
        FileConfiguration config = Main.getInstance().getConfig();

        if (config.contains("Prices")) {
            for (String materialName : config.getConfigurationSection("Prices").getKeys(false)) {
                Material material = Material.getMaterial(materialName);
                if (material != null) {
                    double price = config.getDouble("Prices." + materialName);
                    itemPrices.put(material, price);
                }
            }
        }
        return itemPrices;
    }

    public void loadListener() {
        Bukkit.getPluginManager().registerEvents(new InventoryEvents(), Main.this);
        Bukkit.getPluginManager().registerEvents(new SellRecoverEvent(), Main.this);
        Bukkit.getPluginManager().registerEvents(new SellAllEvent(), Main.this);
        Bukkit.getPluginManager().registerEvents(new BackMenu(), Main.this);
    }

    public void loadCommands() {
        getCommand("armazem").setExecutor(new Commands());
    }

    public static Economy getEconomy() {
        return econ;
    }

    public List<Material> getAllowedItems() {
        return allowedItems;
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

}
