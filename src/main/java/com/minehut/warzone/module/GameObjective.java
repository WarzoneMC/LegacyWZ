package com.minehut.warzone.module;

import com.minehut.warzone.module.modules.team.TeamModule;

public interface GameObjective extends Module {

    public TeamModule getTeam();

    public String getName();

    public String getId();

    public boolean isTouched();

    public boolean isComplete();

    public boolean showOnScoreboard();

    public boolean isRequired();

}
