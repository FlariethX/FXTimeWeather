package ua.fxtimeweather.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ua.fxtimeweather.FXTimeWeather;
import ua.fxtimeweather.util.ColorUtil;

import java.util.Map;

public class FXTimeWeatherExpansion extends PlaceholderExpansion {

    private final FXTimeWeather plugin;

    public FXTimeWeatherExpansion(FXTimeWeather plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "fxtw";
    }

    @Override
    public @NotNull String getAuthor() {
        return "FlariethX";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (!(offlinePlayer instanceof Player player) || !player.isOnline()) {
            return "";
        }

        Map<String, String> values = plugin.getPlaceholderProcessor().buildValues(player.getWorld());
        String value = values.get(params);
        if (value == null) {
            return null;
        }

        return ColorUtil.parseToLegacyString(value);
    }
}