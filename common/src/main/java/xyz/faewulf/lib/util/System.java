package xyz.faewulf.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class System {

    /**
     * Displays a system toast notification in Minecraft Client.
     *
     * @param title   The title of the toast message.
     * @param message The optional message content of the toast. Can be {@code null}.
     */
    public static void sendSystemToast(Component title, @Nullable Component message) {
        if (Minecraft.getInstance() != null)
            SystemToast.add(
                    Minecraft.getInstance().getToasts(),
                    SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                    title, message
            );
    }
}
