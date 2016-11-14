package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.cloud.bukkit.util.chat.S;
import com.minehut.cloud.bukkit.util.damage.DamageInfo;
import com.minehut.cloud.bukkit.util.itemstack.EnchantGlow;
import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.cloud.bukkit.util.particles.ParticleEffect;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

/**
 * Created by luke on 10/20/15.
 */
public class BarbarianAbility extends Ability {
    int slot;
    ArrayList<Player> players = new ArrayList<>();

    public BarbarianAbility(Kit kit, int slot) {
        super(kit, "Brute Force", ItemFactory.createItem(Material.FIREWORK_CHARGE, ChatColor.RED.toString() + ChatColor.BOLD + "BRUTE FORCE"), 20 * 15);
        this.slot = slot;

        super.cooldownAlerts.offCooldownAlert = false;
    }
  
    @Override
    public void onClick(Player player) {
        if (!this.players.contains(player)) {
            this.players.add(player);
        }
        player.sendMessage("Your next attack will deal " + ChatColor.RED + "bonus damage");
        super.putOnCooldown(player);
        EnchantGlow.addGlow(player.getInventory().getItem(this.slot));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        DamageInfo damageInfo = new DamageInfo(event);

        if (damageInfo.getDamagerPlayer() != null) {
            if (this.players.contains(damageInfo.getDamagerPlayer())) {
                this.players.remove(damageInfo.getDamagerPlayer());
                event.setDamage(event.getDamage() + 2);
                ParticleEffect.LAVA.display(.2f, .1f, .2f, 1f, 20, damageInfo.getHurtEntity().getEyeLocation(), 15);
                S.playSound(damageInfo.getDamagerPlayer(), Sound.ENTITY_SKELETON_HURT);
                damageInfo.getDamagerPlayer().getInventory().setItem(this.slot, super.itemStack);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (this.players.contains(event.getPlayer())) {
            this.players.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (this.players.contains(event.getPlayer())) {
            this.players.remove(event.getPlayer());
        }
    }

    @Override
    public void extraUnload() {
        this.players.clear();
    }
}