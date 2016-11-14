package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.tnt.TntTracker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;


public class DemomanAbility extends Ability {
	
	ArrayList<TNTPrimed> tnts = new ArrayList<>();

    public DemomanAbility(Kit kit) {
        super(kit, "Throwing TNT", ItemFactory.createItem(Material.TNT, ChatColor.RED.toString() + ChatColor.BOLD + "THROWING TNT"), 20 * 12);
    }
  
    @Override
    public void onClick(Player player) {
    	final TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(new Location(player.getWorld(), player.getEyeLocation().getX(), player.getEyeLocation().getY() + 1, player.getEyeLocation().getZ()), EntityType.PRIMED_TNT);
		TntTracker.setOwner(tnt, player);
    	tnt.setVelocity(player.getLocation().getDirection().multiply(1.1));
    	tnts.add(tnt);
    	TntTracker.setOwner(tnt, player);

		player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 10, 1);
    	
        super.putOnCooldown(player);
    }
    
    @EventHandler
    public void onExplode(EntityExplodeEvent e){
    	if(e.getEntity() instanceof TNTPrimed){
    		if(tnts.contains(e.getEntity())){
				ArrayList<Block> toRemove = new ArrayList<>();

    			for(Block b : e.blockList()) {
    				if(b.getType() == Material.WOOD || b.getType() == Material.COBBLESTONE_STAIRS || b.getType() == Material.WOOD_STAIRS){

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

							fb.setVelocity(new Vector(xV,yV,zV));
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


    @Override
    public void extraUnload() {
        this.tnts.clear();
    }
	
}
