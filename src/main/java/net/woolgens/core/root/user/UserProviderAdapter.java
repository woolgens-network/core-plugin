package net.woolgens.core.root.user;

import lombok.Getter;
import net.woolgens.api.user.UserProvider;
import net.woolgens.api.user.data.UserData;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.library.common.http.HttpRequestFailedException;
import net.woolgens.library.common.http.HttpRequester;
import net.woolgens.library.common.logger.WrappedLogger;
import net.woolgens.library.common.logger.adapter.NamedLoggerAdapter;
import net.woolgens.library.common.queue.QueueOperation;
import net.woolgens.library.common.queue.QueueOperationPool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class UserProviderAdapter implements UserProvider<UserAdapter> {

    private final String url;
    private final CoreRootBootstrap bootstrap;
    private WrappedLogger logger;
    private QueueOperationPool<QueueOperation> pool;

    private Map<UUID, UserAdapter> users;


    public UserProviderAdapter(CoreRootBootstrap bootstrap) {
        this.url = "users";
        this.bootstrap = bootstrap;
        this.pool = new QueueOperationPool<>("User");
        this.logger = new NamedLoggerAdapter("User");
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public UserAdapter register(UUID uuid) {
        UserData data = new UserData();
        data.setUuid(uuid.toString());

        HttpRequester requester = bootstrap.getRequester();
        try {
            requester.post(getUrl(), UserData.class, data);
        }catch (HttpRequestFailedException exception) {
            logger.severe("User service request failed (register): " + exception.getMessage());
        }
        return new UserAdapter(data);
    }

    @Override
    public CompletableFuture<UserAdapter> registerAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> register(uuid), pool.getThreadPool());
    }

    @Override
    public UserAdapter load(UUID uuid) {
        if(users.containsKey(uuid)) {
            return users.get(uuid);
        }
        HttpRequester requester = bootstrap.getRequester();
        UserAdapter adapter;
        try {
            UserData data = requester.get(getUrl() + "/" + uuid.toString(), UserData.class);
            adapter = new UserAdapter(data);
        } catch (HttpRequestFailedException exception) {
            adapter = register(uuid);
        }
        users.put(uuid, adapter);
        return adapter;
    }

    @Override
    public CompletableFuture<UserAdapter> loadAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> load(uuid), pool.getThreadPool());
    }

    @Override
    public void unload(UUID uuid) {
        users.remove(uuid);
    }

    @Override
    public void unloadAll() {
        users.clear();
    }

    @Override
    public UserAdapter getUserByUUID(UUID uuid) {
        if(users.containsKey(uuid)) {
           return users.get(uuid);
        }
        return load(uuid);
    }

    @Override
    public boolean existsUserByUUID(UUID uuid) {
        if(users.containsKey(uuid)) {
            return true;
        }
        HttpRequester requester = bootstrap.getRequester();
        try {
            requester.get(getUrl() + "/" + uuid.toString(), UserData.class);
            return true;
        }catch (HttpRequestFailedException exception) {}
        return false;
    }

}
