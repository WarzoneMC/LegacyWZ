package com.minehut.warzone.util;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Luke on 11/26/14.
 */
public enum ArmorType {
    helmet, chestplate, leggings, boots;

    public static ArmorType getArmorType(ItemStack itemStack) {
        Material material = itemStack.getType();

        if (material == Material.LEATHER_HELMET || material == Material.CHAINMAIL_HELMET || material == Material.IRON_HELMET || material == Material.GOLD_HELMET || material == Material.DIAMOND_HELMET) {
            return ArmorType.helmet;
        } else if (material == Material.LEATHER_CHESTPLATE || material == Material.CHAINMAIL_CHESTPLATE || material == Material.IRON_CHESTPLATE || material == Material.GOLD_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE) {
            return ArmorType.chestplate;
        } else if (material == Material.LEATHER_LEGGINGS || material == Material.CHAINMAIL_LEGGINGS || material == Material.IRON_LEGGINGS || material == Material.GOLD_LEGGINGS || material == Material.DIAMOND_LEGGINGS) {
            return ArmorType.leggings;
        } else if (material == Material.LEATHER_BOOTS || material == Material.CHAINMAIL_BOOTS || material == Material.IRON_BOOTS || material == Material.GOLD_BOOTS || material == Material.DIAMOND_BOOTS) {
            return ArmorType.boots;
        } else {
            return null;
        }
    }

    public static ArmorType getArmorType(Material material) {
        return getArmorType(ItemFactory.createItem(material));
    }
}
