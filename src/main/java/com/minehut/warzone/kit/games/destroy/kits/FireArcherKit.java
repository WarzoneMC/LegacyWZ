package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.SlotType;
import com.minehut.warzone.kit.KitManager;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.particles.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Created by Liam on 3/20/16.
 */
public class FireArcherKit extends Kit {

    public FireArcherKit() {

        super("Fire Archer", 300, "Chance at shooting burning arrows!", Material.BLAZE_POWDER);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));

        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setItem(2, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(3, ItemFactory.createItem(Material.WOOD, 64));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.LEATHER_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event){
        if (event.getEntity() instanceof Player && event.getForce() >= 1.0) {
            Player player = (Player) event.getEntity();
            if (KitManager.isUsingKit(player, this)) {
                Random r = new Random();
                int chance = r.nextInt(20) + 1;
                if (chance > 3 && chance <= 10) {
                    event.getProjectile().setFireTicks(400);
                }
//                else if (chance <= 3) {
//                    event.setCancelled(true);
//                    Fireball df = (Fireball) player.getWorld().spawnEntity(player.getLocation().clone().add(0, 1, 0), EntityType.FIREBALL);
//                    df.setShooter(player);
//                    df.setCustomName("__firearcher_fireball_shot__");
//                    df.setCustomNameVisible(false);
//                    df.setVelocity(player.getLocation().getDirection());
//                }
            }
        }
    }


    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        if(event.getEntity() instanceof Fireball) {
            Fireball df = (Fireball) event.getEntity();
            if(df.getShooter() instanceof Player)
            if(df.getCustomName().equals("__firearcher_fireball_shot__")){
                ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, df.getLocation(), 100);
                df.getWorld().playSound(df.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL, 100, 100);
                Player player = (Player) df.getShooter();
                for(Entity n : df.getNearbyEntities(4, 4, 4)) {
                    if (n instanceof Player) {
                        Player np = (Player) n;
                        if (!Teams.getTeamByPlayer(np).equals(Teams.getTeamByPlayer(player))) {
                            np.setFireTicks(20 * 10);
                            np.damage(7, player);
                        }
                    } else if (n instanceof Creature) {
                        Creature nc = (Creature) n;
                        if (nc.getCustomName() != null && nc.getCustomName().contains(Teams.getTeamByPlayer(player).orNull().getColor().toString()))
                            continue;
                        nc.setFireTicks(20 * 10);
                        nc.damage(7, player);
                    }
                }

                /*
* (throws 1/3 in air)
*/
                event.blockList().stream().filter(b -> b.getType() == Material.WOOD || b.getType() == Material.COBBLESTONE_STAIRS || b.getType() == Material.WOOD_STAIRS).forEach(b -> {

						/*
                         * (throws 1/3 in air)
    					 */

                    Random rn = new Random();
                    if ((rn.nextInt(9) + 1) <= 3) {
                        Material m = b.getType();
                        byte by = b.getData();
                        b.setType(Material.AIR);

                        FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), m, by);

                        Random r = new Random();

                        double xV = -1.1 + (1.5 - -1.1) * r.nextDouble();
                        double yV = .4 + (1 - .4) * r.nextDouble();
                        double zV = -1.1 + (1.5 - -1.1) * r.nextDouble();
                    }
                });
                event.setCancelled(true);
            }
        }
    }


    @Override
    public void onApply(Player player, ChatColor chatColor) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
    }

    @Override
    public void extraUnload() {

    }
}
