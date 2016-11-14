package com.minehut.warzone.kit.games.infected.infected.abilities;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Created by liamlutton on 4/16/16.
 */
public class CreeperAbility extends Ability {

    public CreeperAbility(Kit kit) {
        super(kit, "Bomb", ItemFactory.createItem(Material.STICK, ChatColor.RED + "Bomb"), 20 * 15);
    }

    @Override
    public void onClick(final Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 10, 10);

        TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation().clone().add(0,1,0), EntityType.PRIMED_TNT);
        tnt.setVelocity(player.getLocation().getDirection().multiply(1.2F));

        TeamModule team = Teams.getTeamByPlayer(player).get();

        tnt.setCustomName("Creeper Bomb " + team.getId());

        super.putOnCooldown(player);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        if(event.getEntity() instanceof TNTPrimed){
            if(event.getEntity().getCustomName().startsWith("Creeper Bomb ")){
                for(Player player : Bukkit.getServer().getOnlinePlayers()){
                    try {
                        TeamModule team = Teams.getTeamById(event.getEntity().getCustomName().replace("Creeper Bomb ", "")).orNull();
                        if (!team.equals(Teams.getTeamByPlayer(player))) {
                            if(player.getLocation().distance(event.getEntity().getLocation()) <= 4){
                                Random r = new Random(1);
                                player.setVelocity(new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble()));
                            }
                        }
                    }catch(NullPointerException ex){ }
                }
                event.setCancelled(true);
            }
        }
    }

}
