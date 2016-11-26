package com.minehut.warzone.kit;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.util.ArmorType;
import com.minehut.warzone.util.ColorConverter;
import com.minehut.warzone.util.itemstack.Unbreakable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luke on 10/17/15.
 */
public abstract class Kit implements Listener {
    public String name;
    public int price;
    public String description;
    public Material material;

    public String teamId = "";
    public boolean selectable = true;

    public ItemStack offHand = new ItemStack(Material.AIR);

    public EntityType entityType = null;

    public boolean unbreaking = true;

    public HashMap<Integer, ItemStack> items = new HashMap<>();
    public List<Ability> abilities = new ArrayList<>();

    public Kit(String name, int price, String description, Material material) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.material = material;
        enable();
    }

    public void setItem(int slot, ItemStack itemStack) {
        this.items.put(slot, itemStack);
    }

    public void setOffHand(ItemStack itemStack){
        Unbreakable.setUnbreakable(itemStack);
        this.offHand = itemStack;
    }

    public void setAbility(int slot, Ability ability) {
        this.abilities.add(ability);
        this.setItem(slot, ability.itemStack);
    }

    /* Chat Color is used to match ownerTeam color */
    public void apply(Player player, ChatColor color) {
        player.getInventory().setItemInOffHand(offHand);
        for (int slot : this.items.keySet()) {
            ItemStack item = this.items.get(slot);

            if (unbreaking) {
                Unbreakable.setUnbreakable(item);
            }

            if (ArmorType.getArmorType(item) == ArmorType.helmet) {

                if (item.getType() == Material.LEATHER_HELMET) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(ColorConverter.getColor(color));
                    item.setItemMeta(meta);
                }

                player.getInventory().setHelmet(item);
            } else if (ArmorType.getArmorType(item) == ArmorType.chestplate) {

                if (item.getType() == Material.LEATHER_CHESTPLATE) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(ColorConverter.getColor(color));
                    item.setItemMeta(meta);
                }

                player.getInventory().setChestplate(item);
            } else if (ArmorType.getArmorType(item) == ArmorType.leggings) {

                if (item.getType() == Material.LEATHER_LEGGINGS) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(ColorConverter.getColor(color));
                    item.setItemMeta(meta);
                }

                player.getInventory().setLeggings(item);
            } else if (ArmorType.getArmorType(item) == ArmorType.boots) {

                if (item.getType() == Material.LEATHER_BOOTS) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                    meta.setColor(ColorConverter.getColor(color));
                    item.setItemMeta(meta);
                }

                player.getInventory().setBoots(item);
            } else {
                player.getInventory().setItem(slot, item);
            }
        }

//        if(entityType == null) {
//            DisguiseAPI.undisguiseToAll(player);
//        } else {
//            MobDisguise mobDisguise = new MobDisguise(entityType, true);
//            mobDisguise.setViewSelfDisguise(false);
//            DisguiseAPI.disguiseToAll(player, mobDisguise);
//        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                onApply(player, color);
            }
        });
    }

    public void onApply(Player player, ChatColor chatColor) {

    }

    public void enable() {
        Warzone.getInstance().getServer().getPluginManager().registerEvents(this, Warzone.getInstance());

        this.abilities.forEach(Ability::enable);

        this.extraEnable();
    }

    public void unload() {
        HandlerList.unregisterAll(this);

        this.abilities.forEach(Ability::unload);

        this.extraUnload();
    }

    /*
     ***********************************
     *
     * Possible Override Methods
     *
     ***********************************
     */

    public void extraEnable() {

    }

    public void extraUnload() {

    }
    
    /*
     ***********************************
     *
     * Getters
     *
     ***********************************
     */
    
    public String getName(){
    	return this.name;
    }

    public int getCost(){
    	return this.price;
    }
    
    public String getDescription(){
    	return this.description;
    }
    
    public Material getMaterial(){
    	return this.material;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
