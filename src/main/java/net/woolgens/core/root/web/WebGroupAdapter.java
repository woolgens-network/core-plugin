package net.woolgens.core.root.web;

import net.woolgens.api.web.WebGroupProvider;
import net.woolgens.api.web.WebUserProvider;
import net.woolgens.api.web.model.WebGroup;
import net.woolgens.api.web.model.WebUser;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.library.common.http.HttpRequester;
import net.woolgens.library.common.http.HttpResponse;
import net.woolgens.library.common.http.OkHttpRequester;
import net.woolgens.library.common.http.auth.HttpAuthenticator;

import java.util.List;
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
public class WebGroupAdapter implements WebGroupProvider {

    private final CoreRootBootstrap bootstrap;
    private final HttpRequester requester;
    private ExecutorService pool;

    public WebGroupAdapter(CoreRootBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        //--------------------------------------------------------
        this.requester = new OkHttpRequester(bootstrap.getConfiguration().getBackend().getGeneric("auth") + "groups");
        this.requester.setAuthenticator(new HttpAuthenticator("Authorization", bootstrap.getVaultProvider().getDefaultAccessToken()));
        this.requester.setMapper(bootstrap.getDefaultExceptionMapper());
        //--------------------------------------------------------

        this.pool = Executors.newCachedThreadPool();
    }

    @Override
    public List<WebGroup> registerMany(List<WebGroup> list) {
        HttpResponse<WebGroup[]> response = requester.put("", WebGroup[].class, list);
        return List.of(response.getBody());
    }

    @Override
    public CompletableFuture<List<WebGroup>> registerManyAsync(List<WebGroup> list) {
        return CompletableFuture.supplyAsync(() -> registerMany(list), pool);
    }
}
