package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.BuilderAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.Material;

/*
 * Created by Gamer on 12/14/15
 */
public class BuilderKit extends Kit {

	public BuilderKit() {
		super("Builder", 300, "Extra blocks to help build fortifications.", Material.WOOD_STAIRS);

		super.setItem(0, ItemFactory.createItem(Material.IRON_PICKAXE));
		super.setItem(1, ItemFactory.createItem(Material.STONE_AXE));
		super.setItem(2, ItemFactory.createItem(Material.BOW));

		super.setAbility(3, new BuilderAbility(this));
		super.setItem(4, ItemFactory.createItem(Material.GOLDEN_APPLE));
		
		super.setItem(6, ItemFactory.createItem(Material.WOOD, 64));
		super.setItem(7, ItemFactory.createItem(Material.WOOD_STAIRS, 64));
		super.setItem(8, ItemFactory.createItem(Material.LADDER, 16));

		super.setItem(9, ItemFactory.createItem(Material.GLASS, 64));
		super.setItem(10, ItemFactory.createItem(Material.LOG, 64));
		super.setItem(11, ItemFactory.createItem(Material.LOG, 64));

		super.setItem(21, ItemFactory.createItem(Material.ARROW, 64));
		
		
		super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
		super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
		super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.IRON_BOOTS));
	}
	
}
