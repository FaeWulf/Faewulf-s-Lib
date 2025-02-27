package xyz.faewulf.lib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import xyz.faewulf.lib.registry.ItemTagRegistry;
import xyz.faewulf.lib.util.config.Config;

@Environment(EnvType.CLIENT)
public class FabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerReloadEvent();
    }

    private static void registerReloadEvent() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return null;
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                ItemTagRegistry.loadAllItemTags();
            }
        });
    }
}

