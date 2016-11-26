package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import com.minehut.warzone.kit.games.destroy.abilities.ThorAbility;
import org.bukkit.Material;

/*
 * Created by lucas on 3/30/16.
 */
public class ThorKit extends Kit {

    public ThorKit() {
        super("Thor", 1350, "Bring the rage of the sky down upon your enemies!", Material.STONE_AXE);

        super.setAbility(0, new ThorAbility(this));
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setItem(2, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.DIAMOND_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
    }

}
