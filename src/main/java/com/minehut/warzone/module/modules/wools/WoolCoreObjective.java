package com.minehut.warzone.module.modules.wools;

import com.minehut.cloud.core.Cloud;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.event.objective.ObjectiveCompleteEvent;
import com.minehut.warzone.module.GameObjective;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.user.MatchUser;
import com.minehut.warzone.user.WarzoneUser;
import com.minehut.warzone.util.*;
import com.minehut.warzone.module.modules.regions.RegionModule;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by luke on 2/17/16.
 */
public class WoolCoreObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final DyeColor color;

    public static int COINS_DESTROY = 5;
    public static int XP_DESTROY = 5;


    private RegionModule region;

    private Set<UUID> playersTouched;
    private boolean complete = false;

    private int health;
    private int maxHealth;

    /**
     * Reminder that this objective is the RED WOOL and for the BLUE TEAM.
     */

    protected WoolCoreObjective(final TeamModule team, final String name, final String id, final RegionModule region, final DyeColor color, final int health) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.color = color;
        this.region = region;
        this.health = health;
        this.maxHealth = health;


        this.playersTouched = new HashSet<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockExplode(BlockExplodeEvent event) {
        if (event.getBlock().getType().equals(Material.WOOL)) {
            if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getType() == Material.WOOL) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.WOOL)) {
            if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                if (Teams.getTeamByPlayer(event.getPlayer()).orNull() == team) {

                    if (this.region == null) {
                        this.health -= 1;

                        if (health <= 0) {
                            this.complete = true;
                        }

                        TeamModule playerTeam = Teams.getTeamByPlayer(event.getPlayer()).get();

                        ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.WHITE + "{0}", new LocalizedChatMessage(ChatConstant.UI_WOOL_CORE_DESTROYED, name + ChatColor.WHITE, playerTeam.getColor() + event.getPlayer().getName() + ChatColor.WHITE)));

                        Fireworks.spawnFirework(event.getPlayer().getLocation(), event.getPlayer().getWorld(), MiscUtil.convertChatColorToColor(MiscUtil.convertDyeColorToChatColor(color)));


                        Color c = Color.RED;
                        if (team.getColor() == ChatColor.BLUE) {
                            c = Color.BLUE;
                        }

                        new InstantFirework(FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(c).build(), event.getBlock().getLocation());

                        WarzoneUser user = Warzone.getInstance().getUserManager().getUser(event.getPlayer());
                        MatchUser matchUser = Warzone.getInstance().getUserManager().getMatchUser(event.getPlayer());

                        Cloud.getInstance().getPlayerManager().addCoins(user.getPlayer().getName(), COINS_DESTROY, "Destroy a Wool", true);

                        matchUser.addCoinsEarned(COINS_DESTROY);

                        user.setXp(user.getXp() + XP_DESTROY);
                        matchUser.setXpEarned(matchUser.getXpEarned() + XP_DESTROY);

                        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 0, false, false));
                        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1, false, false));

                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                        event.setCancelled(false);

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (playerTeam.contains(player)) {
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
                            } else {
                                player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 2, 1);
                            }
                        }
                    }


                }
            }
        }
    }

    @Override
    public TeamModule getTeam() {
        return team;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    public RegionModule getRegion() {
        return region;
    }

    public Set<UUID> getPlayersTouched() {
        return playersTouched;
    }

    @Override
    public boolean isTouched() {
        return false;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean showOnScoreboard() {
        return true;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void unload() {

    }
}
