package net.woolgens.core.root.user;

import lombok.Getter;
import net.woolgens.api.WoolgensApi;
import net.woolgens.api.WoolgensConstants;
import net.woolgens.api.user.User;
import net.woolgens.api.user.UserProvider;
import net.woolgens.api.user.data.Badge;
import net.woolgens.api.user.data.SeasonData;
import net.woolgens.api.user.data.UserData;
import net.woolgens.api.user.data.UserSettings;
import net.woolgens.api.user.data.quest.SeasonQuestData;
import net.woolgens.api.user.data.skills.Skills;
import net.woolgens.core.root.CoreRootBootstrap;
import net.woolgens.core.root.ServerScope;
import net.woolgens.core.spigot.event.UserLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

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
    public void setSetting(UserSettings settings, Object value) {
        setSetting(settings.name().toLowerCase(), value);
    }

    @Override
    public <T> T getSetting(UserSettings settings) {
        String lowered = settings.name().toLowerCase();
        if(!data.getSettings().containsKey(lowered)) {
            data.getSettings().put(lowered, settings.getDefaultValue());
        }
        return (T) this.data.getSettings().get(lowered);
    }

    @Override
    public boolean isSetting(UserSettings settings) {
        return getSetting(settings);
    }

    @Override
    public void setSetting(String setting, Object value) {
        this.data.getSettings().put(setting.toLowerCase(), value);
    }

    @Override
    public <T> T getSetting(String settings) {
        String lowered = settings.toLowerCase();
        return (T) this.data.getSettings().get(lowered);
    }

    @Override
    public boolean isSetting(String settings) {
        return getSetting(settings);
    }

    @Override
    public SeasonData getSeasonData() {

        if(!data.getSeasons().containsKey(WoolgensConstants.CURRENT_SEASON)) {
            //-----------------------------------------------------
            SeasonData seasonData = new SeasonData();
            seasonData.setStats(new HashMap<>());
            seasonData.setCrates(new HashMap<>());
            seasonData.setExtensions(new HashMap<>());
            seasonData.setTimestamps(new HashMap<>());
            seasonData.setHomes(new LinkedHashMap<>());

            seasonData.setLevel(1);

            SeasonQuestData questData = new SeasonQuestData();
            questData.setSelected(new HashMap<>());
            questData.setFinished(new HashMap<>());


            seasonData.setQuests(questData);
            //-----------------------------------------------------

            data.getSeasons().put(WoolgensConstants.CURRENT_SEASON, seasonData);
            UserProvider<User> provider = WoolgensApi.getProvider(UserProvider.class);
            provider.saveAsync(this, true);
        }
        return data.getSeasons().get(WoolgensConstants.CURRENT_SEASON);
    }

    @Override
    public void addExp(long exp) {
        SeasonData data = getSeasonData();

        long expToNextLevel = getExpToNextLevel();
        long nextExp = data.getExp() + exp;
        int nextLevel = data.getLevel();

        while (nextExp >= expToNextLevel) {
            nextExp = nextExp - expToNextLevel;
            nextLevel++;
            expToNextLevel = getExpToNextLevel(nextLevel);
        }

        Player player = getPlayer();
        if(nextLevel > data.getLevel()) {
            if(CoreRootBootstrap.getBootstrap().getScope() == ServerScope.SPIGOT) {
                Bukkit.getPluginManager().callEvent(new UserLevelUpEvent(player, this, data.getLevel(), nextLevel));
            }
        }
        data.setLevel(nextLevel);
        data.setExp(nextExp);

        UserProvider<User> provider = WoolgensApi.getProvider(UserProvider.class);
        provider.saveAsync(this, true);
    }

    @Override
    public String getColoredLevel() {
        int level = getSeasonData().getLevel();
        String color = "§a";
        if(level >= 30 && level <= 59) {
            color = "§e";
        } else if(level >= 60 && level <= 89) {
            color = "§6";
        } else if(level >= 100) {
            color = "§4";
        }
        return color + level;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(getData().getUuid()));
    }

    public long getExpToNextLevel(int level) {
        return Math.round(100 * (Math.pow(1.132, level)));
    }

    @Override
    public long getExpToNextLevel() {
        int level = getSeasonData().getLevel();
        return getExpToNextLevel(level);
    }

    @Override
    public boolean wait(String key, long millis) {
        if (!containsTag(key)) {
            setTag(key, Long.valueOf(0));
        }
        long value = getTag(key);
        if (System.currentTimeMillis() > value) {
            setTag(key, System.currentTimeMillis() + millis);
            return true;
        }
        return false;
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

    @Override
    public boolean isTimestampAvailable(String key) {
        SeasonData data = getSeasonData();
        if(data.getTimestamps() == null) {
            data.setTimestamps(new HashMap<>());
        }

        final String lowered = key.toLowerCase();
        if(!data.getTimestamps().containsKey(lowered)) {
            return true;
        }
        long until = data.getTimestamps().get(lowered);
        if(System.currentTimeMillis() > until) {
            return true;
        }
        return false;
    }

    @Override
    public long getTimestamp(String key) {
        SeasonData data = getSeasonData();
        if(data.getTimestamps() == null) {
            data.setTimestamps(new HashMap<>());
        }

        final String lowered = key.toLowerCase();
        if(!data.getTimestamps().containsKey(lowered)) {
            return 0;
        }
        return data.getTimestamps().get(lowered);
    }

    @Override
    public void setTimestamp(String key, long timestamp) {
        SeasonData data = getSeasonData();
        if(data.getTimestamps() == null) {
            data.setTimestamps(new HashMap<>());
        }
        data.getTimestamps().put(key.toLowerCase(), timestamp);
    }

    @Override
    public void addBadge(Badge badge) {
        if(data.getBadges() == null) {
            data.setBadges(new ArrayList<>());
        }
        data.getBadges().add(badge);
    }

    @Override
    public void removeBadge(Badge badge) {
        if(data.getBadges() == null) {
            data.setBadges(new ArrayList<>());
        }
        data.getBadges().remove(badge);
    }

    @Override
    public boolean hasBadge(String title) {
        return getBadgeByTitle(title) != null;
    }

    @Override
    public Badge getBadgeByTitle(String title) {
        for(Badge badge : data.getBadges()) {
            if(badge.getTitle().equalsIgnoreCase(title)) {
                return badge;
            }
        }
        return null;
    }
}
