package me.gharmazem.configuration;

import me.gharmazem.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigDBase {

    private static File dbaseFile;
    private static FileConfiguration dbaseConfig;

    public static void setupDatabaseFile() {
        if (!Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdirs();
        }
        dbaseFile = new File(Main.getInstance().getDataFolder(), "dbase.yml");
        if (!dbaseFile.exists()) {
            try {
                if (dbaseFile.createNewFile()) {
                    Main.getInstance().getLogger().info("Arquivo dbase.yml criado com sucesso!");
                }
            }catch (IOException e) {
                Main.getInstance().getLogger().severe("Parece que ocorreu um erro não foi criado a dbase.yml!");
                e.printStackTrace();
            }
        }
        dbaseConfig = YamlConfiguration.loadConfiguration(dbaseFile);
    }

    public static FileConfiguration getDatabaseConfig() {
        if (dbaseConfig == null) {
            setupDatabaseFile();
        }
        return dbaseConfig;
    }

    public static void saveDatabaseConfig() {
        if (dbaseConfig == null || dbaseFile == null) {
            Main.getInstance().getLogger().severe("Erro: dbase.yml não foi inicializado corretamente!");
            return;
        }
        try {
            dbaseConfig.save(dbaseFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Parece que ocorreu um erro ao salvar a dbase.yml!");
            e.printStackTrace();
        }
    }
}
