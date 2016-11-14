package com.minehut.warzone.module.modules.wools;

import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.event.CardinalDeathEvent;
import com.minehut.warzone.event.objective.ObjectiveCompleteEvent;
import com.minehut.warzone.event.objective.ObjectiveTouchEvent;
import com.minehut.warzone.module.GameObjective;
import com.minehut.warzone.module.modules.timeLimit.TimeLimit;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.objective.ObjectiveProximityEvent;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Fireworks;
import com.minehut.warzone.util.MiscUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WoolObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final DyeColor color;
    private final BlockRegion place;
    private final boolean craftable;
    private final boolean show;
    private final boolean required;

    private Vector location;
    private double proximity;

    private Set<UUID> playersTouched;
    private boolean touched;
    private boolean complete;


    protected WoolObjective(final TeamModule team, final String name, final String id, final DyeColor color, final BlockRegion place, final boolean craftable, final boolean show, final boolean required, final Vector location) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.color = color;
        this.place = place;
        this.craftable = craftable;
        this.show = show;
        this.required = required;
        this.location = location;

        this.proximity = Double.POSITIVE_INFINITY;

        this.playersTouched = new HashSet<>();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public TeamModule getTeam() {
        return team;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean showOnScoreboard() {
        return show;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    public DyeColor getColor() {
        return color;
    }

    @EventHandler
    public void onWoolPickup(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!this.complete && GameHandler.getGameHandler().getMatch().isRunning()) {
            try {
                if (event.getCurrentItem().getType() == Material.WOOL && event.getCurrentItem().getData().getData() == color.getData()) {
                    if (Teams.getTeamByPlayer(player).orNull() == team) {
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, true, true);
                        Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    @EventHandler
    public void onWoolPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!this.complete && GameHandler.getGameHandler().getMatch().isRunning()) {
            try {
                if (event.getItem().getItemStack().getType() == Material.WOOL && event.getItem().getItemStack().getData().getData() == color.getData()) {
                    if (Teams.getTeamByPlayer(player).orNull() == team) {
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, true, true);
                        Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        while (playersTouched.contains(event.getEntity().getUniqueId())) {
            playersTouched.remove(event.getEntity().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().equals(place.getBlock())) {
            if (event.getBlock().getType().equals(Material.WOOL)) {
                if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                    if (Teams.getTeamByPlayer(event.getPlayer()).orNull() == team) {
                        this.complete = true;
                        Fireworks.spawnFirework(event.getPlayer().getLocation(), event.getPlayer().getWorld(), MiscUtil.convertChatColorToColor(MiscUtil.convertDyeColorToChatColor(color)));
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                        if (this.show)
                            ChatUtil.sendWarningMessage(event.getPlayer(), "You may not complete the other team's objective.");
                    }
                } else {
                    event.setCancelled(true);
                    if (this.show)
                        ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_BLOCK_PLACE, MiscUtil.convertDyeColorToChatColor(color) + color.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.RED));
                }
            } else {
                event.setCancelled(true);
                if (this.show)
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_BLOCK_PLACE, MiscUtil.convertDyeColorToChatColor(color) + color.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.RED));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().equals(place.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonPush(BlockPistonExtendEvent event) {
        if (!event.isCancelled()) {
            if (event.getBlock().getRelative(event.getDirection()).equals(place.getBlock())) {
                event.setCancelled(true);
            } else {
                for (Block block : event.getBlocks()) {
                    if (block.equals(place.getBlock()) || block.equals(place.getBlock().getRelative(event.getDirection().getOppositeFace()))) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (!event.isCancelled()) {
            for (Block block : event.getBlocks()) {
                if (block.equals(place.getBlock()) || block.equals(place.getBlock().getRelative(event.getDirection().getOppositeFace()))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (place.getBlock().equals(event.getBlock()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCraftWool(CraftItemEvent event) {
        if (event.getRecipe().getResult().equals(new ItemStack(Material.WOOL, 1, color.getData())) && !this.craftable) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && location != null && GameHandler.getGameHandler().getMatch().isRunning() && !this.touched && Teams.getTeamByPlayer(event.getKiller()).isPresent() && Teams.getTeamByPlayer(event.getKiller()).get() == this.team) {
            if (event.getKiller().getLocation().toVector().distance(location) < proximity) {
                double old = proximity;
                proximity = event.getKiller().getLocation().toVector().distance(location);
                Bukkit.getServer().getPluginManager().callEvent(new ObjectiveProximityEvent(this, event.getKiller(), old, proximity));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSafetyPlace(BlockPlaceEvent event) {
        if (!event.isCancelled() && this.touched) {
            if (event.getBlock().getType().equals(Material.WOOL)) {
                if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                    if (Teams.getTeamByPlayer(event.getPlayer()).orNull() == team) {
                        if (event.getBlockPlaced().getLocation().distance(place.getLocation()) < proximity) {
                            double old = proximity;
                            proximity = event.getBlockPlaced().getLocation().distance(place.getLocation());
                            Bukkit.getServer().getPluginManager().callEvent(new ObjectiveProximityEvent(this, event.getPlayer(), old, proximity));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        if (event.getObjective() == this) {
            if (!this.playersTouched.contains(event.getPlayer().getUniqueId())) {
                this.playersTouched.add(event.getPlayer().getUniqueId());
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 2f);
                Bukkit.broadcastMessage(team.getColor() + " » " + event.getPlayer().getName() + ChatColor.WHITE + " picked up " + MiscUtil.convertDyeColorToChatColor(this.color) + this.color.toString() + ChatColor.WHITE + " for the " + team.getColor() + team.getName() + " Team");
            }
        }
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        if (event.getObjective() == this) {
            Bukkit.broadcastMessage(team.getColor() + " » " + event.getPlayer().getName() + ChatColor.WHITE + " placed " + MiscUtil.convertDyeColorToChatColor(this.color) + this.color.toString() + ChatColor.WHITE + " for the " + team.getColor() + team.getName() + " Team");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.7f, 2f);
            }
        }
    }

    public double getProximity() {
        return proximity;
    }

    public boolean showProximity() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTimeLimit() != 0 && GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.MOST_OBJECTIVES);
    }
}