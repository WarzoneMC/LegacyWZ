package com.minehut.warzone.kit.games.tdm.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import com.minehut.warzone.kit.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by liamlutton on 4/10/16.
 */
public class BackstabberKit extends Kit {

    public BackstabberKit() {
        super("Backstabber", 400, "Double damage from behind!", Material.STONE_SWORD);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));

        ItemStack bow = ItemFactory.createItem(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        super.setItem(1, bow);

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE, 1));

        super.setItem(21, ItemFactory.createItem(Material.ARROW, 1));

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            if(event.getEntity() instanceof Player){
                Player damager = (Player) event.getDamager();
                Player damaged = (Player) event.getEntity();
                if (KitManager.isUsingKit(damager, this)) {
                    double d = Math.abs(damager.getLocation().getYaw() - damaged.getLocation().getYaw());
                    if(d < 30 || d > 330){
                        event.setDamage(event.getDamage()*2);
                    }
                }
            }
        }
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
