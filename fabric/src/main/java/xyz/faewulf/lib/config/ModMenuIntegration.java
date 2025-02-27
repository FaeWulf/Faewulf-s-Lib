package xyz.faewulf.lib.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ModInfoScreen.getScreen(parent, Constants.MOD_ID, null, null, null, null);
    }
}