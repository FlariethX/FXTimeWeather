package ua.fxtimeweather.manager;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import ua.fxtimeweather.util.ColorUtil;

public class ActionBarManager {

    public void send(Player player, String text) {
        Component component = ColorUtil.parse(text);
        player.sendActionBar(component);
    }
}