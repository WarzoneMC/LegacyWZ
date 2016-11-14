package com.minehut.warzone.kit;

import com.google.common.base.Optional;
import com.minehut.cloud.bukkit.util.chat.S;
import com.minehut.cloud.bukkit.util.itemstack.EnchantGlow;
import com.minehut.cloud.bukkit.util.itemstack.ItemFactory;
import com.minehut.cloud.bukkit.util.page.ConfirmPage;
import com.minehut.cloud.core.Cloud;
import com.minehut.cloud.core.players.data.NetworkPlayer;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.CardinalSpawnEvent;
import com.minehut.warzone.event.CycleCompleteEvent;
import com.minehut.warzone.kit.games.blitz.kits.BlitzKit;
import com.minehut.warzone.kit.games.destroy.kits.*;
import com.minehut.warzone.kit.games.destroy.kits.TankKit;
import com.minehut.warzone.kit.games.elimination.*;
import com.minehut.warzone.kit.games.infected.human.kits.SurvivorKit;
import com.minehut.warzone.kit.games.infected.infected.kits.SkeletonKit;
import com.minehut.warzone.kit.games.infected.infected.kits.ZombieKit;
import com.minehut.warzone.kit.games.tdm.kits.*;
import com.minehut.warzone.match.GameType;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.user.WarzoneUser;
import com.minehut.warzone.util.Teams;
import com.mongodb.BasicDBObject;
import com.sk89q.minecraft.util.commands.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by luke on 3/8/16.
 */
public class KitManager implements Listener {
    public static String INV_NAME = ChatColor.UNDERLINE + "Kit Menu";

    private ArrayList<Kit> kits = new ArrayList<>();
    private ArrayList<String> disabledKits = new ArrayList<>();

    public KitManager() {
        Warzone.registerListener(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCardinalSpawn(CardinalSpawnEvent event) {
        if(kits.isEmpty()) return;

        respawnApplyKit(event.getPlayer());
    }

    public void respawnApplyKit(Player player) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(player);
        if (team.isPresent() && !team.get().isObserver()) {
            WarzoneUser user = Warzone.getInstance().getUserManager().getUser(player);

            Kit kit = getKitFromId(user.getSelectedKit());
            user.setActiveKit(user.getSelectedKit());

            kit.apply(player, team.orNull().getColor());
        }
    }

    @EventHandler
    public void onCycle(CycleCompleteEvent event) {
        kits.forEach(Kit::unload);
        kits.clear();
        this.setupKitsForGameType(GameHandler.getGameHandler().getMatch().getGameType());

        disabledKits.clear();
//        JSONObject jsonObject = DataDragon.getJsonFromUrl("http://datadragon.minehut.com/game/dtw/kits/disabled");
//        if(jsonObject != null) {
//            JSONArray jsonArray = jsonObject.getJSONArray("disabled");
//            Iterator it = jsonArray.iterator();
//            while (it.hasNext()) {
//                JSONObject json = (JSONObject) it.next();
//                disabledKits.add(json.getString("name"));
//                System.out.println("Disabled Kit: " + json.getString("name"));
//            }
//        }

//        for (Player player : Bukkit.getOnlinePlayers()) {
//            DisguiseAPI.undisguiseToAll(player);
//        }
    }

    public void setupKitsForGameType(GameType gameType) {
        this.kits.clear();

        switch (gameType) {
            case DTW:
            case CTW:
                kits.add(new com.minehut.warzone.kit.games.destroy.kits.BarbarianKit());
                kits.add(new TankKit());
                kits.add(new DemomanKit());
                kits.add(new MedicKit());
                kits.add(new InfiltratorKit());
                kits.add(new ChoruserKit());
                kits.add(new BuilderKit());
                kits.add(new PhoenixKit());
                kits.add(new ShielderKit());
                //kits.add(new SheepFarmerKit());
                kits.add(new EngineerKit());
//                kits.add(new BigSpenderKit());
                kits.add(new NecromancerKit());
                kits.add(new FireArcherKit());
                kits.add(new RedstoneMechanicKit());
                //kits.add(new ThorKit());
//                kits.add(new SeekingArcherKit());
                kits.add(new NinjaKit());
                kits.add(new SpyKit());
                break;
            case TDM:
                kits.add(new com.minehut.warzone.kit.games.tdm.kits.BarbarianKit());
                kits.add(new BackstabberKit());
                kits.add(new SniperKit());
                kits.add(new TeleporterKit());
                break;
            case INFECTED:
                kits.add(new SurvivorKit());
                kits.add(new ZombieKit());
//                kits.add(new CreeperKit());
                kits.add(new SkeletonKit());
                break;
            case ELIMINATION:
                kits.add(new com.minehut.warzone.kit.games.elimination.BarbarianKit());
                kits.add(new ArcherKit());
                kits.add(new SpecialistKit());
                kits.add(new com.minehut.warzone.kit.games.elimination.TankKit());
                kits.add(new AssassinKit());
                break;
            case BLITZ:
                kits.add(new BlitzKit());
        }

        //sort kits by cost
        Collections.sort(kits, (k1, k2) -> {
            return k1.getCost() - k2.getCost(); // Ascending
        });
    }

    public Kit getKitFromId(String id) {
        for (Kit kit : this.kits) {
            if (getIdFromKit(kit).equals(id)) {
                return kit;
            }
        }
        return kits.get(0);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(INV_NAME)) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();

            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                final Kit kit = getKitFromId(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).replace(" ", "_").toLowerCase());
                if (kit == null) {
                    return;
                }

                if (this.disabledKits.contains(kit.getName())) {
                    player.sendMessage(ChatColor.RED + "This kit is currently disabled.");
                    return;
                }

                final WarzoneUser user = Warzone.getInstance().getUserManager().getUser(player);
                NetworkPlayer networkPlayer = Cloud.getInstance().getPlayerManager().getNetworkPlayer(player.getName());
                if (kit.getCost() <= 0 || hasKit(user, kit)) {
                    user.setSelectedKit(getIdFromKit(kit));
                    player.sendMessage(ChatColor.WHITE + "You have selected " + ChatColor.GOLD.toString() + kit.getName());
                    S.plingHigh(player);
                    player.closeInventory();
                } else {
                    //purchase
                    if (networkPlayer.hasEnoughCoins(kit.getCost())) {
                        new ConfirmPage(Warzone.getInstance(), player, () -> {
                            Cloud.getInstance().getPlayerManager().addCoins(player.getName(), -kit.getCost(), "Purchased " + kit.getName(), true);

                            final String kitId = getIdFromKit(kit);
                            user.getKits().add(kitId);

                            user.setSelectedKit(kitId);

                            Bukkit.broadcastMessage("");
                            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " has unlocked " + ChatColor.GOLD.toString() + kit.getName());
                            Bukkit.broadcastMessage("");
                            S.playSound(player, Sound.ENTITY_PLAYER_LEVELUP);

                            Bukkit.getScheduler().runTaskAsynchronously(Warzone.getInstance(), () -> {
                                //add kit
                                Warzone.getInstance().getGameHandler().getMatch().getGameCollection().update(new BasicDBObject("uuid", player.getUniqueId().toString()),
                                        new BasicDBObject("$addToSet", new BasicDBObject("kits", kitId)));
                            });
                        }, () -> {

                                },
                        ItemFactory.createItem(kit.getMaterial(), ChatColor.GREEN + kit.getName(), Arrays.asList(
                                "",
                                ChatColor.YELLOW + "Are you sure you want to purchase " + ChatColor.AQUA + kit.getName() + ChatColor.YELLOW + "?",
                                ChatColor.RED.toString() + ChatColor.ITALIC + "This will remove " + ChatColor.GOLD.toString() + kit.getCost() + " coins " + ChatColor.RED.toString() + " from your balance.",
                                "")
                        ),
                        ChatColor.GREEN + "Yes! Purchase " + ChatColor.AQUA + ChatColor.BOLD.toString() + kit.getName(),
                        ChatColor.RED.toString() + "No! I don't want to buy " + ChatColor.AQUA + ChatColor.BOLD.toString() + kit.getName()
                        );
                    } else {
                        player.sendMessage(ChatColor.RED.toString() + "You need " + Math.abs(networkPlayer.getCoins() - kit.getCost()) + " more coins for this.");
                        S.playSound(player, Sound.ENTITY_VILLAGER_NO);
                        return;
                    }
                }
            }
        }
    }

    public void openKitMenu(Player player) {
        WarzoneUser user = Warzone.getInstance().getUserManager().getUser(player);

        int invSize = 9;
        if (this.kits.size() > 45) {
            invSize = 54;
        }
        else if (this.kits.size() > 36) {
            invSize = 45;
        }
        else if (this.kits.size() > 27) {
            invSize = 36;
        }
        else if (this.kits.size() > 18) {
            invSize = 27;
        }
        else if (this.kits.size() > 9) {
            invSize = 18;
        }

        Inventory inv = Bukkit.createInventory(null, invSize, INV_NAME);

        int slot = 0;
        for (Kit kit : this.kits) {
            boolean has = hasKit(user, kit);
            boolean selected = user.getSelectedKit().equals(getIdFromKit(kit));

            ItemStack itemStack;

            if(!kit.isSelectable()) continue;

            if (!kit.getTeamId().equals("")) {
                TeamModule target = Teams.getTeamById(kit.getTeamId()).orNull();
                TeamModule teamModule = Teams.getTeamByPlayer(player).orNull();

                if (!(kit.getTeamId().equals(teamModule.getId()) || (teamModule.isObserver() && target.isAllowJoin()))) {
                    continue;
                }
            }

            if (disabledKits.contains(player.getName())) {
                itemStack = ItemFactory.createItem(kit.getMaterial(), ChatColor.GREEN + kit.getName(), Arrays.asList(
                        "",
                        ChatColor.WHITE + kit.getDescription(),
                        "",
                        ChatColor.RED.toString() + ChatColor.BOLD + "This kit is currently disabled.",
                        ""
                ));
            } else if (has) {
                if (selected) {
                    itemStack = ItemFactory.createItem(kit.getMaterial(), ChatColor.GREEN + kit.getName(), Arrays.asList(
                            "",
                            ChatColor.WHITE + kit.getDescription(),
                            "",
                            ChatColor.AQUA.toString() + ChatColor.ITALIC + "You have selected this kit.",
                            ""
                    ));
                    EnchantGlow.addGlow(itemStack);
                } else {
                    itemStack = ItemFactory.createItem(kit.getMaterial(), ChatColor.GREEN + kit.getName(), Arrays.asList(
                            "",
                            ChatColor.WHITE + kit.getDescription(),
                            "",
                            ChatColor.GREEN.toString() + ChatColor.ITALIC + "Click to select.",
                            ""
                    ));
                }
            } else {
                itemStack = ItemFactory.createItem(kit.getMaterial(), ChatColor.GREEN + kit.getName(), Arrays.asList(
                        "",
                        ChatColor.WHITE + kit.getDescription(),
                        "",
                        ChatColor.WHITE + "Cost: " + ChatColor.GOLD.toString() + kit.getCost() + " Coins",
                        ""
                ));
            }


            inv.setItem(slot, removeAttributes(itemStack));
            slot++;
        }

        player.openInventory(inv);
    }

    private ItemStack removeAttributes(ItemStack i){
        if(i == null || i.getType() == Material.BOOK_AND_QUILL)
            return i;

//        ItemStack com.minehut.tabbed.item = i.clone();
//
//        net.minecraft.server.v1_9_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(com.minehut.tabbed.item);
//        NBTTagCompound tag;
//        if (!nmsStack.hasTag()){
//            tag = new NBTTagCompound();
//            nmsStack.setTag(tag);
//        }else
//            tag = nmsStack.getTag();
//
//        NBTTagList am = new NBTTagList();
//
//        tag.set("AttributeModifiers", am);
//        nmsStack.setTag(tag);

//        return CraftItemStack.asCraftMirror(nmsStack);
        return i;
    }

    public Kit getKitFromName(String name) {
        for (Kit kit : this.kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    public static boolean isUsingKit(Player player, Kit kit) {
        WarzoneUser user = Warzone.getInstance().getUserManager().getUser(player);
        return Warzone.getInstance().getKitManager().getKitFromId(user.getSelectedKit()) == kit;
    }

    public static boolean hasKit(WarzoneUser user, Kit kit) {
        return user.getKits().contains(getIdFromKit(kit));
    }

    public static String getIdFromKit(Kit kit) {
        return kit.getName().replace(" ", "_").toLowerCase();
    }

}
