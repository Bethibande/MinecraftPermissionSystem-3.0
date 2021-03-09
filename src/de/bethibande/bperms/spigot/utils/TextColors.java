package de.bethibande.bperms.spigot.utils;

import lombok.Getter;

public enum TextColors {

    BLACK("§0"),
    DARK_BLUE("§1"),
    DARK_GREEN("§2"),
    DARK_AQUA("§3"),
    DARK_RED("§4"),
    DARK_PURPLE("§5"),
    GOLD("§6"),
    GRAY("§7"),
    DARK_GRAY("§8"),
    BLUE("§9"),
    GREEN("§a"),
    AQUA("§b"),
    RED("§c"),
    PURPLE("§d"),
    YELLOW("§e"),
    WHITE("§f");

    @Getter
    private final String colorCode;

    TextColors(String _colorCode) {
        colorCode = _colorCode;
    }

}
