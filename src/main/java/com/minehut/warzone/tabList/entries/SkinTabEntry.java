package com.minehut.warzone.tabList.entries;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.tabList.TabList;
import com.minehut.warzone.tabList.TabView;
import com.minehut.warzone.util.PacketUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class SkinTabEntry extends TabEntry {

    private int id = Bukkit.allocateEntityId();
    private boolean hat = false;
    private static DataWatcher hatOn = new DataWatcher(null);
    private static DataWatcher hatOff = new DataWatcher(null);

    static {
        hatOn.register(DataWatcherRegistry.a.a(13), (byte) 64);
        hatOff.register(DataWatcherRegistry.a.a(13), (byte) 0);
    }

    public SkinTabEntry(GameProfile profile) {
        super(profile);
    }

    protected void load() {
        this.hide();
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        broadcastCreateSkinParts();
    }

    public void destroy() {
        this.hide();
        broadcastTabListPacket(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        PacketUtils.broadcastPacket(deleteSkinParts());
        for (TabView view : TabList.getTabViews()) {
            view.destroy(this, -1, true);
        }
    }

    public void broadcastCreateSkinParts() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketUtils.broadcastPacket(createSkinPartsPacket());
            }
        });
    }

    public void createSkinParts(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketUtils.sendPacket(player, createSkinPartsPacket());
            }
        });
    }

    public void setHat(boolean hat) {
        if (hat == this.hat) {
            return;
        }
        this.hat = hat;
        updateSkinParts();
    }

    private DataWatcher getDataWatcher() {
        return hat ? hatOn : hatOff;
    }

    private void updateSkinParts() {
        PacketUtils.broadcastPacket(new PacketPlayOutEntityMetadata(id, getDataWatcher(), true));
    }

    private Packet createSkinPartsPacket() {
        UUID uuid = getProfile().getId();
        return new PacketPlayOutNamedEntitySpawn(id, uuid, 0, -1000, 0, (byte) 0, (byte) 0, getDataWatcher().c());
    }

    private Packet deleteSkinParts() {
        return new PacketPlayOutEntityDestroy(id);
    }

}
