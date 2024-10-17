package net.kevarion.christmaspresents.menu.menus;

import net.kevarion.christmaspresents.menu.Menu;
import net.kevarion.christmaspresents.menu.PlayerMenuUtility;
import net.kevarion.christmaspresents.util.PresentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        if (playerMenuUtility.getPresentMessage() != null || playerMenuUtility.getRecipient() != null) {
            if (event.getSlot() == 0) event.setCancelled(true);
        }

            if (event.getSlot() == 40) {
            event.setCancelled(true);

            ArrayList<ItemStack> items = new ArrayList<>();
            ItemStack present;

            try {

                if (playerMenuUtility.getPresentMessage() != null || playerMenuUtility.getRecipient() != null) {
                    for (int i = 1; i < 27; i++) {
                        if (event.getInventory().getItem(i) != null) items.add(event.getInventory().getItem(i));
                    }
                } else {
                    for (int i = 0; i < 27; i++) {
                        if (event.getInventory().getItem(i) != null) items.add(event.getInventory().getItem(i));
                    }
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

        ItemStack info = makeItem(Material.PAPER, color("&e&lInformation"),
                "&7Put the items you want in the present above.");

        ItemStack create = makeItem(Material.BELL, color("&a&lCreate &c&lPresent"),
                color("&aClick this item and the Elves..."),
                color("&awill package your present."));

        if (playerMenuUtility.getPresentMessage() != null || playerMenuUtility.getRecipient() != null) {
            ItemStack tag = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta tagMeta = tag.getItemMeta();

            if (playerMenuUtility.getRecipient() != null) {
                tagMeta.setDisplayName(color("&eTo: &f" + playerMenuUtility.getRecipient()));
            }

            ArrayList<String> messageLore = new ArrayList<>();
            if (playerMenuUtility.getPresentMessage() != null) {
                messageLore.add(color("&#08d30e" + playerMenuUtility.getPresentMessage()));
            } else {
                messageLore.add(color("&7And a happy new year!"));
            }

            tagMeta.setLore(messageLore);
            tag.setItemMeta(tagMeta);

            inventory.addItem(tag);
        }

        inventory.setItem(39, info);
        inventory.setItem(40, create);

    }
}