package com.minehut.warzone.kit.games.infected.infected.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.infected.infected.abilities.EndermanAbility;
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
 * Created by lucas on 4/17/16.
 */
public class EndermanKit extends Kit {

    public EndermanKit() {
        super("Enderman", 600, "Harness the power of the end to teleport short distances", Material.EYE_OF_ENDER);
        super.setTeamId("infected");
        super.setEntityType(EntityType.ENDERMAN);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));

        ItemStack bow = ItemFactory.createItem(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        super.setItem(1, bow);

        super.setAbility(3, new EndermanAbility(this));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 10));

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.IRON_CHESTPLATE));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
    }

}
