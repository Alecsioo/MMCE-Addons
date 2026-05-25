package github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting;

import net.minecraft.util.text.TextFormatting;

// I hate util classes as much as I hate code repetition, so here we are ig
public class FormatUtils {

    public static String format(String left, String right) {
        return format(TextFormatting.DARK_GRAY, left, right);
    }

    public static String format(TextFormatting primaryColor, String left, String right) {
        return String.format("%s%s %s: %s%s", primaryColor, left, TextFormatting.GRAY, primaryColor, right);
    }

    public static String format(TextFormatting color, String toFormat) {
        return String.format("%s%s", color, toFormat);
    }
}
