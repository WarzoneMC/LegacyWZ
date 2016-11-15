package com.minehut.warzone.module.modules.team;

import com.google.common.base.Optional;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.event.PlayerAttemptChangeTeamEvent;
import com.minehut.warzone.event.PlayerChangeTeamEvent;
import com.minehut.warzone.event.PlayerNameUpdateEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class TeamModule<P extends Player> extends ArrayList<Player> implements Module {

    private final Match match;
    private final String id;
    private final boolean observer;
    private String name;
    private int min;
    private int max;
    private int maxOverfill;
    private int respawnLimit;
    private ChatColor color;
    private boolean ready;
    private boolean allowJoin = true;

    public TeamModule(Match match, String name, String id, int min, int max, int maxOverfill, int respawnLimit, ChatColor color, boolean observer) {
        this.match = match;
        this.name = name;
        this.id = id;
        this.min = min;
        this.max = max;
        this.maxOverfill = maxOverfill;
        this.respawnLimit = respawnLimit;
        this.color = color;
        this.observer = observer;
        this.ready = false;
    }

    public boolean add(Player player, boolean force, boolean message) {
        if (!force && size() >= max) {
            player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_TEAM_FULL, getCompleteName() + ChatColor.RED)).getMessage(player.spigot().getLocale()));
            return false;
        }

        Optional<TeamModule> oldTeam = Teams.getTeamByPlayer(player);
        boolean cancel = false;
        if (oldTeam != null) {
            PlayerAttemptChangeTeamEvent attemptEvent = new PlayerAttemptChangeTeamEvent(player, Optional.<TeamModule>of(this), oldTeam);
            Bukkit.getPluginManager().callEvent(attemptEvent);
            cancel = attemptEvent.isCancelled();

            if (cancel) {
                player.sendMessage(attemptEvent.getCancelMessage());
            }
        }

        player.setPlayerListName(this.color + player.getName());
        player.setDisplayName(this.color + player.getName());
        Bukkit.getPluginManager().callEvent(new PlayerNameUpdateEvent(player));

        if(!cancel || this.isObserver()) {
            PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(player, force, Optional.<TeamModule>of(this), oldTeam);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (message && event.getNewTeam().isPresent()) {
                event.getPlayer().sendMessage(ChatColor.WHITE + new LocalizedChatMessage(ChatConstant.GENERIC_JOINED, event.getNewTeam().get().getCompleteName()).getMessage(event.getPlayer().spigot().getLocale()));
            } else if (message) {
                event.getPlayer().sendMessage(ChatColor.WHITE + new LocalizedChatMessage(ChatConstant.GENERIC_JOINED, ChatConstant.MISC_MATCH.asMessage()).getMessage(event.getPlayer().spigot().getLocale()));
            }
            return !event.isCancelled() || force;
        }

        return true;
    }

    public boolean add(Player player, boolean force) {
        return this.add(player, force, true);
    }

    public boolean add(Player player) {
        return this.add(player, false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTeamSwitch(PlayerChangeTeamEvent event) {
        if (!event.isCancelled()) {
            this.remove(event.getPlayer());
        }
        if (event.getNewTeam().orNull() == this) {
            super.add(event.getPlayer());
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public String getCompleteName() {
        return this.color + this.name;
    }

    public Match getMatch() {
        return match;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMaxOverfill() {
        return maxOverfill;
    }

    public void setMaxOverfill(int maxOverfill) {
        this.maxOverfill = maxOverfill;
    }

    public int getRespawnLimit() {
        return respawnLimit;
    }

    public void setRespawnLimit(int respawnLimit) {
        this.respawnLimit = respawnLimit;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public boolean isObserver() {
        return observer;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public boolean equals(Object obj){
        return super.equals(obj) && obj instanceof TeamModule && ((TeamModule) obj).getId().equals(this.id);
    }

    public boolean isAllowJoin() {
        return allowJoin;
    }

    public void setAllowJoin(boolean allowJoin) {
        this.allowJoin = allowJoin;
    }
}
