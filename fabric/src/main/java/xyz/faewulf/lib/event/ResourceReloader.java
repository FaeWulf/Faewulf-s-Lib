package xyz.faewulf.lib.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import xyz.faewulf.lib.registry.ItemTagRegistry;

@Environment(EnvType.CLIENT)
public class ResourceReloader extends SimplePreparableReloadListener<int[]> {

    @Override
    protected int[] prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        ItemTagRegistry.loadAllItemTags();
        return new int[1];
    }

    @Override
    protected void apply(int[] object, ResourceManager resourceManager, ProfilerFiller profiler) {
    }
}
