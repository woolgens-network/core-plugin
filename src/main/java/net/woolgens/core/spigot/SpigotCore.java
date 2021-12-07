package net.woolgens.core.spigot;

import lombok.Getter;
import net.woolgens.api.WoolgensApi;
import net.woolgens.api.WoolgensConstants;
import net.woolgens.api.user.User;
import net.woolgens.api.user.UserProvider;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.core.root.ServerScope;
import net.woolgens.core.spigot.listener.PlayerLoginListener;
import net.woolgens.core.spigot.listener.PlayerQuitListener;
import net.woolgens.core.spigot.location.FileLocationAdapter;
import net.woolgens.core.spigot.location.LocationProvider;
import net.woolgens.library.database.redis.RedisContext;
import net.woolgens.library.spigot.command.CommandNode;
import net.woolgens.library.spigot.command.exception.impl.NoPermissionException;
import net.woolgens.library.spigot.command.exception.impl.SenderException;
import net.woolgens.library.spigot.gui.listener.GUIInventoryClickListener;
import net.woolgens.library.spigot.gui.listener.GUIInventoryCloseListener;
import net.woolgens.library.spigot.npc.NPCProcessor;
import net.woolgens.library.spigot.setup.SpigotSetup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class SpigotCore extends JavaPlugin {

    @Getter
    private static SpigotCore instance;

    private CoreRootBootstrap root;
    private LocationProvider locationProvider;


    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        initialize();
    }

    @Override
    public void onDisable() {
        UserProvider<User> provider = WoolgensApi.getProvider(UserProvider.class);
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = provider.getUserByUUID(player.getUniqueId());
            provider.save(user);
        }
        NPCProcessor.disable();
    }

    private void initialize() {
        this.root = new CoreRootBootstrap(ServerScope.SPIGOT, "plugins" + File.separator + getDescription().getName() +
                File.separator);
        this.locationProvider = new FileLocationAdapter(root.getDefaultDirectory());

        setupCommandExceptionMapper();
        setup();
        NPCProcessor.start(this);
    }

    private void setupCommandExceptionMapper() {
        CommandNode.DEFAULT_EXCEPTION_MAPPER = exception -> {
            if (exception instanceof SenderException) {

            } else if (exception instanceof NoPermissionException noPermissionException) {
                noPermissionException.getSender().sendMessage(WoolgensConstants.PREFIX + "§cYou do not have enough permissions.");
            }
        };
    }

    private void setup() {
        SpigotSetup setup = new SpigotSetup(this, "core");
        /**
         * Commands
         */

        /**
         * Listeners
         */
        setup.addListener(new PlayerLoginListener());
        setup.addListener(new PlayerQuitListener());
        setup.addListener(new GUIInventoryClickListener());
        setup.addListener(new GUIInventoryCloseListener());

        //------------------------------------------------
        setup.register();
    }

}
