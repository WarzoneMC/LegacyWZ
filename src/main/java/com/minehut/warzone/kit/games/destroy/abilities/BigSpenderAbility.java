package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.Teams;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Created by lucas on 3/19/16.
 */
public class BigSpenderAbility extends Ability {

    private Map<Item, Player> coins = new HashMap<>();

    public BigSpenderAbility(Kit kit) {
        super(kit, "Coin Toss", ItemFactory.createItem(Material.DOUBLE_PLANT, ChatColor.GOLD + "COIN TOSS"), 20 * 4);
    }

    @Override
    public void onClick(Player player) {
        Item coin = player.getWorld().dropItemNaturally(player.getEyeLocation(), ItemFactory.createItem(Material.DOUBLE_PLANT));
        coins.put(coin, player);
        coin.setPickupDelay(0);
        coin.setVelocity(player.getEyeLocation().getDirection().multiply(1.5));
//        Warzone.getInstance().getUserManager().getUser(player).addCoins(-1, "Coin Toss");
        super.putOnCooldown(player);
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().getType().equals(Material.DOUBLE_PLANT) && coins.containsKey(event.getItem())) {
            event.setCancelled(true);
            Player coinThrower = coins.get(event.getItem());
            if (Teams.getTeamByPlayer(coinThrower).orNull().contains(event.getPlayer())) {
                return;
            }
            Player player = event.getPlayer();
            player.damage(4, coinThrower);
            coins.remove(event.getItem());
            player.setVelocity(event.getItem().getVelocity().setY(0.7).multiply(ThreadLocalRandom.current().nextDouble(0.5, 0.9)));
            event.getItem().remove();
        }
    }

}
