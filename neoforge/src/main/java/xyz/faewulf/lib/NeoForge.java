package xyz.faewulf.lib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoForge {

    public NeoForge(IEventBus eventBus) {
        CommonClass.init();
        Constants.LOG.info("Init done");
    }
}