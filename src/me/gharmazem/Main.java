package me.gharmazem;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.val;
import me.gharmazem.commands.Commands;
import me.gharmazem.listener.*;
import me.gharmazem.listener.PlotSquared.PSBlockBreak;
import me.gharmazem.manager.mapper.BlockDropMapper;
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
import java.util.logging.Level;

public class Main extends JavaPlugin {

    @Getter private List<Material> allowedItems;
    private File dbaseFile;
    private FileConfiguration dbaseConfig;
    public static Economy econ = null;

    @Override
    public void onEnable() {
        try {
            val loadTime = Stopwatch.createStarted();
            saveDefaultConfig();
            setupDatabaseFile();

            setupEconomy();
            loadItemPrices();
            loadAllowedItems();

            loadListener();
            loadCommands();

            loadTime.stop();
            getLogger().log(Level.INFO, "Plugin inicializado com sucesso ({0})", loadTime);
        }catch (Throwable t) {
            t.printStackTrace();
            getLogger().severe("GHArmazem não foi inicializado devido um erro!");
        }
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
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        dbaseConfig = YamlConfiguration.loadConfiguration(dbaseFile);
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

                Material blockType = BlockDropMapper.getDrop(material);
                if (blockType != null) {
                    allowedItems.add(blockType);
                }
            } catch (Exception ignore) {
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

                    Material blockType = BlockDropMapper.getDrop(material);
                    if (blockType != null) {
                        itemPrices.put(blockType, price);
                    }
                }
            }
        }
        return itemPrices;
    }

    public void loadListener() {
        Bukkit.getPluginManager().registerEvents(new InventoryEvents(), Main.this);
        Bukkit.getPluginManager().registerEvents(new SellRecoverEvent(), Main.this);
        Bukkit.getPluginManager().registerEvents(new SellAllEvent(), Main.this);
        Bukkit.getPluginManager().registerEvents(new PSBlockBreak(), Main.this);
    }

    public void loadCommands() {
        getCommand("armazem").setExecutor(new Commands());
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }
}
