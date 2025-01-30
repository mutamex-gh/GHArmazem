package me.gharmazem.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * @author Gustavo Henrique
 * @github mutamex-gh
 */

@Getter
@Setter
public class InventoryButton {

    private final String id;
    private final ItemStack itemStack;
    private final int slot;
    private final double price;

    public InventoryButton(String id, ItemStack itemStack, int slot, double price) {
        this.id = id;
        this.itemStack = itemStack;
        this.slot = slot;
        this.price = price;
    }

}
