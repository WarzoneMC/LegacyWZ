package com.minehut.warzone.kit.games.tdm.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.KitManager;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by liamlutton on 4/10/16.
 */
public class SniperKit extends Kit {

    public SniperKit() {
        super("Sniper", 750, "Deal extra bow damage with distance!", Material.BOW);

        super.setItem(0, ItemFactory.createItem(Material.WOOD_SWORD));

        ItemStack bow = ItemFactory.createItem(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        super.setItem(1, bow);

        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE, 1));

        super.setItem(21, ItemFactory.createItem(Material.ARROW, 1));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.CHAINMAIL_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.CHAINMAIL_LEGGINGS));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Arrow){
            if(event.getEntity() instanceof Player){
                Arrow a = (Arrow) event.getDamager();
                Player damaged = (Player) event.getEntity();
                if(a.getShooter() instanceof Player){
                    Player shooter = (Player) a.getShooter();
                    if(KitManager.isUsingKit(shooter, this)){
                        double add = 0;
                        add = damaged.getLocation().distance(shooter.getLocation())/5;
                        if(add > 8)add=8;
                        event.setDamage(event.getDamage() + add);
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
