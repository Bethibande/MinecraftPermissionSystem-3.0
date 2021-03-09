package de.bethibande.bperms.storage;

import com.google.gson.Gson;
import de.bethibande.bperms.struct.PermissionCollection;
import de.bethibande.bperms.struct.PermissionGroup;

import java.util.UUID;

public class GroupSerializer implements ISerializer<PermissionGroup> {
    // id, displayName, sortId, permissions, isDefault, prefix, suffix, material

    @Override
    public String[] serialize(PermissionGroup object) {
        String[] data = new String[8];
        data[0] = object.getId().toString();
        data[1] = object.getDisplayName();
        data[2] = object.getSortId() + "";
        data[3] = new Gson().toJson(object.getPermissions());
        data[4] = object.isDefault() ? "1": "0";
        data[5] = object.getPrefix();
        data[6] = object.getSuffix();
        data[7] = object.getMaterial();
        return data;
    }

    @Override
    public PermissionGroup deserialize(String[] s) {
        UUID _id = UUID.fromString(s[0]);
        String _displayName = s[1];
        int _sortId = Integer.parseInt(s[2]);
        PermissionCollection _permissions = new Gson().fromJson(s[3], PermissionCollection.class);
        boolean _isDefault = s[4].equals("1");
        String _prefix = s[5];
        String _suffix = s[6];
        String _material = s[7];
        return new PermissionGroup(_id, _displayName, _sortId, _permissions, _isDefault, _prefix, _suffix, _material);
    }
}
