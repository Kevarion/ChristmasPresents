package net.kevarion.christmaspresents.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.kevarion.christmaspresents.ChristmasPresents;
import net.kevarion.christmaspresents.menu.PlayerMenuUtility;
import net.kevarion.christmaspresents.menu.menus.NewPresentMenu;
import net.kevarion.christmaspresents.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static net.kevarion.christmaspresents.util.ColorUtil.color;

@CommandAlias("present")
public class PresentCommand extends BaseCommand {

    @Default
    public void main(Player player, String[] args) {

        if (args.length > 0) {

            String to = args[0];
            Player recipient = Bukkit.getPlayer(to);

            if (recipient == null) {
                player.sendMessage(color("&cThe specified player doesn't exist."));
                player.sendMessage(color("&cUse: /present (player) (message)"));
                return;
            } else {

                PlayerMenuUtility playerMenuUtility = ChristmasPresents.getPlayerMenuUtility(player);
                if (args.length == 1) {
                    playerMenuUtility.setRecipient(recipient.getDisplayName());
                } else if (args.length > 1) {
                    playerMenuUtility.setPresentMessage(recipient.getDisplayName());

                    StringBuilder message = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        message.append(args[i]).append(" ");
                    }
                    playerMenuUtility.setPresentMessage(message.toString());
                }

                new NewPresentMenu(playerMenuUtility).open();

            }

        } else {
            new NewPresentMenu(ChristmasPresents.getPlayerMenuUtility(player)).open();
        }

    }

}
