package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.SeekingArcherAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.Material;

/*
 * Created by Lucas on 3/31/16.
 */
public class SeekingArcherKit extends Kit {

    public SeekingArcherKit() {
        super("Seeking Archer", 900, "Fire heat-seeking arrows at your enemies!", Material.SPECTRAL_ARROW);

        super.setItem(0, ItemFactory.createItem(Material.WOOD_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setAbility(2, new SeekingArcherAbility(this));

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.GOLD_BOOTS));
    }

}
