package de.bethibande.bperms.spigot.ui;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.spigot.BPermsSpigot;
import de.bethibande.bperms.spigot.ui.selection.GroupSelectionGui;
import de.bethibande.bperms.struct.PermissionGroup;
import de.bethibande.guilib.ui.Gui;
import de.bethibande.guilib.ui.GuiManager;
import de.bethibande.guilib.ui.button.GuiButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GroupMenuGui {

    public static void openGroupMenu(Player _player) {
        Gui gui = new Gui(BPerms.INVENTORY_HEADER + " §8> §7Groups §8> §7actions", 5, _player);
        gui.create();

        String[] createLore = new String[]{"§7Create a new permission group"};
        GuiButton create = new GuiButton("Create group", Material.EMERALD_BLOCK, createLore, 'f', 'a');
        gui.addButton(create, 1, 1);

        String[] editLore = new String[]{"§7Edit an existing permission group"};
        GuiButton edit = new GuiButton("Edit group", Material.BOOK_AND_QUILL, editLore, 'f', 'e');
        edit.setAction(a -> {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(BPermsSpigot.getPlugin(), () -> {
                PermissionGroup group = new GroupSelectionGui().openSelection(_player, 0, a2 -> openGroupMenu(_player));
                if(group != null) {
                    _player.sendMessage("edit group: " + group.getDisplayName());
                }
            });
        });
        gui.addButton(edit, 1, 4);

        String[] deleteLore = new String[]{"§7Delete an existing permission group"};
        GuiButton delete = new GuiButton("Delete group", Material.REDSTONE_BLOCK, deleteLore, 'f', 'c');
        delete.setAction(a -> {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(BPermsSpigot.getPlugin(), () -> {
                PermissionGroup group = new GroupSelectionGui().openSelection(_player, 0, a2 -> openGroupMenu(_player));
                if(group != null) {
                    _player.sendMessage("delete group: " + group.getDisplayName());
                }
            });
        });
        gui.addButton(delete, 1, 7);

        String[] backLore = new String[]{"§7Click to go back to the main menu"};
        GuiButton back = new GuiButton("Back to menu", Material.INK_SACK, backLore, 'f', 'c');
        back.setSubId((short)1);
        back.setAction(a -> MenuGui.openMenu(_player));
        gui.addButton(back, 3, 4);

        GuiManager.openGui(gui);
    }

}
