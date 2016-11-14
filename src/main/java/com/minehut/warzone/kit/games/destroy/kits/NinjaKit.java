package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.destroy.abilities.NinjaAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by lucas on 4/28/16.
 */
public class NinjaKit extends Kit {

    public NinjaKit() {
        super("Ninja", 800, "Don't need armor when you can't get hit!", Material.FLINT);

        super.setItem(0, ItemFactory.createItem(Material.WOOD_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setAbility(3, new NinjaAbility(this));

        super.setItem(6, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(7, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
    }

}
