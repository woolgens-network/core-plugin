package net.woolgens.core.spigot.location;

import net.woolgens.library.file.gson.GsonFileEntity;
import net.woolgens.library.spigot.location.LocationParser;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FileLocationAdapter extends GsonFileEntity<Map<String, String>> implements LocationProvider {

    private Map<String, Location> locations;

    public FileLocationAdapter(String path) {
        super(path, "locations");
        this.locations = new HashMap<>();
        if(getEntity() == null) {
            setEntity(new HashMap<>());
        } else {
            for(Map.Entry<String, String> entry : getEntity().entrySet()) {
                locations.put(entry.getKey(), LocationParser.stringToLocation(entry.getValue()));
            }
        }

    }

    @Override
    public Location getLocation(String key) {
        return locations.get(key.toLowerCase());
    }

    @Override
    public void setLocation(String key, Location location) {
        String lowered = key.toLowerCase();
        locations.put(lowered, location);
        getEntity().put(lowered, LocationParser.locationToString(location));
        save();
    }

    @Override
    public void removeLocation(String key) {
        String lowered = key.toLowerCase();
        if(getEntity().containsKey(lowered)) {
            locations.remove(lowered);
            getEntity().remove(lowered);
        }
    }

    @Override
    public boolean existsLocation(String key) {
        return locations.containsKey(key.toLowerCase());
    }
}
