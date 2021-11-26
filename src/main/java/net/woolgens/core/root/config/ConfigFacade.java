package net.woolgens.core.root.config;

import lombok.Getter;

import java.io.File;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class ConfigFacade {

    private final String defaultDirectory;
    private final String configDirectory;

    private GatewayConfig gateway;
    private VaultConfig vault;

    public ConfigFacade(String defaultDirectory) {
        this.defaultDirectory = defaultDirectory;
        this.configDirectory = defaultDirectory + "config" + File.separator;

        this.gateway = new GatewayConfig(configDirectory);
        this.vault = new VaultConfig(configDirectory);
    }

}
