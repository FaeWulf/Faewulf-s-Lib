package xyz.faewulf.lib.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.registry.ItemTagRegistry;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class onReloadResource {
    @SubscribeEvent
    public static void onReloadResourceEvent(AddPackFindersEvent event) {
        ItemTagRegistry.loadAllItemTags();
    }
}