package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.InfiltratorAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InfiltratorKit extends Kit {

    public InfiltratorKit() {
    	
        super("Infiltrator", 600, "Fastest kit around!", Material.GOLD_AXE);
        
        InfiltratorAbility infiltratorAbility = new InfiltratorAbility(this);
        super.setAbility(0, infiltratorAbility);

        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setItem(2, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(3, ItemFactory.createItem(Material.WOOD, 64));
        
        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));

    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {
    	player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
    }

    @Override
    public void extraUnload() {

    }
	
}
