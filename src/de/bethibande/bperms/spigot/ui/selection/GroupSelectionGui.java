package de.bethibande.bperms.spigot.ui.selection;

import de.bethibande.bperms.core.BPerms;
import de.bethibande.bperms.spigot.BPermsSpigot;
import de.bethibande.bperms.struct.PermissionGroup;
import de.bethibande.bperms.utils.TimeUtils;
import de.bethibande.guilib.ui.Gui;
import de.bethibande.guilib.ui.GuiManager;
import de.bethibande.guilib.ui.button.GuiButton;
import de.bethibande.guilib.ui.button.GuiButtonAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GroupSelectionGui {

    private static final HashMap<UUID, PermissionGroup> groupSelection = new HashMap<>();
    // 10 min
    private static final long timeOut = 60*10;

    private long start;
    private boolean interrupt = false;

    private void _interrupt() {
        interrupt = true;
    }

    public PermissionGroup openSelection(Player _player, int _page, GuiButtonAction closeButtonAction) {
        Gui currentGui = GuiManager.getCurrentGui(_player.getUniqueId());
        if(!_player.getOpenInventory().getTitle().equals(currentGui.getTitle())) currentGui = null;
        Gui gui = new Gui(BPerms.INVENTORY_HEADER + " §8> §7Groups §8> §7select", 5, _player);
        gui.create();

        List<PermissionGroup> groups = BPerms.getInstance().getGroupManager().getAll().stream().collect(Collectors.toList());
        Collections.sort(groups, Collections.reverseOrder());

        int startIndex = _page*36;
        int i = startIndex;
        for(PermissionGroup group : groups) {
            if(i > startIndex+36) break;
            if(i >= startIndex) {
                String[] lore = new String[]{""};
                Material mat = Material.valueOf(group.getMaterial().split(":")[0]);
                short subId = Short.parseShort(group.getMaterial().split(":")[1]);
                GuiButton button = new GuiButton("§b§l" + group.getDisplayName(), mat, lore);
                button.setSubId(subId);
                button.setAction(e -> {
                    groupSelection.remove(_player.getUniqueId());
                    groupSelection.put(_player.getUniqueId(), group);
                });
                gui.addButton(button, (i - startIndex) / 9, (i - startIndex) % 9);
            }

            i++;
        }

        String[] closeLore = new String[]{"§7Click to go back to the previous gui"};
        GuiButton close = new GuiButton("Close menu", Material.INK_SACK, closeLore, 'f', 'c');
        close.setSubId((short)1);
        close.setAction(a -> {
           _interrupt();
           if(closeButtonAction != null) closeButtonAction.handle(a);
        });
        gui.addButton(close, 4, 4);

        if(_page > 0) {
            String[] backLore = new String[]{"§7Click to go back to the previous page"};
            GuiButton back = new GuiButton("Back to previous page", Material.ARROW, backLore, 'f', 'c');
            gui.addButton(back, 4, 0);
        }

        if(groups.size() > _page*36+36) {
            String[] nextLore = new String[]{"§7Click to go to the next page"};
            GuiButton next = new GuiButton("Go to next page", Material.ARROW, nextLore, 'f', 'a');
            gui.addButton(next, 4, 8);
        }

        groupSelection.put(_player.getUniqueId(), null);
        Bukkit.getScheduler().scheduleSyncDelayedTask(BPermsSpigot.getPlugin(), () -> GuiManager.openGui(gui));

        try {
            Thread.sleep(200);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        start = TimeUtils.getTimeInSeconds();
        while(!interrupt && TimeUtils.getTimeInSeconds() < start + timeOut && _player.getOpenInventory().getTitle().equals(GuiManager.getCurrentGui(_player.getUniqueId()).getTitle()) && groupSelection.get(_player.getUniqueId()) == null) {
            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(currentGui != null) {
            Gui finalCurrentGui = currentGui;
            Bukkit.getScheduler().scheduleSyncDelayedTask(BPermsSpigot.getPlugin(), () -> GuiManager.openGui(finalCurrentGui));
        }
        return groupSelection.get(_player.getUniqueId());
    }

}
