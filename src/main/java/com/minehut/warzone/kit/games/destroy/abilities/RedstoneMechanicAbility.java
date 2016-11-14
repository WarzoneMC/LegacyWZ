package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.event.PlayerChangeTeamEvent;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by lucas on 3/29/16.
 */
public class RedstoneMechanicAbility extends Ability {

    private Kit kit;

    private List<TorchPlayer> torches = new ArrayList<>();

    public RedstoneMechanicAbility(Kit kit) {
        super(kit, "Mechanic's Torch", ItemFactory.createItem(Material.REDSTONE_TORCH_ON, ChatColor.RED + "Mechanic's Torch"), 20 * 25);

        this.kit = kit;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!event.isCancelled() && event.getBlock().getLocation().clone().add(0, -1, 0).getBlock().getType().isSolid()) {
            if (KitManager.isUsingKit(player, kit) && super.isOffCooldown(player) && event.getBlockPlaced().getType().equals(Material.REDSTONE_TORCH_ON)) {
                TorchPlayer torchPlayer = getTorchPlayer(player);
                if (torchPlayer == null || player.isSneaking()) {
                    if (torchPlayer != null) {
                        torchPlayer.location.getBlock().setType(Material.AIR);
                        torches.remove(torchPlayer);
                        torchPlayer.armorStand.remove();
                    }
                    event.getBlock().setType(Material.REDSTONE_TORCH_ON);
                    torches.add(new TorchPlayer(player, event.getBlock().getLocation()));
                    super.putOnCooldown(player);
                    player.getInventory().setItem(3, ItemFactory.createItem(Material.REDSTONE_TORCH_ON, ChatColor.RED + "Mechanic's Torch"));
                } else {
                    player.sendMessage(ChatColor.RED + "Sneak to override your previous torch.");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        TorchPlayer torchPlayer = getTorchPlayer(event.getPlayer());
        if (torchPlayer != null) {
            torchPlayer.location.getBlock().setType(Material.AIR);
            torchPlayer.armorStand.remove();
            torches.remove(torchPlayer);
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        TorchPlayer torchPlayer = getTorchPlayer(event.getBlock().getLocation());
        if (torchPlayer != null) {
            event.setCancelled(true);
            Player player = torchPlayer.player;
            TeamModule team = Teams.getTeamByPlayer(player).orNull();
            if (!Teams.isObserver(event.getPlayer())) {
                if ((!team.contains(event.getPlayer()) || (player.equals(torchPlayer.player) && player.isSneaking()))) {
                    torches.remove(torchPlayer);
                    event.getBlock().setType(Material.AIR);
                    torchPlayer.armorStand.remove();
                } else if (player.equals(torchPlayer.player)) {
                    player.sendMessage(ChatColor.RED + "Sneak to break your torch!");
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        TorchPlayer torchPlayer = getTorchPlayer(player);
        if (torchPlayer != null) {
            Bukkit.getScheduler().runTaskLater(Warzone.getInstance(), () -> {
                if (KitManager.isUsingKit(player, kit) && Teams.getTeamByPlayer(player).orNull() == torchPlayer.team) {
                    player.teleport(torchPlayer.location);
                }
                player.setFallDistance(0F);
                torchPlayer.location.getBlock().setType(Material.AIR);
                torchPlayer.armorStand.remove();
                torches.remove(torchPlayer);
            }, 5L);
        }
    }

    private TorchPlayer getTorchPlayer(Player player) {
        for (TorchPlayer tp : torches) {
            if (tp.player.equals(player)) return tp;
        }
        return null;
    }

    private TorchPlayer getTorchPlayer(Location location) {
        for (TorchPlayer tp : torches) {
            if (tp.location.equals(location)) return tp;
        }
        return null;
    }

    private class TorchPlayer {

        Player player;
        Location location;
        ArmorStand armorStand;
        TeamModule team;

        TorchPlayer(Player player, Location location) {
            this.player = player;
            this.location = location;
            this.armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0.5, 1.0, 0.5), EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomName(Teams.getTeamByPlayer(player).get().getColor().toString() + player.getName() + ChatColor.GRAY + "'s spawn torch");
            armorStand.setCustomNameVisible(true);
            armorStand.setBasePlate(false);
            armorStand.setMarker(true);
            team = Teams.getTeamByPlayer(player).orNull();
        }

    }

}
