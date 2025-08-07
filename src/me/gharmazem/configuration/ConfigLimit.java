package me.gharmazem.configuration;

import me.gharmazem.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigLimit {

    private static File limitFile;
    private static FileConfiguration limitConfig;

    public static void setupLimitFile() {
        if (!Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdirs();
        }
        limitFile = new File(Main.getInstance().getDataFolder(), "limites.yml");
        if (!limitFile.exists()) {
            try {
                if (limitFile.createNewFile()) {
                    Main.getInstance().getLogger().info("Arquivo limites.yml criado com sucesso!");
                }
            }catch (IOException e) {
                Main.getInstance().getLogger().severe("Parece que ocorreu um erro não foi criado a limites.yml!");
                e.printStackTrace();
            }
        }
        limitConfig = YamlConfiguration.loadConfiguration(limitFile);
    }

    public static FileConfiguration getLimitConfig() {
        if (limitConfig == null) {
            setupLimitFile();
        }
        return limitConfig;
    }

    public static void saveLimitConfig() {
        if (limitConfig == null || limitFile == null) {
            Main.getInstance().getLogger().severe("Erro: limites.yml não foi inicializado corretamente!");
            return;
        }
        try {
            limitConfig.save(limitFile);
        } catch (IOException e) {
            Main.getInstance().getLogger().severe("Parece que ocorreu um erro ao salvar a limites.yml!");
            e.printStackTrace();
        }
    }
}
