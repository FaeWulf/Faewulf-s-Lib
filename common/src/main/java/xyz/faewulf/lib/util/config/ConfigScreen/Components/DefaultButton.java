package xyz.faewulf.lib.util.config.ConfigScreen.Components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2fStack;

public class DefaultButton extends Button {
    public DefaultButton(String MOD_ID, int x, int y, int width, int height, Component Text, OnPress onPress) {
        super(x, y, width, height, Text, onPress, DEFAULT_NARRATION);
        this.setTooltip(Tooltip.create(Component.translatable(MOD_ID + ".config." + "default.tooltip")));
    }

    @Override
    protected void renderContents(GuiGraphics graphics, int i, int i1, float v) {
        //Render button sprite
        this.renderDefaultSprite(graphics);

        Font font = Minecraft.getInstance().font;
        Matrix3x2fStack pose = graphics.pose();

        float scale = 2f;  // Set the scale (2.0f means 2x size)
        int textWidth = font.width(this.getMessage());
        int textHeight = font.lineHeight;

        // Center the text on the button
        int textX = this.getX() + 1 + (this.width - (int) (textWidth * scale)) / 2;
        int textY = this.getY() - 1 + (this.height - (int) (textHeight * scale)) / 2;

        // Render text
        pose.pushMatrix();
        pose.scale(scale, scale);
        pose.translate((textX / scale), (textY / scale));
        graphics.drawString(font, getMessage(), 0, 0, -1, true);
        pose.popMatrix();
    }
}
