package xyz.faewulf.lib.util.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemUtils {

    /**
     * Checks if the given item is a BlockItem.
     *
     * @param target The item to check.
     * @return {@code true} if the item is an instance of {@link BlockItem}, otherwise {@code false}.
     */
    private static boolean isBlockItem(Item target) {
        return target instanceof BlockItem;
    }

    /**
     * Checks if the given item is a block item corresponding to a specific block state.
     *
     * @param target     The item to check.
     * @param blockState The block state to compare with.
     * @return {@code true} if the item is a {@link BlockItem} and matches the block in the given block state.
     */
    private static boolean isBlockItemOf(Item target, BlockState blockState) {
        return target instanceof BlockItem blockItem && blockItem.getBlock() == blockState.getBlock();
    }

    /**
     * Checks if the given item is a block item corresponding to a specific block.
     *
     * @param target The item to check.
     * @param block  The block to compare with.
     * @return {@code true} if the item is a {@link BlockItem} and matches the given block.
     */
    private static boolean isBlockItemOf(Item target, Block block) {
        return target instanceof BlockItem blockItem && blockItem.getBlock() == block;
    }
}
