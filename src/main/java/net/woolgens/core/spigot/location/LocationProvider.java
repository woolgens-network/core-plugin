package net.woolgens.core.spigot.location;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface LocationProvider {

    @Nullable
    Location getLocation(String key);

    void setLocation(String key, Location location);

    void removeLocation(String key);

    boolean existsLocation(String key);
}
