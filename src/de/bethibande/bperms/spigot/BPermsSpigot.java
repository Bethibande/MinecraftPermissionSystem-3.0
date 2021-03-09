package de.bethibande.bperms.spigot;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.spigot.commands.BPermsCommand;
import de.bethibande.bperms.spigot.listenerns.JoinQuitListener;
import de.bethibande.bperms.spigot.utils.GuiUtils;
import de.bethibande.guilib.ui.GuiManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class BPermsSpigot extends JavaPlugin {

    @Getter
    private static BPermsSpigot plugin;

    @Override
    public void onEnable() {
        plugin = this;
        new BPerms(getName(), getDescription().getVersion(), getDescription().getDescription(), getDataFolder());
        BPerms.getInstance().setLogger(new SpigotLogger());
        BPerms.getInstance().enable();

        getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
        getServer().getPluginManager().registerEvents(new GuiUtils(), this);
        getCommand("bperms").setExecutor(new BPermsCommand());

        getServer().getPluginManager().registerEvents(new GuiManager(), this);
        GuiManager.startUpdater(this);

    }

    @Override
    public void onDisable() {
        assert BPerms.getInstance() != null: "Could not disable " + getName() + ", the plugin has not been initialized properly";
        BPerms.getInstance().disable();
    }

}
