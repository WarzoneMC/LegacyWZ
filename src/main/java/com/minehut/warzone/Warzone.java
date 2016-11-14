package com.minehut.warzone;

import com.minehut.cloud.bukkit.CloudBukkit;
import com.minehut.cloud.bukkit.status.event.StatusUpdateEvent;
import com.minehut.cloud.bukkit.subdata.SubDataModule;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.command.*;
import com.minehut.warzone.module.modules.matchTimer.MatchTimer;
import com.minehut.warzone.tabList.TabList;
import com.minehut.warzone.util.DomUtil;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.LocaleHandler;
import com.minehut.warzone.kit.KitManager;
import com.minehut.warzone.rotation.exception.RotationLoadException;
import com.minehut.warzone.tnt.TntTracker;
import com.minehut.warzone.user.UserManager;
import com.minehut.warzone.util.ChatUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;

public class Warzone extends JavaPlugin implements Listener {

    private final static String CRAFTBUKKIT_VERSION = "v1_8_R3";
    private final static String MINECRAFT_VERSION = "1.8.7";

    private static Warzone instance;
    private static GameHandler gameHandler;
    private static LocaleHandler localeHandler;
    private CommandsManager<CommandSender> commands;

    private TabList tabList;

    private UserManager userManager;
    private KitManager kitManager;

    private TntTracker tntTracker;

    public static LocaleHandler getLocaleHandler() {
        return localeHandler;
    }

    public static Warzone getInstance() {
        return instance;
    }

    @EventHandler
    public void onStatusUpdate(StatusUpdateEvent event) {
        event.getDoc().put("map", this.getGameHandler().getMatch().getLoadedMap().getName());
        event.getDoc().put("gamemode", this.getGameHandler().getMatch().getGameType().displayName);

        event.getDoc().put("next", this.getGameHandler().getCycle().getMap().getName());

        event.getDoc().put("time", MatchTimer.getTimeInSeconds());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String locale = ChatUtil.getLocale(sender);
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(locale));
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage().replace("{cmd}", cmd.getName()));
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NUMBER_STRING).getMessage(locale));
            } else {
                sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_UNKNOWN_ERROR).getMessage(locale));
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        cmdRegister.register(BroadcastCommands.class);
        cmdRegister.register(CancelCommand.class);
        cmdRegister.register(CycleCommand.class);
        cmdRegister.register(InventoryCommand.class);
        cmdRegister.register(JoinCommand.class);
        cmdRegister.register(ListCommand.class);
        cmdRegister.register(MapCommands.class);
        cmdRegister.register(MatchCommand.class);
        cmdRegister.register(ProximityCommand.class);
        cmdRegister.register(RotationCommands.class);
        cmdRegister.register(StartAndEndCommand.class);
        cmdRegister.register(TeamCommands.TeamParentCommand.class);
        cmdRegister.register(TeamCommands.class);
        cmdRegister.register(TeleportCommands.class);
        cmdRegister.register(TimeLimitCommand.class);
        cmdRegister.register(LoadMapsCommand.class);
        cmdRegister.register(StatsCommands.class);
        cmdRegister.register(CoinsCommand.class);
        cmdRegister.register(KitCommand.class);
        cmdRegister.register(ChatCommands.class);

    }

    private void checkCraftVersion() {
        String craftVer = Bukkit.getServer().getClass().getPackage().getName();
        if (!("org.bukkit.craftbukkit." + CRAFTBUKKIT_VERSION).equals(craftVer)) {
            getLogger().warning("########################################");
            getLogger().warning("#####  YOUR VERSION OF SPORTBUKKIT #####");
            getLogger().warning("#####  IS NOT SUPPORTED. PLEASE    #####");
            getLogger().warning("#####  USE  SPORTBUKKIT " + MINECRAFT_VERSION + "      #####");
            getLogger().warning("########################################");
        }
    }

    private void deleteMatches() {
        FileConfiguration config = getConfig();
        if (config.getBoolean("deleteMatches")) {
            getLogger().info("Deleting match files, this can be disabled via the configuration");
            File matches = new File("matches/");
            try {
                FileUtils.deleteDirectory(matches);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        checkCraftVersion();
        try {
            localeHandler = new LocaleHandler(this);
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            getLogger().severe("Failed to initialize because of invalid language files. Please make sure all language files in the plugin are present.");
            setEnabled(false);
            return;
        }

        getDataFolder().mkdir();

        userManager = new UserManager();
        kitManager = new KitManager();

        tntTracker = new TntTracker();

        CloudBukkit.getInstance().getChatFilter().setDoCheck(false);

//        registerListener(new DamageListener());

        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        deleteMatches();
        setupCommands();

        this.tabList = new TabList();
        Bukkit.getPluginManager().registerEvents(tabList, this);

        try {
            gameHandler = new GameHandler();
        } catch (RotationLoadException e) {
            e.printStackTrace();
            getLogger().severe("Failed to initialize because of an invalid rotation configuration.");
            setEnabled(false);
            return;
        }

        if (config.getBoolean("resetSpawnProtection") && Bukkit.getServer().getSpawnRadius() != 0) {
            Bukkit.getServer().setSpawnRadius(0);
        }

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public static void registerListener(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }

    @Override
    public void onDisable() {

    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public JavaPlugin getPlugin() {
        return this;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public TabList getTabList() {
        return tabList;
    }
}
