package xyz.faewulf.lib.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import xyz.faewulf.lib.registry.ItemTagRegistry;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Compare {

    /**
     * Checks if the given {@link Block} has the specified tag.
     *
     * @param block   The {@link Block} to check.
     * @param tagName The name of the tag to look for.</br>
     *                Format {@code namespace:item_id}
     * @return {@code true} if the item has the tag, {@code false} otherwise.
     */
    public static boolean isHasTag(Block block, String tagName) {
        // Create a TagKey for the block using the tagName.
        ResourceLocation path = ResourceLocation.tryParse(tagName);

        if (path == null)
            return false;

        TagKey<Block> blockTag = TagKey.create(BuiltInRegistries.BLOCK.key(), path);

        try {
            // Check if the block is in the specified tag.
            return BuiltInRegistries.BLOCK.getHolderOrThrow(BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow())
                    .is(blockTag);
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    /**
     * Checks if the given {@link Entity} has the specified tag.
     *
     * @param entity  The {@link Entity} to check.
     * @param tagName The name of the tag to look for.</br>
     *                Format {@code namespace:item_id}
     * @return {@code true} if the item has the tag, {@code false} otherwise.
     */
    public static boolean isHasTag(Entity entity, String tagName) {
        ResourceLocation path = ResourceLocation.tryParse(tagName);

        if (path == null)
            return false;

        return EntityType.getKey(entity.getType()).toString().equals(tagName);
    }

    /**
     * Checks if the given {@link Item} has the specified tag.
     *
     * @param item    The {@link Item} to check.
     * @param tagName The name of the tag to look for.</br>
     *                Format {@code namespace:item_id}
     * @return {@code true} if the item has the tag, {@code false} otherwise.
     */
    public static boolean isHasTag(Item item, String tagName) {
        // Create a TagKey for the block using the tagName.


        ResourceLocation path = ResourceLocation.tryParse(tagName);

        if (path == null)
            return false;

        TagKey<Item> itemTag = TagKey.create(BuiltInRegistries.ITEM.key(), path);

        try {
            // Check if the block is in the specified tag.
            return BuiltInRegistries.ITEM.getHolderOrThrow(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow())
                    .is(itemTag);
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    /**
     * Checks if the given item has the specified tag on the client side.
     * <p>
     * This method retrieves the item-tag mapping from {@link ItemTagRegistry} and
     * checks whether the provided item is associated with the given tag name.
     * </p>
     *
     * @param item    The item to check.
     * @param tagName The name of the tag to look for.
     *                Format should be: {@code path:tag}".</br>
     *                Example: {@code backpack/item:glow}
     * @return {@code true} if the item has the specified tag, {@code false} otherwise.
     */
    public static boolean isHasTagClient(Item item, String tagName) {
        Map<String, List<Item>> stringListMap = ItemTagRegistry.getTypeToItemsMap();

        if (!stringListMap.containsKey(tagName))
            return false;

        return stringListMap.get(tagName).contains(item);
    }

    /**
     * Checks the given {@link Block}'s name.
     *
     * @param name  The name of the block to compare with.
     * @param block The {@link Block} to check.</br>
     *              Format {@code namespace:item_id}
     * @return {@code true} if the item has the tag, {@code false} otherwise.
     */
    public static boolean isBlock(String name, Block block) {
        // Get the ResourceLocation of the block from the registry
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);

        // Convert to a string (e.g., "minecraft:dirt")
        String id = resourceLocation != null ? resourceLocation.toString() : "unknown:block";

        return id.equalsIgnoreCase(name);
    }

    /**
     * Checks the given {@link Item}'s name.
     *
     * @param name The name of the block to compare with.
     * @param item The {@link Item} to check.</br>
     *             Format {@code namespace:item_id}
     * @return {@code true} if the item has the tag, {@code false} otherwise.
     */
    public static boolean isItem(String name, Item item) {
        // Get the ResourceLocation of the block from the registry
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(item);

        // Convert to a string (e.g., "minecraft:dirt")
        String id = resourceLocation != null ? resourceLocation.toString() : "unknown:item";

        return id.equalsIgnoreCase(name);
    }
}
