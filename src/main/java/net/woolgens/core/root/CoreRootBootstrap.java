package net.woolgens.core.root;

import lombok.Getter;
import net.woolgens.api.WoolgensApi;
import net.woolgens.api.user.UserCacheProvider;
import net.woolgens.api.user.UserProvider;
import net.woolgens.api.vault.VaultProvider;
import net.woolgens.api.web.WebGroupProvider;
import net.woolgens.api.web.WebTemporaryTokenProvider;
import net.woolgens.api.web.WebUserProvider;
import net.woolgens.core.root.config.ConfigFacade;
import net.woolgens.core.root.database.RedisContextWrapper;
import net.woolgens.core.root.user.UserProviderAdapter;
import net.woolgens.core.root.user.cache.RedisUserCacheAdapter;
import net.woolgens.core.root.vault.VaultAdapter;
import net.woolgens.core.root.web.WebGroupAdapter;
import net.woolgens.core.root.web.WebTemporaryTokenAdapter;
import net.woolgens.core.root.web.WebUserAdapter;
import net.woolgens.library.common.exception.ExceptionMapper;
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

    private final String defaultDirectory;
    private final ServerScope scope;
    private WrappedLogger logger;
    private ConfigFacade configuration;
    private VaultAdapter vaultProvider;
    private UserProviderAdapter userProvider;
    private ExceptionMapper<Exception> defaultExceptionMapper;
    private RedisContextWrapper redisContext;

    public CoreRootBootstrap(ServerScope scope, String defaultDirectory) {
        bootstrap = this;
        //--------------------------------------------------------------
        this.scope = scope;
        this.defaultDirectory = defaultDirectory;
        this.logger = new DefaultLoggerAdapter();

        this.defaultExceptionMapper = exception -> {
            logger.severe("Error: " + exception.getMessage());
        };
        this.configuration = new ConfigFacade(defaultDirectory);
        this.vaultProvider = new VaultAdapter(this);
        this.redisContext = new RedisContextWrapper(vaultProvider);
        this.userProvider = new UserProviderAdapter(this);

        registerProviders();

    }

    private void registerProviders() {
        WoolgensApi.registerProvider(UserProvider.class, userProvider);
        WoolgensApi.registerProvider(VaultProvider.class, vaultProvider);
        WoolgensApi.registerProvider(UserCacheProvider.class, new RedisUserCacheAdapter(this));
        WoolgensApi.registerProvider(WebUserProvider.class, new WebUserAdapter(this));
        WoolgensApi.registerProvider(WebGroupProvider.class, new WebGroupAdapter(this));
        WoolgensApi.registerProvider(WebTemporaryTokenProvider.class, new WebTemporaryTokenAdapter(this));

    }


}
