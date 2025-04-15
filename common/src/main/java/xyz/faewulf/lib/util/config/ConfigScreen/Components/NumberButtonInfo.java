package xyz.faewulf.lib.util.config.ConfigScreen.Components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
    private final String MOD_ID;

    // Text scroll effect
    private long lastTime;
    private int scrollOffset;
    private boolean reverse = false;
    private int effectCooldown = 0;

    public NumberButtonInfo(String MOD_ID, int width, int height, Component message, Font font, ConfigLoaderFromAnnotation.EntryInfo info) {
        super(width, height, message, font);
        this.entryInfo = info;
        this.initMessage = message;
        this.MOD_ID = MOD_ID;

        this.lastTime = System.currentTimeMillis();
        this.scrollOffset = 0;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Font font = Minecraft.getInstance().font;
        // Change info text if value is changing
        String scrollingText = trimTextWithEllipsis(initMessage.getString(), this.width, font);

        Component valueStatusIndicator = Component.literal(scrollingText);

        // Format text for changing indicator
        if (isChanging()) {
            valueStatusIndicator = Component.literal(scrollingText).withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW);
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

    // Trims text to fit within the specified width and adds ellipsis if necessary
    private String trimTextWithEllipsis(String text, int maxWidth, Font textRenderer) {
        if (textRenderer.width(text) <= maxWidth) {
            return text;  // Text fits, no need to trim
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > 150) {  // Scroll every 150ms (slow scroll effect)

            if (effectCooldown > 0) {
                effectCooldown--;
                String scrollingText = getScrollingText(text, this.scrollOffset, maxWidth, textRenderer);
                return textRenderer.plainSubstrByWidth(scrollingText, maxWidth - textRenderer.width("...")) + "...";
            }

            //reverse offset value
            if (!reverse)
                this.scrollOffset = (this.scrollOffset + 1) % text.length();
            else
                this.scrollOffset = (this.scrollOffset - 1) % text.length();

            //this one will check if trimmed text's width < max width, then stop the trim and reverse the effect
            //to prevent the text fly out (disappear) of the button, leaves empty button in the process, imo that is ugly

            //the 2nd criteria is reset animation if scrolloffset back to 0
            String checkText = getScrollingText(text, this.scrollOffset, maxWidth, textRenderer);
            if (textRenderer.width(checkText) + 6 < maxWidth || this.scrollOffset < 1) {
                reverse = !reverse;
                //set cooldown to make the text stay for a while
                this.effectCooldown = 90;
            }
            lastTime = currentTime;
        }


        String scrollingText = getScrollingText(text, this.scrollOffset, maxWidth, textRenderer);
        return textRenderer.plainSubstrByWidth(scrollingText, maxWidth - textRenderer.width("...")) + "...";
    }

    // Generates a substring of the text for scrolling
    private String getScrollingText(String text, int scrollOffset, int maxWidth, Font textRenderer) {
        // Get a substring of the text starting from the current scroll offset

        if (scrollOffset < 0)
            scrollOffset = 0;

        if (scrollOffset > text.length())
            scrollOffset = text.length();

        String visiblePart = text.substring(scrollOffset);

        // Trim the visible part to fit in the available width
        if (textRenderer.width(visiblePart) > maxWidth) {
            visiblePart = textRenderer.plainSubstrByWidth(visiblePart, maxWidth);
        }

        return visiblePart;
    }
}
