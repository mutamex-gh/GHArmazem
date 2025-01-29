package me.gharmazem.manager;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class InventoryButton {

    private String id;
    private ItemStack itemStack;
    private int slot;
    private double price;

    public InventoryButton(String id, ItemStack itemStack, int slot, double price) {
        this.id = id;
        this.itemStack = itemStack;
        this.slot = slot;
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

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }


    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
