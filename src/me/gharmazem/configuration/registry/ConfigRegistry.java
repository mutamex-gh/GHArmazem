package me.gharmazem.configuration.registry;

import me.gharmazem.configuration.ConfigValues;

public class ConfigRegistry {

    public static void register() {
        ConfigValues.loadItemPrices();
        ConfigValues.loadAllowedItems();
    }
}
