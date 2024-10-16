package net.kevarion.christmaspresents.event;

import net.kevarion.christmaspresents.ChristmasPresents;
import net.kevarion.christmaspresents.menu.PlayerMenuUtility;
import net.kevarion.christmaspresents.util.PresentUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;

import static net.kevarion.christmaspresents.util.ColorUtil.color;

public class PresentListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        ItemStack itemPlaced = event.getItemInHand();
        if (itemPlaced.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(ChristmasPresents.getInstance(), "present"), PersistentDataType.STRING)) {

            Chest chest = (Chest) event.getBlockPlaced().getState();

            try {

                ItemStack[] items = PresentUtil.getItemsFromPresent(itemPlaced.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ChristmasPresents.getInstance(), "present"), PersistentDataType.STRING));
                chest.getSnapshotInventory().setContents(items);

                PersistentDataContainer container = chest.getPersistentDataContainer();
                container.set(new NamespacedKey(ChristmasPresents.getInstance(), "presents"), PersistentDataType.INTEGER, 0);

                if (itemPlaced.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(ChristmasPresents.getInstance(), "presentRecipient"), PersistentDataType.STRING)) {
                    container.set(new NamespacedKey(ChristmasPresents.getInstance(), "presentRecipient"), PersistentDataType.STRING, itemPlaced.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ChristmasPresents.getInstance(), "presentRecipient"), PersistentDataType.STRING));
                }

                if (itemPlaced.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(ChristmasPresents.getInstance(), "presentMessage"), PersistentDataType.STRING)) {
                    container.set(new NamespacedKey(ChristmasPresents.getInstance(), "presentMessage"), PersistentDataType.STRING, itemPlaced.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ChristmasPresents.getInstance(), "presentMessage"), PersistentDataType.STRING));
                }

                chest.update();

            } catch (IOException | ClassNotFoundException ex) {
                System.out.print(ex);
            }

        }

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {

        if (event.getInventory().getHolder() instanceof Chest || event.getInventory().getHolder() instanceof DoubleChest) {
            Player player = (Player) event.getPlayer();

            TileState state = (TileState) event.getInventory().getHolder();
            PersistentDataContainer container = state.getPersistentDataContainer();

            if (container.has(new NamespacedKey(ChristmasPresents.getInstance(), "present"), PersistentDataType.INTEGER)) {
                Chest chest = (Chest) event.getInventory().getHolder();
                Block c = state.getBlock();

                ItemStack[] items = chest.getInventory().getContents();
                event.setCancelled(true);

                ArmorStand merryChristmas = (ArmorStand) c.getLocation().getWorld().spawnEntity(c.getLocation().add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND);
                merryChristmas.setGravity(false);
                merryChristmas.setCanPickupItems(false);
                merryChristmas.setCustomName(color("&f&kAA&r &a&lMerry &c&lChristmas &f&kAA&r"));
                merryChristmas.setCustomNameVisible(true);
                merryChristmas.setVisible(false);

            }

        }

    }

}
