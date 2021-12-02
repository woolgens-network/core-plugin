package net.woolgens.core.spigot;

import lombok.Getter;
import net.woolgens.api.WoolgensConstants;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.core.root.ServerScope;
import net.woolgens.core.spigot.command.TestCommand;
import net.woolgens.core.spigot.listener.PlayerLoginListener;
import net.woolgens.core.spigot.listener.PlayerQuitListener;
import net.woolgens.library.spigot.command.exception.CommandExceptionMapper;
import net.woolgens.library.spigot.command.exception.impl.NoPermissionException;
import net.woolgens.library.spigot.command.exception.impl.SenderException;
import net.woolgens.library.spigot.gui.listener.GUIInventoryClickListener;
import net.woolgens.library.spigot.gui.listener.GUIInventoryCloseListener;
import net.woolgens.library.spigot.setup.SpigotSetup;
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

    private CommandExceptionMapper commandExceptionMapper;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        initialize();
    }

    private void initialize() {
        this.root = new CoreRootBootstrap(ServerScope.SPIGOT, "plugins" + File.separator + getDescription().getName() +
                File.separator);
        setupCommandExceptionMapper();
        setup();
    }

    private void setupCommandExceptionMapper() {
        commandExceptionMapper = exception -> {
            if(exception instanceof SenderException) {

            } else if(exception instanceof NoPermissionException noPermissionException) {
                noPermissionException.getSender().sendMessage(WoolgensConstants.PREFIX + "§cYou do not have enough permissions.");
            }
        };
    }

    private void setup() {
        SpigotSetup setup = new SpigotSetup(this, "core");
        /**
         * Commands
         */
        setup.addCommand(new TestCommand());

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
