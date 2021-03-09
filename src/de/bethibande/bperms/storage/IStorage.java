package de.bethibande.bperms.storage;

import java.util.Collection;

public interface IStorage<K, V> {

    void put(K key, V value);

    V get(K key);

    Collection<String> filterBy(String field, Object value);

    void delete(K key);

    void remove(K key);

    void clearCache();

    Collection<String> getAllKeys();

    Collection<K> getAllCachedKeys();

    Collection<V> getAllCachedValues();

    boolean exists(K key);

    void save(K key);

    void saveAll();

    void setSerializer(ISerializer<V> serializer);

    String getId();

}
