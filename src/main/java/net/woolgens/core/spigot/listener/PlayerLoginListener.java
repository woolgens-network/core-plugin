package net.woolgens.core.spigot.listener;

import net.woolgens.api.WoolgensApi;
import net.woolgens.api.user.User;
import net.woolgens.api.user.UserCacheProvider;
import net.woolgens.api.user.UserProvider;
import net.woolgens.core.root.user.UserProviderAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PlayerLoginListener implements Listener {

    private static final String MESSAGE = "§cYour data is still loading. Please have patience";

    @EventHandler
    public void onAsync(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        UserProvider<User> provider = WoolgensApi.getProvider(UserProvider.class);
        User user = provider.load(uuid);
        if(!user.getData().getName().equals(event.getName())) {
            user.getData().setName(event.getName());
            provider.saveAsync(user, true);
        }

    }

    @EventHandler
    public void onSync(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        UserProviderAdapter provider = WoolgensApi.getProvider(UserProvider.class);
        if(!provider.getUsers().containsKey(player.getUniqueId())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, MESSAGE);
            return;
        }
        UserCacheProvider cacheProvider = WoolgensApi.getProvider(UserCacheProvider.class);
        cacheProvider.put(player.getUniqueId().toString(), player.getName());
    }


}
