package xyz.faewulf.lib.event;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.util.config.Config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class onReloadServer {
    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
        // This registers your custom listener that runs whenever /reload happens
        event.addListener(new MyCustomReloadListener());
    }
}

class MyCustomReloadListener implements PreparableReloadListener {

    @Override
    public @NotNull CompletableFuture<Void> reload(@NotNull PreparationBarrier preparationBarrier, @NotNull ResourceManager resourceManager, @NotNull Executor executor, @NotNull Executor executor1) {
        return CompletableFuture.runAsync(Config::reloadAllConfig, executor).thenCompose(preparationBarrier::wait);
    }
}