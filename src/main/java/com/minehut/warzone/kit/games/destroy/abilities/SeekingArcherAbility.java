package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.cloud.bukkit.util.particles.ParticleEffect;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.Players;
import com.minehut.warzone.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by Lucas on 3/31/16.
 */
public class SeekingArcherAbility extends Ability {

    private Kit kit;

    private List<Player> enabled = new ArrayList<>();
    private Map<Arrow, BukkitTask> tasks = new HashMap<>();

    public SeekingArcherAbility(Kit kit) {
        super(kit, "Heat-Seeker Attachment", ItemFactory.createItem(Material.CLAY_BALL, ChatColor.RED + "Heat-Seeker Attachment"), 20 * 20);

        this.kit = kit;
    }

    @Override
    public void onClick(Player player) {
        enabled.add(player);
        super.putOnCooldown(player);
    }

    @EventHandler
    public void onFireArrow(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow && event.getEntity() instanceof Player && KitManager.isUsingKit((Player)event.getEntity(), kit)) {
            Player player = (Player)event.getEntity();
            if (enabled.contains(player)) {
                LivingEntity target = Players.getTargetEnemy(player, 25);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Could not find an enemy in that direction!");
                } else {
                    Arrow arrow = (Arrow)event.getProjectile();
                    tasks.put(arrow, Bukkit.getScheduler().runTaskTimer(Warzone.getInstance(), () -> {
                        if (arrow.isDead()) {
                            if (tasks.containsKey(arrow)) {
                                tasks.get(arrow).cancel();
                                tasks.remove(arrow);
                            }
                        } else {
                            arrow.setVelocity(processVector(player.getLocation().toVector().subtract(arrow.getLocation().toVector())));
                            ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, arrow.getLocation(), 50);
                        }
                    }, 5L, 5L));
                    enabled.remove(player);
                }
            }
        }
    }

    private Vector processVector(Vector v) {
        return new Vector(processDouble(v.getX()), processDouble(v.getY()), processDouble(v.getZ()));
    }

    private double processDouble(double d) {
        if (d < -0.5) {
            return -1.0;
        } else if (d > 0.5) {
            return 1.0;
        } else {
            return 0;
        }
    }

}
