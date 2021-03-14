package de.bethibande.bperms.spigot.utils;

import de.bethibande.bperms.core.BPerms;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.HashMap;

public class GuiUtils implements Listener {

    public static enum ChatInputType {
        TEXT, NUMBER;
    }

    public static class ChatInputProvider {
        @Getter
        private final Player player;
        @Getter
        private final String title;
        @Getter
        private final String subtitle;
        @Getter
        private final ChatInputType type;
        @Getter
        private final int maxLength;
        @Getter
        @Setter
        private String result = null;

        public ChatInputProvider(Player _player, String _title, String _subtitle, ChatInputType _type, int _maxLength) {
            player = _player;
            title = _title;
            subtitle = _subtitle;
            type = _type;
            maxLength = _maxLength;
        }

    }

    public static HashMap<Player, ChatInputProvider> chatInputs = new HashMap<>();

    public static String awaitTextInput(Player p, String title, String subtitle, int maxLength) {
        p.closeInventory();
        p.sendMessage(BPerms.CHAT_PREFIX + "§lTo cancel this, enter §bcancel §7or §bexit");

        ChatInputProvider provider = new ChatInputProvider(p, title, subtitle, ChatInputType.TEXT, maxLength);
        chatInputs.put(p, provider);

        while(chatInputs.containsKey(p) && provider.getResult() == null) {
            try {
                p.sendTitle(title, subtitle);
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        chatInputs.remove(p);
        return provider.getResult();
    }

    public static int awaitNumberInput(Player p, String title, String subtitle, int maxLength) {
        p.closeInventory();
        p.sendMessage(BPerms.CHAT_PREFIX + "§lTo cancel this, enter §bcancel §7or §bexit");

        ChatInputProvider provider = new ChatInputProvider(p, title, subtitle, ChatInputType.NUMBER, maxLength);
        chatInputs.put(p, provider);

        while(chatInputs.containsKey(p) && provider.getResult() == null) {
            try {
                p.sendTitle(title, subtitle);
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        chatInputs.remove(p);
        if(provider.getResult() == null) provider.setResult("-1");
        return Integer.parseInt(provider.getResult());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if(chatInputs.containsKey(e.getPlayer())) {
            String text = e.getMessage();
            ChatInputProvider provider = chatInputs.get(e.getPlayer());
            e.setCancelled(true);

            if(text.equalsIgnoreCase("cancel") || text.equalsIgnoreCase("exit")) {
                chatInputs.remove(e.getPlayer());
                return;
            }

            if(text.length() <= provider.getMaxLength()) {
                if(provider.getType() == ChatInputType.TEXT) {
                    provider.setResult(text);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
                }
                if(provider.getType() == ChatInputType.NUMBER) {
                    try {
                        int number = Integer.parseInt(text);
                        if(number >= 0) {
                            provider.setResult(number + "");
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
                        } else e.getPlayer().sendMessage(BPerms.CHAT_PREFIX + "§cYour input has to be greater or equal to 0!");
                    } catch(NumberFormatException ex) {
                        e.getPlayer().sendMessage(BPerms.CHAT_PREFIX + "§cYour input has to be a whole number!");
                    }
                }
            } else e.getPlayer().sendMessage(BPerms.CHAT_PREFIX + "§cYour input was too long, it has to be less or equal to " + provider.getMaxLength() + " characters.");
        }
    }

}
