package com.minehut.warzone.module.modules.filter.type;

import com.google.common.base.Optional;
import com.minehut.warzone.module.modules.filter.FilterModule;
import com.minehut.warzone.module.modules.filter.FilterState;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import org.bukkit.entity.Player;

public class TeamFilter extends FilterModule {

    private final TeamModule team;

    public TeamFilter(TeamModule team) {
        this.team = team;
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) {
                Optional<TeamModule> team = Teams.getTeamByPlayer((Player) object);
                if (team.isPresent())
                    if (team.get() == this.team)
                        return FilterState.ALLOW;
                    else
                        return FilterState.DENY;
                else
                    return FilterState.DENY;
            }
        }
        return FilterState.ABSTAIN;
    }

}
