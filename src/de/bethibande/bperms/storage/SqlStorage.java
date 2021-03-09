package de.bethibande.bperms.storage;

import de.bethibande.bperms.core.BPerms;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SqlStorage<K, V> implements IStorage {

    private final HashMap<K, V> cache = new HashMap<>();

    private ISerializer<V> serializer;

    private String table;
    private SqlField[] fields;

    @Getter
    private final String id;

    public SqlStorage(String _id) {
        id = _id;
    }

    @Override
    public void setSerializer(ISerializer _serializer) {
        serializer = _serializer;
    }

    public void init(String table, Object... fields) {
        if(!(fields[0] instanceof SqlField)) {
            BPerms.getInstance().getLogger().logError("Error while initializing sql storage, no valid fields specified.");
            return;
        }
        this.table = table;
        this.fields = Arrays.stream(fields).map(f -> (SqlField)f).collect(Collectors.toList()).toArray(new SqlField[fields.length]);

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS `" + table + "`(");
        for(SqlField s : this.fields) {
            sb.append("`" + s.getName() + "` " + s.getType() + ", ");
        }
        sb.append("UNIQUE KEY(`" + ((SqlField) fields[0]).getName() + "`));");

        BPerms.getInstance().getMysql().update(sb.toString());
    }

    @Override
    public void put(Object key, Object value) {
        K _key = (K)key;
        cache.remove(_key);
        cache.put(_key, (V)value);
        save(key);
    }

    @Override
    public void delete(Object key) {
        cache.remove(key);
        String deleteCommand = "delete from `" + table + "` where `" + fields[0].getName() + "`='" + key + "'";
        BPerms.getInstance().getMysql().update(deleteCommand);
    }

    @Override
    public V get(Object key) {
        if(cache.containsKey((K)key)) {
            return cache.get((K) key);
        } else {
            try {
                ResultSet rs = BPerms.getInstance().getMysql().query("select * from `" + table + "` where `" + fields[0].getName() + "`='" + key.toString() + "';");
                if (rs.next()) {
                    String[] values = new String[fields.length];
                    for(int i = 0; i < fields.length; i++) {
                        values[i] = rs.getString(fields[i].getName());
                    }
                    V object = serializer.deserialize(values);
                    cache.put((K)key, object);
                    return object;
                } else return null;
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Collection<String> filterBy(String field, Object value) {
        List<String> filtered = new ArrayList<>();
        try {
            ResultSet rs = BPerms.getInstance().getMysql().query("select * from `" + table + "` where `" + field + "`='" + value.toString() + "';");
            while(rs.next()) {
                filtered.add(rs.getString(fields[0].getName()));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return filtered;
    }

    @Override
    public Collection<K> getAllCachedKeys() {
        return cache.keySet();
    }

    @Override
    public Collection<V> getAllCachedValues() {
        return cache.values();
    }

    @Override
    public void remove(Object key) {
        cache.remove((K)key);
    }

    @Override
    public void clearCache() {
        cache.keySet().forEach(this::remove);
    }

    @Override
    public Collection<String> getAllKeys() {
        List<String> keys = new ArrayList<>();
        try {
            ResultSet rs = BPerms.getInstance().getMysql().query("select * from `" + table + "`;");
            while(rs.next()) {
                keys.add(rs.getString(fields[0].getName()));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }

    @Override
    public boolean exists(Object key) {
        return cache.containsKey(key) || keyExistsInSql(key);
    }

    private boolean keyExistsInSql(Object key) {
        try {
            ResultSet rs = BPerms.getInstance().getMysql().query("select * from `" + table + "` where `" + fields[0].getName() + "`='" + key.toString() + "';");
            return rs.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void save(Object key) {
        V object = cache.get((K)key);
        String[] values = serializer.serialize(object);
        if(keyExistsInSql(key)) {
            String updateCommand = "update `" + table + "` set ";
            for(int i = 1; i < values.length; i++) {
                updateCommand = updateCommand + "`" + fields[i].getName() + "`='" + values[i] + "', ";
            }
            updateCommand = updateCommand.substring(0, updateCommand.length()-2);
            updateCommand = updateCommand + " where `" + fields[0].getName() + "`='" + values[0] + "';";
            BPerms.getInstance().getMysql().update(updateCommand);
        } else {
            String insertCommand = "insert into `" + table + "` values (";
            for(int i = 0; i < values.length; i++) {
                insertCommand = insertCommand + "'" + values[i] + "', ";
            }
            insertCommand = insertCommand.substring(0, insertCommand.length()-2);
            insertCommand = insertCommand + ");";
            BPerms.getInstance().getMysql().update(insertCommand);
        }
    }

    @Override
    public void saveAll() {
        cache.keySet().forEach(this::save);
    }
}
