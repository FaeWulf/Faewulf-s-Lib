package xyz.faewulf.lib;

import net.minecraft.SharedConstants;
import xyz.faewulf.lib.platform.Services;
import xyz.faewulf.lib.util.config.Config;

public class CommonClass {
    public static void init() {

        //for debug/testing
        if (Services.PLATFORM.isDevelopmentEnvironment() && Services.PLATFORM.isClientSide())
            SharedConstants.IS_RUNNING_IN_IDE = true;

        Config.init();
    }
}