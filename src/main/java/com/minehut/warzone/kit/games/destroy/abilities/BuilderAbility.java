package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.InstantFirework;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
 * Created by Gamer on 12/14/15
 */
public class BuilderAbility extends Ability {

	public BuilderAbility(Kit kit) {
		super(kit, "Super Breaker", ItemFactory.createItem(Material.FEATHER, ChatColor.RED + "Super Breaker"), 20 * 10);
	}
	
	@Override
	public void onClick(final Player player) {
		player.sendMessage(ChatColor.GRAY + "You activated " + ChatColor.GREEN + "Super Breaker" + ChatColor.GRAY + " (5 seconds)");

		new InstantFirework(FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.YELLOW).build(), player.getLocation());

		player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 5, 1));
		super.putOnCooldown(player);
	}

}
