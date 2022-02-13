package net.woolgens.core.root.web;

import net.woolgens.api.web.WebUserProvider;
import net.woolgens.api.web.model.WebUser;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.library.common.http.HttpRequester;
import net.woolgens.library.common.http.HttpResponse;
import net.woolgens.library.common.http.OkHttpRequester;
import net.woolgens.library.common.http.auth.HttpAuthenticator;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class WebUserAdapter implements WebUserProvider {

    private final CoreRootBootstrap bootstrap;
    private final HttpRequester requester;
    private ExecutorService pool;

    public WebUserAdapter(CoreRootBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        //--------------------------------------------------------
        this.requester = new OkHttpRequester(bootstrap.getConfiguration().getBackend().getGeneric("auth") + "users");
        this.requester.setAuthenticator(new HttpAuthenticator("Authorization", bootstrap.getVaultProvider().getDefaultAccessToken()));
        this.requester.setMapper(bootstrap.getDefaultExceptionMapper());
        //--------------------------------------------------------

        this.pool = Executors.newCachedThreadPool();
    }

    @Override
    public boolean existsUserByUUID(UUID uuid) {
        HttpResponse<WebUser> response = requester.get("/" + uuid.toString(), WebUser.class);
        return response.isSuccess();
    }

    @Override
    public WebUser getUserByUUID(UUID uuid) {
        HttpResponse<WebUser> response = requester.get("/" + uuid.toString(), WebUser.class);
        return response.getBody();
    }

    @Override
    public WebUser save(WebUser webUser) {
        HttpResponse<WebUser> response = requester.put("", WebUser.class, webUser);
        return response.getBody();
    }

    @Override
    public CompletableFuture<WebUser> saveAsync(WebUser webUser) {
        return CompletableFuture.supplyAsync(() -> save(webUser), pool);
    }
}
