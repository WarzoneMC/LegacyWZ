package com.minehut.warzone.kit.games.infected.infected.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import com.minehut.warzone.kit.games.infected.infected.abilities.CreeperAbility;
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
public class CreeperKit extends Kit {

    public CreeperKit() {
        super("Creeper", 450, "Creeper", Material.SULPHUR);
        super.setTeamId("infected");
        super.setEntityType(EntityType.CREEPER);

        ItemStack sword = ItemFactory.createItem(Material.STONE_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        super.setItem(0, sword);

        CreeperAbility ability = new CreeperAbility(this);
        super.setAbility(2, ability);

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.GOLD_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
    }

    @Override
    public void extraUnload() {

    }
}
