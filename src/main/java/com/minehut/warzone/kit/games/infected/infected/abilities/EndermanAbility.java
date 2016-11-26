package com.minehut.warzone.kit.games.infected.infected.abilities;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.Players;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by lucas on 4/17/16.
 */
public class EndermanAbility extends Ability {

    public EndermanAbility(Kit kit) {
        super(kit, "Teleport", ItemFactory.createItem(Material.ENDER_PEARL, ChatColor.RED + "Teleport"), 15 * 20);
    }

    @Override
    public void onClick(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
        player.teleport(Players.getTargetBlock(player, 9).getLocation());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
        super.putOnCooldown(player);
    }

}
