package net.woolgens.core.root.config;

import net.woolgens.library.file.yaml.YamlConfig;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GatewayConfig extends YamlConfig {

    public GatewayConfig(String path) {
        super(path, "gateway");
    }

    @Override
    public void writeDefaults() {
        set("url", "http://127.0.0.1:1922/");
    }

    public String getUrl() {
        return getGeneric("url");
    }
}
