package com.minehut.warzone.kit.games.infected.infected.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
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
 * Created by liamlutton on 4/15/16.
 */
public class SkeletonKit extends Kit {

    public SkeletonKit() {
        super("Skeleton", 350, "Skeleton", Material.BONE);
        super.setTeamId("infected");
        super.setEntityType(EntityType.SKELETON);

        ItemStack sword = ItemFactory.createItem(Material.WOOD_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        super.setItem(0, sword);

        ItemStack bow = ItemFactory.createItem(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        super.setItem(1, bow);

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 1));

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.CHAINMAIL_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false));

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 2, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, false, false));
    }

    @Override
    public void extraUnload() {

    }
}
