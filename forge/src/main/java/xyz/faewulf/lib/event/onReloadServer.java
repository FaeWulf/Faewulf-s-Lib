package xyz.faewulf.lib.event;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
    public @NotNull CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller preparationsProfiler, @NotNull ProfilerFiller reloadProfiler, @NotNull Executor backgroundExecutor, @NotNull Executor gameExecutor) {
        return CompletableFuture.runAsync(Config::reloadAllConfig, backgroundExecutor).thenCompose(stage::wait);
    }
}