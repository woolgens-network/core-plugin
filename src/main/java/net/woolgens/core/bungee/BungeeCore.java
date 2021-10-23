package net.woolgens.core.bungee;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.core.root.ServerScope;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class BungeeCore extends Plugin {

    @Getter
    private static BungeeCore instance;

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
        this.root = new CoreRootBootstrap(ServerScope.BUNGEE);

        setup();
    }

    private void setup() {

    }

}
