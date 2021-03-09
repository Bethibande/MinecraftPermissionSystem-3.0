package de.bethibande.bperms.management;

import de.bethibande.bperms.storage.GroupSerializer;
import de.bethibande.bperms.storage.SqlField;
import de.bethibande.bperms.storage.SqlStorage;
import de.bethibande.bperms.struct.PermissionCollection;
import de.bethibande.bperms.struct.PermissionGroup;
import lombok.NonNull;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class GroupManager {

    private SqlStorage<UUID, PermissionGroup> groupStorage;

    public PermissionGroup createGroup(UUID _id, String _displayName) {
        PermissionGroup group = new PermissionGroup(_id, _displayName, 0, new PermissionCollection(new ArrayList<>()), false, null, null, Material.WOOL + ":0");
        groupStorage.put(group.getId(), group);
        return group;
    }

    public PermissionGroup createGroup(String _displayName) {
        PermissionGroup group = new PermissionGroup(generateUnusedGroupId(), _displayName, 0, new PermissionCollection(new ArrayList<>()), false, null, null, Material.WOOL + ":0");
        groupStorage.put(group.getId(), group);
        return group;
    }

    public PermissionGroup getGroup(UUID id) {
        return groupStorage.get(id);
    }

    public void deleteGroup(UUID id) {
        groupStorage.delete(id);
    }

    private UUID generateUnusedGroupId() {
        UUID uuid = UUID.randomUUID();
        if(groupStorage.get(uuid) == null) return uuid;
        return generateUnusedGroupId();
    }

    public Collection<PermissionGroup> filterDefault() {
        return groupStorage.getAllCachedValues().stream().filter(PermissionGroup::isDefault).collect(Collectors.toList());
    }

    public @NonNull Collection<PermissionGroup> filterByName(String _displayName) {
        return groupStorage.getAllCachedValues().stream().filter(group -> group.getDisplayName() != null && group.getDisplayName().equalsIgnoreCase(_displayName)).collect(Collectors.toList());
    }

    public void loadAllGroupsFromMysql() {
        Collection<UUID> keys = groupStorage.getAllKeys().stream().map(key -> UUID.fromString(key)).collect(Collectors.toList());
        keys.forEach(groupStorage::get);
    }

    public Collection<PermissionGroup> getAll() {
        return groupStorage.getAllCachedValues();
    }

    public boolean groupNameExists(String _displayName) {
        return !filterByName(_displayName).isEmpty();
    }

    public boolean groupExists(UUID _group) {
        return getGroup(_group) != null;
    }

    public void saveChanges(UUID _group) {
        groupStorage.save(_group);
    }

    public void reloadFromSql(UUID _group) {
        groupStorage.remove(_group);
        groupStorage.get(_group);
    }

    public void initGroupStorage(String _id, String _tableName) {
        SqlStorage<UUID, PermissionGroup> storage = new SqlStorage<>(_id);
        // id, displayName, sortId, permissions, isDefault, prefix, suffix, material
        storage.init(_tableName, new SqlField("id", "VARCHAR(128)"), new SqlField("displayName", "VARCHAR(128)"), new SqlField("sortId", "INT(255)"), new SqlField("permissions", "TEXT(65000)"), new SqlField("isDefault", "CHAR"), new SqlField("prefix", "VARCHAR(16)"), new SqlField("suffix", "VARCHAR(16)"), new SqlField("material", "VARCHAR(128)"));
        storage.setSerializer(new GroupSerializer());
        groupStorage = storage;
    }

}
