package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.NMSUtil;
import com.minehut.warzone.util.Players;
import com.minehut.warzone.util.itemstack.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.weather.LightningStrikeEvent;

/*
 * Created by lucas on 3/30/16.
 */
public class ThorAbility extends Ability {

    public ThorAbility(Kit kit) {
        super(kit, "Lightning Strike", ItemFactory.createItem(Material.STONE_AXE, ChatColor.GOLD + "Strikedown " + ChatColor.GRAY + "(Right Click)"), 20 * 30);
    }

    @Override
    public void onRightClick(Player player) {
        Block target = Players.getTargetBlock(player, 30);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Could not find a block to strike in that direction!");
        } else {
            LightningStrike strike = target.getWorld().strikeLightning(target.getLocation().clone().add(0, 1, 0));
            strike.getNearbyEntities(30, 50, 30).forEach(e -> {
                if (e instanceof Player) {
                    Player p = (Player)e;
                    NMSUtil.sendLightning(p, strike.getLocation());
                }
            });
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onStrike(LightningStrikeEvent event) {
        event.setCancelled(false);
        Bukkit.getScheduler().runTaskLater(Warzone.getInstance(), () -> event.getLightning().getLocation().getBlock().setType(Material.AIR), 2L);
    }

}
