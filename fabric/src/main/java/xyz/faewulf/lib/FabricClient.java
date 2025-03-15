package xyz.faewulf.lib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }
}

