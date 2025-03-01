package xyz.faewulf.lib.api.v1.config;

import net.minecraft.client.gui.screens.Screen;
import xyz.faewulf.lib.util.config.Config;
import xyz.faewulf.lib.util.config.ModConfigs;
import xyz.faewulf.lib.util.config.Entry;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

public class ConfigHelper {

    /**
     * Registers a config handler for the specified configuration class.
     *
     * @param MOD_ID          The unique mod identifier.
     * @param ModConfigsClass A class that defines public static fields used as config values.
     *                        Each field must be annotated with {@link Entry} to be considered a valid config.
     * @see Entry
     * @see ModConfigs
     */
    public static void register(String MOD_ID, Class<?> ModConfigsClass) {
        Config.registerConfig(MOD_ID, ModConfigsClass);
    }


    /**
     * Retrieves the configuration screen for the specified mod.
     *
     * @param parent The parent screen from which the config screen is opened.
     * @param MOD_ID The unique identifier of the mod.
     * @return The configuration {@link Screen} for the given mod.
     */
    public static Screen getConfigScreen(Screen parent, String MOD_ID) {
        return ModInfoScreen.getScreen(parent, MOD_ID);
    }
}
