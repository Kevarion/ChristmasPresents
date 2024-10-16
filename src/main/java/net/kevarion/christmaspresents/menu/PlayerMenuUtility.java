package net.kevarion.christmaspresents.menu;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;
    private String recipient;

    private String presentMessage;

    public PlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }

    public String getPresentMessage() {
        return presentMessage;
    }

    public void setPresentMessage(String presentMessage) {
        this.presentMessage = presentMessage;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
