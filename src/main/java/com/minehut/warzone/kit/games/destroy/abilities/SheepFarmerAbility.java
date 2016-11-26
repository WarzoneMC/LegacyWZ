package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.warzone.util.chat.S;
import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.ColorConverter;
import com.minehut.warzone.util.InstantFirework;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.particles.ParticleEffect;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Created by lucas on 3/15/16.
 */
public class SheepFarmerAbility extends Ability {

    private Map<Player, BukkitTask> tasks = new HashMap<>();
    private Map<Sheep, Player> sheeps = new HashMap<>();

    public SheepFarmerAbility(Kit kit) {
        super(kit, "Exploding Sheep", ItemFactory.createItem(Material.MONSTER_EGG, ChatColor.GOLD + "Exploding Sheep", Arrays.asList("", ChatColor.YELLOW + "Hurl an exploding sheep", ChatColor.YELLOW + "at your enemies!", ""), 1, (byte) 91), 20 * 10);
    }

    @Override
    public void onClick(Player player) {
        TeamModule team = Teams.getTeamByPlayer(player).orNull();

        Sheep sheep = (Sheep)player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.SHEEP);

        sheep.setCustomName(team.getColor().toString() + player.getName() + ChatColor.GRAY + "'s prized sheep");

        if (team != null) {
            switch (team.getColor()) {
                case RED:
                    sheep.setColor(DyeColor.RED);
                    break;
                case BLUE:
                    sheep.setColor(DyeColor.BLUE);
                    break;
            }
        }

        sheeps.put(sheep, player);

        sheep.setVelocity(player.getLocation().getDirection().multiply(3.0));

        player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 10, 1);

        tasks.put(player, Bukkit.getScheduler().runTaskTimer(Warzone.getInstance(), () -> {
            Location sLoc = sheep.getLocation();

            if (sheep == null || sheep.isDead()) {
                tasks.get(player).cancel();
                return;
            }

            if (sheep.isOnGround() || sLoc.clone().add(maxOne(sLoc.getDirection()).toLocation(sLoc.getWorld())).getBlock().getType().isSolid()) {
                explode(sheep, player);
            } else {
                ParticleEffect.CLOUD.display(0.1F, 0.1F, 0.1F, 0, 5, sheep.getLocation(), 50);
            }
        }, 3L, 3L));
        super.putOnCooldown(player);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Sheep && sheeps.containsKey(event.getEntity())) {
            Sheep sheep = (Sheep)event.getEntity();
            Player player = sheeps.get(sheep);
            explode(sheep, player);
        }
    }

    public void explode(Sheep sheep, Player player) {
        TeamModule team = Teams.getTeamByPlayer(player).orNull();
        new InstantFirework(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(ColorConverter.getColor(team.getColor())).build(), sheep.getLocation());
        sheep.remove();
        Location sLoc = sheep.getLocation().clone();
        List<Entity> nearby = sheep.getNearbyEntities(6, 5, 6);
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        nearby.forEach(e -> {
            if (e instanceof LivingEntity) {
                Location eLoc = e.getLocation();
                if (player != null) {
                    if (e instanceof Player) {
                        if (team.contains(e)) {
                            return;
                        }
                    } else if (e instanceof Creature) {
                        if (e.getCustomName() != null && e.getCustomName().contains(team.getColor().toString())) {
                            return;
                        }
                    }
                    ((LivingEntity) e).damage(rand.nextInt(5, 8), player);
                    e.setVelocity(eLoc.toVector().subtract(sLoc.toVector()).multiply(1.0 / eLoc.distance(sLoc)).setY(0.5));
                }
            }
        });
        if (nearby.size() > 0) {
            S.arrowHit(player);
        }

        sheeps.remove(sheep);
        tasks.get(player).cancel();
        tasks.remove(player);
    }

    private Vector maxOne(Vector loc) {
        return new Vector(loc.getX() > 1 ? 1 : loc.getX(), loc.getY() > 1 ? 1 : loc.getY(), loc.getZ() > 1 ? 1 : loc.getZ());
    }

    private double getPos(double n1, double n2) { return Math.abs(n1) > Math.abs(n2) ? n1 : n2; }

}
