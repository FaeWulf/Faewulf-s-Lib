package xyz.faewulf.lib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class NeoForge {

    public NeoForge(IEventBus eventBus) {
        CommonClass.init();
        Constants.LOG.info("Init done");
    }
}