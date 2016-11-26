package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.warzone.util.LocationUtils;
import com.minehut.warzone.util.chat.S;
import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.particles.ParticleEffect;
import com.minehut.warzone.util.particles.ParticleUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MedicAbility extends Ability {
	public int zoneRadius = 5;

	public MedicAbility(Kit kit) {
		//TODO Update cool-down time.
		super(kit, "Healing Zone", ItemFactory.createItem(Material.GOLD_INGOT, ChatColor.RED.toString() + ChatColor.BOLD + "HEAL ZONE"), 20 * 40);
	}

	@Override
	public void onClick(Player player) {

		final TeamModule team = Teams.getTeamByPlayer(player).orNull();

        S.playSound(player, Sound.ENTITY_VILLAGER_YES);

		final Item item = player.getWorld().dropItemNaturally(player.getEyeLocation(), new ItemStack(Material.WOOL, 1));
//		com.minehut.tabbed.item.getItemStack().setData(new MaterialData(GameAPI.getMatch().game.getTeam(player).getBlockData()));
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setVelocity(player.getVelocity().multiply(.1));

		for(int i = 0; i < 20; i++){

			final int i2 = i;

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), () -> {
                if (item != null) {
                    ParticleEffect.HEART.display(0, 0, 0, 0, 3, new Location(item.getLocation().getWorld(), item.getLocation().getX(), item.getLocation().getY() + .5, item.getLocation().getZ()), 80);

                    ParticleEffect teamParticle;
                    if (team.getColor() == ChatColor.BLUE) {
                        teamParticle = ParticleEffect.DRIP_WATER;
                    } else {
                        teamParticle = ParticleEffect.DRIP_LAVA;
                    }

                    ParticleUtils.circle(item.getLocation(), teamParticle, zoneRadius, 30);
                    ParticleUtils.circle(new Location(item.getWorld(), item.getLocation().getX(), item.getLocation().getY() + 1, item.getLocation().getZ()), teamParticle, zoneRadius, 30);
                    ParticleUtils.circle(new Location(item.getWorld(), item.getLocation().getX(), item.getLocation().getY() + 2, item.getLocation().getZ()), teamParticle, zoneRadius, 30);
                    ParticleUtils.circle(new Location(item.getWorld(), item.getLocation().getX(), item.getLocation().getY() + 3, item.getLocation().getZ()), teamParticle, zoneRadius, 30);

                    ParticleUtils.circle(item.getLocation(), ParticleEffect.SMOKE_LARGE, zoneRadius, 30);
                    ParticleUtils.circle(new Location(item.getWorld(), item.getLocation().getX(), item.getLocation().getY() + 1, item.getLocation().getZ()), ParticleEffect.SMOKE_LARGE, zoneRadius, 30);
                    ParticleUtils.circle(new Location(item.getWorld(), item.getLocation().getX(), item.getLocation().getY() + 2, item.getLocation().getZ()), ParticleEffect.SMOKE_LARGE, zoneRadius, 30);
                    ParticleUtils.circle(new Location(item.getWorld(), item.getLocation().getX(), item.getLocation().getY() + 3, item.getLocation().getZ()), ParticleEffect.SMOKE_LARGE, zoneRadius, 30);

                    for (Entity entity : item.getNearbyEntities(zoneRadius, zoneRadius, zoneRadius)) {
                        if (entity instanceof Player) {
                            Player pl = (Player) entity;

                            if (Teams.getTeamByPlayer(pl).orNull().equals(team)) {


                                if (entity.getLocation().distance(item.getLocation()) <= zoneRadius) {

                                    ParticleEffect.HEART.display(0, 0, 0, 0, 3, new Location(pl.getWorld(), pl.getLocation().getX(), pl.getEyeLocation().getY() + .5, pl.getLocation().getZ()), 80);

                                    if (pl.getHealth() <= 18) {
                                        pl.setHealth(pl.getHealth() + 2);
                                    } else {
                                        pl.setHealth(20);
                                    }

                                    for (double n = 0; n < (10 * pl.getLocation().distance(item.getLocation())); n = n + 1) {
                                        Location li = item.getLocation();
                                        Location lp = LocationUtils.add(pl.getEyeLocation(), 0, -.1, 0);

                                        double xDif;
                                        double yDif;
                                        double zDif;

                                        //Just FYI luke, if this doesn't work I will comment the code:

                                        //I have to check which one is bigger as distance is not going to work for adding as it can't be negative
                                        if (li.getX() > lp.getX()) {
                                            xDif = (new Location(li.getWorld(), li.getX(), 0, 0).distance(new Location(li.getWorld(), lp.getX(), 0, 0)));
                                        } else {
                                            //So this is the negative one.
                                            xDif = -1 * (new Location(li.getWorld(), li.getX(), 0, 0).distance(new Location(li.getWorld(), lp.getX(), 0, 0)));
                                        }

                                        if (li.getY() > lp.getY() + .3) {
                                            yDif = (new Location(li.getWorld(), 0, li.getY(), 0).distance(new Location(li.getWorld(), 0, lp.getY() + .3, 0)));
                                        } else {
                                            yDif = -1 * (new Location(li.getWorld(), 0, li.getY(), 0).distance(new Location(li.getWorld(), 0, lp.getY() + .3, 0)));
                                            if (li.getY() > lp.getY() + 0.75) {
                                                yDif = (new Location(li.getWorld(), 0, li.getY(), 0).distance(new Location(li.getWorld(), 0, lp.getY() + 0.75, 0)));
                                            } else {
                                                yDif = -1 * (new Location(li.getWorld(), 0, li.getY(), 0).distance(new Location(li.getWorld(), 0, lp.getY() + 0.75, 0)));
                                            }

                                            if (li.getZ() > lp.getZ()) {
                                                zDif = (new Location(li.getWorld(), 0, 0, li.getZ()).distance(new Location(li.getWorld(), 0, 0, lp.getZ())));
                                            } else {
                                                zDif = -1 * (new Location(li.getWorld(), 0, 0, li.getZ()).distance(new Location(li.getWorld(), 0, 0, lp.getZ())));
                                            }

                                            Location particleLocation = new Location(
                                                    li.getWorld(),
                                                    li.getX() - (xDif / (n / 10)),
                                                    li.getY() - (yDif / (n / 10)),
                                                    li.getZ() - (zDif / (n / 10))
                                            );

                                            ParticleEffect.REDSTONE.display(0, 0, 0, 1, 2, particleLocation, 80);
                                        }
                                    }

                                }
                            }

                            if (i2 >= 19) {
                                item.remove();
                            }

                        }
                    }
                }
            }, i * 10);
		}

		super.putOnCooldown(player);
	}
}