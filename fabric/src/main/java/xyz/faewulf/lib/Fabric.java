package xyz.faewulf.lib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.lib.util.config.Config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class Fabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                Config.reloadAllConfig();
            }

            @Override
            public ResourceLocation getFabricId() {
                return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "reload_listener");
            }
        });
    }

}
