package com.minehut.warzone.user;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.util.MongoUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by luke on 2/24/16.
 */
public class UserManager implements Listener {
    private ArrayList<WarzoneUser> users = new ArrayList<>();
    private ArrayList<MatchUser> matchUsers = new ArrayList<>();

    private DBCollection globalUserCollection;

    public UserManager() {
        Warzone.getInstance().getServer().getPluginManager().registerEvents(this, Warzone.getInstance());
        this.globalUserCollection = Warzone.getInstance().getDb().getCollection("warzone_users");
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

                DBObject globalDoc = globalUserCollection.findOne(query);
                DBObject gameDoc = GameHandler.getGameHandler().getMatch().getGameCollection().findOne(query);

                if (globalDoc != null) {
                    if (globalDoc.get("coins") != null) {
                        user.setCoins((Integer) globalDoc.get("coins"));
                    }

                    if (gameDoc.get("xp") != null) {
                        user.setXp(user.getXp() + (int) gameDoc.get("xp"));
                    }

                    if (!gameDoc.get("name").equals(event.getPlayer().getName())) {
                        GameHandler.getGameHandler().getMatch().getGameCollection().update(query, new BasicDBObject("$set", new BasicDBObject("name", event.getPlayer().getName())));
                    }
                } else {
                    DBObject doc = new BasicDBObject("uuid", event.getPlayer().getUniqueId().toString());

                    doc.put("name", event.getPlayer().getName());

                    doc.put("initial_join_date", new Date());
                    doc.put("last_online_date", new Date());

                    globalUserCollection.insert(doc);
                }

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
        if(player == null) return null;

        for (WarzoneUser user : this.users) {
            if (user.getPlayer() == player) {
                return user;
            }
        }
        return null;
    }

    public WarzoneUser getUser(String name) {
        Player player = Bukkit.getPlayer(name);
        return getUser(player);
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

    public void addCoins(String name, int amount, String message, boolean async) {


        if (message != null) {
            Player player = Bukkit.getPlayer(name);
            if (player != null) {
                if (amount >= 0) {
                    player.sendMessage(ChatColor.GREEN + "+" + amount + " coins" +
                            ChatColor.WHITE + ChatColor.DARK_PURPLE.toString() + " | " + ChatColor.RESET
                            + ChatColor.WHITE.toString() + message);
                } else {
                    player.sendMessage(ChatColor.RED.toString() + amount + " coins" +
                            ChatColor.WHITE + ChatColor.DARK_PURPLE.toString() + " | " + ChatColor.RESET
                            + ChatColor.WHITE.toString() + message);
                }
            }
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                globalUserCollection.update(MongoUtils.getIgnoreCaseQuery("name", name),
                        new BasicDBObject("$inc", new BasicDBObject("coins", amount)));
            }
        };

        if (async) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, 0);
        } else {
            runnable.run();
        }


        WarzoneUser warzoneUser = getUser(name);
        if (warzoneUser != null) {
            warzoneUser.setCoins(warzoneUser.getCoins() + amount);
        }
    }
}
