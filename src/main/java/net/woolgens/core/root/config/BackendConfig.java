package net.woolgens.core.root.config;

import net.woolgens.library.file.yaml.YamlConfig;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BackendConfig extends YamlConfig {

    public BackendConfig(String path) {
        super(path, "backend");
    }

    @Override
    public void writeDefaults() {
        set("user", "http://127.0.0.1:8080/");
    }

    public String getUser() {
        return getGeneric("user");
    }
}
