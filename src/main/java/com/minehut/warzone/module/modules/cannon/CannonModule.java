package com.minehut.warzone.module.modules.cannon;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.event.CycleCompleteEvent;
import com.minehut.warzone.event.MatchStartEvent;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.tnt.TntTracker;
import com.minehut.warzone.util.Teams;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;
import java.util.Random;

/*
 * Created by lucas on 3/18/16.
 */
public class CannonModule implements Module {

    private BlockRegion blockRegion;
    private Minecart minecart;

    private Player player;
    private float range = 1;
    private long lastFire = 0;
    private int fireRate; //ticks

    private ArrayList<TNTPrimed> tnts = new ArrayList<>();
    private int guiRunnable;

    protected CannonModule(BlockRegion blockRegion, int fireRate, float range) {
        this.blockRegion = blockRegion;
        this.fireRate = fireRate * 20;
        this.range = range;
    }

    @Override
    public void unload() {
        Bukkit.getScheduler().cancelTask(this.guiRunnable);
        HandlerList.unregisterAll(this);
    }

    public int gui() {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    player.showTitle(new TextComponent(""), new TextComponent(org.bukkit.ChatColor.RED + "Left Click to fire!"), 0, 20, 10);
                }
            }
        }, 0, 20);
    }

    @EventHandler
    public void onCycle(CycleCompleteEvent event) {
        Location location = blockRegion.getLocation();
        location.getChunk().load();
        this.minecart = (Minecart) location.getWorld().spawnEntity(location, EntityType.MINECART);
        minecart.setMaxSpeed(0);
        minecart.setInvulnerable(true);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        this.guiRunnable = gui();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) {
            if (this.player != null && event.getPlayer() == this.player) {
                event.setCancelled(true);
                this.shoot(player);
            }
        }
    }

//    @EventHandler
//    public void onRightClick(PlayerInteractAtEntityEvent event) {
//        if (this.player != null && event.getPlayer() == this.player) {
//            event.setCancelled(true);
//            this.shoot(player);
//        }
//    }

    private void shoot(Player player) {
        if (System.currentTimeMillis() - lastFire <= fireRate) {
            player.sendMessage(ChatColor.RED + "Cannon is reloading!");
            return;
        }

        this.lastFire = System.currentTimeMillis();

        final TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(new Location(player.getWorld(), player.getEyeLocation().getX(), player.getEyeLocation().getY() + 1, player.getEyeLocation().getZ()), EntityType.PRIMED_TNT);
        tnt.setFuseTicks(80);
        TntTracker.setOwner(tnt, player);
        tnt.setVelocity(player.getLocation().getDirection().multiply(1.8));
        tnts.add(tnt);
        TntTracker.setOwner(tnt, player);

        player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 10, 1);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        if(e.getEntity() instanceof TNTPrimed){
            if(tnts.contains(e.getEntity())){
                ArrayList<Block> toRemove = new ArrayList<>();

                for(Block b : e.blockList()) {
                    if(b.getType() == Material.WOOD || b.getType() == Material.LOG || b.getType() == Material.LOG_2 ||  b.getType() == Material.COBBLESTONE_STAIRS || b.getType() == Material.WOOD_STAIRS){

						/*
    					 * Wood (throws 1/4 in air)
    					 */

                        Random rn = new Random();
                        if((rn.nextInt(4) + 1) <= 3){
                            Material m = b.getType();
                            byte by = b.getData();
                            b.setType(Material.AIR);

                            FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), m, by);

                            Random r = new Random();

                            double xV = -1.1 + (1.5 - -1.1) * r.nextDouble();
                            double yV = .4 + (1 - .4) * r.nextDouble();
                            double zV = -1.1 + (1.5 - -1.1) * r.nextDouble();

                            fb.setVelocity(new Vector(xV,yV,zV).multiply(range));
                        }else{
                            toRemove.add(b);
                        }
                    }else{
                        toRemove.add(b);
                    }
                }

    			/*
    			 * Takes the blocks it shouldn't break out of list.
    			 */

                for (Block remove : toRemove) {
                    e.blockList().remove(remove);
                }
            }
        }
    }

    @EventHandler
    public void onEnter(EntityMountEvent event) {
        if (event.getMount() == this.minecart) {
            if (event.getEntity() instanceof Player) {

                if (Teams.isObserver((Player) event.getEntity())) {
                    event.setCancelled(true);
                    return;
                }

                player = (Player) event.getEntity();
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onExit(EntityDismountEvent event) {
        if (this.player != null && event.getEntity() == this.player) {
            this.player = null;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (this.player != null && this.player == event.getPlayer()) {
            this.player.eject();
            this.player = null;
        }
    }

    @EventHandler
    public void onQuit(PlayerKickEvent event) {
        if (this.player != null && this.player == event.getPlayer()) {
            this.player.eject();
            this.player = null;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (this.player != null && event.getEntity() == this.player) {
            this.player.eject();
        }
    }

}
