package ua.fxtimeweather.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import ua.fxtimeweather.FXTimeWeather;
import ua.fxtimeweather.util.ColorUtil;

public class ReloadCommand implements CommandExecutor {

    private final FXTimeWeather plugin;

    public ReloadCommand(FXTimeWeather plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!sender.hasPermission("fxtw.reload")) {
            Component message = ColorUtil.parse(
                    plugin.getConfigManager().getMessagesPrefix()
                            + plugin.getConfigManager().getNoPermissionMessage()
            );
            sender.sendMessage(message);
            return true;
        }

        plugin.reloadPlugin();

        Component message = ColorUtil.parse(
                plugin.getConfigManager().getMessagesPrefix()
                        + plugin.getConfigManager().getReloadSuccessMessage()
        );
        sender.sendMessage(message);
        return true;
    }
}