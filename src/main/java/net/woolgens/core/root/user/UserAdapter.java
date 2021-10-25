package net.woolgens.core.root.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.woolgens.api.WoolgensApi;
import net.woolgens.api.user.User;
import net.woolgens.api.user.UserProvider;
import net.woolgens.api.user.data.UserData;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.library.common.http.HttpRequestFailedException;
import net.woolgens.library.common.http.HttpRequester;
import net.woolgens.library.common.queue.QueueOperationTask;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public class UserAdapter implements User {

    private UserData data;

    @Override
    public User save() {
        UserProviderAdapter provider = WoolgensApi.getProvider(UserProvider.class);
        HttpRequester requester = provider.getBootstrap().getRequester();
        try {
            requester.put(provider.getUrl() + "/" + data.getUuid(), UserData.class, data);
        }catch (HttpRequestFailedException exception) {
            provider.getLogger().severe("Can't save user: " + data.getUuid());
        }
        return this;
    }

    @Override
    public CompletableFuture<User> saveAsync(boolean queue) {
        UserProviderAdapter provider = WoolgensApi.getProvider(UserProvider.class);
        if(!queue) {
            return CompletableFuture.supplyAsync(() -> null, provider.getPool().getThreadPool());
        }
        CompletableFuture<User> future = new CompletableFuture<>();
        provider.getPool().addTask(data.getUuid(), 1000 * 10, () -> future.complete(save()));
        return future;
    }
}
