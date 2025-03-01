package xyz.faewulf.lib;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.lib.util.config.infoScreen.ModInfoScreen;

@Mod(Constants.MOD_ID)
public class Forge {

    public Forge() {
        Constants.LOG.info("Loading");

        CommonClass.init();

        //config
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ModInfoScreen.getScreen(parent, Constants.MOD_ID))
        );

        Constants.LOG.info("Init done");
    }
}