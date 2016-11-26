package com.minehut.warzone.module.modules.itemDrop;

import com.google.common.base.Optional;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.PlayerChangeTeamEvent;
import com.minehut.warzone.match.GameType;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.itemstack.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemDrop implements Module {
    private boolean dropBones;
    private ArrayList<Material> list = new ArrayList<>();

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public ItemDrop(boolean bones) {
        this.dropBones = bones;
    }

    public ItemDrop(boolean bones, ArrayList<Material> list) {
        this.dropBones = bones;
        this.list = list;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning()
                && (!team.isPresent() || !team.get().isObserver())) {
            dump(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSwitchTeam(PlayerChangeTeamEvent event) {
        if(GameHandler.getGameHandler().getMatch().getGameType() == GameType.INFECTED) return;

        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning()
                && (!team.isPresent() || !team.get().isObserver())) {
            dump(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();

        this.dump(event.getEntity());
    }

    @EventHandler
    public void onItemCombine(ItemMergeEvent event) {
        if (event.getTarget().getItemStack().getType() == Material.BONE) {
            event.setCancelled(true);
        }
    }

    private void dump(Player player) {

        if(GameHandler.getGameHandler().getMatch().getGameType() == GameType.INFECTED) {
            if (Teams.getTeamById("humans").get().contains(player)) {
                return; //cancel the dump
            }
        }

        if (!this.list.isEmpty()) {
            for (int i = 0; i < 36; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack != null && stack.getType() != null && this.list.contains(stack.getType())) {
                    player.getWorld().dropItemNaturally(player.getLocation().add(0, 0.5, 0), stack).setPickupDelay(20);
                }
            }
        }

        if (this.dropBones) {
            final ArrayList<Item> bones = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Item bone = player.getWorld().dropItemNaturally(player.getLocation().add(0, 0.5, 0), ItemFactory.createItem(Material.BONE));
                bone.setPickupDelay(120);
                bones.add(bone);
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (Item bone : bones) {
                        if (bone != null) {
                            bone.remove();
                        }
                    }
                }
            }, 40L);
        }

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

}
