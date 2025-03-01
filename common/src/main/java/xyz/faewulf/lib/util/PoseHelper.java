package xyz.faewulf.lib.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

public class PoseHelper {

    public static float MODEL_OFFSET = 0.03125f;
    public static float UNIT_SIZE = 0.0625f;
    public static float Y_OFFSET_VALUE = 1f;

    // x, y, z from -8 to 8

    /**
     * Translates the given {@link PoseStack} by applying a unit size and offset adjustments.
     *
     * @param poseStack The {@link PoseStack} to apply the translation to.
     * @param x         The x-axis translation factor.
     * @param y         The y-axis translation factor.
     * @param z         The z-axis translation factor.
     */
    public static void translate(PoseStack poseStack, float x, float y, float z) {
        poseStack.translate(UNIT_SIZE * -x, UNIT_SIZE * -y + Y_OFFSET_VALUE, UNIT_SIZE * (z) + MODEL_OFFSET);
    }

    /**
     * Translates the given {@link PoseStack} without applying the Y-axis offset adjustment.
     *
     * @param poseStack The {@link PoseStack} to apply the translation to.
     * @param x         The x-axis translation factor.
     * @param y         The y-axis translation factor.
     * @param z         The z-axis translation factor.
     */
    public static void translateNoOffset(PoseStack poseStack, float x, float y, float z) {
        poseStack.translate(UNIT_SIZE * -x, UNIT_SIZE * -y, UNIT_SIZE * (z) + MODEL_OFFSET);
    }

    /**
     * Scales the given {@link PoseStack} uniformly across all axes.
     *
     * @param poseStack The {@link PoseStack} to scale.
     * @param value     The scale factor to apply.
     */
    public static void scale(PoseStack poseStack, float value) {
        poseStack.scale(value, value, value);
    }

    /**
     * Standardizes the pose stack transformation for rendering a backpack.
     * <p>
     * Applies translation and rotates the pose stack along the Z-axis.
     *
     * @param poseStack The {@link PoseStack} to transform.
     */
    public static void standardizePoseForBackpack(PoseStack poseStack) {
        poseStack.translate(0, Y_OFFSET_VALUE, UNIT_SIZE * 2);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180F));
    }
}
