package net.woolgens.core.spigot;

import lombok.Getter;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.core.root.ServerScope;
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
        setup();
    }

    private void setup() {
        SpigotSetup setup = new SpigotSetup(this, "core");
        /**
         * Commands
         */

        /**
         * Listeners
         */

        setup.register();
    }

}
