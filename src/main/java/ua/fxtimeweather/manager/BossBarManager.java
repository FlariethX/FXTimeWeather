package ua.fxtimeweather.manager;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ua.fxtimeweather.config.ConfigManager;
import ua.fxtimeweather.util.ColorUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarManager {

    private final ConfigManager configManager;
    private final Map<UUID, BossBar> bossBars = new ConcurrentHashMap<>();

    public BossBarManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void show(Player player, String text) {
        Component title = ColorUtil.parse(text);
        BossBar bossBar = bossBars.get(player.getUniqueId());

        if (bossBar == null) {
            bossBar = BossBar.bossBar(
                    title,
                    1.0f,
                    resolveColor(configManager.getBossbarColor()),
                    resolveOverlay(configManager.getBossbarStyle())
            );
            bossBars.put(player.getUniqueId(), bossBar);
            player.showBossBar(bossBar);
        } else {
            bossBar.name(title);
        }
    }

    public void hide(Player player) {
        BossBar bossBar = bossBars.remove(player.getUniqueId());
        if (bossBar != null) {
            player.hideBossBar(bossBar);
        }
    }

    public void hideAll() {
        for (Map.Entry<UUID, BossBar> entry : bossBars.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                player.hideBossBar(entry.getValue());
            }
        }
        bossBars.clear();
    }

    private BossBar.Color resolveColor(String value) {
        try {
            return BossBar.Color.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return BossBar.Color.PURPLE;
        }
    }

    private BossBar.Overlay resolveOverlay(String value) {
        return switch (value.toUpperCase()) {
            case "SEGMENTED_6" -> BossBar.Overlay.NOTCHED_6;
            case "SEGMENTED_10" -> BossBar.Overlay.NOTCHED_10;
            case "SEGMENTED_12" -> BossBar.Overlay.NOTCHED_12;
            case "SEGMENTED_20" -> BossBar.Overlay.NOTCHED_20;
            default -> BossBar.Overlay.PROGRESS;
        };
    }
}