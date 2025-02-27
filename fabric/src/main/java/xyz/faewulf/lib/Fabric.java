package xyz.faewulf.lib;

import net.fabricmc.api.ModInitializer;

public class Fabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();
    }

}
