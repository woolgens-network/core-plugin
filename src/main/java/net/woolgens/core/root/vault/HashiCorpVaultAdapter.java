package net.woolgens.core.root.vault;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultException;
import net.woolgens.api.vault.VaultProvider;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.core.root.config.VaultConfig;
import net.woolgens.library.common.exception.ExceptionMapper;
import net.woolgens.library.common.http.mapper.HttpExceptionMapper;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HashiCorpVaultAdapter implements VaultProvider {

    private final CoreRootBootstrap bootstrap;
    private final VaultConfig config;
    private com.bettercloud.vault.VaultConfig vaultConfig;
    private Vault vault;

    private ExceptionMapper<Exception> mapper;

    public HashiCorpVaultAdapter(CoreRootBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        this.config = bootstrap.getConfiguration().getVault();
        this.mapper = exception -> {
            exception.printStackTrace();
        };
        try {
            this.vaultConfig = new com.bettercloud.vault.VaultConfig()
                    .address(config.getHost())
                    .token(config.getToken())
                    .build();
            this.vault = new Vault(vaultConfig, 2);
        } catch (VaultException exception) {
            this.mapper.map(exception);
        }
    }

    @Override
    public String getSecret(String category, String key) {
        try {
            return this.vault.logical().read(config.getSecretsPath().concat(category.toLowerCase())).getData().get(key.toLowerCase());
        } catch (VaultException exception) {
            this.mapper.map(exception);
        }
        return null;
    }
}
