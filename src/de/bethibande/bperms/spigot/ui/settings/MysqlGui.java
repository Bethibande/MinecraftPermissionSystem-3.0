package de.bethibande.bperms.spigot.ui.settings;

import de.bethibande.bperms.configs.MysqlConfig;
import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.spigot.SpigotMain;
import de.bethibande.bperms.spigot.ui.SettingsGui;
import de.bethibande.bperms.spigot.utils.GuiUtils;
import de.bethibande.guilib.ui.Gui;
import de.bethibande.guilib.ui.GuiManager;
import de.bethibande.guilib.ui.button.GuiButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MysqlGui {

    public static void openMysqlSettings(Player p) {
        Gui gui = new Gui(BPerms.INVENTORY_HEADER + " §8> §7Settings §8> §7Mysql", 5, p);
        gui.create();

        String[] backLore = new String[]{"§7Go back the the settings menu"};
        GuiButton back = new GuiButton("Back to settings", Material.INK_SACK, backLore, 'f', 'c');
        back.setSubId((short)1);
        back.setAction(e -> SettingsGui.openSettings(p));
        gui.addButton(back, 3, 4);

        MysqlConfig mysqlConfig = BPerms.getInstance().getMysqlConfig();

        String[] hostLore = new String[]{"§7Click to change the host ip address", "§7Current host ip§8: §b" + mysqlConfig.host};
        GuiButton host = new GuiButton("Mysql host address", Material.ENDER_CHEST, hostLore, 'f', 'b');
        host.setAction(e -> {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(SpigotMain.getPlugin(), () -> {
                String ip = GuiUtils.awaitTextInput(p, "§b§lChange mysql host", "§7Please enter a new address (in chat, ipv4)", 128);
                if(ip != null) BPerms.getInstance().getMysqlConfig().host = ip;
               host.setLore(new String[]{"§7Click to change the host ip address", "§7Current host ip§8: §b" + mysqlConfig.host});
               GuiManager.reopenGui(p.getUniqueId());
            });
        });
        gui.addButton(host, 1, 1);

        String[] portLore = new String[]{"§7Click to change the host port", "§7Current host port§8: §b" + mysqlConfig.port};
        GuiButton port = new GuiButton("Mysql host port", Material.EYE_OF_ENDER, portLore, 'f', 'b');
        port.setAction(e -> {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(SpigotMain.getPlugin(), () -> {
                int _port = GuiUtils.awaitNumberInput(p, "§b§lChange mysql port", "§7Please enter a new port (in chat, number)", 8);
                if(_port != 0) BPerms.getInstance().getMysqlConfig().port = _port + "";
                port.setLore(new String[]{"§7Click to change the host port", "§7Current host port§8: §b" + mysqlConfig.port});
                GuiManager.reopenGui(p.getUniqueId());
            });
        });
        gui.addButton(port, 1, 2);

        String[] databaseLore = new String[]{"§7Click to change the mysql database", "§7Current database§8: §b" + mysqlConfig.database};
        GuiButton database = new GuiButton("Mysql database", Material.CHEST, databaseLore, 'f', 'b');
        database.setAction(e -> {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(SpigotMain.getPlugin(), () -> {
                String db = GuiUtils.awaitTextInput(p, "§b§lChange mysql database", "§7Please enter a new database name (in chat, text)", 128);
                if (db != null) BPerms.getInstance().getMysqlConfig().database = db;
                database.setLore(new String[]{"§7Click to change the mysql database", "§7Current database§8: §b" + mysqlConfig.database});
                GuiManager.reopenGui(p.getUniqueId());
            });
        });
        gui.addButton(database, 1, 4);

        String[] userLore = new String[]{"§7Click to change the mysql username", "§7Current username§8: §b" + mysqlConfig.username};
        GuiButton user = new GuiButton("Mysql username", Material.LEATHER_CHESTPLATE, userLore, 'f', 'b');
        user.setAction(e -> {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(SpigotMain.getPlugin(), () -> {
                String username = GuiUtils.awaitTextInput(p, "§b§lChange mysql username", "§7Please enter a new username (in chat, text)", 128);
                if (username != null) BPerms.getInstance().getMysqlConfig().username = username;
                user.setLore(new String[]{"§7Click to change the mysql username", "§7Current username§8: §b" + mysqlConfig.username});
                GuiManager.reopenGui(p.getUniqueId());
            });
        });
        gui.addButton(user, 1, 6);

        String[] passwordLore = new String[]{"§7Click to change the mysql password", "§7Current password§8: §b" + mysqlConfig.password.replaceAll("[a-z0-9A-Z]", "*")};
        GuiButton password = new GuiButton("Mysql password", Material.NAME_TAG, passwordLore, 'f', 'b');
        password.setAction(e -> {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(SpigotMain.getPlugin(), () -> {
                String pw = GuiUtils.awaitTextInput(p, "§b§lChange mysql password", "§7Please enter a new password (in chat, text)", 128);
                if (pw != null) BPerms.getInstance().getMysqlConfig().password = pw;
                password.setLore(new String[]{"§7Click to change the mysql password", "§7Current password§8: §b" + mysqlConfig.password.replaceAll("[a-z0-9A-Z]", "*")});
                GuiManager.reopenGui(p.getUniqueId());
            });
        });
        gui.addButton(password, 1, 7);

        GuiManager.openGui(gui);
    }

}
