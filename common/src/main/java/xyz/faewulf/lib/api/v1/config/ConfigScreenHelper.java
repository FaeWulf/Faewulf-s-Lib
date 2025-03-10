package xyz.faewulf.lib.api.v1.config;

import net.minecraft.client.gui.screens.Screen;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

public class ConfigScreenHelper {
    /**
     * Retrieves the configuration screen for the specified mod.
     * Warn: Client side only
     *
     * @param parent The parent screen from which the config screen is opened.
     * @param MOD_ID The unique identifier of the mod.
     * @return The configuration {@link Screen} for the given mod.
     */
    public static Screen getConfigScreen(Screen parent, String MOD_ID) {
        return ModInfoScreen.getScreen(parent, MOD_ID);
    }
}