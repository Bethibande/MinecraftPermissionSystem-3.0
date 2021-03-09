package de.bethibande.bperms.storage;

import de.bethibande.bperms.utils.TimeUtils;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StorageController<k, v> {

    private final IStorage<k, v> storage;

    private final HashMap<k, Long> lastAccess = new HashMap<>();
    @Getter
    // in seconds
    private final long maxItemLifetime;

    public StorageController(IStorage<k, v> _storage, long _maxItemLifetime) {
        storage = _storage;
        maxItemLifetime = _maxItemLifetime;
    }

    public v get(k key) {
        lastAccess.remove(key);
        lastAccess.put(key, TimeUtils.getTimeInSeconds());
        return storage.get(key);
    }

    // returns all keys which match the filter
    // for example all UUID's of all players
    public Collection<String> filterBy(String field, Object value) {
        return storage.filterBy(field, value);
    }

    public void put(k key, v value) {
        lastAccess.put(key, TimeUtils.getTimeInSeconds());
        storage.put(key, value);
    }

    public boolean entryExists(k key) {
        lastAccess.remove(key);
        lastAccess.put(key, TimeUtils.getTimeInSeconds());
        return storage.exists(key);
    }

    public void remove(k key) {
        lastAccess.remove(key);
        storage.remove(key);
    }

    public void clearCache() {
        lastAccess.clear();
        storage.clearCache();
    }

    public void delete(k key) {
        lastAccess.remove(key);
        storage.delete(key);
    }

    public void saveItem(k key) {
        lastAccess.remove(key);
        lastAccess.put(key, TimeUtils.getTimeInSeconds());
        storage.save(key);
    }

    public void saveAll() {
        storage.saveAll();
    }

    public void setSerializer(ISerializer<v> serializer) {
        storage.setSerializer(serializer);
    }

    public String getStorageId() {
        return storage.getId();
    }

    public void update() {
        long timeNow = TimeUtils.getTimeInSeconds();
        Collection<k> lifetimeExceeded = lastAccess.keySet().stream().filter(key -> lastAccess.get(key) + maxItemLifetime < timeNow).collect(Collectors.toList());
        lifetimeExceeded.forEach(this::remove);
    }

}
