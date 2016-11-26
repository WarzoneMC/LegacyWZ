package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.games.destroy.abilities.SpyAbility;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import org.bukkit.Material;

/**
 * Created by lucas on 4/28/16.
 */
public class SpyKit extends Kit {

    public SpyKit() {
        super("Spy", 1600, "Keep your friends close, and your enemies closer!", Material.LEATHER_HELMET);

        super.setItem(0, ItemFactory.createItem(Material.WOOD_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.BOW));
        super.setItem(2, ItemFactory.createItem(Material.GOLDEN_APPLE));
        super.setAbility(3, new SpyAbility(this));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));

    }

}
