package xyz.faewulf.lib.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.util.gameTests.registerGameTests;


@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class gameTest {
    @SubscribeEvent
    public static void onGameTest(RegisterGameTestsEvent registerGameTestsEvent) {
        registerGameTestsEvent.register(registerGameTests.class);
    }
}
