package com.minehut.warzone.module.modules.teamPicker;

import com.google.common.base.Optional;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Items;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.util.MiscUtil;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TeamPicker implements Module {

    private boolean donorOnlyTeamPicking = true;

    protected TeamPicker() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }


    public Inventory getTeamPicker(Player player) {
        int size = (((GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class).size() + (Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get().isObserver() ? 0 : 1 )) / 9) + 1) * 9;
        Inventory picker = Bukkit.createInventory(null, size, ChatColor.DARK_RED + new LocalizedChatMessage(ChatConstant.UI_TEAM_PICK).getMessage(player.spigot().getLocale()));
        int item = 0;

        int maxPlayers = 0;
        int totalPlayers = 0;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) {
                maxPlayers += team.getMax();
                totalPlayers += team.size();
            }
        }
        ItemStack autoJoin = Items.createItem(Material.CHAINMAIL_HELMET, 1, (short) 0, ChatColor.GRAY + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_AUTO).getMessage(player.spigot().getLocale()), Arrays.asList((totalPlayers >= maxPlayers ? ChatColor.RED + "" : ChatColor.GREEN + "") + totalPlayers + ChatColor.GOLD + " / " + ChatColor.RED + "" + maxPlayers, ChatColor.AQUA + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_AUTO_LORE).getMessage(player.spigot().getLocale())));
        picker.setItem(item, autoJoin);
        item++;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (!team.isObserver() && team.isAllowJoin()) {
                ItemStack teamStack;
                if (GameHandler.getGameHandler().getMatch().getModules().getModule(TeamPicker.class).isDonorOnlyTeamPicking()) {
                    teamStack = Items.createLeatherArmor(Material.LEATHER_HELMET, 1, team.getColor() + "" + ChatColor.BOLD + team.getName(), Arrays.asList((team.size() >= team.getMax() ? ChatColor.RED + "" : ChatColor.GREEN + "") + team.size() + ChatColor.GOLD + " / " + ChatColor.RED + "" + team.getMax(), ""), MiscUtil.convertChatColorToColor(team.getColor()));
                } else {
                    teamStack = Items.createLeatherArmor(Material.LEATHER_HELMET, 1, team.getColor() + "" + ChatColor.BOLD + team.getName(), Arrays.asList((team.size() >= team.getMax() ? ChatColor.RED + "" : ChatColor.GREEN + "") + team.size() + ChatColor.GOLD + " / " + ChatColor.RED + "" + team.getMax(), ChatColor.DARK_PURPLE + "Only " + ChatColor.AQUA + "donators" + ChatColor.DARK_PURPLE + " can select their teams."), MiscUtil.convertChatColorToColor(team.getColor()));
                }

                picker.setItem(item, teamStack);
                item++;
            }
        }
        item = size;
        if (!(Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get().isObserver())){
            ItemStack leave = Items.createItem(Material.LEATHER_BOOTS, 1, (short) 0, ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_LEAVE).getMessage(player.spigot().getLocale()), Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_LEAVE_LORE).getMessage(player.spigot().getLocale())));
            picker.setItem(item - 1, leave);
        }
        return picker;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item != null) {
            Optional<TeamModule> team = Teams.getTeamByPlayer(player);
            if (team.isPresent() && team.get().isObserver() || !GameHandler.getGameHandler().getMatch().isRunning()) {
                if (event.getInventory().getName().equals(ChatColor.DARK_RED + new LocalizedChatMessage(ChatConstant.UI_TEAM_PICK).getMessage(player.spigot().getLocale()))) {
                    if (item.getType().equals(Material.CHAINMAIL_HELMET)) {
                        if (item.hasItemMeta()) {
                            if (item.getItemMeta().hasDisplayName()) {
                                if (item.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_AUTO).getMessage(player.spigot().getLocale()))) {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "join");
                                }
                            }
                        }
                    } else if (item.getType().equals(Material.LEATHER_BOOTS)) {
                        if (item.hasItemMeta()) {
                            if (item.getItemMeta().hasDisplayName()) {
                                if (item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_LEAVE).getMessage(player.spigot().getLocale()))) {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "leave");
                                }
                            }
                        }
                    } else if (item.getType().equals(Material.LEATHER_HELMET)) {
                        if (item.hasItemMeta()) {
                            if (item.getItemMeta().hasDisplayName()) {
                                if (Teams.getTeamByName(ChatColor.stripColor(item.getItemMeta().getDisplayName())) != null) {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "join " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                                } else {
                                    event.setCancelled(true);
                                    player.closeInventory();
                                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                    Bukkit.dispatchCommand(player, "class " + ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                                }
                            }
                        }
                    } else {
                        if (item.hasItemMeta()) {
                            if (item.getItemMeta().hasDisplayName()) {

                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        //todo: disable late join option
        if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) &&
                (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) &&
                event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.LEATHER_HELMET) &&
                event.getPlayer().getItemInHand().hasItemMeta() && event.getPlayer().getItemInHand().getItemMeta().hasDisplayName() &&
                (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(event.getPlayer().spigot().getLocale())) ||
                        ChatColor.stripColor(event.getPlayer().getItemInHand().getItemMeta().getDisplayName()).equals(new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(event.getPlayer().spigot().getLocale())))) {
                    event.getPlayer().openInventory(getTeamPicker(event.getPlayer()));
        }
    }

//    @EventHandler
//    public void onPlayerLocaleChange(PlayerLocaleChangeEvent event) {
//        for (ItemStack com.minehut.tabbed.item : event.getPlayer().getInventory().getContents()) {
//            if (com.minehut.tabbed.item != null) {
//                if (com.minehut.tabbed.item.getType().equals(Material.LEATHER_HELMET)) {
//                    if (com.minehut.tabbed.item.hasItemMeta()) {
//                        if (com.minehut.tabbed.item.getItemMeta().hasDisplayName()) {
//                            ItemMeta meta = com.minehut.tabbed.item.getItemMeta();
//
//                            StringBuilder name = new StringBuilder();
//                            name.append(ChatColor.GREEN);
//                            name.append(ChatColor.BOLD);
//
//                            StringBuilder lore = new StringBuilder();
//                            lore.append(ChatColor.DARK_PURPLE);
//                            lore.append(new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(event.getPlayer().spigot().getLocale()));
//
//                            if (com.minehut.tabbed.item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(event.getOldLocale()))) {
//                                name.append(new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(event.getNewLocale()));
//                                meta.setDisplayName(name.toString());
//                                meta.setLore(Arrays.asList(lore.toString()));
//                            } else if (com.minehut.tabbed.item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(event.getOldLocale()))) {
//                                name.append(new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(event.getNewLocale()));
//                                meta.setDisplayName(name.toString());
//                                meta.setLore(Arrays.asList(lore.toString()));
//                            }
//                            com.minehut.tabbed.item.setItemMeta(meta);
//                        }
//                    }
//                }
//            }
//        }
//    }


    public boolean isDonorOnlyTeamPicking() {
        return donorOnlyTeamPicking;
    }

    public void setDonorOnlyTeamPicking(boolean donorOnlyTeamPicking) {
        this.donorOnlyTeamPicking = donorOnlyTeamPicking;
    }
}
