package com.minehut.warzone.kit.games.tdm.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.tdm.abilities.BarbarianAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by luke on 10/17/15.
 */
public class BarbarianKit extends Kit {

    public BarbarianKit() {
        super("Barbarian", 0, "Strike with your sword to deal masses of damage!", Material.IRON_SWORD);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));

        ItemStack bow = ItemFactory.createItem(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        super.setItem(1, bow);

        BarbarianAbility barbarianAbility = new BarbarianAbility(this, 2);
        super.setAbility(2, barbarianAbility);

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE, 1));

        super.setItem(21, ItemFactory.createItem(Material.ARROW, 1));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
