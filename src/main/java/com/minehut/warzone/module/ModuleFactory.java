package com.minehut.warzone.module;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.gameModules.GameModuleBuilder;
import com.minehut.warzone.module.modules.buildHeight.BuildHeightBuilder;
import com.minehut.warzone.module.modules.cannon.CannonModuleBuilder;
import com.minehut.warzone.module.modules.chat.ChatModuleBuilder;
import com.minehut.warzone.module.modules.deathTracker.DeathTrackerBuilder;
import com.minehut.warzone.module.modules.doubleKillPatch.DoubleKillPatchBuilder;
import com.minehut.warzone.module.modules.header.HeaderModuleBuilder;
import com.minehut.warzone.module.modules.hunger.HungerBuilder;
import com.minehut.warzone.module.modules.match.MatchModuleBuilder;
import com.minehut.warzone.module.modules.matchTimer.MatchTimerBuilder;
import com.minehut.warzone.module.modules.motd.MOTDBuilder;
import com.minehut.warzone.module.modules.oldPvP.OldPvPModuleBuilder;
import com.minehut.warzone.module.modules.permissions.PermissionModuleBuilder;
import com.minehut.warzone.module.modules.playable.PlayableBuilder;
import com.minehut.warzone.module.modules.snowflakes.SnowflakesBuilder;
import com.minehut.warzone.module.modules.spawn.SpawnModuleBuilder;
import com.minehut.warzone.module.modules.startTimer.StartTimerBuilder;
import com.minehut.warzone.module.modules.stats.StatsModuleBuilder;
import com.minehut.warzone.module.modules.teamManager.TeamManagerModuleBuilder;
import com.minehut.warzone.module.modules.teamPicker.TeamPickerBuilder;
import com.minehut.warzone.module.modules.timeLock.TimeLockBuilder;
import com.minehut.warzone.module.modules.timeNotifications.TimeNotificationsBuilder;
import com.minehut.warzone.module.modules.tntDefuse.TntDefuseBuilder;
import com.minehut.warzone.module.modules.tracker.TrackerBuilder;
import com.minehut.warzone.module.modules.visibility.VisibilityBuilder;
import com.minehut.warzone.module.modules.wildcard.WildCardBuilder;
import com.minehut.warzone.module.modules.wools.WoolCoreObjectiveBuilder;
import com.minehut.warzone.module.modules.team.TeamModuleBuilder;
import com.minehut.warzone.module.modules.scoreboard.ScoreboardModuleBuilder;
import com.minehut.warzone.module.modules.appliedRegion.AppliedRegionBuilder;
import com.minehut.warzone.module.modules.craft.CraftModuleBuilder;
import com.minehut.warzone.module.modules.cycleTimer.CycleTimerModuleBuilder;
import com.minehut.warzone.module.modules.deathMessages.DeathMessagesBuilder;
import com.minehut.warzone.module.modules.friendlyFire.FriendlyFireBuilder;
import com.minehut.warzone.module.modules.gameComplete.GameCompleteBuilder;
import com.minehut.warzone.module.modules.guiKeep.GuiKeepModuleBuilder;
import com.minehut.warzone.module.modules.invisibleBlock.InvisibleBlockBuilder;
import com.minehut.warzone.module.modules.killStreakCount.KillStreakBuilder;
import com.minehut.warzone.module.modules.mobSpawn.MobSpawnModuleBuilder;
import com.minehut.warzone.module.modules.observers.ObserverModuleBuilder;
import com.minehut.warzone.module.modules.regions.RegionModuleBuilder;
import com.minehut.warzone.module.modules.respawn.RespawnModuleBuilder;
import com.minehut.warzone.module.modules.sound.SoundModuleBuilder;
import com.minehut.warzone.module.modules.tasker.TaskerModuleBuilder;
import com.minehut.warzone.module.modules.timeLimit.TimeLimitBuilder;
import com.minehut.warzone.module.modules.toolRepair.ToolRepairBuilder;
import com.minehut.warzone.module.modules.woolfix.WoolFixModuleBuilder;
import com.minehut.warzone.module.modules.wools.WoolObjectiveBuilder;
import com.minehut.warzone.module.modules.worldFreeze.WorldFreezeBuilder;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.logging.Level;

public class ModuleFactory {

    private Set<Class<? extends ModuleBuilder>> builderClasses;
    private final List<ModuleBuilder> builders;

    private void addBuilders() {
        this.builderClasses.addAll(Arrays.asList(
                BuildHeightBuilder.class,
                WoolObjectiveBuilder.class,
                WoolCoreObjectiveBuilder.class,
                StatsModuleBuilder.class,
                WoolFixModuleBuilder.class,
                MobSpawnModuleBuilder.class,
                ToolRepairBuilder.class,
                TimeLockBuilder.class,
                FriendlyFireBuilder.class,
                HungerBuilder.class,
                HungerBuilder.class,
                VisibilityBuilder.class,
                MOTDBuilder.class,
                WorldFreezeBuilder.class,
                TeamManagerModuleBuilder.class,
                RespawnModuleBuilder.class,
                ObserverModuleBuilder.class,
                KillStreakBuilder.class,
                TeamPickerBuilder.class,
                ScoreboardModuleBuilder.class,
                TeamModuleBuilder.class,
                SpawnModuleBuilder.class,
                DeathMessagesBuilder.class,
                TntDefuseBuilder.class,
                GameCompleteBuilder.class,
                RegionModuleBuilder.class,
                DoubleKillPatchBuilder.class,
                TaskerModuleBuilder.class,
                MatchTimerBuilder.class,
                MatchModuleBuilder.class,
                TimeNotificationsBuilder.class,
                ChatModuleBuilder.class,
                AppliedRegionBuilder.class,
                TrackerBuilder.class,
                /* BloodBuilder.class, */
                PermissionModuleBuilder.class,
                DeathTrackerBuilder.class,
                SnowflakesBuilder.class,
                SoundModuleBuilder.class,
                StartTimerBuilder.class,
                HeaderModuleBuilder.class,
                CycleTimerModuleBuilder.class,
                TimeLimitBuilder.class,
                PlayableBuilder.class,
                WildCardBuilder.class,
                InvisibleBlockBuilder.class,
                GuiKeepModuleBuilder.class,
                CraftModuleBuilder.class,
                GameModuleBuilder.class,
                CannonModuleBuilder.class,
                OldPvPModuleBuilder.class
        ));
    }

    public ModuleFactory() {
        this.builders = new ArrayList<>();
        this.builderClasses = new HashSet<>();
        this.addBuilders();
        for (Class<? extends ModuleBuilder> clazz : builderClasses) {
            try {
                builders.add(clazz.getConstructor().newInstance());
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().log(Level.SEVERE, clazz.getName() + " is an invalid ModuleBuilder.");
                e.printStackTrace();
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public ModuleCollection<Module> build(Match match, ModuleLoadTime time) {
        ModuleCollection<Module> results = new ModuleCollection<>();
        for (ModuleBuilder builder : builders) {
            try {
                if (builder.getClass().getAnnotation(BuilderData.class).load().equals(time)) {
                    try {
                        results.addAll(builder.load(match));
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                if (time != ModuleLoadTime.NORMAL) ;
                else try {
                    results.addAll(builder.load(match));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return results;
    }

    public void registerBuilder(ModuleBuilder builder) {
        builders.add(builder);
    }

    public void unregisterBuilder(ModuleBuilder builder) {
        builders.remove(builder);
    }

}
