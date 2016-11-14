package com.minehut.warzone.module.modules.filter.parsers;

import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.module.modules.filter.FilterParser;

public class TeamFilterParser extends FilterParser {

    private final TeamModule team;

    public TeamFilterParser(final TeamModule team) {
        super();
        this.team = team;
    }

    public TeamModule getTeam() {
        return this.team;
    }

}
