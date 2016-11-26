package com.minehut.warzone.kit.games.tdm.abilities;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by liamlutton on 4/10/16.
 */
public class TeleporterAbility extends Ability {

    public TeleporterAbility(Kit kit, int slot) {
        super(kit, "Teleportation Wand", ItemFactory.createItem(Material.BLAZE_ROD, ChatColor.RED.toString() + ChatColor.BOLD + "TELEPORTATION WAND"), 20 * 6);

        super.cooldownAlerts.offCooldownAlert = false;
    }

    @Override
    public void onClick(Player player) {
        Location f = player.getLineOfSight((Set<Material>)null, 6).get(4).getLocation();
        player.teleport(f);
        player.getWorld().playSound(f, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
        super.putOnCooldown(player);
    }
}