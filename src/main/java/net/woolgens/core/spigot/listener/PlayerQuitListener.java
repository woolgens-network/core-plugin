package net.woolgens.core.spigot.listener;

import net.woolgens.api.WoolgensApi;
import net.woolgens.api.user.User;
import net.woolgens.api.user.UserProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCall(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserProvider<User> provider = WoolgensApi.getProvider(UserProvider.class);
        provider.saveAsync(provider.getUserByUUID(player.getUniqueId()), false)
                .thenAccept(user -> provider.unload(player.getUniqueId()));

    }
}
