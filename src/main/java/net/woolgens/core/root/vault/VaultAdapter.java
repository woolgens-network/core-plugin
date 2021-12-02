package net.woolgens.core.root.vault;

import lombok.Getter;
import net.woolgens.api.vault.VaultProvider;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.library.vault.VaultBootstrap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/

public class VaultAdapter implements VaultProvider {

    private VaultBootstrap vaultBootstrap;

    @Getter
    private String defaultAccessToken;

    public VaultAdapter(CoreRootBootstrap bootstrap) {
        try {
            this.vaultBootstrap = new VaultBootstrap(bootstrap.getConfiguration().getConfigDirectory());
            this.defaultAccessToken = getSecret("tokens", "default");
        } catch (Exception exception) {
            bootstrap.getDefaultExceptionMapper().map(exception);
        }
    }

    @Override
    public String getSecret(String category, String key) {
        return vaultBootstrap.getProvider().getSecret(category, key);
    }
}
