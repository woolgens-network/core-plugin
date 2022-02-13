package net.woolgens.core.spigot.user;

import net.woolgens.api.user.settings.UserSetting;
import net.woolgens.api.user.settings.UserSettingsRegistry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class UserSettingsAdapter implements UserSettingsRegistry {

    private Map<String, UserSetting> settings;

    public UserSettingsAdapter() {
        this.settings = new LinkedHashMap<>();
    }

    @Override
    public void register(UserSetting userSetting) {
        this.settings.put(userSetting.getKey().toLowerCase(), userSetting);
    }

    @Override
    public UserSetting getSettingByKey(String key) {
        return this.settings.get(key.toLowerCase());
    }

    @Override
    public Collection<UserSetting> getSettings() {
        return this.settings.values();
    }
}
