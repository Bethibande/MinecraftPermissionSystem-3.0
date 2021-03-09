package de.bethibande.bperms.struct;

import java.util.Collection;
import java.util.stream.Collectors;

public class PermissionCollection {

    private Collection<BPermission> permissions;

    public PermissionCollection(Collection<BPermission> _permissions) {
        permissions = _permissions;
    }

    public PermissionCollection filterServer(String server) {
        return new PermissionCollection(permissions.stream().filter(perm -> perm.getServer() != null && perm.getServer().equals(server)).collect(Collectors.toList()));
    }

    public PermissionCollection filterWorld(String world) {
        return new PermissionCollection(permissions.stream().filter(perm -> perm.getWorld() != null && perm.getWorld().equals(world)).collect(Collectors.toList()));
    }

    public PermissionCollection bungee() {
        return new PermissionCollection(permissions.stream().filter(BPermission::isBungee).collect(Collectors.toList()));
    }

    public boolean containsPermission(BPermission permission) {
        return permissions.contains(permission);
    }

    public boolean containsPermission(String permission) {
        return permissions.stream().anyMatch(perm -> perm.getPermission() != null && perm.getPermission().equalsIgnoreCase(permission));
    }

    public void addPermission(BPermission permission) {
        permissions.add(permission);
    }

    public void removePermission(BPermission permission) {
        permissions.remove(permission);
    }

    public void removePermission(String permission) {
        permissions = permissions.stream().filter(perm -> perm.getPermission() != null && !perm.getPermission().equalsIgnoreCase(permission)).collect(Collectors.toList());
    }

    public Collection<BPermission> asCollection() {
        return permissions;
    }

}
