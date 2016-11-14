package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.cloud.bukkit.util.LocationUtils;
import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.cloud.bukkit.util.particles.ParticleEffect;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.ColorConverter;
import com.minehut.warzone.util.InstantFirework;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InfiltratorAbility extends Ability {

    public InfiltratorAbility(Kit kit) {
        super(kit, "Speed Boost", ItemFactory.createItem(Material.WOOD_AXE, ChatColor.RED.toString() + ChatColor.BOLD + "SPEED BOOST"), 20 * 7);
    }
  
    @Override
    public void onRightClick(final Player player) {
		TeamModule team = Teams.getTeamByPlayer(player).orNull();

    	player.removePotionEffect(PotionEffectType.SPEED);

		new InstantFirework(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(ColorConverter.getColor(team.getColor())).build(), player.getLocation());
    	
    	player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1, 1);
    	
    	player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*3, 1));

		player.sendMessage(ChatColor.GRAY + "You activated " + ChatColor.AQUA + "Speed Boost! " + ChatColor.GRAY + "(3 seconds)");

		for(int i = 0; i < 30; i++){
    		
    		final int n = i;
    	
    		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), () -> {

                ParticleEffect.VILLAGER_HAPPY.display(0, 0, 0, 0, 3, LocationUtils.add(player.getEyeLocation(), 0, -.75, 0), 40);

                if(n == 29){
                    player.removePotionEffect(PotionEffectType.SPEED);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                }

            }, i*2);
    		
    	
    	}
        
        super.putOnCooldown(player);
        
    }
	
}
