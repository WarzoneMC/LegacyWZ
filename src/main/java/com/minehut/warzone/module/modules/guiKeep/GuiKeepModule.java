package com.minehut.warzone.module.modules.guiKeep;

import com.google.common.base.Optional;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.GameHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GuiKeepModule implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning() && (!team.isPresent() || !team.orNull().isObserver())) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getClickedBlock() != null) {
                    if (event.getClickedBlock().getType().equals(Material.WORKBENCH)) {
                        if (!event.getPlayer().isSneaking() || event.getPlayer().getItemInHand() == null) {
                            event.setCancelled(true);
                            event.getPlayer().openWorkbench(event.getPlayer().getLocation(), true);
                        }
                    }
                }
            }
        }
    }

}
