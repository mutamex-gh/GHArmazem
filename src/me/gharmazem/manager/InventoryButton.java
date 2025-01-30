package me.gharmazem.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * @author Gustavo Henrique
 * @github mutamex-gh
 */

@Getter
@Setter
@AllArgsConstructor
public class InventoryButton {

    private final String id;
    private final ItemStack itemStack;
    private final int slot;
    private final double price;

}
