package com.minehut.warzone.module.modules.killStreakCount;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.event.CardinalDeathEvent;
import com.minehut.warzone.event.CardinalSpawnEvent;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Messages;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.metadata.LazyMetadataValue;

public class KillStreakCounter implements Module {


    protected KillStreakCounter() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerKill(CardinalDeathEvent event) {
        if (event.getKiller() != null && event.getKiller().getHealth() > 0) {
            int old = event.getKiller().getMetadata("killstreak").get(0).asInt();
            int ks = old + 1;

            event.getKiller().removeMetadata("killstreak", Warzone.getInstance());
            event.getKiller().setMetadata("killstreak", new LazyMetadataValue(Warzone.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new KillStreak(ks)));

            if (ks < 4) {
                return;
            }

            TeamModule team = Teams.getTeamByPlayer(event.getKiller()).orNull();

            if (ks == 5) {
                Bukkit.broadcastMessage(Messages.TAB + team.getColor() + event.getKiller().getName() + ChatColor.WHITE + " is on a " + ChatColor.GREEN + "5 kill streak!");
            }
            else if (ks == 10) {
                Bukkit.broadcastMessage(Messages.TAB + team.getColor() + event.getKiller().getName() + ChatColor.WHITE + " is on a " + ChatColor.RED + ChatColor.BOLD + "10 KILL STREAK!");
            }
            else if (ks % 10 == 0) {
                Bukkit.broadcastMessage(Messages.TAB + team.getColor() + event.getKiller().getName() + ChatColor.WHITE + " is on a " + ChatColor.RED + ChatColor.BOLD + ks + " KILL STREAK!");
            }

        }
    }

    @EventHandler
    public void onPgmSpawn(CardinalSpawnEvent event) {
        try {
            event.getPlayer().removeMetadata("killstreak", Warzone.getInstance());
        } catch (NullPointerException e) {
        }
        event.getPlayer().setMetadata("killstreak", new LazyMetadataValue(Warzone.getInstance(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new KillStreak(0)));
    }

}
