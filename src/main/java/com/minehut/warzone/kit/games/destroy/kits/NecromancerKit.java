package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.NecromancerAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/*
 * Created by lucas on 3/19/16.
 */
public class NecromancerKit extends Kit {

    public NecromancerKit() {
        super("Necromancer", 1300, "Raise the dead to fight alongside you!", Material.BONE);

        ItemStack sword = ItemFactory.createItem(Material.WOOD_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        super.setItem(0, sword);
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setAbility(2, new NecromancerAbility(this));

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.SKULL_ITEM));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.IRON_BOOTS));
    }

}
