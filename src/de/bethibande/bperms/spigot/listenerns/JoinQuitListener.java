package de.bethibande.bperms.spigot.listenerns;

import de.bethibande.bperms.spigot.PermissibleInjector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerLoginEvent e) {
        PermissibleInjector.inject(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        PermissibleInjector.uninject(e.getPlayer());
    }

}
