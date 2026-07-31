package ua.fxtimeweather.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import ua.fxtimeweather.FXTimeWeather;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private final FXTimeWeather plugin;
    private final File file;
    private YamlConfiguration storage;
    private final Map<UUID, Boolean> cache = new ConcurrentHashMap<>();

    public PlayerDataManager(FXTimeWeather plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "players.yml");
    }

    public void load() {
        if (!file.exists()) {
            if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs()) {
                plugin.getLogger().warning("Не вдалося створити папку плагіну: " + plugin.getDataFolder().getPath());
            }
            try {
                if (!file.createNewFile()) {
                    plugin.getLogger().warning("Не вдалося створити players.yml: файл вже існує");
                }
            } catch (IOException exception) {
                plugin.getLogger().warning("Не вдалося створити players.yml: " + exception.getMessage());
            }
        }

        storage = YamlConfiguration.loadConfiguration(file);
        cache.clear();

        for (String key : storage.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                cache.put(uuid, storage.getBoolean(key, true));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public boolean isEnabled(UUID uuid) {
        return cache.getOrDefault(uuid, true);
    }

    public boolean hasEntry(UUID uuid) {
        return cache.containsKey(uuid);
    }

    public void ensureEntry(UUID uuid) {
        if (!hasEntry(uuid)) {
            setEnabled(uuid, true);
        }
    }

    public void setEnabled(UUID uuid, boolean enabled) {
        cache.put(uuid, enabled);
        storage.set(uuid.toString(), enabled);
        save();
    }

    private void save() {
        try {
            storage.save(file);
        } catch (IOException exception) {
            plugin.getLogger().warning("Не вдалося зберегти players.yml: " + exception.getMessage());
        }
    }
}