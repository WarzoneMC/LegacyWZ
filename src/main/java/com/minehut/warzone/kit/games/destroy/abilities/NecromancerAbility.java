package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.warzone.util.chat.F;
import com.minehut.warzone.util.damage.DamageInfo;
import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.module.modules.scoreboard.ScoreboardModule;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.ColorConverter;
import com.minehut.warzone.util.Teams;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Created by lucas on 3/19/16.
 */
public class NecromancerAbility extends Ability {

    private ConcurrentLinkedQueue<NCreature> creatures = new ConcurrentLinkedQueue<>();

    public NecromancerAbility(Kit kit) {
        super(kit, "Raise Dead", ItemFactory.createItem(Material.ROTTEN_FLESH, ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "RAISE DEAD"), 20 * 40);
    }

    @Override
    public void onClick(Player player) {
        List<Location> possibleLocations = new ArrayList<>();
        TeamModule team = Teams.getTeamByPlayer(player).orNull();

        for (int x = -7; x < 7; x++) {
            for (int z = -7; z < 7; z++ ) {
                Location loop = player.getLocation().clone().add(x, -1, z);
                if (loop.getBlock().getType().isSolid() && loop.clone().add(0, 1, 0).getBlock().getType().equals(Material.AIR) &&
                        loop.clone().add(0, 2, 0).getBlock().getType().equals(Material.AIR))
                    possibleLocations.add(loop);
            }
        }

        if (possibleLocations.size() < 3) {
            F.message(player, ChatColor.RED + "There isn't enough space to spawn the mobs!");
            return;
        }

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int i = 0; i < 3; i++) {
            Location randomLocation = possibleLocations.get(rand.nextInt(0, possibleLocations.size() - 1));
            possibleLocations.remove(randomLocation);
            Creature creature = (Creature) player.getWorld().spawnEntity(randomLocation, rand.nextInt(0, 2) == 0 ? EntityType.ZOMBIE : EntityType.SKELETON);
            creature.setCustomName(team.getColor().toString() + player.getName() + ChatColor.GRAY + "'s " + (creature instanceof Zombie ? "Zombie" : "Skeleton") + " minion");
            setupCreature(creature, team, rand);
            creature.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30, 0));
            NCreature nCreature = new NCreature(creature, player, Bukkit.getScheduler().runTaskTimer(Warzone.getInstance(), () -> {
                LivingEntity target = null;
                for (LivingEntity next : creature.getWorld().getLivingEntities()) {
                    if (next == player || next == creature) continue;
                    if (next instanceof Player) {
                        if ((team.contains(next)) || Teams.isObserver((Player)next))
                            continue;
                        else {
                            boolean cont = false;
                            for (ScoreboardModule scoreboards : Warzone.getInstance().getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
                                if (scoreboards.getSimpleScoreboard().getScoreboard().getEntryTeam(((Player)next).getName()).getName().contains("_spy")) {
                                    if (next.getLocation().distance(creature.getLocation()) > 6)
                                        cont = true;
                                }
                            }
                            if (cont) continue;
                        }
                    } else if (next instanceof Creature) {
                        if (next.getCustomName().contains(team.getColor().toString()))
                            continue;
                        NCreature nCNext = getNCreature((Creature)next);
                        if (nCNext != null && team.contains(nCNext.player))
                            continue;
                    } else continue;
                    if (target == null || next.getLocation().distance(creature.getLocation()) < target.getLocation().distance(creature.getLocation())) {
                        target = next;
                    }
                }

                if (target != null)
                    creature.setTarget(target);
            }, 35L, 100L));
            creatures.add(nCreature);
        }

        super.putOnCooldown(player);
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (event.getEntity() instanceof Creature) {
            NCreature nCreature = getNCreature((Creature)event.getEntity());
            if (nCreature != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        DamageInfo info = new DamageInfo(event);
        if (info.getDamagerEntity() != null) {
            LivingEntity damager = info.getDamagerEntity();
            if (damager instanceof Player) {
                TeamModule team = Teams.getTeamByPlayer((Player) damager).orNull();
                if (info.getHurtPlayer() != null) {
                    Player hurt = info.getHurtPlayer();
                    if (team.contains(hurt)) {
                        event.setCancelled(true);
                    }
                } else if (info.getHurtEntity() != null) {
                    LivingEntity hurt = info.getHurtEntity();
                    if (hurt instanceof Creature) {
                        NCreature nCreature = getNCreature((Creature)hurt);
                        if (nCreature != null && team.contains(nCreature.player)) {
                            event.setCancelled(true);
                        }
                    }
                }
            } else if (damager instanceof Creature) {
                if (info.getHurtPlayer() != null) {
                    NCreature nCreature = getNCreature((Creature) damager);
                    if (nCreature != null) {
                        Player hurt = info.getHurtPlayer();
                        TeamModule team = Teams.getTeamByPlayer(hurt).orNull();
                        if (team.contains(nCreature.player)) {
                            event.setCancelled(true);
                        } else {
                            hurt.damage(event.getDamage(), nCreature.player);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Creature) {
            NCreature nCreature = getNCreature((Creature)event.getEntity());
            if (nCreature != null) {
                nCreature.task.cancel();
                creatures.remove(nCreature);
                event.getDrops().clear();
            }
        }
    }

    private void setupCreature(Creature creature, TeamModule team, ThreadLocalRandom rand) {
        ItemStack helmet = ItemFactory.createItem(Material.LEATHER_HELMET);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) helmet.getItemMeta();
        leatherArmorMeta.setColor(ColorConverter.getColor(team.getColor()));
        helmet.setItemMeta(leatherArmorMeta);
        creature.getEquipment().setHelmet(helmet);

        int randChestplate = rand.nextInt(0, 100);
        if (randChestplate > 17 && randChestplate < 22) {
            creature.getEquipment().setChestplate(ItemFactory.createItem(Material.CHAINMAIL_CHESTPLATE));
        } else if (randChestplate > 74 && randChestplate < 83) {
            creature.getEquipment().setChestplate(ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        }

        int randLeggings = rand.nextInt(0, 100);
        if (randLeggings > 44 && randLeggings < 49) {
            creature.getEquipment().setLeggings(ItemFactory.createItem(Material.CHAINMAIL_LEGGINGS));
        }

        int randBoots = rand.nextInt(0, 100);
        if (randBoots > 97) {
            creature.getEquipment().setBoots(ItemFactory.createItem(Material.IRON_BOOTS));
        } else if (randBoots > 55 && randBoots < 63) {
            creature.getEquipment().setBoots(ItemFactory.createItem(Material.CHAINMAIL_BOOTS));
        }
    }

    private NCreature getNCreature(Creature creature) {
        for (NCreature nc : creatures) {
            if (nc.creature == creature)
                return nc;
        }
        return null;
    }

    private NCreature getNCreature(Player player) {
        for (NCreature nc : creatures) {
            if (nc.player == player)
                return nc;
        }
        return null;
    }

    class NCreature {

        Creature creature;
        Player player;
        BukkitTask task;

        NCreature(Creature creature, Player player, BukkitTask task) {
            this.creature = creature;
            this.player = player;
            this.task = task;
        }

    }

}
