package ua.fxtimeweather.manager;

import org.bukkit.World;
import ua.fxtimeweather.config.ConfigManager;
import ua.fxtimeweather.model.DayPhase;
import ua.fxtimeweather.model.WeatherState;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeWeatherManager {

    private final ConfigManager configManager;

    public TimeWeatherManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public DayPhase getDayPhase(World world) {
        return DayPhase.fromTicks(((world.getTime() % 24000) + 24000) % 24000);
    }

    public WeatherState getWeatherState(World world) {
        if (world.isThundering()) {
            return WeatherState.STORM;
        }
        if (world.hasStorm()) {
            return WeatherState.RAIN;
        }
        return WeatherState.CLEAR;
    }

    public long getDayCount(World world) {
        return world.getFullTime() / 24000L;
    }

    public String getFormattedTime(World world) {
        long ticks = ((world.getTime() % 24000) + 24000) % 24000;
        long secondsOfDay = (ticks * 86400L) / 24000L;
        secondsOfDay = (secondsOfDay + 21600L) % 86400L;

        int hour = (int) (secondsOfDay / 3600L);
        int minute = (int) ((secondsOfDay % 3600L) / 60L);
        int second = (int) (secondsOfDay % 60L);

        LocalTime localTime = LocalTime.of(hour, minute, second);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(configManager.getDayTimeFormat());
        return localTime.format(formatter);
    }
}