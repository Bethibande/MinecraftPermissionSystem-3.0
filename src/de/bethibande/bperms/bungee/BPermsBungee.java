package de.bethibande.bperms.bungee;

import de.bethibande.bperms.core.BPerms;
import net.md_5.bungee.api.plugin.Plugin;

public class BPermsBungee extends Plugin {

    @Override
    public void onEnable() {
        new BPerms(getDescription().getName(), getDescription().getVersion(), getDescription().getDescription(), getDataFolder()).enable();
    }

    @Override
    public void onDisable() {
        assert BPerms.getInstance() != null: "Could not disable " + getDescription().getName() + ", the plugin has not been initialized properly";
        BPerms.getInstance().disable();
    }

}
