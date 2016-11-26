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
public class AssassinKit extends Kit {

    public AssassinKit() {
        super("Assassin", 300, "Glass Cannon Damage", Material.DIAMOND_SWORD);

        super.setItem(0, ItemFactory.createItem(Material.DIAMOND_SWORD));

        super.setItem(1, ItemFactory.createItem(Material.WOOD, 16));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.IRON_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
