package net.woolgens.core.spigot;

import lombok.Getter;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.core.root.ServerScope;
import org.bukkit.plugin.java.JavaPlugin;

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
        this.root = new CoreRootBootstrap(ServerScope.SPIGOT);

        setup();
    }

    private void setup() {

    }

}
