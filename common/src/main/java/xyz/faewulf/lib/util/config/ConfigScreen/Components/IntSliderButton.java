package xyz.faewulf.lib.util.config.ConfigScreen.Components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.lib.Constants;
import xyz.faewulf.lib.util.config.ConfigLoaderFromAnnotation;
import xyz.faewulf.lib.util.config.ConfigScreen.ConfigScreen;

import java.lang.reflect.Field;
import java.util.Objects;

public class IntSliderButton extends AbstractSliderButton {

    String MOD_ID;
    ConfigLoaderFromAnnotation.EntryInfo entryInfo = null;
    ConfigLoaderFromAnnotation.SliderInfo sliderInfo = null;

    public IntSliderButton(String MOD_ID, int width, int height, Component message, ConfigLoaderFromAnnotation.EntryInfo entryInfo) {
        super(0, 0, width, height, message, normalizeValue(entryInfo));

        this.MOD_ID = MOD_ID;
        this.sliderInfo = entryInfo.slider;
        this.entryInfo = entryInfo;

        updateMessage();
    }

    // Converts raw value to normalized slider value (0.0 - 1.0)
    private static double normalizeValue(ConfigLoaderFromAnnotation.EntryInfo entryInfo) {

        int value = 0;
        try {
            value = (int) entryInfo.targetField.get(null);
        } catch (IllegalAccessException e) {
            Constants.LOG.error("[backpack] Something went wrong with the Number button...");
            e.printStackTrace();
        }

        return (double) (value - entryInfo.slider.min) / (double) (entryInfo.slider.max - entryInfo.slider.min);
    }


    // Converts normalized slider value to raw value
    private int denormalizeValue(double sliderValue) {

        int range = sliderInfo.max - sliderInfo.min;
        int rawValue = (int) (sliderValue * range) + sliderInfo.min;

        // Snap to nearest step
        int stepped = ((rawValue - sliderInfo.min) / sliderInfo.step) * sliderInfo.step + sliderInfo.min;

        // Clamp in case of rounding issues
        return Math.max(sliderInfo.min, Math.min(sliderInfo.max, stepped));
    }

    @Override
    protected void updateMessage() {
        // Update the button's label with the current value
        this.setMessage(Component.literal("Value: " + denormalizeValue(this.value)));
    }

    @Override
    protected void applyValue() {
        // Called when the slider is moved
        int currentValue = denormalizeValue(this.value);

        Field field = entryInfo.targetField;
        try {
            field.set(null, currentValue);
        } catch (IllegalAccessException | NumberFormatException e) {
            Constants.LOG.error(" Something went wrong with the config system...");
            e.printStackTrace();
        }
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        // Update the value (dynamic change from reset to default button)
        double newValue = normalizeValue(this.entryInfo);

        if (Math.abs(newValue - this.value) > 1e-9) {
            this.value = newValue;
            updateMessage();
        }

        // Handle for showing infos in right tab
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
}
