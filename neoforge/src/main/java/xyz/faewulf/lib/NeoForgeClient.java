package xyz.faewulf.lib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeClient {

    public NeoForgeClient(IEventBus eventBus) {
        //config
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> ModInfoScreen.getScreen(parent, Constants.MOD_ID)
        );
    }
}