package de.bethibande.bperms.struct;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class PermissionGroup implements Comparable<PermissionGroup> {

    @Getter
    private final UUID id;
    @Getter
    @Setter
    private String displayName;
    @Getter
    @Setter
    private Integer sortId;
    @Getter
    private final PermissionCollection permissions;
    @Getter
    @Setter
    private boolean isDefault;
    @Getter
    @Setter
    private String prefix;
    @Getter
    @Setter
    private String suffix;
    @Getter
    @Setter
    private String material;

    public PermissionGroup(UUID _id, String _displayName, int _sortId, PermissionCollection _permissions, boolean _isDefault, String _prefix, String _suffix, String _material) {
        id = _id;
        displayName = _displayName;
        sortId = _sortId;
        permissions = _permissions;
        isDefault = _isDefault;
        prefix = _prefix;
        suffix = _suffix;
        material = _material;
    }

    public void addPermission(BPermission permission) {
        permissions.addPermission(permission);
    }

    public void removePermission(BPermission permission) {
        permissions.removePermission(permission);
    }

    public void removePermission(String permission) {
        permissions.removePermission(permission);
    }

    @Override
    public int compareTo(PermissionGroup group) {
        return getSortId().compareTo(group.getSortId());
    }

}
