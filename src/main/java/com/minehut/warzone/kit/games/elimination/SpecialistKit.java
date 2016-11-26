package com.minehut.warzone.kit.games.elimination;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by luke on 10/17/15.
 */
public class SpecialistKit extends Kit {

    public SpecialistKit() {
        super("Specialist", 300, "Utility Kit", Material.WATER_BUCKET);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setItem(2, ItemFactory.createItem(Material.WOOD, 32));
        super.setItem(3, ItemFactory.createItem(Material.WATER_BUCKET, 1));
        super.setItem(4, ItemFactory.createItem(Material.LADDER, 3));


        super.setItem(8, ItemFactory.createItem(Material.ARROW, 12));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.GOLD_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
