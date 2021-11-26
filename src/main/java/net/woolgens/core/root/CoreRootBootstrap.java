package net.woolgens.core.root;

import lombok.Getter;
import net.woolgens.api.WoolgensApi;
import net.woolgens.api.user.UserProvider;
import net.woolgens.api.vault.VaultProvider;
import net.woolgens.core.root.config.ConfigFacade;
import net.woolgens.core.root.http.RootExceptionMapper;
import net.woolgens.core.root.user.UserProviderAdapter;
import net.woolgens.core.root.vault.HashiCorpVaultAdapter;
import net.woolgens.library.common.http.HttpRequester;
import net.woolgens.library.common.http.OkHttpRequester;
import net.woolgens.library.common.logger.WrappedLogger;
import net.woolgens.library.common.logger.adapter.DefaultLoggerAdapter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class CoreRootBootstrap {

    @Getter
    private static CoreRootBootstrap bootstrap;

    private ServerScope scope;
    private WrappedLogger logger;
    private ConfigFacade configuration;
    private HttpRequester requester;
    private UserProviderAdapter userProvider;
    private HashiCorpVaultAdapter vaultProvider;

    public CoreRootBootstrap(ServerScope scope, String defaultDirectory) {
        bootstrap = this;
        //--------------------------------------------------------------
        this.scope = scope;
        this.logger = new DefaultLoggerAdapter();
        this.configuration = new ConfigFacade(defaultDirectory);
        this.requester = new OkHttpRequester(configuration.getGateway().getUrl());
        this.requester.setMapper(new RootExceptionMapper(this));
        registerProviders();
    }

    private void registerProviders() {
        WoolgensApi.registerProvider(UserProvider.class, userProvider = new UserProviderAdapter(this));
        WoolgensApi.registerProvider(VaultProvider.class, vaultProvider = new HashiCorpVaultAdapter(this));

    }


}
