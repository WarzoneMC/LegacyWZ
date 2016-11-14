package com.minehut.warzone.module.modules.toolRepair;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

public class ToolRepairBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ToolRepair> load(Match match) {
        ModuleCollection<ToolRepair> results = new ModuleCollection<>();
        Set<Material> materials = new HashSet<>(128);
        //todo
//        for (Element itemRemove : match.getDocument().getRootElement().getChildren("toolrepair")) {
//            for (Element com.minehut.tabbed.item : itemRemove.getChildren("tool")) {
//                materials.add(Material.matchMaterial(com.minehut.tabbed.item.getText()));
//            }
//        }
        results.add(new ToolRepair(materials));
        return results;
    }

}
