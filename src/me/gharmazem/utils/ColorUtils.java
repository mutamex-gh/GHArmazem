package me.gharmazem.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

public final class ColorUtils {

    public static String colored(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String[] colored(String... messages) {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = colored(messages[i]);
        }

        return messages;
    }

    public static List<String> colored(List<String> description) {

        return description.stream()
                .map(ColorUtils::colored)
                .collect(Collectors.toList());

    }

}
