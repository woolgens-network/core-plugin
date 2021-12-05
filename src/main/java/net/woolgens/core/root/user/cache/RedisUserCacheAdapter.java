package net.woolgens.core.root.user.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.woolgens.api.user.UserCacheProvider;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.core.root.database.RedisContextWrapper;
import org.redisson.api.RMap;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RedisUserCacheAdapter implements UserCacheProvider {

    private final RedisContextWrapper context;

    private RMap<String, String> uuidCache;
    private RMap<String, String> nameCache;

    private LoadingCache<UUID, String> localUuidCache;
    private LoadingCache<String, UUID> localNameCache;

    public RedisUserCacheAdapter(CoreRootBootstrap bootstrap) {
        this.context = bootstrap.getRedisContext();
        this.uuidCache = context.getClient().getMap("uuid_cache");
        this.nameCache = context.getClient().getMap("uuid_name_cache");
        this.localUuidCache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new CacheLoader<UUID, String>() {
                    @Override
                    public String load(UUID uuid) throws Exception {
                        return uuidCache.get(uuid.toString());
                    }
                });
        this.localNameCache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new CacheLoader<String, UUID>() {
                    @Override
                    public UUID load(String name) throws Exception {
                        return UUID.fromString(nameCache.get(name));
                    }
                });
    }

    @Override
    public void put(String uuid, String name) {
        this.uuidCache.put(uuid, name);
        this.nameCache.put(name.toLowerCase(), uuid);
    }

    @Override
    public String getNameByUUID(UUID uuid) {
        return localUuidCache.getUnchecked(uuid);
    }

    @Override
    public UUID getUUIDByName(String name) {
        return localNameCache.getUnchecked(name.toLowerCase());
    }

    @Override
    public boolean existsByUUID(UUID uuid) {
        return uuidCache.containsKey(uuid.toString());
    }

    @Override
    public boolean existsByName(String name) {
        return nameCache.containsKey(name.toLowerCase());
    }
}
