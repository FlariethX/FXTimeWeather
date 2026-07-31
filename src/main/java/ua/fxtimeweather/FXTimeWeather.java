package ua.fxtimeweather;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ua.fxtimeweather.command.ReloadCommand;
import ua.fxtimeweather.command.ToggleCommand;
import ua.fxtimeweather.config.ConfigManager;
import ua.fxtimeweather.listener.PlayerConnectionListener;
import ua.fxtimeweather.manager.ActionBarManager;
import ua.fxtimeweather.manager.BossBarManager;
import ua.fxtimeweather.manager.PlaceholderProcessor;
import ua.fxtimeweather.manager.PlayerDataManager;
import ua.fxtimeweather.manager.TimeWeatherManager;
import ua.fxtimeweather.placeholder.FXTimeWeatherExpansion;

import java.util.Objects;

public class FXTimeWeather extends JavaPlugin {

    private ConfigManager configManager;
    private PlaceholderProcessor placeholderProcessor;
    private BossBarManager bossBarManager;
    private ActionBarManager actionBarManager;
    private PlayerDataManager playerDataManager;

    private BukkitTask displayTask;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.load();

        playerDataManager = new PlayerDataManager(this);
        playerDataManager.load();

        TimeWeatherManager timeWeatherManager = new TimeWeatherManager(configManager);
        placeholderProcessor = new PlaceholderProcessor(configManager, timeWeatherManager);
        bossBarManager = new BossBarManager(configManager);
        actionBarManager = new ActionBarManager();

        Objects.requireNonNull(getCommand("twreload")).setExecutor(new ReloadCommand(this));
        Objects.requireNonNull(getCommand("twtoggle")).setExecutor(new ToggleCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new FXTimeWeatherExpansion(this).register();
        }

        startTasks();

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerDataManager.ensureEntry(player.getUniqueId());
            updatePlayerDisplay(player);
        }
    }

    @Override
    public void onDisable() {
        stopTasks();
        bossBarManager.hideAll();
    }

    public void reloadPlugin() {
        stopTasks();
        bossBarManager.hideAll();
        configManager.load();
        startTasks();

        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerDisplay(player);
        }
    }

    private void startTasks() {
        if (configManager.isBossbarMode()) {
            displayTask = Bukkit.getScheduler().runTaskTimer(
                    this, this::tickBossBar, 0L, Math.max(1, configManager.getBossbarUpdateInterval())
            );
        } else if (configManager.isActionbarMode()) {
            displayTask = Bukkit.getScheduler().runTaskTimer(
                    this, this::tickActionBar, 0L, Math.max(1, configManager.getActionbarUpdateInterval())
            );
        }
    }

    private void stopTasks() {
        if (displayTask != null) {
            displayTask.cancel();
            displayTask = null;
        }
    }

    private void tickBossBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                if (!playerDataManager.isEnabled(player.getUniqueId())) {
                    continue;
                }
                String text = placeholderProcessor.apply(configManager.getBossbarText(), player.getWorld());
                bossBarManager.show(player, text);
            } catch (Exception exception) {
                getLogger().warning("Помилка оновлення боссбару для " + player.getName() + ": " + exception.getMessage());
            }
        }
    }

    private void tickActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                if (!playerDataManager.isEnabled(player.getUniqueId())) {
                    continue;
                }
                String text = placeholderProcessor.apply(configManager.getActionbarText(), player.getWorld());
                actionBarManager.send(player, text);
            } catch (Exception exception) {
                getLogger().warning("Помилка оновлення actionbar для " + player.getName() + ": " + exception.getMessage());
            }
        }
    }

    public void updatePlayerDisplay(Player player) {
        if (!playerDataManager.isEnabled(player.getUniqueId())) {
            return;
        }

        if (configManager.isBossbarMode()) {
            String text = placeholderProcessor.apply(configManager.getBossbarText(), player.getWorld());
            bossBarManager.show(player, text);
        } else if (configManager.isActionbarMode()) {
            String text = placeholderProcessor.apply(configManager.getActionbarText(), player.getWorld());
            actionBarManager.send(player, text);
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlaceholderProcessor getPlaceholderProcessor() {
        return placeholderProcessor;
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}