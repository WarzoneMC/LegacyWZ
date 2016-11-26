package com.minehut.warzone.kit.games.infected.infected.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by luke on 10/17/15.
 */
public class ZombieKit extends Kit {

    public ZombieKit() {
        super("Zombie", 0, "Zombie", Material.ROTTEN_FLESH);
        super.setTeamId("infected");
        super.setEntityType(EntityType.ZOMBIE);

        ItemStack sword = ItemFactory.createItem(Material.STONE_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
        super.setItem(0, sword);


        //armor --------------------
        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));


        ItemStack chestplate = ItemFactory.createItem(Material.LEATHER_CHESTPLATE);
//        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        super.setItem(SlotType.CHESTPLATE.slot, chestplate);

        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
    }

    @Override
    public void extraUnload() {

    }
}
