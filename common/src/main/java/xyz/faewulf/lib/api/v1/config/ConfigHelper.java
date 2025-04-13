package xyz.faewulf.lib.api.v1.config;

import xyz.faewulf.lib.util.config.Config;
import xyz.faewulf.lib.util.config.ModConfigs;
import xyz.faewulf.lib.util.config.Entry;

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
     * Reload a config file for the specified configuration class.
     *
     * @param MOD_ID The unique mod identifier of the config needs to reload.
     */
    public static void reload(String MOD_ID) {
        Config.reloadConfig(MOD_ID);
    }
}
