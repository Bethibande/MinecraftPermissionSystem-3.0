package de.bethibande.bperms.management;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.storage.*;
import de.bethibande.bperms.struct.PermissionCollection;
import de.bethibande.bperms.struct.PermissionUser;
import de.bethibande.bperms.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserManager {

    private StorageController<UUID, PermissionUser> userStorage;

    public UserManager(StorageController<UUID, PermissionUser> _userStorage) {
        userStorage = _userStorage;
    }

    public boolean userExists(UUID uuid) {
        return userStorage.entryExists(uuid);
    }

    public Collection<UUID> getUsersByName(String username) {
        return userStorage.filterBy("username", username).stream().map(UUID::fromString).collect(Collectors.toList());
    }

    public PermissionUser getUser(UUID uuid) {
        return userStorage.get(uuid);
    }

    public void createUser(UUID _uuid, String _username) {
        HashMap<UUID, Long> defaultGroups = new HashMap<>();
        BPerms.getInstance().getGroupManager().filterDefault().forEach(group -> defaultGroups.put(group.getId(), -1L));
        PermissionUser user = new PermissionUser(_uuid, _username, TimeUtils.getTimeInSeconds(), BPerms.getInstance().getConfig().default_parent_group, defaultGroups, new PermissionCollection(new ArrayList<>()), null, null);
        userStorage.put(_uuid, user);
    }

    public static StorageController<UUID, PermissionUser> initSqlUserStorage(String _id, String _tableName, long _maxCacheItemLifetime) {
        SqlStorage<UUID, PermissionUser> storage = new SqlStorage<>(_id);
        // sql fields = UUID, username, lastConnected, groups, permissions, prefix, suffix, parent
        storage.init(_tableName, new SqlField("UUID", "VARCHAR(128)"), new SqlField("username", "VARCHAR(128)"), new SqlField("lastConnected", "VARCHAR(128)"), new SqlField("groups", "TEXT(65000)"), new SqlField("permissions", "TEXT(65000)"), new SqlField("prefix", "VARCHAR(8)"), new SqlField("suffix", "VARCHAR(8)"), new SqlField("parent", "VARCHAR(128)"));
        storage.setSerializer(new UserSerializer());
        return new StorageController<UUID, PermissionUser>(storage, _maxCacheItemLifetime);
    }

}
