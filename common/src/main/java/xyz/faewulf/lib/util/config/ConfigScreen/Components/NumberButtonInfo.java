package xyz.faewulf.lib.util.config.ConfigScreen.Components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.util.config.ConfigLoaderFromAnnotation;
import xyz.faewulf.lib.util.config.ConfigScreen.ConfigScreen;

import java.util.Objects;

import static xyz.faewulf.lib.util.config.ConfigScreen.ConfigScreen.CONFIG_VALUES;

public class NumberButtonInfo extends StringWidget {

    private final ConfigLoaderFromAnnotation.EntryInfo entryInfo;
    private final Component initMessage;
    private String MOD_ID;

    public NumberButtonInfo(String MOD_ID, int width, int height, Component message, Font font, ConfigLoaderFromAnnotation.EntryInfo info) {
        super(width, height, message, font);
        this.entryInfo = info;
        this.initMessage = message;
        this.MOD_ID = MOD_ID;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Change info text if value is changing
        Component valueStatusIndicator = initMessage;

        if (isChanging()) {
            valueStatusIndicator = Component.literal(initMessage.getString()).withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW);
        }

        setMessage(valueStatusIndicator);

        if (isMouseOver(mouseX, mouseY) && !Objects.equals(this.entryInfo.name, ConfigScreen.currentInfo)) {

            ConfigScreen.infoTab_Title.setMessage(Component.literal(this.entryInfo.humanizeName).withStyle(ChatFormatting.BOLD));

            MutableComponent info = Component.translatable(MOD_ID + ".config." + this.entryInfo.name + ".tooltip");

            if (this.entryInfo.require_restart)
                info.append(Component.literal("\n\n").append(Component.translatable(MOD_ID + ".config." + "require_restart").withStyle(ChatFormatting.GOLD)));

            ConfigScreen.infoTab_Info.setMessage(info);
            ConfigScreen.infoTab.arrangeElements();
            ConfigScreen.currentInfo = this.entryInfo.name;
        }

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    private boolean isChanging() {
        Object value;
        try {
            value = this.entryInfo.targetField.get(null);
        } catch (IllegalAccessException e) {
            Constants.LOG.error("[backpack] Something went wrong with the Option button...");
            e.printStackTrace();
            return false;
        }

        Object lastValue = CONFIG_VALUES.get(this.entryInfo.name);

        return !value.equals(lastValue);
    }
}
