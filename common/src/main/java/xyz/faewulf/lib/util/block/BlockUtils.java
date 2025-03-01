package xyz.faewulf.lib.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockUtils {

    /**
     * Breaks a block at the specified position and drops its items.
     * <p>
     * If a player is provided, the block will register the player as the one who broke it.
     * Otherwise, it is broken without a player context.
     *
     * @param level  The level where the block is located.
     * @param pos    The position of the block to break.
     * @param player The player responsible for breaking the block (can be {@code null}).
     */
    public static void breakBlockWithDrops(Level level, BlockPos pos, @Nullable Player player) {
        BlockState state = level.getBlockState(pos);

        if (player != null) {
            level.destroyBlock(pos, true, player);
            state.getBlock().playerWillDestroy(level, pos, state, player);
        } else {
            level.destroyBlock(pos, true);
        }

    }

    /**
     * Replaces a block at a given position in a {@link ServerLevel}.
     * <p>
     * If {@code breakEffect} is {@code true}, the block update includes all effects.
     * Otherwise, it updates the block without additional effects.
     *
     * @param level       The server-level where the block is replaced.
     * @param blockState  The new block state to place.
     * @param blockPos    The position where the block should be placed.
     * @param breakEffect Whether to apply full update effects and drops previous block's drops
     */
    public static void replaceBlock(ServerLevel level, @NotNull BlockState blockState, BlockPos blockPos, boolean breakEffect) {

        if (breakEffect)
            level.setBlock(blockPos, blockState, Block.UPDATE_ALL);
        else
            level.setBlockAndUpdate(blockPos, blockState);

    }

    /**
     * Check if a BlockState can be placed at a given position in a {@link ServerLevel}.
     * <p>
     *
     * @param level      The server-level where the block is checked.
     * @param blockState The new block state to check.
     * @param blockPos   The position where the block will check.
     * @return {@code true} if can be placed, otherwise {@code false}.
     */
    public static boolean canPlaceOn(ServerLevel level, @NotNull BlockState blockState, BlockPos blockPos) {
        return blockState.canSurvive(level, blockPos);
    }
}
