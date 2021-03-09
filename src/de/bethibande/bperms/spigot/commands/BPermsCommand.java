package de.bethibande.bperms.spigot.commands;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.spigot.ui.MenuGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPermsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if(sender instanceof Player) {
            Player p = (Player)sender;
            if(p.hasPermission("bperms.admin")) {
                MenuGui.openMenu(p);
            } else p.sendMessage(BPerms.CHAT_PREFIX + "You are not permitted to do this!");
        } else sender.sendMessage("You have to be a player to use this command!");
        return false;
    }

}
