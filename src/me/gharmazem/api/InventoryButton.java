package me.gharmazem.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InventoryButton {

    private final String id;
    private final ItemStack itemStack;
    private final List<String> lore;
    private final int slot;
    private final double price;

}
