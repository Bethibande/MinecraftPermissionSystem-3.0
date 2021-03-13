package de.bethibande.bperms.spigot.ui;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.spigot.BPermsSpigot;
import de.bethibande.bperms.spigot.ui.selection.GroupSelectionGui;
import de.bethibande.bperms.spigot.ui.settings.MysqlGui;
import de.bethibande.bperms.spigot.utils.GuiUtils;
import de.bethibande.bperms.struct.PermissionGroup;
import de.bethibande.guilib.ui.Gui;
import de.bethibande.guilib.ui.GuiManager;
import de.bethibande.guilib.ui.button.GuiButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SettingsGui {

    public static void openSettings(Player p) {
        Gui gui = new Gui(BPerms.INVENTORY_HEADER + " §8> §7Settings", 5, p);
        gui.create();

        String[] backLore = new String[]{"§7Go back the the main menu"};
        GuiButton back = new GuiButton("Back to menu", Material.INK_SACK, backLore, 'f', 'c');
        back.setSubId((short)1);
        back.setAction(e -> MenuGui.openMenu(p));
        gui.addButton(back, 3, 4);

        String[] sqlLore = new String[]{"§7Edit the mysql settings"};
        GuiButton sql = new GuiButton("Mysql settings", Material.ENDER_CHEST, sqlLore, 'f', 'b');
        sql.setAction(e -> MysqlGui.openMysqlSettings(p));
        gui.addButton(sql, 1, 7);

        String[] chatLore = new String[]{"§7Change the default chat format", "§7Current format§8: §b" + BPerms.getInstance().getConfig().chat_format};
        GuiButton chatFormat = new GuiButton("Change default chat format", Material.BOOK, chatLore, 'f', 'b');
        chatFormat.setAction(e -> {
            String newFormat = GuiUtils.awaitTextInput(p, "§b§lChange chat format", "§7Placeholders, %prefix%, %player%, %suffix%, %message%", 256);
            if(newFormat != null) {
                BPerms.getInstance().getConfig().chat_format = newFormat;
                BPerms.getInstance().saveConfigToMysql();
            }
            chatFormat.setLore(new String[]{"§7Change the default chat format", "§7Current format§8: §b" + BPerms.getInstance().getConfig().chat_format});
            GuiManager.reopenGui(p.getUniqueId());
        });
        gui.addButton(chatFormat, 1, 4);

        String[] groupLore = new String[]{"§7Select the default parent group", "§7Current default parent group§8: §b" + BPerms.getInstance().getGroupManager().getGroup(BPerms.getInstance().getConfig().default_parent_group).getDisplayName()};
        GuiButton group = new GuiButton("Change default parent group", Material.IRON_CHESTPLATE, groupLore, 'f', 'b');
        group.setAction(e -> {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(BPermsSpigot.getPlugin(), () -> {
                PermissionGroup parent = new GroupSelectionGui().openSelection(p, 0, a -> openSettings(p));
                if(parent != null) {
                    BPerms.getInstance().getConfig().default_parent_group = parent.getId();
                    BPerms.getInstance().saveConfigToMysql();
                    group.setLore(new String[]{"§7Select the default parent group", "§7Current default parent group§8: §b" + BPerms.getInstance().getGroupManager().getGroup(BPerms.getInstance().getConfig().default_parent_group).getDisplayName()});
                }
            });
        });
        gui.addButton(group, 1, 1);

        GuiManager.openGui(gui);
    }

}
