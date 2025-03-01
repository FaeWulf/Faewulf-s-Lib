package xyz.faewulf.lib.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class CopperUtils {

    /**
     * Attempts to degrade a given copper block state based on its weathering stage.
     *
     * @param oxidizable The {@link WeatheringCopper} instance representing the block type.
     * @param state      The current block state.
     * @return The next degraded state if available; otherwise, returns the current state.
     */
    public static BlockState tryDegrade(WeatheringCopper oxidizable, BlockState state) {
        var result = oxidizable.getNext(state);
        return result.orElse(state);
    }

    /**
     * Checks if there is water nearby a given position in the world.
     *
     * @param pos   The {@link BlockPos} to check from.
     * @param world The {@link LevelAccessor} representing the world.
     * @return {@code true} if the block is waterlogged or has at least four adjacent water blocks.
     */
    public static boolean isWaterNearby(BlockPos pos, LevelAccessor world) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        Direction[] directions = Direction.values();

        BlockState blockState = world.getBlockState(mutable);

        //if it is water logged
        if (blockState.getFluidState().is(Fluids.WATER))
            return true;

        //should be surrounded by water
        int sideCount = 0;
        for (Direction direction : directions) {
            mutable.setWithOffset(pos, direction);
            blockState = world.getBlockState(mutable);

            if (blockState.getFluidState().is(Fluids.WATER))
                sideCount++;
        }

        return sideCount >= 4;
    }
}
