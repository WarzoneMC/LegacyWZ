package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.MedicAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MedicKit extends Kit {

    public MedicKit() {
    	super("Medic", 600, "Heal yourself and others!", Material.GOLDEN_APPLE);

        super.setItem(0, ItemFactory.createItem(Material.STONE_AXE));
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        MedicAbility medicAbility = new MedicAbility(this);
        super.setAbility(2, medicAbility);

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        
        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));
        
        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.GOLD_CHESTPLATE));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));

    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
	
}
