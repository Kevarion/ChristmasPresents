package net.kevarion.christmaspresents.util;

import lombok.SneakyThrows;
import net.kevarion.christmaspresents.ChristmasPresents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static net.kevarion.christmaspresents.util.ColorUtil.color;

public class PresentUtil {

    public static ItemStack createPresent(List<ItemStack> items, String from, String to, String message) throws IOException {
        ByteArrayOutputStream bio = new ByteArrayOutputStream();
        BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(bio);

        outputStream.writeInt(items.size());

        for (ItemStack item : items) {
            try {
                outputStream.writeObject(item);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

        outputStream.flush();

        byte[] serialized = bio.toByteArray();
        String encodedString = Base64.getEncoder().encodeToString(serialized);

        ItemStack present = new ItemStack(Material.CHEST, 1);
        ItemMeta presentMeta = present.getItemMeta();
        presentMeta.setDisplayName("Present from " + from);

        PersistentDataContainer container = presentMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(ChristmasPresents.getInstance(), "present"), PersistentDataType.STRING, encodedString);

        ArrayList<String> lore = new ArrayList<>();

        if (to != null) {
            container.set(new NamespacedKey(ChristmasPresents.getInstance(), "presentRecipient"), PersistentDataType.STRING, to);
            lore.add(color("&a&lTO: &c&l" + to));
            presentMeta.setLore(lore);
        }

        if (message != null) {
            container.set(new NamespacedKey(ChristmasPresents.getInstance(), "presentMessage"), PersistentDataType.STRING, message);
        }

        present.setItemMeta(presentMeta);

        bio.close();
        outputStream.close();

        return present;
    }

    public static ItemStack[] getItemsFromPresent(String encodedString) throws IOException, ClassNotFoundException {
        byte[] rawData = Base64.getDecoder().decode(encodedString);
        ByteArrayInputStream bio = new ByteArrayInputStream(rawData);
        BukkitObjectInputStream inputStream = new BukkitObjectInputStream(bio);

        int size = inputStream.readInt();

        ItemStack[] items = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            items[i] = (ItemStack) inputStream.readObject();
        }

        return items;
    }

}

