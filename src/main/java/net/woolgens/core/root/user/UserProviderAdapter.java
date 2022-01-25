package net.woolgens.core.root.user;

import lombok.Getter;
import net.woolgens.api.WoolgensApi;
import net.woolgens.api.user.UserProvider;
import net.woolgens.api.user.data.UserData;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.library.common.http.HttpRequester;
import net.woolgens.library.common.http.HttpResponse;
import net.woolgens.library.common.http.OkHttpRequester;
import net.woolgens.library.common.http.auth.HttpAuthenticator;
import net.woolgens.library.common.logger.WrappedLogger;
import net.woolgens.library.common.logger.adapter.NamedLoggerAdapter;
import net.woolgens.library.common.queue.QueueOperation;
import net.woolgens.library.common.queue.QueueOperationPool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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
    private HttpRequester requester;


    public UserProviderAdapter(CoreRootBootstrap bootstrap) {
        this.url = "users";
        this.bootstrap = bootstrap;

        //--------------------------------------------------------
        this.requester = new OkHttpRequester(bootstrap.getConfiguration().getBackend().getUser());
        this.requester.setAuthenticator(new HttpAuthenticator("Authorization", bootstrap.getVaultProvider().getDefaultAccessToken()));
        this.requester.setMapper(bootstrap.getDefaultExceptionMapper());
        //--------------------------------------------------------

        this.pool = new QueueOperationPool<>("User");
        this.logger = new NamedLoggerAdapter("User");
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public UserAdapter save(UserAdapter user) {
        HttpRequester requester = getRequester();
        HttpResponse<UserData> response = requester.post(getUrl(), UserData.class, user.getData());
        if(!response.isSuccess()) {
            getLogger().warning("Can't save user: " + user.getData().getUuid() + " status-code: " + response.getStatus());
            return null;
        }
        return user;
    }

    @Override
    public CompletableFuture<UserAdapter> saveAsync(UserAdapter user, boolean queue) {
        UserProviderAdapter provider = WoolgensApi.getProvider(UserProvider.class);
        if(!queue) {
            return CompletableFuture.supplyAsync(() -> save(user), provider.getPool().getThreadPool());
        }
        CompletableFuture<UserAdapter> future = new CompletableFuture<>();
        provider.getPool().addTask(user.getData().getUuid(), 1000 * 10, () -> future.complete(save(user)));
        return future;
    }

    @Override
    public UserAdapter register(UUID uuid) {
        //-----------------------------------------------------
        UserData data = new UserData();
        data.setUuid(uuid.toString());
        data.setName("");
        data.setLand("");
        data.setJoined(System.currentTimeMillis());
        data.setStats(new HashMap<>());
        data.setSettings(new HashMap<>());
        data.setBooster(new HashMap<>());
        data.setSeasons(new HashMap<>());
        data.setExtensions(new HashMap<>());
        //-----------------------------------------------------

        HttpRequester requester = getRequester();
        HttpResponse<UserData> response = requester.post(getUrl(), UserData.class, data);
        if(!response.isSuccess()) {
            logger.warning("Can't register user: " + data.getUuid() + " status-code: " + response.getStatus());
            return null;
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
        HttpRequester requester = getRequester();
        UserAdapter adapter;
        HttpResponse<UserData> response = requester.get(getUrl() + "/" + uuid.toString(), UserData.class);
        if(!response.isSuccess()) {
            adapter = register(uuid);
        } else {
            adapter = new UserAdapter(response.getBody());
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
        HttpRequester requester = getRequester();
        HttpResponse<UserData> response = requester.get(getUrl() + "/" + uuid.toString(), UserData.class);
        return response.isSuccess();
    }

}
