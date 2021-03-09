package de.bethibande.bperms.storage;

import lombok.Getter;

public class SqlField {

    @Getter
    private final String name;
    @Getter
    private final String type;

    public SqlField(String _name, String _type) {
        name = _name;
        type = _type;
    }

}
