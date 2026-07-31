package ua.fxtimeweather.manager;

import org.bukkit.World;
import ua.fxtimeweather.config.ConfigManager;
import ua.fxtimeweather.model.DayPhase;
import ua.fxtimeweather.model.WeatherState;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceholderProcessor {

    private final ConfigManager configManager;
    private final TimeWeatherManager timeWeatherManager;

    public PlaceholderProcessor(ConfigManager configManager, TimeWeatherManager timeWeatherManager) {
        this.configManager = configManager;
        this.timeWeatherManager = timeWeatherManager;
    }

    public Map<String, String> buildValues(World world) {
        DayPhase phase = timeWeatherManager.getDayPhase(world);
        WeatherState weather = timeWeatherManager.getWeatherState(world);

        Map<String, String> values = new LinkedHashMap<>();
        values.put("time_icon", configManager.getDayIcon(phase));
        values.put("time_name", configManager.getDayName(phase));
        values.put("weather_icon", configManager.getWeatherIcon(weather));
        values.put("weather_name", configManager.getWeatherName(weather));
        values.put("day_count", String.valueOf(timeWeatherManager.getDayCount(world)));
        values.put("day_time", timeWeatherManager.getFormattedTime(world));
        return values;
    }

    public String apply(String text, World world) {
        Map<String, String> values = buildValues(world);
        String result = text;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace("%fxtw_" + entry.getKey() + "%", entry.getValue());
            result = result.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return result;
    }
}