package com.minehut.warzone.util.cheat;

import com.minehut.warzone.Warzone;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by luke on 10/25/15.
 */
public class CheatUtils {
    public CheatUtils() {
        
    }

    public static void exemptFlight(Player player, long time) {
        NCPExemptionManager.exemptPermanently(player, CheckType.MOVING_SURVIVALFLY);
        unexemptFlight(player, time);
    }

    private static void unexemptFlight(final Player player, long delay) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                NCPExemptionManager.unexempt(player, CheckType.MOVING_SURVIVALFLY);
            }
        }, delay);
    }
}
