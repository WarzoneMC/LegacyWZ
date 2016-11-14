package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.BigSpenderAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/*
 * Created by lucas on 3/19/16.
 */
public class BigSpenderKit extends Kit {

    public BigSpenderKit() {
        super("Big Spender", 10000, "Empty your pockets!", Material.GOLD_INGOT);

        super.setItem(0, ItemFactory.createItem(Material.GOLD_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setAbility(2, new BigSpenderAbility(this));
        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));

        ItemStack yellowGlass = new ItemStack(Material.GLASS, 64, DyeColor.YELLOW.getData());
        super.setItem(5, yellowGlass);
        super.setItem(6, ItemFactory.createItem(Material.GOLD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.GOLD_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.GOLD_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.GOLD_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.GOLD_BOOTS));
    }

}
