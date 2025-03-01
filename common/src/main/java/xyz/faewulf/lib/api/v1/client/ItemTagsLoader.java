package xyz.faewulf.lib.api.v1.client;

import net.minecraft.world.item.Item;
import xyz.faewulf.lib.registry.ItemTagRegistry;

public class ItemTagsLoader {


    /**
     * Registers a new resource location path for retrieving all tag JSON data.
     * <p>
     * This method register its path to the {@code ItemTagRegistry} for custom client tags.</br>
     * </p>
     * <p>
     * <b>Warn</b>: The same path loaded via multiple resource pack will be combine instead of replace.
     * </p>
     *
     * <h3>Example Json tag file:</h3>
     * - Register for {@code backpack/item}
     * <pre>{@code
     * path: assets/client_backpack/backpack/item/example.json
     *   {
     *     "type": "<string>", <-- this is tag name
     *     "items": [          <-- Array of items that contains this tag
     *       "<minecraft tag> or <itemID>"
     *     ]
     *   }
     *   }</pre>
     *
     * @param location The resource location representing the `path` in
     *                 {@code assets/<namespace>/path} where tag JSON data is stored.
     * @see xyz.faewulf.lib.util.Compare#isHasTagClient(Item, String)
     */
    public static void register(String location) {
        ItemTagRegistry.add(location);
    }
}
