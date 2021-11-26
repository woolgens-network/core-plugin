package net.woolgens.core.root.http;

import lombok.AllArgsConstructor;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.library.common.http.mapper.HttpExceptionMapper;

import java.net.ConnectException;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class RootExceptionMapper implements HttpExceptionMapper {

    private CoreRootBootstrap bootstrap;

    @Override
    public void map(Exception exception) {
        if(exception instanceof ConnectException) {
            bootstrap.getLogger().severe("Seems like a service is down: " + exception.getMessage());
        } else {
            exception.printStackTrace();
        }
    }
}
