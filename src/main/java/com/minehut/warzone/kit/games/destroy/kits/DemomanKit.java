package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.DemomanAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DemomanKit extends Kit {

    public DemomanKit() {
    	
    	/*
    	 * Don't you think that may be a bit too high of a price? I don't know.
    	 * Seems too much to me for a kit that isn't that good.
    	 */
    	
        super("Demoman", 700, "Throw grenades to blow up towers and players!", Material.TNT);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.BOW));


        DemomanAbility demomanAbility = new DemomanAbility(this);
        super.setAbility(2, demomanAbility);

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.IRON_CHESTPLATE));
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
