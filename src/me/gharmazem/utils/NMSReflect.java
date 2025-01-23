package me.gharmazem.utils;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSReflect {
    private static final String VERSION = Bukkit.getServer().getClass().getName().split("\\.")[3];

    private static final HashMap<String, Class> CLASSES = new HashMap<>();

    public static Class getNMSClass(String name) {
        if (!CLASSES.containsKey(name))
            try {
                CLASSES.put(name, Class.forName("net.minecraft.server." + VERSION + "." + name));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        return CLASSES.get(name);
    }

    public static Class getCraftBukkitClass(String prefix, String name) {
        if (!CLASSES.containsKey(prefix + "." + name))
            try {
                CLASSES.put(prefix + "." + name, Class.forName("org.bukkit.craftbukkit." + VERSION + "." + prefix + "." + name));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        return CLASSES.get(prefix + "." + name);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
        } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
