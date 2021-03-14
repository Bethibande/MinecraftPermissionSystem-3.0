package de.bethibande.bperms.spigot.ui;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.management.GroupManager;
import de.bethibande.bperms.spigot.utils.GuiUtils;
import de.bethibande.bperms.struct.PermissionGroup;
import de.bethibande.guilib.ui.Gui;
import de.bethibande.guilib.ui.GuiManager;
import de.bethibande.guilib.ui.button.GuiButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;

public class GroupEditMenu {

    public static void openGroupEditMenu(Player _player, PermissionGroup group) {
        Gui gui = new Gui(BPerms.INVENTORY_HEADER + " §8> §7Groups §8> §7" + group.getDisplayName(), 5, _player);
        gui.create();

        Material mat = Material.valueOf(group.getMaterial().split(":")[0]);
        short subId = Short.parseShort(group.getMaterial().split(":")[1]);

        String[] backLore = new String[]{"§7Click to go back to the group menu"};
        GuiButton back = new GuiButton("Go back", Material.INK_SACK, backLore, 'f', 'c');
        back.setSubId((short)1);
        back.setAction(a -> GroupMenuGui.openGroupMenu(_player));
        gui.addButton(back, 3, 7);

        String[] nameLore = new String[]{"§7Click to change the name of this group", "§7Current name§8: §b" + group.getDisplayName()};
        GuiButton name = new GuiButton("Group name", Material.NAME_TAG, nameLore, 'f', 'b');
        name.setAction(a -> {
            String newName = GuiUtils.awaitTextInput(_player, "§b§lChange group name", "§7Please enter a new name", 128);
            if(newName != null) {
                GroupManager manager = BPerms.getInstance().getGroupManager();
                if(!manager.groupNameExists(newName)) {
                    group.setDisplayName(newName);
                    manager.saveChanges(group.getId());
                } else _player.sendMessage(BPerms.CHAT_PREFIX + "There already is a group with this name");
            }
            //name.setLore(new String[]{"§7Click to change the name of this group", "§7Current name§8: §b" + group.getDisplayName()});
            //GuiManager.reopenGui(_player.getUniqueId());
            GroupEditMenu.openGroupEditMenu(_player, group);
        });
        gui.addButton(name, 1, 1);

        String[] defaultLore = new String[]{"§7Click to change whether the group is a default group or not", "§7Currently a default group§8: §b" + (group.isDefault() ? "yes": "no")};
        GuiButton defaultB = new GuiButton("Default group", Material.SLIME_BALL, defaultLore, 'f', 'b');
        defaultB.setAction(a -> {
            group.setDefault(!group.isDefault());
            BPerms.getInstance().getGroupManager().saveChanges(group.getId());
            defaultB.setLore(new String[]{"§7Click to change whether the group is a default group or not", "§7Currently a default group§8: §b" + (group.isDefault() ? "yes": "no")});
        });
        gui.addButton(defaultB, 1, 3);

        String[] sortIdLore = new String[]{"§7Click to change sort id, used for player list and gui", "§7Current sort id§8: §b" + group.getSortId()};
        GuiButton sortId = new GuiButton("Group sort id", Material.HOPPER, sortIdLore, 'f', 'b');
        sortId.setAction(a -> {
           int id =  GuiUtils.awaitNumberInput(_player, "§b§lChange group sort id", "§7Please enter a number greater or equal to 0", 128);
           if(id != -1) {
               group.setSortId(id);
               BPerms.getInstance().getGroupManager().saveChanges(group.getId());
               sortId.setLore(new String[]{"§7Click to change sort id, used for player list and gui", "§7Current sort id§8: §b" + group.getSortId()});
           }
           GuiManager.reopenGui(_player.getUniqueId());
        });
        gui.addButton(sortId, 1, 5);

        String[] materialLore = new String[]{"§7Drag item here to change group icon", "§7current icon§8: §b" + group.getMaterial()};
        GuiButton material = new GuiButton("Group icon", mat, materialLore, 'f', 'b');
        material.setSubId(subId);
        material.setAction(a -> {
           if(a.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
               material.setMaterial(a.getExtra().getType());
               material.setSubId(a.getExtra().getDurability());
               group.setMaterial(a.getExtra().getType() + ":" + a.getExtra().getDurability());
               BPerms.getInstance().getGroupManager().saveChanges(group.getId());
           }
        });
        gui.addButton(material, 1, 7);

        String[] prefixLore = new String[]{"§7Click to change prefix", "§7r-click to remove prefix", "§7Current prefix§8: §b" + ((group.getPrefix() == null) ? "none": group.getPrefix())};
        GuiButton prefix = new GuiButton("Group prefix", Material.BOOK, prefixLore, 'f', 'b');
        prefix.setAction(a -> {
           if(a.getAction() == InventoryAction.PICKUP_HALF) {
               group.setPrefix(null);
               BPerms.getInstance().getGroupManager().saveChanges(group.getId());
               prefix.setLore(new String[]{"§7Click to change prefix", "§7r-click to remove prefix", "§7Current prefix§8: §b" + ((group.getPrefix() == null) ? "none": group.getPrefix())});
           } else {
               String newPrefix = GuiUtils.awaitTextInput(_player, "§b§lChange prefix", "§7Please enter a new prefix (use &a, &1... color codes)", 16);
               if(newPrefix != null) {
                   group.setPrefix(newPrefix.replaceAll("&", "§"));
                   BPerms.getInstance().getGroupManager().saveChanges(group.getId());
                   prefix.setLore(new String[]{"§7Click to change prefix", "§7r-click to remove prefix", "§7Current prefix§8: §b" + ((group.getPrefix() == null) ? "none": group.getPrefix())});
               }
               GuiManager.reopenGui(_player.getUniqueId());
           }
        });
        gui.addButton(prefix, 3, 1);

        String[] permissionsLore = new String[]{"§7Click to edit permissions"};
        GuiButton permissions = new GuiButton("Group permissions", Material.CHEST, permissionsLore, 'f', 'b');
        gui.addButton(permissions, 3, 3);

        String[] suffixLore = new String[]{"§7Click to change suffix", "§7r-click to remove suffix", "§7Current suffix§8: §b" + ((group.getSuffix() == null) ? "none": group.getSuffix())};
        GuiButton suffix = new GuiButton("Group suffix", Material.BOOK, suffixLore, 'f', 'b');
        suffix.setAction(a -> {
            if(a.getAction() == InventoryAction.PICKUP_HALF) {
                group.setSuffix(null);
                BPerms.getInstance().getGroupManager().saveChanges(group.getId());
                suffix.setLore(new String[]{"§7Click to change suffix", "§7r-click to remove suffix", "§7Current suffix§8: §b" + ((group.getSuffix() == null) ? "none": group.getSuffix())});
            } else {
                String newSuffix = GuiUtils.awaitTextInput(_player, "§b§lChange suffix", "§7Please enter a new suffix (use &a, &1... color codes)", 16);
                if(newSuffix != null) {
                    group.setSuffix(newSuffix.replaceAll("&", "§"));
                    BPerms.getInstance().getGroupManager().saveChanges(group.getId());
                    suffix.setLore(new String[]{"§7Click to change suffix", "§7r-click to remove suffix", "§7Current suffix§8: §b" + ((group.getSuffix() == null) ? "none": group.getSuffix())});
                }
                GuiManager.reopenGui(_player.getUniqueId());
            }
        });
        gui.addButton(suffix, 3, 5);

        GuiManager.openGui(gui);
    }

}
