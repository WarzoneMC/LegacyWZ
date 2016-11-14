package com.minehut.warzone.kit.games.tdm.kits;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.games.tdm.abilities.TeleporterAbility;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by liamlutton on 4/10/16.
 */
public class TeleporterKit extends Kit {

    public TeleporterKit() {
        super("Teleporter", 600, "Teleport in small leaps!", Material.ENDER_PEARL);

        super.setItem(0, ItemFactory.createItem(Material.WOOD_SWORD));

        ItemStack bow = ItemFactory.createItem(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        super.setItem(1, bow);

        TeleporterAbility teleporterAbility = new TeleporterAbility(this, 2);
        super.setAbility(2, teleporterAbility);

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE, 1));

        super.setItem(21, ItemFactory.createItem(Material.ARROW, 1));

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.IRON_LEGGINGS));
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
