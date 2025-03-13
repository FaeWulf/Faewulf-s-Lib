package xyz.faewulf.lib.event;

import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.util.gameTests.registerGameTests;


@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class gameTest {
    @SubscribeEvent
    public static void onGameTest(RegisterGameTestsEvent registerGameTestsEvent) {
        registerGameTestsEvent.register(registerGameTests.class);
    }
}
