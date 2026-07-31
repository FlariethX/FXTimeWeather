package ua.fxtimeweather.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ua.fxtimeweather.FXTimeWeather;

public class PlayerConnectionListener implements Listener {

    private static final long JOIN_DISPLAY_DELAY_TICKS = 10L;

    private final FXTimeWeather plugin;

    public PlayerConnectionListener(FXTimeWeather plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPlayerDataManager().ensureEntry(event.getPlayer().getUniqueId());

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (event.getPlayer().isOnline()) {
                plugin.updatePlayerDisplay(event.getPlayer());
            }
        }, JOIN_DISPLAY_DELAY_TICKS);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getBossBarManager().hide(event.getPlayer());
    }
}