package com.minehut.warzone.kit.games.destroy.kits;

import com.minehut.warzone.util.itemstack.ItemFactory;
import com.minehut.warzone.event.CardinalDeathEvent;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.kit.KitManager;
import com.minehut.warzone.util.SlotType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

/**
 * Created by luke on 10/17/15.
 */
public class ChoruserKit extends Kit {

    public ChoruserKit() {
        super("Choruser", 400, "Harvest the power of Chorus Fruits.", Material.CHORUS_FRUIT);

        super.setItem(0, ItemFactory.createItem(Material.STONE_SWORD));
        super.setItem(1, ItemFactory.createItem(Material.BOW));

        super.setItem(2, ItemFactory.createItem(Material.CHORUS_FRUIT, 2));
        super.setItem(3, ItemFactory.createItem(Material.GOLDEN_APPLE));

        super.setItem(5, ItemFactory.createItem(Material.WOOD, 64));
        super.setItem(6, ItemFactory.createItem(Material.WOOD_AXE));

        super.setItem(8, ItemFactory.createItem(Material.ARROW, 64));

        super.setItem(SlotType.HELMET.slot, ItemFactory.createItem(Material.LEATHER_HELMET));
        super.setItem(SlotType.CHESTPLATE.slot, ItemFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.setItem(SlotType.LEGGINGS.slot, ItemFactory.createItem(Material.CHAINMAIL_LEGGINGS));
        super.setItem(SlotType.BOOTS.slot, ItemFactory.createItem(Material.LEATHER_BOOTS));
    }

    @EventHandler
    public void onKill(CardinalDeathEvent event) {
        if(event.getKiller() == null) return;

        if (KitManager.isUsingKit(event.getKiller(), this)) {

            for (int i = 0; i < event.getKiller().getInventory().getSize(); i++) {
                ItemStack itemStack = event.getKiller().getInventory().getItem(i);

                if (itemStack != null && itemStack.getType() != null && itemStack.getType() == Material.CHORUS_FRUIT) {
                    ItemStack updated = ItemFactory.createItem(Material.CHORUS_FRUIT, itemStack.getAmount() + 1);
                    event.getKiller().getInventory().setItem(i, updated);
                    event.getKiller().updateInventory();
                    return;
                }
            }

            //check if slot 3 is empty
            if (event.getKiller().getInventory().getItem(3) == null || event.getKiller().getInventory().getItem(3).getType() == null || event.getKiller().getInventory().getItem(3).getType() == Material.AIR) {
                event.getKiller().getInventory().setItem(3, ItemFactory.createItem(Material.CHORUS_FRUIT, 2));
            } else {
                event.getKiller().getInventory().addItem(ItemFactory.createItem(Material.CHORUS_FRUIT, 1));
            }

            event.getKiller().updateInventory();
        }
    }

    @Override
    public void onApply(Player player, ChatColor chatColor) {

    }

    @Override
    public void extraUnload() {

    }
}
