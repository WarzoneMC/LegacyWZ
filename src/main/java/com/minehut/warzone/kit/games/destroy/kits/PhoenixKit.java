package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import com.minehut.warzone.kit.games.destroy.abilities.PhoenixAbility;
import org.bukkit.Material;

/**
 * Created by Gamer on 12/5/15
 */
public class PhoenixKit extends Kit {

	public PhoenixKit() {
		super("Phoenix", 900, "Fight with the power of the sun!", Material.FIREWORK_CHARGE);
		
		super.setItem(0, ItemFactory.createItem(Material.WOOD_SWORD));
		super.setItem(1, ItemFactory.createItem(Material.BOW));
		
		PhoenixAbility phoenixAbility = new PhoenixAbility(this, 2);
		super.setAbility(2,	phoenixAbility);

		super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));
		
		super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
		super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

		super.setItem(8,  ItemFactory.createItem(Material.ARROW, 64));
		
		super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.CHAINMAIL_HELMET));
		super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
	}
	
}