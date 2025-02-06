package me.gharmazem.configuration.registry;

import lombok.Data;
import me.gharmazem.configuration.ConfigValues;

public class ConfigRegistry {

    public static void register() {
        ConfigValues.loadItemPrices();
        ConfigValues.loadAllowedItems();
    }
}
