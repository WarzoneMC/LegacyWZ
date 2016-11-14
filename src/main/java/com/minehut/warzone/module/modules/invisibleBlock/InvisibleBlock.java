package com.minehut.warzone.module.modules.invisibleBlock;

import com.minehut.warzone.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkLoadEvent;

public class InvisibleBlock implements Module {

    protected InvisibleBlock() {
//        Bukkit.getScheduler().runTask(GameHandler.getGameHandler().getPlugin(), new Runnable() {
//            @Override
//            public void run() {
//                for (Chunk chunk : GameHandler.getGameHandler().getMatchWorld().getLoadedChunks()) {
//                    for (Block block36 : chunk.getBlocks(Material.getMaterial(36))) {
//                        block36.setType(Material.AIR);
//                        block36.setMetadata("block36", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), true));
//                    }
//                    for (Block door : chunk.getBlocks(Material.IRON_DOOR_BLOCK)) {
//                        if (door.getRelative(BlockFace.DOWN).getType() != Material.IRON_DOOR_BLOCK
//                                && door.getRelative(BlockFace.UP).getType() != Material.IRON_DOOR_BLOCK)
//                            door.setType(Material.BARRIER);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
//        final Chunk chunk = event.getChunk();
//        Bukkit.getScheduler().runTask(GameHandler.getGameHandler().getPlugin(), new Runnable() {
//            @Override
//            public void run() {
//                for (Block block36 : chunk.getBlocks(Material.getMaterial(36))) {
//                    block36.setType(Material.AIR);
//                    block36.setMetadata("block36", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), true));
//                }
//                for (Block door : chunk.getBlocks(Material.IRON_DOOR_BLOCK)) {
//                    if (door.getRelative(BlockFace.DOWN).getType() != Material.IRON_DOOR_BLOCK
//                            && door.getRelative(BlockFace.UP).getType() != Material.IRON_DOOR_BLOCK)
//                        door.setType(Material.BARRIER);
//                }
//            }
//        });
    }

}
