package xyz.faewulf.lib.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class Converter {

    /**
     * Retrieves the {@link Enchantment} holder from the registry in the given world.
     *
     * @param world   The {@link Level} instance from which to access the enchantment registry.
     * @param enchant The {@link ResourceKey} representing the enchantment to retrieve.
     * @return A {@link Holder} containing the requested enchantment.
     * @throws IllegalStateException if the enchantment is not found in the registry.
     */
    public static Holder<Enchantment> getEnchant(Level world, ResourceKey<Enchantment> enchant) {
        HolderGetter<Enchantment> registryEntryLookup = world.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT);

        return registryEntryLookup.getOrThrow(enchant);
    }

    /**
     * Retrieves the {@link Enchantment} holder from the registry in the given world.
     *
     * @param world     The {@link Level} instance from which to access the enchantment registry.
     * @param namespace The namespace of the enchantment.
     * @param path      enchantment path in data.
     * @return A {@link Holder} containing the requested enchantment.
     * @throws IllegalStateException if the enchantment is not found in the registry.
     */
    public static Holder<Enchantment> getEnchant(Level world, String namespace, String path) {
        return world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .get(ResourceLocation.fromNamespaceAndPath(namespace, path))
                .orElse(null);

    }

    /**
     * Returns the music note corresponding to the given note index.
     * <p>
     * If the index is out of range (not between 0 and 24), "Unknown" is returned.
     *
     * @param note The note index, where 0 corresponds to "F#" and 24 cycles back to "F#".
     * @return The musical note as a {@link String}, or "Unknown" if the index is invalid.
     */
    public static String getNoteCharacter(int note) {
        return switch (note) {
            case 0 -> "F#";
            case 1 -> "G";
            case 2 -> "G#";
            case 3 -> "A";
            case 4 -> "A#";
            case 5 -> "B";
            case 6 -> "C";
            case 7 -> "C#";
            case 8 -> "D";
            case 9 -> "D#";
            case 10 -> "E";
            case 11 -> "F";
            case 12 -> "F#";
            case 13 -> "G";
            case 14 -> "G#";
            case 15 -> "A";
            case 16 -> "A#";
            case 17 -> "B";
            case 18 -> "C";
            case 19 -> "C#";
            case 20 -> "D";
            case 21 -> "D#";
            case 22 -> "E";
            case 23 -> "F";
            case 24 -> "F#";
            default -> "Unknown";
        };
    }

    /**
     * Converts a time value in Minecraft ticks to a human-readable time format (HHhMMmSSs).
     * <p>
     * Minecraft operates at 20 ticks per second. This method converts the given tick count into
     * hours, minutes, and seconds, formatting the result accordingly:
     * <ul>
     *     <li>If the duration includes hours, the format is {@code HHhMMmSSs}.</li>
     *     <li>If the duration includes only minutes and seconds, the format is {@code MMmSSs}.</li>
     *     <li>If the duration is less than a minute, only seconds are displayed as {@code SSs}.</li>
     * </ul>
     *
     * @param ticks The number of ticks to convert.
     * @return A formatted time string representing the equivalent hours, minutes, and seconds.
     */
    public static String tick2Time(long ticks) {
        long seconds = ticks / 20;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        //ensure in range 60
        seconds %= 60;
        minutes %= 60;

        if (hours > 0)
            return String.format("%02dh%02dm%02ds", hours, minutes, seconds);
        if (minutes > 0)
            return String.format("%02dm%02ds", minutes, seconds);

        return String.format("%02ds", seconds);
    }

    /**
     * Converts a time value in Minecraft ticks to the in-game time format (HH:MM:SS).
     * <p>
     * Minecraft time operates on a 24,000-tick day cycle, where:
     * <ul>
     *     <li>0 ticks = 06:00 AM</li>
     *     <li>6,000 ticks = 12:00 PM</li>
     *     <li>12,000 ticks = 06:00 PM</li>
     *     <li>18,000 ticks = 12:00 AM</li>
     *     <li>24,000 ticks = 06:00 AM (next day)</li>
     * </ul>
     * This method calculates the in-game time by wrapping around 24,000 ticks and adjusting
     * the hour to match Minecraft's time system, where 0 ticks start at 6:00 AM.
     *
     * @param ticks The number of ticks to convert.
     * @return A formatted string representing the equivalent in-game time in HH:MM:SS format.
     */
    public static String tick2MinecraftTime(long ticks) {
        // Wrap around 24000 ticks
        long dayTime = ticks % 24000;

        // Calculate hours, minutes, and seconds
        long hours = (dayTime / 1000 + 6) % 24;  // Minecraft time starts at 6:00 AM, so add 6 hours
        long minutes = (dayTime % 1000) * 60 / 1000;
        long seconds = ((dayTime % 1000) * 60 % 1000) * 60 / 1000;

        // Format the time as HH:MM:SS
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Converts a horse's jump strength attribute to its corresponding jump height in blocks.
     * <p>
     * The calculation simulates Minecraft's jump physics, applying gravity (-0.08) and drag (0.98)
     * to determine the total height a horse can reach when jumping.
     *
     * @param strength The horse's jump strength value.
     * @return The jump height in blocks.
     */
    public static double horseJumpStrength2JumpHeight(double strength) {
        double height = 0;
        double velocity = strength;
        while (velocity > 0) {
            height += velocity;
            velocity = (velocity - .08) * .98 * .98;
        }
        return height;
    }

    /**
     * Converts a generic movement speed value to blocks per second.
     * <p>
     * In Minecraft, movement speed is multiplied by a constant factor to determine the
     * speed in blocks per second.
     *
     * @param speed The movement speed value.
     * @return The equivalent speed in blocks per second.
     */
    public static double genericSpeed2BlockPerSecond(double speed) {
        return 42.157796 * speed;
    }

    /**
     * Capitalizes the first letter of the given string.
     *
     * @param input The string to modify.
     * @return A new string with the first letter capitalized.
     */
    public static String UppercaseFirstLetter(@NotNull String input) {
        if (input.isBlank() || input.isEmpty())
            return "";

        return input.substring(0, 1).toUpperCase() + input.substring(1);

    }
}