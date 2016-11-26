package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.KitManager;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.SlotType;
import com.minehut.warzone.util.Teams;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by lucas on 3/18/16.
 */
public class EngineerKit extends Kit {

    private Map<Shulker, BukkitTask> turrets = new HashMap<>();
    private Map<Shulker, Player> players = new HashMap<>();
    private Map<Player, Integer> hasTurrets = new HashMap<>();

    public EngineerKit() {
        super("Engineer", 650, "Deploy two defensive turrets", Material.ARROW);

        super.setItem(0, ItemFactory.createItem(Material.IRON_PICKAXE));
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setItem(2, ItemFactory.createItem(Material.PURPUR_BLOCK, ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Turret"));
        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.GOLD_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot place that here!");
            return;
        }
        Player player = event.getPlayer();
        if (KitManager.isUsingKit(player, this) && event.getBlock().getType().equals(Material.PURPUR_BLOCK)) {
            event.setCancelled(true);
            if (hasTurrets.containsKey(player)) {
                if (hasTurrets.get(player) == 0) {
                    player.sendMessage(ChatColor.BLUE + "Ability> " + ChatColor.GRAY + "You currently have 0 " + ChatColor.YELLOW + "turrets" + ChatColor.GRAY + ".");
                    return;
                }
            } else {
                hasTurrets.put(player, 2);
            }
            TeamModule team = Teams.getTeamByPlayer(player).orNull();
            Shulker turret = (Shulker) player.getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.SHULKER);
            turret.setMaxHealth(80);
            turret.setHealth(80);
            turret.setCustomName(team.getColor().toString() + player.getName() + ChatColor.GRAY + "'s turret");
            players.put(turret, player);
            Location sLoc = turret.getLocation().clone();
            turrets.put(turret, Bukkit.getScheduler().runTaskTimer(Warzone.getInstance(), () -> {
                if (turret.isDead()) {
                    turrets.get(turret).cancel();
                    turrets.remove(turret);
                    if (!turret.isDead())
                        turret.remove();
                } else {
                    List<Entity> nearby = turret.getNearbyEntities(14, 8, 14);
                    nearby.remove(turret);

                    if (nearby.size() > 0) {
                        LivingEntity nearest = null;
                        for (Entity e : nearby) {
                            if (e instanceof LivingEntity) {
                                LivingEntity le = (LivingEntity) e;
                                if (le instanceof Player) {
                                    Player pe = (Player) le;
                                    if (team.contains(pe))
                                        continue;
                                    if (nearest == null || pe.getLocation().distance(sLoc) < nearest.getLocation().distance(sLoc))
                                        if (!Teams.isObserver(pe))
                                            nearest = pe;
                                } else {
                                    if (le.getCustomName().contains(team.getColor().toString())) {
                                        continue;
                                    }
                                    nearest = le;
                                }
                            }
                        }

                        if (nearest != null) {
                            turret.eject();
                            ShulkerBullet bullet = (ShulkerBullet) turret.getWorld().spawnEntity(turret.getLocation(), EntityType.SHULKER_BULLET);
                            bullet.setTarget(nearest);
                            bullet.setShooter(turret);
                            bullet.setVelocity(bullet.getVelocity().multiply(2));
                        }
                    }
                }
            }, 0L, 30L));
            Bukkit.getScheduler().runTaskLater(Warzone.getInstance(), () -> {
                if (turrets.containsKey(turret))
                    turret.remove();
            }, 20 * 35);
            hasTurrets.put(player, hasTurrets.get(player) - 1);
            player.sendMessage(ChatColor.BLUE + "Ability> " + ChatColor.GRAY + "You used a " + ChatColor.YELLOW + "turret" + ChatColor.GRAY + "! You now have " + ChatColor.GOLD + hasTurrets.get(player));
            Bukkit.getScheduler().runTaskLater(Warzone.getInstance(), () -> {
                hasTurrets.put(player, hasTurrets.get(player) + 1);
                player.sendMessage(ChatColor.BLUE + "Ability> " + ChatColor.GRAY + "You got a" + ChatColor.YELLOW + " turret " + ChatColor.GRAY + "back! You now have " + ChatColor.GOLD + hasTurrets.get(player));
            }, 20 * 45);
        }
    }

    @EventHandler
    public void onShulkerTeleport(EntityTeleportEvent event) {
        if (event.getEntity() instanceof Shulker) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (event.getEntity() instanceof Shulker) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Shulker)
            if(this.players.containsKey(event.getEntity())) {
                Player player = this.players.get(event.getEntity());
                TeamModule team = Teams.getTeamByPlayer(player).orNull();
                if (event.getDamager() instanceof Player)
                    if (team.contains(event.getDamager()))
                        event.setCancelled(true);
            }

        if (event.getDamager() instanceof ShulkerBullet) {
            ShulkerBullet bullet = (ShulkerBullet)event.getDamager();
            if (bullet.getShooter() instanceof Shulker) {
                Shulker shooter = (Shulker)bullet.getShooter();
                if (players.containsKey(shooter)) {
                    Player player = players.get(shooter);
                    TeamModule team = Teams.getTeamByPlayer(player).orNull();
                    event.setCancelled(true);
                    if (event.getEntity() instanceof Player && team.contains(event.getEntity()))
                        return;
                    if (event.getEntity() instanceof LivingEntity)
                        ((LivingEntity)event.getEntity()).damage(event.getDamage(), player);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent)player.getLastDamageCause()).getDamager() instanceof ShulkerBullet) {
                Shulker turret = (Shulker)((ShulkerBullet)((EntityDamageByEntityEvent)player.getLastDamageCause()).getDamager()).getShooter();
                EntityDamageByEntityEvent newDamage = new EntityDamageByEntityEvent(players.get(turret), player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 1);
                Bukkit.getPluginManager().callEvent(newDamage);
                player.setLastDamageCause(newDamage);
            }
        }
    }

}
