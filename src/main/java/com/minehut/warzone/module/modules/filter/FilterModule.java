package com.minehut.warzone.module.modules.filter;

import com.minehut.warzone.module.Module;

public abstract class FilterModule implements Module {

    protected FilterModule() {

    }

    public abstract FilterState evaluate(final Object... objects);

    @Override
    public void unload() {

    }

}
