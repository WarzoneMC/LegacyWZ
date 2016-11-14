package com.minehut.warzone.tabList.entries;

import com.minehut.warzone.util.Teams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_10_R1.EnumChatFormat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class PlayerTabEntry extends SkinTabEntry {

    private Player player;

    public PlayerTabEntry(Player player) {
        super(getProfile(getPlayerSkin(player)));
        this.player = player;
        load();
    }

    private String getDisplayName() {
        // The method is broken, removes black color, https://hub.spigotmc.org/jira/browse/SPIGOT-2711
        //return player.getPlayerListName();

        return Teams.getTeamByPlayer(player).get().getColor() + player.getName();
//        return player.getName();
//        return CraftChatMessage.fromComponent(((CraftPlayer) player).getHandle().listName, EnumChatFormat.WHITE);
    }

    public String getDisplayName(Player viewer) {
        if(viewer == player) return Teams.getTeamByPlayer(player).get().getColor() + ChatColor.BOLD.toString() + player.getName();
        return Teams.getTeamByPlayer(player).get().getColor() + player.getName();
//        if (viewer == player) return getDisplayName().replace(viewer.getName(), ChatColor.BOLD + viewer.getName());
//        return getDisplayName();
    }

    public int getPing() {
        return ((CraftPlayer) player).getHandle().ping;
    }

    private static Property getPlayerSkin(Player player) {
        return getPlayerSkin(((CraftPlayer) player).getProfile());
    }

    private static Property getPlayerSkin(GameProfile profile) {
        for (Property property : profile.getProperties().get("textures")) {
            return new Property("textures", property.getValue(), property.getSignature());
        }
        return DEFAULT_PROPERTY;
    }


}
