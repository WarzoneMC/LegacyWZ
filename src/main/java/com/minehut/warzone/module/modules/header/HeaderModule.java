package com.minehut.warzone.module.modules.header;

import com.minehut.warzone.event.MatchEndEvent;
import com.minehut.warzone.module.modules.matchTimer.MatchTimer;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.ChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.event.CycleCompleteEvent;
import com.minehut.warzone.event.MatchStartEvent;
import com.minehut.warzone.event.PlayerNameUpdateEvent;
import com.minehut.warzone.module.TaskedModule;
import com.minehut.warzone.rotation.LoadedMap;
import com.minehut.warzone.util.Strings;
import com.minehut.warzone.util.tabHeader.TitleAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;


public class HeaderModule implements TaskedModule {

    private ChatMessage header;
    private String footer;
    private final String mapName;
    private final String builder;
    private final String message = ChatColor.translateAlternateColorCodes('`', Warzone.getInstance().getConfig().getString("server-message"));
    private int last = 0;

    public HeaderModule(LoadedMap map) {
        this.mapName = map.getName();
        this.builder = map.getBuilder();
    }


    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (Math.round(MatchTimer.getTimeInSeconds()) > last) {
            last = (int) Math.round(MatchTimer.getTimeInSeconds());
            updateFooter();
            for (Player player : Bukkit.getOnlinePlayers()) {
                updatePlayer(player, player.spigot().getLocale());
            }
        }
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        updateAll();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateHeader();
        updateAll();
    }

    @EventHandler
    public void onPlayerNameChange(PlayerNameUpdateEvent event) {
        updateHeader();
        updateAll();
    }

    @EventHandler
    public void onMatchStart (MatchStartEvent event) {
        last = 0;
        updateFooter();
        updateAll();
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        updateFooter();
        updateAll();
    }

//    @EventHandler //todo: player locale update
//    public void onLangChange(PlayerLocaleChangeEvent event) {
//        updatePlayer(event.getPlayer(), event.getNewLocale());
//    }

    public void updateHeader() {
        header = new UnlocalizedChatMessage(ChatColor.AQUA + mapName + ChatColor.RESET.toString() + ChatColor.GRAY + " {0} " + ChatColor.YELLOW + "{1}", ChatConstant.MISC_BY.asMessage() , assembleAuthors());
    }

    public void updateFooter() {
        footer = ChatColor.WHITE + Warzone.getInstance().getGameHandler().getMatch().getGameType().displayName
                + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Time: " + ChatColor.GREEN.toString()
                + Strings.formatTime(MatchTimer.getTimeInSeconds()) + ChatColor.DARK_GRAY.toString() + " - "
                + ChatColor.WHITE + "Minehut.com";
    }

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player, player.spigot().getLocale());
        }
    }

    private void updatePlayer(Player player, String locale) {
        if (header == null) updateHeader();
        if (footer == null) updateFooter();
//        player.setPlayerListHeaderFooter(new TextComponent(header.getMessage(locale)), new TextComponent(footer.getMessage(locale)));


        TitleAPI.sendTabTitle(player, new TextComponent(header.getMessage(locale)).getText(), footer);

    }

    private ChatMessage assembleAuthors() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.builder);
        return new UnlocalizedChatMessage(builder.toString(), ChatConstant.MISC_AND.asMessage());
    }

}
