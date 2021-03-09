package de.bethibande.bperms.spigot;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.utils.ILogger;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.command.ColouredConsoleSender;

public class SpigotLogger implements ILogger {

    private final ConsoleCommandSender logger;

    public SpigotLogger() {
        logger = SpigotMain.getPlugin().getServer().getConsoleSender();
    }

    @Override
    public void log(String message) {
        logger.sendMessage("§r" + BPerms.CONSOLE_PREFIX + message);
    }

    @Override
    public void logError(String message) {
        logger.sendMessage("§r" + BPerms.CONSOLE_PREFIX + "§cError §7» §c" + message);
    }
}
