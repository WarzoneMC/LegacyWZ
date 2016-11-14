package com.minehut.warzone.module.modules.noBuildModule;

import com.minehut.warzone.module.Module;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NoBuildModule implements Module {

    public NoBuildModule() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEmptyBucket(PlayerBucketEmptyEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFillBucket(PlayerBucketFillEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PAINTING
                || event.getEntityType() == EntityType.ITEM_FRAME
                || event.getEntityType() == EntityType.ARMOR_STAND) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.PAINTING
                || event.getRightClicked().getType() == EntityType.ITEM_FRAME
                || event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            event.setCancelled(true);
        }
    }
}
