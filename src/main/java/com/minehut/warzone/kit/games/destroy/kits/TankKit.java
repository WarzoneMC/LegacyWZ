package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.TankAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by luke on 10/17/15.
 */
public class TankKit extends Kit {

    public TankKit() {
        super("Tank", 200, "Toss your enemies up!", Material.DIAMOND_CHESTPLATE);

        ItemStack sword = ItemFactory.createItem(Material.WOOD_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        super.setItem(0, sword);
        super.setItem(1, ItemFactory.createItem(Material.BOW));
        
        TankAbility tankAbility = new TankAbility(this);
        super.setAbility(2, tankAbility);

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.DIAMOND_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.IRON_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
