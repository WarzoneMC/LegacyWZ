package com.minehut.warzone.module.modules.match;

import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.event.CycleCompleteEvent;
import com.minehut.warzone.event.MatchEndEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.tabHeader.TitleAPI;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class MatchModule implements Module {

    private final Match match;

    protected MatchModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        if (event.getTeam().isPresent()) {
            ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().get().getCompleteName() + ChatColor.WHITE)));
            for (Player player : Bukkit.getOnlinePlayers()) {

                player.getInventory().setHeldItemSlot(1);

                String title;
                title = new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, event.getTeam().get().getCompleteName() + ChatColor.WHITE).getMessage(player.spigot().getLocale());
                if (Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get() == event.getTeam().get()) {
                    player.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_TEAM_WIN)).getMessage(player.spigot().getLocale()));
                    String subtitle = new LocalizedChatMessage(ChatConstant.UI_MATCH_TEAM_WIN).getMessage(player.spigot().getLocale());
//                    player.showTitle(new TextComponent(title),new TextComponent(ChatColor.GREEN + subtitle), 0, 40, 30);
//                    new Titles(player).title(new TextComponent(title).getText()).subtitle(new TextComponent(ChatColor.GREEN + subtitle).getText()).times(0, 40, 30).send();
                    TitleAPI.sendTitle(player, 0, 40, 30, title, ChatColor.GREEN + subtitle);
                } else if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                    player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_TEAM_LOSE)).getMessage(player.spigot().getLocale()));
                    String subtitle = new LocalizedChatMessage(ChatConstant.UI_MATCH_TEAM_LOSE).getMessage(player.spigot().getLocale());
//                    player.showTitle(new TextComponent(title), new TextComponent(ChatColor.RED + subtitle), 0, 40, 30);
//                    new Titles(player).title(new TextComponent(title).getText()).subtitle(new TextComponent(ChatColor.RED + subtitle).getText()).times(0, 40, 30).send();
                    TitleAPI.sendTitle(player, 0, 40, 30, title, ChatColor.RED + subtitle);
                } else {
                    TitleAPI.sendTitle(player, 0, 40, 30, title);
//                    new Titles(player).title(new TextComponent(title).getText()).subtitle(new TextComponent("").getText()).times(0, 40, 30).send();
//                    player.showTitle(new TextComponent(title), new TextComponent(""), 0, 40, 30);
                }
            }
        } else if (event.getPlayer().isPresent()){
            ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, ChatColor.YELLOW + (event.getPlayer().get()).getName() + ChatColor.WHITE)));
            for (Player player : Bukkit.getOnlinePlayers()) {
                String title = new LocalizedChatMessage(ChatConstant.UI_MATCH_WIN, ChatColor.YELLOW + (event.getPlayer().get()).getName() + ChatColor.WHITE).getMessage(player.spigot().getLocale());
//                player.showTitle(new TextComponent(title),new TextComponent(""), 0, 40, 30);
//                new Titles(player).title(new TextComponent(title).getText()).subtitle(new TextComponent("").getText()).times(0, 40, 30).send();
                TitleAPI.sendTitle(player, 0, 40, 30, title);
            }
        } else {
            ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_OVER)));
            for (Player player : Bukkit.getOnlinePlayers()) {
                String title = new LocalizedChatMessage(ChatConstant.UI_MATCH_OVER).getMessage(player.spigot().getLocale());
//                player.showTitle(new TextComponent(title),new TextComponent(""), 0, 20, 20);
//                new Titles(player).title(new TextComponent(title).getText()).subtitle(new TextComponent("").getText()).times(0, 20, 20).send();
                TitleAPI.sendTitle(player, 0, 20, 20, title);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCycleComplete(CycleCompleteEvent event) {
        ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + event.getMatch().getLoadedMap().getName())));
        for (Player player : Bukkit.getOnlinePlayers()) {
            Teams.getTeamById("observers").get().add(player, true, false);
        }
    }
}
