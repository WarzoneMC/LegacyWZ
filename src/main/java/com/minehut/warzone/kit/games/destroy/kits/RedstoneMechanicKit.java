package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.RedstoneMechanicAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.Material;

/*
 * Created by lucas on 3/29/16.
 */
public class RedstoneMechanicKit extends Kit {

    public RedstoneMechanicKit() {
        super("Redstone Mechanic", 700, "Use your redstone torch to set your spawnpoint!", Material.REDSTONE_TORCH_ON);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.BOW));
        super.setItem(2, ItemFactory.createItem(Material.GOLDEN_APPLE));
        super.setAbility(3, new RedstoneMechanicAbility(this));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.IRON_CHESTPLATE));
    }

}
