package net.woolgens.core.spigot.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.woolgens.api.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class UserLevelUpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private Player player;
    private User user;

    private int fromLevel;
    private int toLevel;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}