package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Gamer on 12/5/15
 */
public class PhoenixAbility extends Ability {

    public Map<FallingBlock, BukkitTask> tasks = new HashMap<>();

    public PhoenixAbility(Kit kit, int slot) {
        super(kit, "Fire Breath", ItemFactory.createItem(Material.BLAZE_POWDER, ChatColor.GOLD.toString() + ChatColor.BOLD + "FIRE BREATH"), 20 * 15);
    }

    @Override
    public void onClick(final Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
        TeamModule team = Teams.getTeamByPlayer(player).orNull();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int i = 0; i < 20; i++) {
            Bukkit.getScheduler().runTaskLater(Warzone.getInstance(), () -> createFire(player, player.getLocation().getDirection().multiply(new Vector(rand.nextDouble(0.8, 1.2), rand.nextDouble(0.8, 1.2), rand.nextDouble(0.8, 1.2))), team), i * 2L);
        }

        super.putOnCooldown(player);
    }

    private void createFire(Player player, Vector velocity, TeamModule team) {
        final FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation(), Material.FIRE, (byte)0);
        fallingBlock.setVelocity(velocity);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_SAND_FALL, 1, 1);
        tasks.put(fallingBlock, Bukkit.getScheduler().runTaskTimer(Warzone.getInstance(), () -> {
            if (fallingBlock.isDead()) {
                tasks.get(fallingBlock).cancel();
            } else if (fallingBlock.isOnGround()) {
                fallingBlock.remove();
            } else {
                for (Entity entity : fallingBlock.getNearbyEntities(0.7, 1.0, 0.7)) {
                    if (entity.equals(player)) continue;
                    if (entity instanceof LivingEntity) {
                        if (entity instanceof Player) {
                            if (team.contains(entity) || Teams.isObserver((Player) entity))
                                continue;
                        } else if (entity instanceof Creature) {
                            if (entity.getCustomName() != null && entity.getCustomName().contains(team.getColor().toString()))
                                continue;
                        }
                        entity.setFireTicks(240);
                        ((LivingEntity) entity).damage(6.0, player);
                    }
                }
            }
        }, 1L, 1L));
    }

    @EventHandler
    public void onBlockChangeForm(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            if (event.getTo() == Material.FIRE) {
                event.setCancelled(true);
            }
        }
    }

}
