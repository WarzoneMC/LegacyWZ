package com.minehut.warzone.module.gameModules;

import com.minehut.warzone.match.GameType;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.*;
import com.minehut.warzone.module.gameModules.blitz.BlitzGameModule;
import com.minehut.warzone.module.gameModules.elimination.EliminationGameModule;
import com.minehut.warzone.module.gameModules.infected.InfectedGameModule;
import com.minehut.warzone.module.modules.arrowHitRemove.ArrowHitRemoveModule;
import com.minehut.warzone.module.modules.itemDrop.ItemDrop;
import com.minehut.warzone.module.gameModules.ctw.CtwGameModule;
import com.minehut.warzone.module.gameModules.destroy.DestroyGameModule;
import com.minehut.warzone.module.gameModules.tdm.TdmGameModule;
import com.minehut.warzone.module.modules.noBuildModule.NoBuildModule;
import org.bukkit.Material;

import java.util.ArrayList;

@BuilderData(load = ModuleLoadTime.LATER)
public class GameModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Module> load(Match match) {
        ModuleCollection<Module> results = new ModuleCollection<>();

        if (match.getGameType() == GameType.DTW) {
            results.add(new DestroyGameModule());

            ArrayList<Material> drops = new ArrayList<>();
            drops.add(Material.GOLDEN_APPLE);
            results.add(new ItemDrop(true, drops));

            results.add(new ArrowHitRemoveModule());
        }

        else if (match.getGameType() == GameType.CTW) {
            results.add(new CtwGameModule());

            ArrayList<Material> drops = new ArrayList<>();
            drops.add(Material.GOLDEN_APPLE);
            results.add(new ItemDrop(true, drops));

            results.add(new ArrowHitRemoveModule());
        }

        else if (match.getGameType() == GameType.TDM) {
            results.add(new TdmGameModule());

            results.add(new NoBuildModule());

            ArrayList<Material> drops = new ArrayList<>();
            drops.add(Material.GOLDEN_APPLE);
            results.add(new ItemDrop(true, drops));

            results.add(new ArrowHitRemoveModule());
        }

        else if (match.getGameType() == GameType.INFECTED) {
            results.add(new InfectedGameModule());

            results.add(new NoBuildModule());

            results.add(new ItemDrop(true));
        }

        else if (match.getGameType() == GameType.ELIMINATION) {
            results.add(new EliminationGameModule());
//            results.add(new NoRegenModule());

            ArrayList<Material> drops = new ArrayList<>();
            drops.add(Material.ARROW);
            drops.add(Material.WOOD);
            results.add(new ItemDrop(true, drops));
        }

        else if (match.getGameType() == GameType.BLITZ) {
            results.add(new BlitzGameModule());
            results.add(new ItemDrop(true));
        }

        return results;
    }

}
