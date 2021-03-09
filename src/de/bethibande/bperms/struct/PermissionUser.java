package de.bethibande.bperms.struct;

import de.bethibande.bperms.utils.TimeUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class PermissionUser {

    @Getter
    private final UUID uuid;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private long lastJoined;
    @Getter
    private final HashMap<UUID, Long> groups;
    @Getter
    private final PermissionCollection permissions;
    @Getter
    @Setter
    private String prefix;
    @Getter
    @Setter
    private String suffix;

    @Getter
    @Setter
    private UUID parentGroup;

    public PermissionUser(UUID _uuid, String _username, long _lastJoined, UUID _parentGroup, HashMap<UUID, Long> _groups, @NonNull PermissionCollection _permissions, String _prefix, String _suffix) {
        uuid = _uuid;
        username = _username;
        lastJoined = _lastJoined;
        parentGroup = _parentGroup;
        groups = _groups;
        permissions = _permissions;
        prefix = _prefix;
        suffix = _suffix;
    }

    public UUID[] getGroupsAsArray() {
        return groups.keySet().toArray(new UUID[0]);
    }

    public void update() {
        long timeNow = TimeUtils.getTimeInSeconds();
        Collection<UUID> expiredGroups = groups.keySet().stream().filter(group -> groups.get(group) > 0 && groups.get(group) < timeNow).collect(Collectors.toList());
        expiredGroups.forEach(groups::remove);
    }

}
