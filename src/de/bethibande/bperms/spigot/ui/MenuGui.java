package de.bethibande.bperms.spigot.ui;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.guilib.ui.Gui;
import de.bethibande.guilib.ui.GuiManager;
import de.bethibande.guilib.ui.button.GuiButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MenuGui {

    public static void openMenu(Player p) {
        Gui gui = new Gui(BPerms.INVENTORY_HEADER + " §8> §7Menu", 5, p);
        gui.create();

        String[] closeLore = new String[]{"§7Click to close this menu or press 'esc'"};
        GuiButton close = new GuiButton("Close menu", Material.INK_SACK, closeLore, 'f', 'c');
        close.setSubId((short)1);
        close.setAction(e -> p.closeInventory());
        gui.addButton(close, 3, 4);

        String[] settingsLore = new String[]{"§7Click to open the settings", " §7- Default chat format", " §7- Mysql settings", " §7- default parent group"};
        GuiButton settings = new GuiButton("Settings", Material.ANVIL, settingsLore, 'f', 'b');
        settings.setAction(e -> SettingsGui.openSettings(p));
        gui.addButton(settings, 1, 7);


        GuiManager.openGui(gui);
    }

}
