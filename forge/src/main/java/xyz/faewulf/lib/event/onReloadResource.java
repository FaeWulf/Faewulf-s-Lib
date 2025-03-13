package xyz.faewulf.lib.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.registry.ItemTagRegistry;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class onReloadResource {
    @SubscribeEvent
    public static void onReloadResourceEvent(AddPackFindersEvent event) {
        ItemTagRegistry.loadAllItemTags();
    }
}