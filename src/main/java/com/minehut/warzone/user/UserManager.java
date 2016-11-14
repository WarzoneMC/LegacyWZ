package com.minehut.warzone.user;

import com.minehut.cloud.bukkit.subdata.AsyncSubPlayerDbSyncEvent;
import com.minehut.cloud.bukkit.subdata.SubRank;
import com.minehut.cloud.core.Cloud;
import com.minehut.cloud.core.players.data.NetworkPlayer;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.Warzone;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by luke on 2/24/16.
 */
public class UserManager implements Listener {
    private ArrayList<WarzoneUser> users = new ArrayList<>();
    private ArrayList<MatchUser> matchUsers = new ArrayList<>();

    public UserManager() {
        Warzone.getInstance().getServer().getPluginManager().registerEvents(this, Warzone.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        WarzoneUser user = new WarzoneUser(event.getPlayer());
        this.users.add(user);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        WarzoneUser user = getUser(event.getPlayer());
        if (user != null) {
            this.users.remove(user);
        }
    }

    @EventHandler
    public void onQuit(PlayerKickEvent event) {
        WarzoneUser user = getUser(event.getPlayer());
        if (user != null) {
            this.users.remove(user);
        }
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        WarzoneUser user = new WarzoneUser(event.getPlayer());
        this.users.add(user);

        Bukkit.getScheduler().runTaskAsynchronously(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                DBObject query = new BasicDBObject("uuid", event.getPlayer().getUniqueId().toString());

                DBObject gameDoc = GameHandler.getGameHandler().getMatch().getGameCollection().findOne(query);

                if(gameDoc != null) {

                    if (gameDoc.get("selected_kit") != null) {
                        user.setSelectedKit((String) gameDoc.get("selected_kit"));
                    }

                    if (gameDoc.get("kits") != null) {
                        BasicDBList list = (BasicDBList) gameDoc.get("kits");
                        for (Object o : list) {
                            user.getKits().add((String) o);
                        }
                    }

                    if (gameDoc.get("kills") != null) {
                        user.setTotalKills(user.getTotalKills() + (int) gameDoc.get("kills"));
                    }

                    if (gameDoc.get("deaths") != null) {
                        user.setTotalDeaths(user.getTotalDeaths() + (int) gameDoc.get("deaths"));
                    }

                    if (gameDoc.get("matches") != null) {
                        user.setTotalMatches(user.getTotalMatches() + (int) gameDoc.get("matches"));
                    }

                    if (gameDoc.get("wins") != null) {
                        user.setTotalWins(user.getTotalWins() + (int) gameDoc.get("wins"));
                    }

                    if (gameDoc.get("deaths") != null) {
                        user.setTotalDeaths(user.getTotalDeaths() + (int) gameDoc.get("deaths"));
                    }

                    if (gameDoc.get("xp") != null) {
                        user.setXp(user.getXp() + (int) gameDoc.get("xp"));
                    }

                    if (gameDoc.get("longest_snipe") != null) {
                        if(user.getLongestSnipe() < (int) gameDoc.get("longest_snipe")) {
                            user.setLongestSnipe((int) gameDoc.get("longest_snipe"));
                        }
                    }

                    if (!gameDoc.get("name").equals(event.getPlayer().getName())) {
                        GameHandler.getGameHandler().getMatch().getGameCollection().update(query, new BasicDBObject("$set", new BasicDBObject("name", event.getPlayer().getName())));
                    }

                    WarzoneUserJoinEvent joinEvent = new WarzoneUserJoinEvent(event.getPlayer(), user);
                    Bukkit.getServer().getPluginManager().callEvent(joinEvent);

                } else {
                    DBObject doc = new BasicDBObject("uuid", event.getPlayer().getUniqueId().toString());

                    doc.put("name", event.getPlayer().getName());

                    doc.put("initial_join_date", new Date());
                    doc.put("last_online_date", new Date());

                    GameHandler.getGameHandler().getMatch().getGameCollection().insert(doc);

                    WarzoneUserJoinEvent joinEvent = new WarzoneUserJoinEvent(event.getPlayer(), user);
                    Bukkit.getServer().getPluginManager().callEvent(joinEvent);
                }
            }
        });

        /**
         * existing data from the match at play.
         * this is used to save data for players that disconnect mid game and rejoin later.
         */
        MatchUser matchUser = getMatchUser(event.getPlayer());
        if(matchUser == null) {
            this.matchUsers.add(new MatchUser(event.getPlayer()));
        } else {
            user.setXp(user.getXp() + matchUser.getXpEarned());
            user.setTotalKills(user.getTotalKills() + matchUser.getKills());
            user.setTotalDeaths(user.getTotalDeaths() + matchUser.getDeaths());

            if (user.getLongestSnipe() < matchUser.getLongestSnipe()) {
                user.setLongestSnipe(matchUser.getLongestSnipe());
            }
        }
    }

    public WarzoneUser getUser(Player player) {
        for (WarzoneUser user : this.users) {
            if (user.getPlayer() == player) {
                return user;
            }
        }
        return null;
    }

    public MatchUser getMatchUser(Player player) {
        for (MatchUser matchUser : this.matchUsers) {
            if (matchUser.getUuid().equals(player.getUniqueId())) {
                return matchUser;
            }
        }
        return null;
    }

    public ArrayList<WarzoneUser> getUsers() {
        return users;
    }

    public ArrayList<MatchUser> getMatchUsers() {
        return matchUsers;
    }
}
