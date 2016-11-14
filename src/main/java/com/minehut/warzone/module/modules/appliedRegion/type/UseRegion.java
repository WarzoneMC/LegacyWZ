package com.minehut.warzone.module.modules.appliedRegion.type;

import com.minehut.warzone.module.modules.appliedRegion.AppliedRegion;
import com.minehut.warzone.module.modules.filter.FilterModule;
import com.minehut.warzone.module.modules.filter.FilterState;
import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseRegion extends AppliedRegion {

    public UseRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || (event.getItem() != null && event.getItem().getType().isBlock())) return;
        if (region.contains(new BlockRegion(null, event.getClickedBlock().getLocation().toVector())) && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && filter.evaluate(event.getPlayer(), event.getClickedBlock(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        if (region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector())) && filter.evaluate(event.getPlayer(), event.getBlock(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        if (region.contains(new BlockRegion(null, event.getBlockAgainst().getLocation().toVector())) && filter.evaluate(event.getPlayer(), event.getBlockAgainst(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }
}
