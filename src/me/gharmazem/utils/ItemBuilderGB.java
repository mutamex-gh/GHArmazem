package me.gharmazem.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilderGB implements Cloneable {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilderGB(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilderGB(Material material, int amount, int data) {
        this.itemStack = new ItemStack(material, amount, (byte) data);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilderGB(ItemStack otherItem) {
        this.itemStack = otherItem;
        this.itemMeta = otherItem.getItemMeta();
    }

    public ItemBuilderGB(ItemStack itemStack, ItemMeta itemMeta) {
        this.itemStack = itemStack;
        this.itemMeta = itemMeta;
    }

    public ItemBuilderGB itemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemBuilderGB itemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilderGB material(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemBuilderGB name(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilderGB amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilderGB lore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilderGB lore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilderGB loreLine(String... line) {
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.addAll(Arrays.asList(line));
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilderGB loreLineIf(boolean condition, String... line) {
        if (condition) {
            loreLine(line);
        }
        return this;
    }

    public ItemBuilderGB durability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilderGB data(MaterialData materialData) {
        itemStack.setData(materialData);
        return this;
    }

    public ItemBuilderGB acceptItemStack(Consumer<ItemStack> consumer) {
        consumer.accept(itemStack);
        return this;
    }

    public ItemBuilderGB acceptItemMeta(Consumer<ItemMeta> consumer) {
        consumer.accept(itemMeta);
        return this;
    }

    public ItemBuilderGB enchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilderGB glow(boolean b) {
        if (b) {
            enchantment(Enchantment.ARROW_DAMAGE, 1);
            hideEnchantments();
        }
        return this;
    }

    public ItemBuilderGB glow() {
        enchantment(Enchantment.ARROW_DAMAGE, 1);
        hideEnchantments();
        return this;
    }

    public ItemBuilderGB addFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilderGB hideEnchantments() {
        addFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilderGB skullOwner(String owner) {
        SkullMeta skull = (SkullMeta) itemMeta;
        itemStack.setDurability((short) 3);
        skull.setOwner(owner);
        return this;
    }

    /*
        SkullTexture corrigido para suportar
        as URLS.
     */
    public ItemBuilderGB skullTexture(String textureUrl) {
        if (textureUrl == null || textureUrl.isEmpty()) return this;

        if (!textureUrl.startsWith("https://textures.minecraft.net/texture/")) {
            textureUrl = "https://textures.minecraft.net/texture/" + textureUrl;
        }

        if (!(this.itemMeta instanceof SkullMeta)) return this;
        SkullMeta skullMeta = (SkullMeta) this.itemMeta;

        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(textureUrl.getBytes()), null);
        profile.getProperties().put("textures", new Property(
                "textures",
                new String(Base64.encodeBase64((
                        "{\"textures\":{\"SKIN\":{\"url\":\"" + textureUrl + "\"}}}"
                ).getBytes()))
        ));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.itemMeta = skullMeta;

        return this;
    }

    public ItemBuilderGB clone() {
        return new ItemBuilderGB(itemStack.clone(), itemMeta.clone());
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
