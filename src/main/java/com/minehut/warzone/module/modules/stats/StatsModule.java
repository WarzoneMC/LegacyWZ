package com.minehut.warzone.module.modules.stats;

import com.minehut.warzone.user.MongoPlayer;
import com.minehut.warzone.user.WarzoneUser;
import com.minehut.warzone.util.Messages;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.CardinalDeathEvent;
import com.minehut.warzone.event.MatchEndEvent;
import com.minehut.warzone.event.StatMatchEndEvent;
import com.minehut.warzone.match.GameType;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.tracker.Type;
import com.minehut.warzone.module.modules.tracker.event.TrackerDamageEvent;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.user.MatchUser;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class StatsModule implements Module {
    public static int COINS_KILL = 2;
    public static int COINS_WIN = 20;
    public static int XP_KILL = 1;

    private DBCollection killsCollection;
    private ArrayList<KillStat> killStats = new ArrayList<>();

    protected StatsModule() {
        this.killsCollection = Warzone.getInstance().getDb().getCollection("warzone_kills");
    }

    public void insertKillStats() {
        ArrayList<DBObject> list = new ArrayList<>();
        for (KillStat killStat : killStats) {
            list.add(killStat.getDoc());
        }

        if (!list.isEmpty()) {
            this.killsCollection.insert(list);
        }

        this.killStats.clear();
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && Teams.getTeamByPlayer(event.getPlayer()).orNull() != Teams.getTeamByPlayer(event.getKiller()).orNull()) {

            this.killStats.add(new KillStat(event.getKiller(), event.getPlayer()));

            EntityDamageEvent.DamageCause cause = event.getPlayer().getLastDamageCause().getCause();

            WarzoneUser killer = Warzone.getInstance().getUserManager().getUser(event.getKiller());
            WarzoneUser dead = Warzone.getInstance().getUserManager().getUser(event.getPlayer());

            MatchUser killerMatchUser = Warzone.getInstance().getUserManager().getMatchUser(event.getKiller());
            MatchUser deadMatchUser = Warzone.getInstance().getUserManager().getMatchUser(event.getPlayer());

            killer.setTotalKills(killer.getTotalKills() + 1);
            killerMatchUser.setKills(killerMatchUser.getKills() + 1);

            dead.setTotalDeaths(dead.getTotalDeaths() + 1);
            deadMatchUser.setDeaths(deadMatchUser.getDeaths() + 1);

            Warzone.getInstance().getUserManager().addCoins(killer.getPlayer().getName(), COINS_KILL, "Killed " + event.getPlayer().getName(), true);
            killerMatchUser.addCoinsEarned(COINS_KILL);

            killer.setXp(killer.getXp() + XP_KILL);
            killerMatchUser.setXpEarned(killerMatchUser.getXpEarned() + XP_KILL);

            TrackerDamageEvent damageEvent = event.getTrackerDamageEvent();
            if (damageEvent.getType().equals(Type.SHOT)) {
                if (damageEvent.getDistance() > killerMatchUser.getLongestSnipe()) {
                    killerMatchUser.setLongestSnipe(damageEvent.getDistance());
                }
                if (damageEvent.getDistance() > killer.getLongestSnipe()) {
                    killer.setLongestSnipe(damageEvent.getDistance());
                }
            }

            event.getKiller().playSound(event.getKiller().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5f);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMatchEnd(final MatchEndEvent event) {

        StatMatchEndEvent statEvent = new StatMatchEndEvent(event);

        ArrayList<MongoPlayer> winners = new ArrayList<>();
        ArrayList<MongoPlayer> losers = new ArrayList<>();
        TeamModule observers = Teams.getObservers().get();

        if(event.getTeam() != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (event.getTeam().get().contains(player)) {
                    winners.add(new MongoPlayer(player));
                } else if (!observers.contains(player)) {
                    losers.add(new MongoPlayer(player));
                }
            }
        }

        statEvent.setWinners(winners);
        statEvent.setLosers(losers);

        Bukkit.getPluginManager().callEvent(statEvent);

        Bukkit.getServer().getScheduler().runTaskAsynchronously(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (MatchUser matchUser : Warzone.getInstance().getUserManager().getMatchUsers()) {
                    Player player = Bukkit.getServer().getPlayer(matchUser.getUuid());

                    boolean win = false;
                    boolean loss = false;

                    if (player != null) {
                        final String playerName = player.getName();
                        WarzoneUser user = Warzone.getInstance().getUserManager().getUser(player);

                        user.setTotalMatches(user.getTotalMatches() + 1);

                        if (event.getTeam().orNull() != null) {
                            if (!observers.contains(player)) {
                                if (Teams.containsPlayer(statEvent.getWinners(), player)) {
                                    win = true;

                                    user.setTotalWins(user.getTotalWins() + 1);

                                    Warzone.getInstance().getUserManager().addCoins(user.getPlayer().getName(), COINS_WIN, "Win the Game", false);
                                    matchUser.addCoinsEarned(COINS_WIN);

                                    user.setXp(user.getXp() + 10);
                                    matchUser.setXpEarned(matchUser.getXpEarned() + 10);

                                } else {
                                    loss = true;

                                    user.setTotalLosses(user.getTotalLosses() + 1);

                                    user.setXp(user.getXp() + 5);
                                    matchUser.setXpEarned(matchUser.getXpEarned() + 5);
                                }
                            }
                        }

                        final int kills = matchUser.getKills();
                        final int deaths = matchUser.getDeaths();
                        final int coinsEarned = matchUser.getCoinsEarned();
                        final int xpEarned = matchUser.getXpEarned();

                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                Player p = Bukkit.getServer().getPlayer(playerName);
                                if (p != null) {
                                    p.sendMessage(Messages.DIVIDER);
                                    p.sendMessage(Messages.TAB + ChatColor.GREEN + kills + " Kills");
                                    p.sendMessage(Messages.TAB + ChatColor.RED + deaths + " Deaths");
                                    p.sendMessage(Messages.TAB + ChatColor.GOLD + coinsEarned + " Coins");
                                    p.sendMessage(Messages.TAB + ChatColor.AQUA + xpEarned + " XP");
                                    p.sendMessage(Messages.DIVIDER);
                                }
                            }
                        }, 40L);
                    }

                    BasicDBObject update = new BasicDBObject("kills", matchUser.getKills());
                    update.append("deaths", matchUser.getDeaths());
                    update.append("kills", matchUser.getKills());
                    update.append("xp", matchUser.getXpEarned());

                    GameType gameType = GameHandler.getGameHandler().getMatch().getGameType();
                    update.append(gameType.toString() + "_kills", matchUser.getKills());
                    update.append(gameType.toString() + "_deaths", matchUser.getKills());
                    update.append(gameType.toString() + "_xp", matchUser.getXpEarned());
                    update.append(gameType.toString() + "_coins", matchUser.getCoinsEarned());

                    if (win || loss) {
                        update.append("matches", 1);
                        update.append(gameType.toString() + "_matches", 1);
                    }

                    if (win) {
                        update.append("wins", 1);
                        update.append(gameType.toString() + "_wins", 1);
                    } else if (loss) {
                        update.append("losses", 1);
                        update.append(gameType.toString() + "_loss6es", 1);
                    }

                    Warzone.getInstance().getGameHandler().getMatch().getGameCollection().update(new BasicDBObject("uuid", matchUser.getUuid().toString()), new BasicDBObject("$inc", update), true, true);

                    Warzone.getInstance().getGameHandler().getMatch().getGameCollection().update(new BasicDBObject("uuid", matchUser.getUuid().toString()),
                            new BasicDBObject("$max", new BasicDBObject("longest_snipe", matchUser.getLongestSnipe())), true, true);

                    Warzone.getInstance().getGameHandler().getMatch().getGameCollection().update(new BasicDBObject("uuid", matchUser.getUuid().toString()),
                            new BasicDBObject("$max", new BasicDBObject(gameType.toString() + "_longest_snipe", matchUser.getLongestSnipe())), true, true);

                    matchUser.reset();
                }

                insertKillStats();
            }
        });
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
