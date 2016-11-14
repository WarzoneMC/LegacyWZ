package com.minehut.warzone.util;

import com.google.common.collect.Queues;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.module.modules.permissions.PermissionModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Players {

    public static void resetPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
        for (PotionEffect effect : player.getActivePotionEffects()) {
            try {
                player.removePotionEffect(effect.getType());
            } catch (NullPointerException ignored) {
            }
        }
        player.setTotalExperience(0);
        player.setExp(0);
//        player.setPotionParticles(true);
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.1F);
    }

    public static double getSnowflakeMultiplier(OfflinePlayer player) {
        if (player.isOp()) return 2.5;
        if (PermissionModule.isDeveloper(player.getUniqueId())) return 2.0;
        return 1.0;
    }

    public static String getName(ServerOperator who, boolean flairs) {
        if (who instanceof OfflinePlayer) {
            OfflinePlayer player = (OfflinePlayer) who;
            return player.isOnline() ? (flairs ? player.getPlayer().getDisplayName() : Teams.getTeamColorByPlayer(player) + player.getPlayer().getName()) : ChatColor.DARK_AQUA + player.getName();
        } else {
            return ChatColor.GOLD + "\u2756" + ChatColor.DARK_AQUA + "Console";
        }
    }

    public static String getName(ServerOperator who) {
        return getName(who, true);
    }

    public static Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    public static LivingEntity getTargetEnemy(Player player, int range) {
        TeamModule team = Teams.getTeamByPlayer(player).orNull();
        ConcurrentLinkedQueue<Entity> inRange = Queues.newConcurrentLinkedQueue(player.getNearbyEntities(range, range, range));
        for (Entity entity : inRange) {
            if (!isLookingAt(player, entity, 0.1))
                inRange.remove(entity);
            if (entity instanceof Player) {
                if (team.contains(entity) || Teams.isObserver((Player)entity))
                    inRange.remove(entity);
            } else if (entity instanceof Creature) {
                if (entity.getCustomName() != null && entity.getCustomName().contains(team.getColor().toString()))
                    inRange.remove(entity);
            } else inRange.remove(entity);
        }
        if (inRange.size() == 0)
            return null;
        LivingEntity nearest = null;
        for (Entity e : inRange) {
            if (nearest == null || player.getLocation().distance(e.getLocation()) < player.getLocation().distance(nearest.getLocation()))
                nearest = (LivingEntity) e;
        }
        return nearest;
    }

    public static boolean isLookingAt(Player player, Entity entity, double accuracy) {
        Vector toEntity = entity.getLocation().toVector().subtract(player.getLocation().toVector());
        Vector direction = player.getEyeLocation().getDirection();
        return toEntity.normalize().dot(direction) <= accuracy;
    }

}
