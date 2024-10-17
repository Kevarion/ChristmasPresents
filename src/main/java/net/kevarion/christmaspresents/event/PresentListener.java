package net.kevarion.christmaspresents.event;

import net.kevarion.christmaspresents.ChristmasPresents;
import net.kevarion.christmaspresents.menu.PlayerMenuUtility;
import net.kevarion.christmaspresents.util.PresentUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

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

                if (container.has(new NamespacedKey(ChristmasPresents.getInstance(), "presentRecipient"), PersistentDataType.STRING)) {
                    if (!container.get(new NamespacedKey(ChristmasPresents.getInstance(), "presentRecipient"), PersistentDataType.STRING).equalsIgnoreCase(player.getDisplayName())) {
                        event.setCancelled(true);
                        player.sendMessage(color("&cYou are not the recipient of this present. You'll be on the naughty list if you keep trying.."));
                        return;
                    }
                }

                Chest chest = (Chest) event.getInventory().getHolder();
                Block c = state.getBlock();

                ItemStack[] items = chest.getInventory().getContents();
                event.setCancelled(true);

                container.remove(new NamespacedKey(ChristmasPresents.getInstance(), "present"));
                state.update();

                Chest newChest = (Chest) c.getState();
                newChest.getSnapshotInventory().clear();
                newChest.update();

                ArmorStand merryChristmas = (ArmorStand) c.getLocation().getWorld().spawnEntity(c.getLocation().add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND);
                merryChristmas.setGravity(false);
                merryChristmas.setCanPickupItems(false);
                merryChristmas.setCustomName(color("&f&kAA&r &a&lMerry &c&lChristmas &f&kAA&r"));
                merryChristmas.setCustomNameVisible(true);
                merryChristmas.setVisible(false);

                ArmorStand message = (ArmorStand) c.getLocation().getWorld().spawnEntity(c.getLocation().add(0.5, -1, 0.5), EntityType.ARMOR_STAND);
                message.setGravity(false);
                message.setCanPickupItems(false);

                if (state.getPersistentDataContainer().has(new NamespacedKey(ChristmasPresents.getInstance(), "presentMessage"), PersistentDataType.STRING)) {
                    message.setCustomName(color("\" &f&o" + state.getPersistentDataContainer().get(new NamespacedKey(ChristmasPresents.getInstance(), "presentMessage"), PersistentDataType.STRING) + " &r\""));
                    message.setCustomNameVisible(true);
                    message.setVisible(false);
                } else {
                    message.setCustomName("");
                    message.setCustomNameVisible(false);
                }

                ArmorStand tree = (ArmorStand) c.getLocation().getWorld().spawnEntity(c.getLocation().add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND);
                tree.setGravity(false);
                tree.setCanPickupItems(false);
                tree.setVisible(false);
                tree.setHelmet(new ItemStack(Material.SPRUCE_SAPLING, 1));

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if ((tree.getLocation().getY() - c.getLocation().getY()) >= 3.5) {
                            tree.remove();

                            player.setInvulnerable(true);

                            final Firework fw = (Firework) c.getWorld().spawnEntity(tree.getLocation().add(0.5, 0, 0.5), EntityType.FIREWORK_ROCKET);
                            FireworkMeta fm = fw.getFireworkMeta();
                            fm.addEffect(FireworkEffect.builder()
                                    .flicker(true)
                                    .trail(true)
                                    .with(FireworkEffect.Type.BURST)
                                    .withColor(Color.RED, Color.GREEN, Color.LIME)
                                    .build());
                            fm.setPower(0);
                            fw.setFireworkMeta(fm);

                            fw.detonate();
                            tree.getWorld().playSound(tree.getLocation(), Sound.BLOCK_BELL_RESONATE,  2.0F, 2.0F);

                            player.setInvulnerable(false);

                            Location location = c.getLocation().add(0.5, 0, 0.5);
                            for (ItemStack item : items) {
                                if (item != null) {
                                    c.getWorld().dropItem(location.add(0, 1, 0), item);
                                }
                            }

                            this.cancel();
                        } else {
                            tree.teleport(tree.getLocation().add(0, 0.25,  0));
                            tree.setRotation(tree.getLocation().getYaw() + 25f, tree.getLocation().getPitch() + 25f);
                        }

                    }
                }.runTaskTimer(ChristmasPresents.getInstance(), 0, 15);

            }

        }

    }

}