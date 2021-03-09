package de.bethibande.bperms.spigot.utils;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerUtils {

    public static Method METHOD_UPDATE_COMMANDS;

    static {
        try {
            METHOD_UPDATE_COMMANDS = Player.class.getMethod("updateCommands", null);
        } catch(Exception e) {
            METHOD_UPDATE_COMMANDS = null;
        }
    }

    public static void updateCommand(Player p) {
        if(METHOD_UPDATE_COMMANDS == null) return;
        try {
            METHOD_UPDATE_COMMANDS.invoke(p, null);
        } catch(IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
