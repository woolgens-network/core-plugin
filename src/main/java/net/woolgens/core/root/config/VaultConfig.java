package net.woolgens.core.root.config;

import net.woolgens.library.file.yaml.YamlConfig;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class VaultConfig extends YamlConfig {

    public VaultConfig(String path) {
        super(path, "vault");
    }

    @Override
    public void writeDefaults() {
        set("host", "http://127.0.0.1:8200");
        set("secrets.path", "secret/");
        set("token", "XXXXXXXXXXXX");
    }

    public String getSecretsPath(){
        return getGeneric("secrets.path");
    }

    public String getHost() {
        return getGeneric("host");
    }

    public String getToken() {
        return getGeneric("token");
    }
}
