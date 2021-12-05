package net.woolgens.core.root.user;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.woolgens.api.WoolgensApi;
import net.woolgens.api.WoolgensConstants;
import net.woolgens.api.user.User;
import net.woolgens.api.user.UserProvider;
import net.woolgens.api.user.data.SeasonData;
import net.woolgens.api.user.data.UserData;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class UserAdapter implements User {

    private UserData data;
    private Map<String, Object> tags;

    public UserAdapter(UserData data) {
        this.data = data;
        this.tags = new HashMap<>();
    }

    @Override
    public SeasonData getSeasonData() {

        if(!data.getSeasons().containsKey(WoolgensConstants.CURRENT_SEASON)) {
            //-----------------------------------------------------
            SeasonData seasonData = new SeasonData();
            seasonData.setStats(new HashMap<>());
            seasonData.setCrates(new HashMap<>());
            seasonData.setLevel(1);
            //-----------------------------------------------------

            data.getSeasons().put(WoolgensConstants.CURRENT_SEASON, seasonData);
            UserProvider<User> provider = WoolgensApi.getProvider(UserProvider.class);
            provider.saveAsync(this, true);
        }
        return data.getSeasons().get(WoolgensConstants.CURRENT_SEASON);
    }

    @Override
    public void setTag(String key, Object value) {
        this.tags.put(key.toLowerCase(), value);
    }

    @Override
    public void removeTag(String key) {
        this.tags.remove(key.toLowerCase());
    }

    @Override
    public <T> T getTag(String key) {
        return (T)tags.get(key.toLowerCase());
    }

    @Override
    public boolean containsTag(String key) {
        return tags.containsKey(key.toLowerCase());
    }
}
