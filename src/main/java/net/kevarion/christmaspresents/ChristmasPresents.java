package net.kevarion.christmaspresents;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.kevarion.christmaspresents.command.PresentCommand;
import net.kevarion.christmaspresents.event.MenuListener;
import net.kevarion.christmaspresents.event.PresentListener;
import net.kevarion.christmaspresents.menu.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@SuppressWarnings("LombokGetterMayBeUsed")
public final class ChristmasPresents extends JavaPlugin {

    private static final HashMap<PlayerMenuUtility, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    @Getter private static ChristmasPresents instance;
    private PaperCommandManager commandManager;

    public static ChristmasPresents getInstance() {
        return instance;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void onEnable() {

        instance = this;
        commandManager = new PaperCommandManager(this);
        PluginManager pm = getServer().getPluginManager();

        commandManager.registerCommand(new PresentCommand());
        pm.registerEvents(new MenuListener(), this);
        pm.registerEvents(new PresentListener(), this);

    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(player))) {

            playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(playerMenuUtility, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(player);
        }

    }

}
