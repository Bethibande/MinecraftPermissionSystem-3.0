package de.bethibande.bperms.struct;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class GroupCollection {

    private Collection<PermissionGroup> groups;

    public GroupCollection(Collection<PermissionGroup> _groups) {
        groups = _groups;
    }

    public GroupCollection filterName(String name) {
        return new GroupCollection(groups.stream().filter(group -> group.getDisplayName() != null && group.getDisplayName().equalsIgnoreCase(name)).collect(Collectors.toList()));
    }

    public GroupCollection filterUUID(UUID id) {
        return new GroupCollection(groups.stream().filter(group -> group.getId() != null && group.getId().equals(id)).collect(Collectors.toList()));
    }

    public GroupCollection defaults() {
        return new GroupCollection(groups.stream().filter(PermissionGroup::isDefault).collect(Collectors.toList()));
    }

    public Collection<PermissionGroup> asCollection() {
        return groups;
    }

}
