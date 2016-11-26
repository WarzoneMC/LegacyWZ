package com.minehut.warzone.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by luke on 3/25/16.
 */
public class MongoPlayer {
    private UUID uuid;
    private String name;

    public MongoPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public MongoPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public boolean isOnline() {
        Player player = Bukkit.getPlayer(uuid);
        return player != null;
    }

    public Player getPlayer() {
        Player player = Bukkit.getPlayer(uuid);
        return player;
    }

    public DBObject getDoc() {
        DBObject doc = new BasicDBObject("uuid", uuid.toString());
        doc.put("name", name);

        return doc;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
