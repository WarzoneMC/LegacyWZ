package com.minehut.warzone.module.modules.stats;

import com.minehut.cloud.bukkit.util.MongoPlayer;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.user.WarzoneUser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.minehut.warzone.GameHandler;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * Created by luke on 4/10/16.
 */
public class KillStat {
    private MongoPlayer killer;
    private MongoPlayer dead;
    private DBObject doc;

    public KillStat(Player killerPlayer, Player deadPlayer) {
        this.killer = new MongoPlayer(killerPlayer);
        this.dead = new MongoPlayer(deadPlayer);

        WarzoneUser killerUser = Warzone.getInstance().getUserManager().getUser(killerPlayer);
        WarzoneUser deadUser = Warzone.getInstance().getUserManager().getUser(deadPlayer);

        doc = new BasicDBObject("killer", this.killer.getDoc());
        doc.put("dead", this.dead.getDoc());

        doc.put("killer_kit", killerUser.getActiveKit());
        doc.put("dead_kit", deadUser.getActiveKit());

        doc.put("gametype", GameHandler.getGameHandler().getMatch().getGameType().toString());

        doc.put("date", new Date());
    }

    public MongoPlayer getKiller() {
        return killer;
    }

    public MongoPlayer getDead() {
        return dead;
    }

    public DBObject getDoc() {
        return doc;
    }
}
