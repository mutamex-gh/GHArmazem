package me.gharmazem.manager;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class InventoryButton {

    private String id;
    private ItemStack itemStack;
    private Enchantment enchantment;
    private int enchantLevel;
    private int slot;
    private int xp;
    private double price;

    public InventoryButton(String id, ItemStack itemStack, int slot, double price) {
        this.id = id;
        this.itemStack = itemStack;
        //this.enchantment = enchantment;
        //this.enchantLevel = enchantLevel;
        this.slot = slot;
        //this.xp = xp;
        this.price = price;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public int getEnchantLevel() {
        return this.enchantLevel;
    }

    public void setEnchantLevel(int enchantLevel) {
        this.enchantLevel = enchantLevel;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
