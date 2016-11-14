package com.minehut.warzone.module.modules.playable;

import com.google.common.base.Optional;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Playable implements Module {

    private final RegionModule region;

    public Playable(RegionModule region) {
        this.region = region;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning() && team.isPresent() && !team.get().isObserver()) {
            if (region != null) {
                if (region.contains(event.getTo().toVector()) && !region.contains(event.getFrom().toVector())) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_PLAYABLE_LEAVE));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning() && team.isPresent() && !team.get().isObserver()) {
            if (region != null) {
                if (region.contains(event.getBlock().getLocation())) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_PLAYABLE_INTERACT));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning() && team.isPresent() && !team.get().isObserver()) {
            if (region != null) {
                if (region.contains(event.getBlock().getLocation())) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_PLAYABLE_INTERACT));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning() && team.isPresent() && !team.get().isObserver()) {
            if (region != null && event.getClickedBlock() != null) {
                if (region.contains(event.getClickedBlock().getLocation())) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_PLAYABLE_INTERACT));
                    event.setCancelled(true);
                }
            }
        }
    }


}
