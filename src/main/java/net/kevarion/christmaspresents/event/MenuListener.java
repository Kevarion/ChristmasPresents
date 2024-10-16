package net.kevarion.christmaspresents.event;

import net.kevarion.christmaspresents.menu.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.common.value.qual.EnumVal;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) {
            if (event.getCurrentItem() == null) return;

            Menu menu = (Menu) holder;
            menu.handleMenu(event);

        }
    }

}
