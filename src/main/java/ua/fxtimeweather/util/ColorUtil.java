package ua.fxtimeweather.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    private static final Pattern GRADIENT_PATTERN =
            Pattern.compile("<#([A-Fa-f0-9]{6})>(.*?)</#([A-Fa-f0-9]{6})>");
    private static final Pattern LEFTOVER_TAG_PATTERN =
            Pattern.compile("</?#([A-Fa-f0-9]{6})>");

    public static Component parse(String input) {
        String withGradients = applyGradients(input);
        String withoutLeftoverTags = LEFTOVER_TAG_PATTERN.matcher(withGradients).replaceAll("");
        String legacy = ChatColor.translateAlternateColorCodes('&', withoutLeftoverTags);
        return LegacyComponentSerializer.legacySection().deserialize(legacy);
    }

    public static String parseToLegacyString(String input) {
        return LegacyComponentSerializer.legacySection().serialize(parse(input));
    }

    private static String applyGradients(String input) {
        Matcher matcher = GRADIENT_PATTERN.matcher(input);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String startHex = matcher.group(1);
            String text = matcher.group(2);
            String endHex = matcher.group(3);

            matcher.appendReplacement(result, Matcher.quoteReplacement(buildGradientText(text, startHex, endHex)));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private static String buildGradientText(String text, String startHex, String endHex) {
        if (text.isEmpty()) {
            return text;
        }

        int startRgb = Integer.parseInt(startHex, 16);
        int endRgb = Integer.parseInt(endHex, 16);

        int startRed = (startRgb >> 16) & 0xFF;
        int startGreen = (startRgb >> 8) & 0xFF;
        int startBlue = startRgb & 0xFF;

        int endRed = (endRgb >> 16) & 0xFF;
        int endGreen = (endRgb >> 8) & 0xFF;
        int endBlue = endRgb & 0xFF;

        int length = text.length();
        StringBuilder gradientText = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char character = text.charAt(i);
            double ratio = (length == 1) ? 0.0 : (double) i / (double) (length - 1);

            int red = (int) Math.round(startRed + (endRed - startRed) * ratio);
            int green = (int) Math.round(startGreen + (endGreen - startGreen) * ratio);
            int blue = (int) Math.round(startBlue + (endBlue - startBlue) * ratio);

            gradientText.append(toLegacyHex(red, green, blue)).append(character);
        }

        return gradientText.toString();
    }

    private static String toLegacyHex(int red, int green, int blue) {
        String hex = String.format("%02x%02x%02x", red, green, blue);
        StringBuilder builder = new StringBuilder("§x");
        for (char character : hex.toCharArray()) {
            builder.append('§').append(character);
        }
        return builder.toString();
    }
}