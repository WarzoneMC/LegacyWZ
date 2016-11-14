package com.minehut.warzone.kit.games.elimination;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by luke on 10/17/15.
 */
public class TankKit extends Kit {

    public TankKit() {
        super("Tank", 300, "Armor Stacked", Material.DIAMOND_CHESTPLATE);

        super.setItem(0, ItemFactory.createItem(Material.WOOD_SWORD));

        super.setItem(1, ItemFactory.createItem(Material.WOOD, 3));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.DIAMOND_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.CHAINMAIL_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.CHAINMAIL_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
