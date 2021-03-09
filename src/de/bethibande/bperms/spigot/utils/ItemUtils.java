package de.bethibande.bperms.spigot.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemUtils {

    public static ItemStack[] materialToItem(Material... materials) {
        return Arrays.stream(materials).map(m -> m != null ? new ItemStack(m): null).collect(Collectors.toList()).toArray(new ItemStack[materials.length]);
    }

}
