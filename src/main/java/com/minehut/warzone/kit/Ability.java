package com.minehut.warzone.kit;

import com.minehut.cloud.bukkit.util.damage.DamageInfo;
import com.minehut.cloud.bukkit.util.parse.TimeUtils;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by luke on 10/19/15.
 */
public abstract class Ability implements Listener {
//    public Kit kit;
    public String name;
    public int cooldown; //ticks
    public ItemStack itemStack;

    private Kit kit;

    public CooldownAlerts cooldownAlerts = new CooldownAlerts();

    HashMap<UUID, Integer> cooldowns = new HashMap<>();
    int runnableID;

    public Ability(Kit kit, String name, ItemStack itemStack, int cooldown) {
        this.kit = kit;
        this.name = name;
        this.itemStack = itemStack;
        this.cooldown = cooldown;
        enable();
    }

    int cooldownRunnable() {
        return Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Warzone.getInstance(), () -> {
            if(cooldowns.isEmpty()) return;

            ArrayList<UUID> remove = new ArrayList<UUID>();
            for (UUID uuid : cooldowns.keySet()) {
                cooldowns.put(uuid, cooldowns.get(uuid) - 1);

                if (cooldowns.get(uuid) <= 0) {
                    remove.add(uuid);
                }
            }

            for (UUID uuid : remove) {
                cooldowns.remove(uuid);
                Player player = Bukkit.getServer().getPlayer(uuid);
                if(player != null) {
                    offCooldownMessage(player);
                }
            }
        }, 0L, 0L);
    }

    /*
    * ************************************************
    *
    * Events
    *
    * ************************************************
    */

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (isAbilityItem(this.itemStack, event.getItemDrop().getItemStack())) {
            if (KitManager.isUsingKit(event.getPlayer(), this.kit)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot drop ability items!");
            }
        }
    }

    @EventHandler
    public void onAbilityInteractEvent(PlayerInteractEvent event) {
            if(isAbilityItem(this.itemStack, event.getItem())) {
                if(KitManager.isUsingKit(event.getPlayer(), this.kit)) {
                    if (this.isOffCooldown(event.getPlayer())) {

                        if (this.isOffCooldown(event.getPlayer())) {
                            /* Air Clicks */
                            if (event.getAction() == Action.LEFT_CLICK_AIR) {
                                this.onClick(event.getPlayer());
                                this.onLeftClick(event.getPlayer());
                                this.onLeftClickAir(event.getPlayer());
                            } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                                this.onClick(event.getPlayer());
                                this.onRightClick(event.getPlayer());
                                this.onRightClickAir(event.getPlayer());
                            }

            /* Block Clicks */
                            else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                this.onClick(event.getPlayer());
                                this.onLeftClick(event.getPlayer());
                                this.onLeftClickBlock(event.getPlayer(), event.getClickedBlock());
                            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                this.onClick(event.getPlayer());
                                this.onRightClick(event.getPlayer());
                                this.onRightClickBlock(event.getPlayer(), event.getClickedBlock());
                            }
                        }
                    } else {
                        if (this.cooldownAlerts.onInteract && !this.cooldownAlerts.noAlerts) {
                            event.getPlayer().sendMessage(ChatColor.BLUE + "Ability> " + ChatColor.YELLOW + this.name + ChatColor.GRAY + " is usable in " + ChatColor.GREEN + TimeUtils.formatToSeconds(this.getCooldown(event.getPlayer())));
                        }
                    }
                } else {
                    event.setCancelled(true);
                    ChatUtil.sendWarningMessage(event.getPlayer(), "You cannot use this com.minehut.tabbed.item.");
                }
            }
    }

    @EventHandler
    public void onAbilityDamage(EntityDamageEvent event) {
        DamageInfo damageInfo = new DamageInfo(event);

        if (damageInfo.getDamagerPlayer() != null) {
            ItemStack item = damageInfo.getDamagerPlayer().getItemInHand();
            if (isAbilityItem(this.itemStack, item)) {
                if (KitManager.isUsingKit(damageInfo.getDamagerPlayer(), this.kit)) {
                    if (this.isOffCooldown(damageInfo.getDamagerPlayer())) {
                        if (damageInfo.getHurtPlayer() != null) {
                            if (Teams.isObserver(damageInfo.getHurtPlayer())) {
                                return;
                            }
                            this.onHitPlayer(damageInfo.getDamagerPlayer(), damageInfo.getHurtPlayer());
                        } else {
                            this.onHitEntity(damageInfo.getDamagerPlayer(), damageInfo.getHurtEntity());
                        }
                    } else {
                        if (this.cooldownAlerts.onDamage && !this.cooldownAlerts.noAlerts) {
                            damageInfo.getDamagerPlayer().sendMessage(ChatColor.BLUE + "Ability> " + ChatColor.YELLOW + this.name + ChatColor.GRAY + " is usable in " + ChatColor.GREEN + TimeUtils.formatToSeconds(this.getCooldown(damageInfo.getDamagerPlayer())));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAbilityInteractWithEntity(PlayerInteractAtEntityEvent event) {
        if (isAbilityItem(this.itemStack, event.getPlayer().getItemInHand())) {
            if(KitManager.isUsingKit(event.getPlayer(), this.kit)) {
                if (this.isOffCooldown(event.getPlayer())) {
                    if (event.getRightClicked() instanceof Player) {
                        if (Teams.getObservers().orNull().contains(event.getRightClicked())) {
                            return;
                        }
                        this.onClick(event.getPlayer());
                        this.onRightClick(event.getPlayer());
                        this.onRightClickPlayer(event.getPlayer(), (Player) event.getRightClicked());
                    }
                } else {
                    if (this.cooldownAlerts.onInteractEntity && !this.cooldownAlerts.noAlerts) {
                        event.getPlayer().sendMessage(ChatColor.YELLOW + this.name + ChatColor.GRAY + " is usable in " + ChatColor.GREEN + TimeUtils.formatToSeconds(this.getCooldown(event.getPlayer())));
                    }
                }
            } else {
                event.setCancelled(true);
                ChatUtil.sendWarningMessage(event.getPlayer(), "You cannot use this com.minehut.tabbed.item.");
            }
        }
    }

    @EventHandler
    public void onAbilityBlockPlace(BlockPlaceEvent event) {
        if (isAbilityItem(this.itemStack, event.getItemInHand())) {
            if (KitManager.isUsingKit(event.getPlayer(), this.kit)) {

                if (this.isOffCooldown(event.getPlayer())) {
                    this.onClick(event.getPlayer());
                    this.onPlaceBlock(event.getPlayer(), event.getBlockPlaced().getLocation(), event);
                } else {
                    event.setCancelled(true);
                    if (this.cooldownAlerts.onBlockPlace && !this.cooldownAlerts.noAlerts) {
                        event.getPlayer().sendMessage(ChatColor.BLUE + "Ability> " + ChatColor.YELLOW + this.name + ChatColor.GRAY + " is usable in " + ChatColor.GREEN + TimeUtils.formatToSeconds(this.getCooldown(event.getPlayer())));
                    }
                }
            } else {
                event.setCancelled(true);
                ChatUtil.sendWarningMessage(event.getPlayer(), "You cannot use this com.minehut.tabbed.item.");
            }
        }
    }

    /*
    * ************************************************
    *
    * Possible Override Methods
    *
    * ************************************************
    */

    public void onPlaceBlock(Player player, Location location, BlockPlaceEvent event) {}

    public void onClick(Player player) {}

    public void onHitPlayer(Player player, Player hurt) {}

    public void onHitEntity(Player player, LivingEntity hurt) {}

    public void offCooldown(Player player) {}

    public void onLeftClickAir(Player player) {}

    public void onRightClickAir(Player player) {}

    public void onRightClick(Player player) {}

    public void onLeftClick(Player player) {}

    public void onLeftClickBlock(Player player, Block block) {}

    public void onRightClickBlock(Player player, Block block) {}

    public void onRightClickPlayer(Player player, Player target) {}

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void onOffCooldown(Player player) {}

    public void offCooldownMessage(Player player) {
        if(this.cooldownAlerts.offCooldownAlert && !this.cooldownAlerts.noAlerts) {
            player.sendMessage(ChatColor.YELLOW + this.name + ChatColor.GRAY + " is now usable");
        }
    }

    /*
    * ************************************************
    *
    * Getters and Setters
    *
    * ************************************************
    */

    public static boolean isAbilityItem(ItemStack abilityItem, ItemStack check) {
        if (check != null) {
            if (check.getItemMeta() != null) {
                if (check.getItemMeta().getDisplayName() != null) {
                    if (check.getItemMeta().getDisplayName().equals(abilityItem.getItemMeta().getDisplayName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void putOnCooldown(Player player) {
        this.cooldowns.put(player.getUniqueId(), this.cooldown);
    }

    public boolean isOffCooldown(Player player) {
        return !this.cooldowns.containsKey(player.getUniqueId());
    }

    public int getCooldown(Player player) {
        if (!this.isOffCooldown(player)) {
            return this.cooldowns.get(player.getUniqueId());
        }
        return 0;
    }

    /*
    * ************************************************
    *
    * Unload / Enable
    *
    * ************************************************
    */

    public class CooldownAlerts {
        public boolean noAlerts = false;
        public boolean onBlockPlace = true;
        public boolean onDamage = true;
        public boolean onInteractEntity = true;
        public boolean onInteract = true;
        public boolean offCooldownAlert = true;
    }

    public void enable() {
        this.runnableID = cooldownRunnable();
        Warzone.registerListener(this);

        this.extraEnable();
    }

    public void extraEnable() {

    }

    public void unload() {
        HandlerList.unregisterAll(this);
        Warzone.getInstance().getServer().getScheduler().cancelTask(this.runnableID);


        this.extraUnload();
    }

    public void extraUnload() {}


}
