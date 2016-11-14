package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.Warzone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

/**
 * Created by lucas on 4/28/16.
 */
public class NinjaAbility extends Ability {

    public NinjaAbility(Kit kit) {
        super(kit, "Star Spin", ItemFactory.createItem(Material.FLINT, ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "Star Spin"), 20 * 12);
    }

    @Override
    public void onClick(Player player) {
        Location transform = player.getLocation().clone();
        float startYaw = player.getEyeLocation().getYaw();
        for (int i = 0; i < 9; i++) {
            final int n = i;
            Bukkit.getScheduler().runTaskLater(Warzone.getInstance(), () -> {
                float yaw = startYaw + ((n * 10) - 40);
                transform.setYaw(yaw);
                Location tp = player.getLocation().clone();
                tp.setYaw(yaw);
                player.teleport(tp);
                Location spawn = transform.clone().add(0, 1.5, 0).add(transform.getDirection());
                Arrow arrow = player.getWorld().spawnArrow(spawn, transform.getDirection().clone().multiply(4.0), 1, 0);
                arrow.setShooter(player);
                if (n == 8) {
                    Location start = player.getLocation().clone();
                    start.setYaw(startYaw);
                    player.teleport(start);
                }
            }, (long)n);
        }
        super.putOnCooldown(player);
    }

}
