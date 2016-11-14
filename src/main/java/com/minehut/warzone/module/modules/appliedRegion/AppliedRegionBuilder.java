package com.minehut.warzone.module.modules.appliedRegion;

import com.minehut.warzone.match.GameType;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.modules.appliedRegion.type.BlockBreakRegion;
import com.minehut.warzone.module.modules.appliedRegion.type.BlockPlaceRegion;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.modules.filter.type.TeamFilter;
import com.minehut.warzone.module.modules.regions.RegionModuleBuilder;
import com.minehut.warzone.util.Teams;

@BuilderData(load = ModuleLoadTime.LATE)
public class AppliedRegionBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<AppliedRegion> load(Match match) {
        ModuleCollection<AppliedRegion> results = new ModuleCollection<>();

        if (match.getGameType() == GameType.DTW || match.getGameType() == GameType.CTW) {
            TeamModule blue = Teams.getTeamById("blue").get();
            TeamModule red = Teams.getTeamById("red").get();

            //blue ownership regions
            results.add(new BlockBreakRegion(RegionModuleBuilder.getRegion("blue-spawn"), new TeamFilter(red), null));
            results.add(new BlockPlaceRegion(RegionModuleBuilder.getRegion("blue-spawn"), new TeamFilter(red), null));

            //red ownership regions
            results.add(new BlockBreakRegion(RegionModuleBuilder.getRegion("red-spawn"), new TeamFilter(blue), null));
            results.add(new BlockPlaceRegion(RegionModuleBuilder.getRegion("red-spawn"), new TeamFilter(blue), null));
        }
        return results;
    }
}
