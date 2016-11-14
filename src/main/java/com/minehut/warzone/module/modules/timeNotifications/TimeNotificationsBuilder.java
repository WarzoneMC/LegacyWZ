package com.minehut.warzone.module.modules.timeNotifications;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;

@BuilderData(load = ModuleLoadTime.LATEST)
public class TimeNotificationsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TimeNotifications> load(Match match) {
        return new ModuleCollection<>(new TimeNotifications());
    }

}
