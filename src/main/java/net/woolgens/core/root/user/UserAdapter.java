package net.woolgens.core.root.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.woolgens.api.user.User;
import net.woolgens.api.user.data.UserData;

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


}
