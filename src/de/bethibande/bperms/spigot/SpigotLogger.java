package de.bethibande.bperms.spigot;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.utils.ILogger;
import org.bukkit.command.ConsoleCommandSender;

public class SpigotLogger implements ILogger {

    private final ConsoleCommandSender logger;

    public SpigotLogger() {
        logger = BPermsSpigot.getPlugin().getServer().getConsoleSender();
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
