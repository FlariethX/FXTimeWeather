package ua.fxtimeweather.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import ua.fxtimeweather.FXTimeWeather;
import ua.fxtimeweather.util.ColorUtil;

public class ToggleCommand implements CommandExecutor {

    private final FXTimeWeather plugin;

    public ToggleCommand(FXTimeWeather plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Цю команду може використовувати лише гравець.");
            return true;
        }

        if (!player.hasPermission("fxtw.use")) {
            Component message = ColorUtil.parse(
                    plugin.getConfigManager().getMessagesPrefix()
                            + plugin.getConfigManager().getNoPermissionMessage()
            );
            player.sendMessage(message);
            return true;
        }

        boolean newState = !plugin.getPlayerDataManager().isEnabled(player.getUniqueId());
        plugin.getPlayerDataManager().setEnabled(player.getUniqueId(), newState);

        if (newState) {
            plugin.updatePlayerDisplay(player);
        } else {
            plugin.getBossBarManager().hide(player);
        }

        String text = newState
                ? plugin.getConfigManager().getToggleEnabledMessage()
                : plugin.getConfigManager().getToggleDisabledMessage();

        Component message = ColorUtil.parse(plugin.getConfigManager().getMessagesPrefix() + text);
        player.sendMessage(message);
        return true;
    }
}