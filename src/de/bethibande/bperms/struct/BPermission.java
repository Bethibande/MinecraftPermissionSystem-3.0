package de.bethibande.bperms.struct;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class BPermission {

    @Getter
    private final String permission;
    @Getter
    @Setter
    private String world = null;
    @Getter
    @Setter
    private String server = null;
    @Getter
    @Setter
    private boolean bungee = false;

    public BPermission(String _permission, String _world, String _server) {
        permission = _permission;
        world = _world;
        server = _server;
    }

    public BPermission(String _permission, boolean _bungee) {
        permission = _permission;
        bungee = _bungee;
    }

}
