package net.woolgens.core.root;

import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class CoreRootBootstrap {

    private ServerScope scope;

    public CoreRootBootstrap(ServerScope scope) {
        this.scope = scope;
    }
}
