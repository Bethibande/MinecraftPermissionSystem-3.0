package de.bethibande.bperms.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.bethibande.bperms.struct.PermissionCollection;
import de.bethibande.bperms.struct.PermissionUser;

import java.util.HashMap;
import java.util.UUID;

public class UserSerializer implements ISerializer<PermissionUser> {
    // UUID, username, lastConnected, groups, permissions, prefix, suffix, parent

    @Override
    public String[] serialize(PermissionUser object) {
        String[] data = new String[8];
        data[0] = object.getUuid().toString();
        data[1] = object.getUsername();
        data[2] = object.getLastJoined() + "";
        data[3] = new Gson().toJson(object.getGroups());
        data[4] = new Gson().toJson(object.getPermissions());
        data[5] = object.getPrefix();
        data[6] = object.getSuffix();
        data[7] = object.getParentGroup().toString();
        return data;
    }

    @Override
    public PermissionUser deserialize(String[] s) {
        UUID uuid = UUID.fromString(s[0]);
        String username = s[1];
        long lastJoined = Long.parseLong(s[2]);
        HashMap<UUID, Long> groups = new Gson().fromJson(s[3], new TypeToken<HashMap<UUID, Long>>() {}.getType());
        PermissionCollection permissions = new Gson().fromJson(s[4], PermissionCollection.class);
        String prefix = s[5];
        String suffix = s[6];
        UUID parent = UUID.fromString(s[7]);

        return new PermissionUser(uuid, username, lastJoined, parent, groups, permissions, prefix, suffix);
    }

}
