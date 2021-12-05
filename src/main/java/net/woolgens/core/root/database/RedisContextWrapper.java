package net.woolgens.core.root.database;

import net.woolgens.api.vault.VaultProvider;
import net.woolgens.library.database.Credentials;
import net.woolgens.library.database.redis.RedisContext;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RedisContextWrapper extends RedisContext {

    public RedisContextWrapper(VaultProvider provider) {
        super(new Credentials(provider.getSecret("redis", "host"),
                Integer.valueOf(provider.getSecret("redis", "port"))
                        , "", "", provider.getSecret("redis", "password")));
        connect();
    }

}
