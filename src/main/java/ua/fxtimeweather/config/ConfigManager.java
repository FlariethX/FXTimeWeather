package ua.fxtimeweather.config;

import org.bukkit.configuration.file.FileConfiguration;
import ua.fxtimeweather.FXTimeWeather;
import ua.fxtimeweather.model.DayPhase;
import ua.fxtimeweather.model.WeatherState;

import java.util.EnumMap;
import java.util.Map;

public class ConfigManager {

    private final FXTimeWeather plugin;

    private String dayTimeFormat;
    private final Map<DayPhase, String> dayNames = new EnumMap<>(DayPhase.class);
    private final Map<DayPhase, String> dayIcons = new EnumMap<>(DayPhase.class);
    private final Map<WeatherState, String> weatherNames = new EnumMap<>(WeatherState.class);
    private final Map<WeatherState, String> weatherIcons = new EnumMap<>(WeatherState.class);

    private String displayType;

    private String bossbarStyle;
    private String bossbarColor;
    private int bossbarUpdateInterval;
    private String bossbarText;

    private int actionbarUpdateInterval;
    private String actionbarText;

    private String messagesPrefix;
    private String reloadSuccessMessage;
    private String noPermissionMessage;
    private String toggleEnabledMessage;
    private String toggleDisabledMessage;

    public ConfigManager(FXTimeWeather plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        dayTimeFormat = config.getString("day_time_format", "HH:mm");

        for (DayPhase phase : DayPhase.values()) {
            dayNames.put(phase, config.getString("day_name." + phase.getKey(), phase.getKey()));
            dayIcons.put(phase, config.getString("day_icon." + phase.getKey(), ""));
        }

        for (WeatherState state : WeatherState.values()) {
            weatherNames.put(state, config.getString("weather_name." + state.getKey(), state.getKey()));
            weatherIcons.put(state, config.getString("weather_icon." + state.getKey(), ""));
        }

        displayType = config.getString("display.type", "bossbar").toLowerCase();

        bossbarStyle = config.getString("display.bossbar.style", "SOLID");
        bossbarColor = config.getString("display.bossbar.color", "PURPLE");
        bossbarUpdateInterval = config.getInt("display.bossbar.update_interval", 20);
        bossbarText = config.getString("display.bossbar.text", "");

        actionbarUpdateInterval = config.getInt("display.actionbar.update_interval", 20);
        actionbarText = config.getString("display.actionbar.text", "");

        messagesPrefix = config.getString("messages.prefix", "");
        reloadSuccessMessage = config.getString("messages.reload_success", "");
        noPermissionMessage = config.getString("messages.no_permission", "");
        toggleEnabledMessage = config.getString("messages.toggle_enabled", "");
        toggleDisabledMessage = config.getString("messages.toggle_disabled", "");
    }

    public String getDayTimeFormat() {
        return dayTimeFormat;
    }

    public String getDayName(DayPhase phase) {
        return dayNames.get(phase);
    }

    public String getDayIcon(DayPhase phase) {
        return dayIcons.get(phase);
    }

    public String getWeatherName(WeatherState state) {
        return weatherNames.get(state);
    }

    public String getWeatherIcon(WeatherState state) {
        return weatherIcons.get(state);
    }

    public boolean isBossbarMode() {
        return "bossbar".equals(displayType);
    }

    public boolean isActionbarMode() {
        return "actionbar".equals(displayType);
    }

    public String getBossbarStyle() {
        return bossbarStyle;
    }

    public String getBossbarColor() {
        return bossbarColor;
    }

    public int getBossbarUpdateInterval() {
        return bossbarUpdateInterval;
    }

    public String getBossbarText() {
        return bossbarText;
    }

    public int getActionbarUpdateInterval() {
        return actionbarUpdateInterval;
    }

    public String getActionbarText() {
        return actionbarText;
    }

    public String getMessagesPrefix() {
        return messagesPrefix;
    }

    public String getReloadSuccessMessage() {
        return reloadSuccessMessage;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getToggleEnabledMessage() {
        return toggleEnabledMessage;
    }

    public String getToggleDisabledMessage() {
        return toggleDisabledMessage;
    }
}