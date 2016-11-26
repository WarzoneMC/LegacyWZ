package com.minehut.warzone.kit.games.blitz.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.BarbarianAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by luke on 10/17/15.
 */
public class BlitzKit extends Kit {

    public BlitzKit() {
        super("Blitz", 0, "Default Blitz Kit", Material.STONE_SWORD);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 1));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
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
