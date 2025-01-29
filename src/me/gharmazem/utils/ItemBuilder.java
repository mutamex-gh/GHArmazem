package me.gharmazem.utils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import me.gharmazem.utils.some.ColorUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material type) {
        this(new ItemStack(type));
    }

    public ItemBuilder(Material type, int quantity, int data) {
        this(new ItemStack(type, quantity, (short)data));
    }

    public ItemBuilder(String name) {
        this.item = new ItemStack(Material.getMaterial(397), 1, (short)3);
        SkullMeta meta = (SkullMeta)this.item.getItemMeta();
        meta.setOwner(name);
        this.item.setItemMeta((ItemMeta)meta);
    }

    public ItemBuilder changeItemMeta(Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = this.item.getItemMeta();
        consumer.accept(itemMeta);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder changeItem(Consumer<ItemStack> consumer) {
        consumer.accept(this.item);
        return this;
    }

    public ItemBuilder name(String name) {
        return changeItemMeta(it -> it.setDisplayName(ColorUtil.colored(name)));
    }

    public ItemBuilder setLore(String... lore) {
        return changeItemMeta(it -> it.setLore(Arrays.asList(ColorUtil.colored(lore))));
    }

    public ItemBuilder setLore(List<String> lore) {
        return changeItemMeta(it -> it.setLore(lore));
    }

    public ItemBuilder addLore(List<String> lore) {
        if (lore == null || lore.isEmpty())
            return this;
        return changeItemMeta(meta -> {
            List<String> list = meta.getLore();
            list.addAll(lore);
            meta.setLore(list);
        });
    }

    public ItemBuilder setNBTTag(String key, String value) {
        try {
            Object nmsCopy = NMSReflect.getCraftBukkitClass("inventory", "CraftItemStack").getMethod("asNMSCopy", new Class[] { ItemStack.class }).invoke(null, new Object[] { this.item });
            Object nbtTagCompound = NMSReflect.getNMSClass("NBTTagCompound").getConstructor(new Class[0]).newInstance(new Object[0]);
            boolean b = (nmsCopy.getClass().getMethod("getTag", new Class[0]).invoke(nmsCopy, new Object[0]) != null);
            Object nbtTag = b ? nmsCopy.getClass().getMethod("getTag", new Class[0]).invoke(nmsCopy, new Object[0]) : nbtTagCompound;
            Constructor nbsString = NMSReflect.getNMSClass("NBTTagString").getConstructor(new Class[] { String.class });
            nbtTag.getClass().getMethod("set", new Class[] { String.class, NMSReflect.getNMSClass("NBTBase") }).invoke(nbtTag, new Object[] { key, nbsString.newInstance(new Object[] { value }) });
            nmsCopy.getClass().getMethod("setTag", new Class[] { NMSReflect.getNMSClass("NBTTagCompound") }).invoke(nmsCopy, new Object[] { nbtTag });
            this
                    .item = (ItemStack)NMSReflect.getCraftBukkitClass("inventory", "CraftItemStack").getMethod("asBukkitCopy", new Class[] { NMSReflect.getNMSClass("ItemStack") }).invoke(null, new Object[] { nmsCopy });
        } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|InstantiationException e) {
            e.printStackTrace();
        }
        return this;
    }

    public static String getNBTTag(ItemStack item, String key) {
        try {
            Object nmsCopy = NMSReflect.getCraftBukkitClass("inventory", "CraftItemStack").getMethod("asNMSCopy", new Class[] { ItemStack.class }).invoke(null, new Object[] { item });
            if (nmsCopy.getClass().getMethod("getTag", new Class[0]).invoke(nmsCopy, new Object[0]) != null) {
                Object tagCompound = nmsCopy.getClass().getMethod("getTag", new Class[0]).invoke(nmsCopy, new Object[0]);
                return (String)tagCompound.getClass().getMethod("getString", new Class[] { String.class }).invoke(tagCompound, new Object[] { key });
            }
        } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException|NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemStack wrap() {
        return this.item;
    }
}
