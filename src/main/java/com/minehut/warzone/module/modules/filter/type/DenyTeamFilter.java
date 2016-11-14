package com.minehut.warzone.module.modules.filter.type;

import com.google.common.base.Optional;
import com.minehut.warzone.module.modules.filter.FilterModule;
import com.minehut.warzone.module.modules.filter.FilterState;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import org.bukkit.entity.Player;

public class DenyTeamFilter extends FilterModule {

    private final TeamModule team;

    public DenyTeamFilter(TeamModule team) {
        this.team = team;
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) {
                Optional<TeamModule> team = Teams.getTeamByPlayer((Player) object);
                if (team.isPresent())
                    if (team.get() == this.team)
                        return FilterState.DENY;
                    else
                        return FilterState.ALLOW;
                else
                    return FilterState.ALLOW;
            }
        }
        return FilterState.ABSTAIN;
    }

}
