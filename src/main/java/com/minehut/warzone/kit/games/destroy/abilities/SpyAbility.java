package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.warzone.util.ActionBar;
import com.minehut.warzone.util.damage.DamageInfo;
import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.KitManager;
import com.minehut.warzone.util.InstantFirework;
import com.minehut.warzone.util.Teams;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by lucas on 4/28/16.
 */
public class SpyAbility extends Ability {

    private Kit kit;
    private ConcurrentLinkedQueue<Player> disguised = new ConcurrentLinkedQueue<>();

    public SpyAbility(Kit kit) {
        super(kit, "Disguise", ItemFactory.createItem(Material.INK_SACK, ChatColor.WHITE + "Disguise"), 20 * 25);
        this.kit = kit;
    }

    @Override
    public void onClick(Player player) {
        Color alternate;
        if (Teams.getTeamByPlayer(player).get().getColor() == ChatColor.RED) {
            alternate = Color.BLUE.mixDyes(DyeColor.LIGHT_BLUE);
        } else {
            alternate = Color.RED.mixColors(Color.BLACK).mixColors(Color.RED);
        }

        new InstantFirework(FireworkEffect.builder().withColor(alternate).build(), player.getLocation());

        ItemStack helmet = ItemFactory.createItem(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(alternate);
        helmet.setItemMeta(helmetMeta);
        player.getInventory().setHelmet(helmet);

        ItemStack chestplate = ItemFactory.createItem(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.setColor(alternate);
        chestplate.setItemMeta(chestplateMeta);
        player.getInventory().setChestplate(chestplate);

        ItemStack leggings = ItemFactory.createItem(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setColor(alternate);
        leggings.setItemMeta(leggingsMeta);
        player.getInventory().setLeggings(leggings);

        ItemStack boots = ItemFactory.createItem(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(alternate);
        boots.setItemMeta(bootsMeta);
        player.getInventory().setBoots(boots);

        disguised.add(player);
//        player.setCustomName(alternate == Color.RED ? C.dred + player.getName() : C.dblue + player.getName());
//        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
//            scoreboard.getSimpleScoreboard().getScoreboard().getTeam(Teams.getTeamByPlayer(player).get().getId() + "_spy").addEntry(player.getName());
//        }
        super.putOnCooldown(player);

        for (int i = 0; i < 40; i++) {
            final int n = i;
            Bukkit.getScheduler().runTaskLater(Warzone.getInstance(), () -> {
                if (n == 39) {
                    new ActionBar(ChatColor.RED + "No longer disguised!").sendToPlayer(player);

                    if (!Teams.isObserver(player)) {
                        Color current;
                        if (Teams.getTeamByPlayer(player).get().getColor() == ChatColor.RED) {
                            current = Color.RED;
                        } else {
                            current = Color.BLUE;
                        }

                        helmetMeta.setColor(current);
                        helmet.setItemMeta(helmetMeta);
                        player.getInventory().setHelmet(helmet);

                        chestplateMeta.setColor(current);
                        chestplate.setItemMeta(chestplateMeta);
                        player.getInventory().setChestplate(chestplate);

                        leggingsMeta.setColor(current);
                        leggings.setItemMeta(leggingsMeta);
                        player.getInventory().setLeggings(leggings);

                        bootsMeta.setColor(current);
                        boots.setItemMeta(bootsMeta);
                        player.getInventory().setBoots(boots);

                        new InstantFirework(FireworkEffect.builder().withColor(current).build(), player.getLocation());

//                        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
//                            scoreboard.getSimpleScoreboard().getScoreboard().getTeam(Teams.getTeamByPlayer(player).get().getId()).addEntry(player.getName());
//                        }
                    }
                    disguised.remove(player);
                } else {
                    String loadingBar = ChatColor.GREEN.toString();
                    for (int j = n; j < 40; j ++) {
                        loadingBar += ":";
                    }
                    loadingBar += ChatColor.RED;
                    for (int j = 0; j < n; j ++) {
                        loadingBar += ":";
                    }

                    new ActionBar(loadingBar).sendToPlayer(player);
                }
            }, n * 5L);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        DamageInfo damageInfo = new DamageInfo(event);
        if (damageInfo.getHurtPlayer() != null && damageInfo.getDamagerPlayer() != null && damageInfo.getProjectile() == null) {
            Player damager = damageInfo.getDamagerPlayer();
            Player hurt = damageInfo.getHurtPlayer();
            if (KitManager.isUsingKit(damager, kit) && disguised.contains(damager)) {
                if (isWithinAngle(damager.getEyeLocation().getYaw(), hurt.getEyeLocation().getYaw(), 30)) {
                    event.setDamage(event.getDamage() * 2.0);
                    damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1F, 1F);
                    damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1F, 1F);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof PlayerInventory && event.getWhoClicked() instanceof Player && disguised.contains(event.getWhoClicked())) {
            Player player = (Player)event.getWhoClicked();
            if (disguised.contains(player) && event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isWithinAngle(float yaw1, float yaw2, int range) {
        yaw1 += 180;
        yaw2 += 180;
        return yaw1 > yaw2 - range && yaw1 < yaw2 + range;
    }

}
