package com.minehut.warzone.module.modules.respawn;

import com.google.common.base.Optional;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.event.CardinalSpawnEvent;
import com.minehut.warzone.event.CycleCompleteEvent;
import com.minehut.warzone.event.MatchStartEvent;
import com.minehut.warzone.event.PlayerChangeTeamEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Items;
import com.minehut.warzone.util.Players;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.spawn.SpawnModule;
import com.minehut.warzone.util.itemstack.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Collections;

public class RespawnModule implements Module {

    private final Match match;

    protected RespawnModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCardinalSpawn(CardinalSpawnEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (((team.isPresent() && !team.get().isObserver()) || !team.isPresent()) && match.isRunning()) {
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
        event.getPlayer().updateInventory();

    }

//    @EventHandler(priority = EventPriority.LOW)
//    public void clearIgnorantEffects(CardinalSpawnEvent event) {
//        event.getPlayer().setPotionParticles(true);
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInitLogin(PlayerSpawnLocationEvent event) {
        Optional<TeamModule> teamModule = Teams.getTeamById("observers");
        ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
        for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
            if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
        }
        SpawnModule chosen = modules.getRandom();
        event.setSpawnLocation(chosen.getLocation());
        event.getPlayer().setMetadata("initSpawn", new FixedMetadataValue(Warzone.getInstance(), chosen));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Players.resetPlayer(event.getPlayer());
        CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), (SpawnModule) event.getPlayer().getMetadata("initSpawn").get(0).value(), Teams.getTeamById("observers").orNull());
        Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(final PlayerDeathEvent event) {
        if (event.getEntity().getHealth() < event.getEntity().getMaxHealth()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), new Runnable() {
                @Override
                public void run() {
                    event.getEntity().spigot().respawn();
                }
            }, 0L);
        }
    }

    @EventHandler
    public void onMinecraftRespawn(PlayerRespawnEvent event) {
        if (match.getState().equals(MatchState.PLAYING)) {
            Optional<TeamModule> teamModule = Teams.getTeamByPlayer(event.getPlayer());
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, Teams.getTeamByPlayer(event.getPlayer()).orNull());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                event.setRespawnLocation(chosen.getLocation());
            }
        } else {
            Optional<TeamModule> teamModule = Teams.getTeamById("observers");
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, Teams.getTeamById("observers").get());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                Player player = event.getPlayer();
                event.setRespawnLocation(chosen.getLocation());
                Players.resetPlayer(player);
                giveObserversKit(player);
                player.teleport(chosen.getLocation());
            }
        }
    }


    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<TeamModule> team = Teams.getTeamByPlayer(player);
            if (!team.isPresent() || !team.get().isObserver()) {
                Players.resetPlayer(player);
                ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == team.orNull()) modules.add(spawnModule);
                }
                SpawnModule chosen = modules.getRandom();
                CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(player, chosen, Teams.getTeamByPlayer(player).orNull());
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    player.teleport(chosen.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<TeamModule> teamModule = Teams.getTeamByPlayer(player);
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(player, chosen, Teams.getTeamById("observers").get());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                Players.resetPlayer(player);
                giveObserversKit(player);
                player.teleport(chosen.getLocation());
            }
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (event.getOldTeam() == null || !event.getOldTeam().isPresent()) {
            System.out.println("detected new to server join player[" + event.getPlayer().getName() + "]");
            event.getPlayer().setMaxHealth(20);
            Players.resetPlayer(event.getPlayer());
            Optional<TeamModule> teamModule = event.getNewTeam();
            ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
            for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
            }
            SpawnModule chosen = modules.getRandom();
            CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, event.getNewTeam().orNull());
            Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
            if (!spawnEvent.isCancelled()) {
                event.getPlayer().teleport(chosen.getLocation());
            }
        } else if (match.getState().equals(MatchState.PLAYING)) {
            if (!event.getNewTeam().isPresent() || !(event.getNewTeam().get().isObserver() && event.getOldTeam().isPresent() && event.getOldTeam().get().isObserver())) {

                if (event.getOldTeam().isPresent() && event.getOldTeam().get().isObserver()) {
//                    event.getPlayer().setMaxHealth(20); //todo: kept crashing server. look into fix.
                    Players.resetPlayer(event.getPlayer());
                    Optional<TeamModule> teamModule = event.getNewTeam();
                    ModuleCollection<SpawnModule> modules = new ModuleCollection<SpawnModule>();
                    for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                        if (spawnModule.getTeam() == teamModule.orNull()) modules.add(spawnModule);
                    }
                    SpawnModule chosen = modules.getRandom();
                    CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), chosen, event.getNewTeam().orNull());
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().teleport(chosen.getLocation());
                    }
                } else {
                    event.getPlayer().setMetadata("teamChange", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), "teamChange"));
                    System.out.println("killing " + event.getPlayer().getName() + " onTeamChange for case 1");
                    event.getPlayer().setHealth(0);
                }
            } else {
                Optional<TeamModule> teamModule = event.getNewTeam();
                SpawnModule spawn = null;
                for (SpawnModule spawnModule : match.getModules().getModules(SpawnModule.class)) {
                    if (spawnModule.getTeam() == teamModule.orNull()) spawn = spawnModule;
                }
                CardinalSpawnEvent spawnEvent = new CardinalSpawnEvent(event.getPlayer(), spawn, event.getNewTeam().orNull());
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    event.getPlayer().setMetadata("teamChange", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), "teamChange"));
                    System.out.println("killing " + event.getPlayer().getName() + " onTeamChange for case 2");
                    event.getPlayer().setHealth(0);
                }
            }

        }

    }

    public void giveObserversKit(Player player) {
        player.getInventory().setItem(0, Items.createItem(Material.COMPASS, 1, (short) 0, ChatColor.BLUE + "" + ChatColor.BOLD + ChatConstant.UI_COMPASS.getMessage(player.spigot().getLocale())));
        if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            player.getInventory().setItem(2, Items.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                    ChatColor.GREEN + "" + ChatColor.BOLD + (ChatConstant.UI_TEAM_SELECTION.getMessage(player.spigot().getLocale())),
                    Collections.singletonList(ChatColor.DARK_PURPLE + ChatConstant.UI_TEAM_JOIN_TIP.getMessage(player.spigot().getLocale()))));
        }
        player.getInventory().setItem(3, ItemFactory.createItem(Material.EMERALD, ChatColor.GREEN.toString() + ChatColor.BOLD + "KITS"));
    }

    @EventHandler
    public void onKitOpen(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN.toString() + ChatColor.BOLD + "KITS")) {
            event.setCancelled(true);
            Warzone.getInstance().getKitManager().openKitMenu(event.getPlayer());
        }
    }
}
