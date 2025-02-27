package xyz.faewulf.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class System {
    public static void sendSystemToast(Component title, @Nullable Component message) {

        if (Minecraft.getInstance() != null)
            SystemToast.add(
                    Minecraft.getInstance().getToastManager(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                    title, message
            );

    }
}
