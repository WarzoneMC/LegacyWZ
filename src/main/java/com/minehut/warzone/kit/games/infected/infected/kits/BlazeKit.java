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
 * Created by liamlutton on 4/15/16.
 */
public class BlazeKit extends Kit {

    public BlazeKit() {
        super("Blaze", 450, "Blaze", Material.BLAZE_ROD);
        super.setTeamId("infected");
        super.setEntityType(EntityType.BLAZE);

        ItemStack sword = ItemFactory.createItem(Material.GOLD_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        super.setItem(0, sword);

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.GOLD_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false));

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 2, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, false, false));
    }

    @Override
    public void extraUnload() {

    }
}
