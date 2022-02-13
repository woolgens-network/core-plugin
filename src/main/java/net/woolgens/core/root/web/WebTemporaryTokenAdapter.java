package net.woolgens.core.root.web;

import net.woolgens.api.web.WebGroupProvider;
import net.woolgens.api.web.WebTemporaryTokenProvider;
import net.woolgens.api.web.model.WebGroup;
import net.woolgens.api.web.model.WebTemporaryToken;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.library.common.http.HttpRequester;
import net.woolgens.library.common.http.HttpResponse;
import net.woolgens.library.common.http.OkHttpRequester;
import net.woolgens.library.common.http.auth.HttpAuthenticator;

import java.util.List;
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
public class WebTemporaryTokenAdapter implements WebTemporaryTokenProvider {

    private final CoreRootBootstrap bootstrap;
    private final HttpRequester requester;
    private ExecutorService pool;

    public WebTemporaryTokenAdapter(CoreRootBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        //--------------------------------------------------------
        this.requester = new OkHttpRequester(bootstrap.getConfiguration().getBackend().getGeneric("auth") + "tokens");
        this.requester.setAuthenticator(new HttpAuthenticator("Authorization", bootstrap.getVaultProvider().getDefaultAccessToken()));
        this.requester.setMapper(bootstrap.getDefaultExceptionMapper());
        //--------------------------------------------------------

        this.pool = Executors.newCachedThreadPool();
    }

    @Override
    public WebTemporaryToken register(WebTemporaryToken webTemporaryToken) {
        HttpResponse<WebTemporaryToken> response = requester.put("" , WebTemporaryToken.class, webTemporaryToken);
        return response.getBody();
    }

    @Override
    public CompletableFuture<WebTemporaryToken> registerAsync(WebTemporaryToken webTemporaryToken) {
        return CompletableFuture.supplyAsync(() -> register(webTemporaryToken), pool);
    }

    @Override
    public WebTemporaryToken getTokenById(String id) {
        HttpResponse<WebTemporaryToken> response = requester.get("/" + id, WebTemporaryToken.class);
        return response.getBody();
    }

    @Override
    public boolean isTokenValid(String id) {
        HttpResponse<WebTemporaryToken> response = requester.get("/" + id, WebTemporaryToken.class);
        return response.isSuccess();
    }
}
