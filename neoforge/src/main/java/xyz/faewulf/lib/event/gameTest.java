package xyz.faewulf.lib.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import xyz.faewulf.lib.Constants;


@EventBusSubscriber(modid = Constants.MOD_ID)
public class gameTest {
    @SubscribeEvent
    public static void onGameTest(RegisterGameTestsEvent registerGameTestsEvent) {
    }
}
