package net.kevarion.christmaspresents.menu.menus;

import net.kevarion.christmaspresents.menu.Menu;
import net.kevarion.christmaspresents.menu.PlayerMenuUtility;
import net.kevarion.christmaspresents.util.PresentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;

import static net.kevarion.christmaspresents.util.ColorUtil.color;

public class NewPresentMenu extends Menu {

    public NewPresentMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "New Present";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

        Player player = playerMenuUtility.getOwner();

        if (event.getSlot() == 40) {
            event.setCancelled(true);

            ArrayList<ItemStack> items = new ArrayList<>();
            ItemStack present;

            try {
                for (int i = 0; i < 27; i++) {
                    if (event.getInventory().getItem(i) != null) items.add(event.getInventory().getItem(i));
                }

                present = PresentUtil.createPresent(items, playerMenuUtility.getOwner().getDisplayName(), playerMenuUtility.getRecipient(), playerMenuUtility.getPresentMessage());
                player.closeInventory();
                player.getInventory().addItem(present);
                player.sendMessage(color("&aYou have been given the present."));

            } catch (IOException ex) {
                player.sendMessage(color("&cUnable to create present. Sorry."));
            }

        } else if (event.getSlot() >= 27 && event.getSlot() < 54) {
            event.setCancelled(true);
        }

    }

    @Override
    public void setMenuItems() {

        for (int i = 27; i < 54; i++) {
            inventory.setItem(i, FILLER_GLASS);
        }

        ItemStack create = makeItem(Material.BELL, color("Create Present"),
                color("Click this item and the Elves..."),
                color("will package your present."));

        inventory.setItem(40, create);

    }
}
