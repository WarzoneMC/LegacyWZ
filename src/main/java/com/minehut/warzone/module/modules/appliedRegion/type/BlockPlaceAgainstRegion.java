package com.minehut.warzone.module.modules.appliedRegion.type;

import com.minehut.warzone.module.modules.appliedRegion.AppliedRegion;
import com.minehut.warzone.module.modules.filter.FilterModule;
import com.minehut.warzone.module.modules.filter.FilterState;
import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BlockPlaceAgainstRegion extends AppliedRegion {

    public BlockPlaceAgainstRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector())) && filter.evaluate(event.getPlayer(), event.getBlockPlaced(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Material material = (event.getBucket().equals(Material.WATER_BUCKET) ? Material.WATER : (event.getBucket().equals(Material.LAVA_BUCKET) ? Material.LAVA : Material.AIR));
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().toVector()))
                && filter.evaluate(event.getPlayer(), material, event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }
}
