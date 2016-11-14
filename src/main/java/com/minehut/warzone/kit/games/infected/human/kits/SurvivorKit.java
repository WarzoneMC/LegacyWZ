package com.minehut.warzone.kit.games.infected.human.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by luke on 10/17/15.
 */
public class SurvivorKit extends Kit {

    public SurvivorKit() {
        super("Survivor", 0, "Typical Apocalypse Survivor", Material.IRON_SWORD);
        super.setTeamId("humans");

        ItemStack sword = ItemFactory.createItem(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
        super.setItem(0, sword);

        ItemStack bow = ItemFactory.createItem(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
        super.setItem(1, bow);

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 10));


        //armor --------------------
        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.CHAINMAIL_HELMET));

        ItemStack chestplate = ItemFactory.createItem(Material.LEATHER_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        super.setItem(SlotType.CHESTPLATE.slot, chestplate);

        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.CHAINMAIL_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.IRON_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
