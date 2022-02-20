package net.woolgens.core.spigot.listener;

import net.woolgens.api.WoolgensApi;
import net.woolgens.api.user.User;
import net.woolgens.api.user.UserProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onCall(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserProvider<User> provider = WoolgensApi.getProvider(UserProvider.class);
        User user = provider.getUserByUUID(player.getUniqueId());
        user.getData().setLastJoined(System.currentTimeMillis());
        provider.saveAsync(user, true);
    }
}
