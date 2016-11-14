package com.minehut.warzone.kit.games.destroy.abilities;

import com.minehut.cloud.bukkit.util.LocationUtils;
import com.minehut.cloud.bukkit.util.chat.S;
import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.warzone.kit.Ability;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.cheat.CheatUtils;
import net.minecraft.server.v1_10_R1.EntityFallingBlock;
import net.minecraft.server.v1_10_R1.PacketPlayOutSpawnEntity;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class TankAbility extends Ability {

	private ArrayList<Player> thrownUp = new ArrayList<>();

	public TankAbility(Kit kit) {
		super(kit, "Ground Slam", ItemFactory.createItem(Material.IRON_INGOT, ChatColor.RED + ChatColor.BOLD.toString() + "GROUND SLAM"), 20 * 12);
	}

	@Override
	public void onClick(Player player) {

//		S.playSound(player, Sound.ENTITY_ENDERDRAGON_FLAP);
//		CheatUtils.exemptFlight(player, 60);
//		player.setVelocity(new Vector(0, 1.5, 0));

//		thrownUp.add(player);

		Location b = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ());

		player.getWorld().playEffect(b, Effect.STEP_SOUND, b.getBlock().getType());

		for (Location loc : LocationUtils.getCircle(b, 1, 4)) {
			player.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType());
		}

		for (Location loc : LocationUtils.getCircle(b, 5, 40)) {
			player.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType());
		}

		//play sound for tank
		S.playSound(player, Sound.ENTITY_ENDERDRAGON_HURT);

		for(Entity e : player.getNearbyEntities(5, 2, 5)){
			if(e instanceof Player){
				Player p = (Player)e;

				if (!Teams.getTeamByPlayer(player).orNull().contains(p)) {
					S.playSound(p, Sound.ENTITY_ENDERDRAGON_HURT);
					CheatUtils.exemptFlight(p, 60); //3 second knockup
					p.setVelocity(new Vector(0, 1, 0));
				}
			}
		}

		super.putOnCooldown(player);

	}

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player)
			if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
				if(thrownUp.contains((Player)event.getEntity())) {
					thrownUp.remove((Player)event.getEntity());
					event.setDamage(0);
					knockBackNear((Player)event.getEntity());
					startParticles((Player)event.getEntity());
				}
	}

	private void spawnBlock(Location loc){
		ArrayList<Player> players = new ArrayList<>();
		Material mat = loc.getBlock().getType();
		byte data = loc.getBlock().getData();

		loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 1, 1);

		net.minecraft.server.v1_10_R1.World world = ((CraftWorld) loc.getWorld()).getHandle();

		EntityFallingBlock entityfallingblock = new EntityFallingBlock(world);

		entityfallingblock.setLocation(loc.getX(), loc.getY()+.9, loc.getZ(), 0, 0);

		PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(entityfallingblock, 70, mat.getId() + (data << 12));
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.getLocation().distance(loc) <= 70)
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}

	}

	private void startParticles(Player player){
//		Location pLoc = player.getLocation().clone().add(0,-1,0).getBlock().getLocation();
//		if(!pLoc.equals(null)){
//			spawnBlock(pLoc);
//			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable(){
//				@Override
//				public void run(){
//					if(pLoc == null || !pLoc.getChunk().isLoaded()) return;
//					for(int x = -4; x <= 4; x++){
//						for(int z = -4; x <= 4; z++){
//							Location loc = pLoc.clone().add(x, 0, z).getBlock().getLocation();
//							if(loc.getBlock().getType().isSolid())
//								if(loc.distance(pLoc) > 0 && loc.distance(pLoc) <= 1.1){
//									spawnBlock(loc);
//								}
//						}
//					}
//				}
//			}, 3);
//			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable(){
//				@Override
//				public void run(){
//					for(int x = -4; x <= 4; x++){
//						for(int z = -4; x <= 4; z++){
//							Location loc = pLoc.clone().add(x, 0, z).getBlock().getLocation();
//							if(loc.getBlock().getType().isSolid())
//								if(loc.distance(pLoc) > 1.1 && loc.distance(pLoc) <= 2.1){
//									spawnBlock(loc);
//								}
//						}
//					}
//				}
//			}, 10);
//		}
	}

	private void knockBackNear(Player player){

	}

	@Override
	public void extraUnload(){
		thrownUp.clear();
	}

}