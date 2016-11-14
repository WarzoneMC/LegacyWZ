package com.minehut.warzone.module.modules.team;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleLoadTime;
import org.bukkit.ChatColor;

@BuilderData(load = ModuleLoadTime.EARLIEST)
public class TeamModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TeamModule> load(Match match) {
        ModuleCollection<TeamModule> results = new ModuleCollection<>();

        switch(match.getGameType()) {

            case INFECTED:
                results.add(new TeamModule(match, "Infected", "infected", 0, 50, 60, -1, ChatColor.RED, false));
                results.add(new TeamModule(match, "Humans", "humans", 0, 50, 60, -1, ChatColor.BLUE, false));
                break;
            case ELIMINATION:
            case BLITZ:
                results.add(new TeamModule(match, "Blue", "blue", 0, 50, 60, -1, ChatColor.BLUE, false));
                results.add(new TeamModule(match, "Red", "red", 0, 50, 60, -1, ChatColor.RED, false));
                break;
            case DTW:
            case CTW:
            case TDM:
                results.add(new TeamModule(match, "Blue", "blue", 0, 50, 60, -1, ChatColor.BLUE, false));
                results.add(new TeamModule(match, "Red", "red", 0, 50, 60, -1, ChatColor.RED, false));
        }


        results.add(new TeamModule(match, "Observers", "observers", 0, Integer.MAX_VALUE, Integer.MAX_VALUE, -1, ChatColor.AQUA, true));
        return results;
    }
}
