package xyz.faewulf.lib.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EnchantHelper {

    /**
     * Retrieves the {@link Enchantment} holder from the registry in the given world.
     *
     * @param level   The {@link Level} instance from which to access the enchantment registry.
     * @param enchant The {@link ResourceKey} representing the enchantment to retrieve.
     * @return A {@link Holder} containing the requested enchantment.
     * {@code Null} if not found any
     */
    @Nullable
    public static Holder<Enchantment> getEnchant(Level level, ResourceKey<Enchantment> enchant) {
        try {
            HolderGetter<Enchantment> registryEntryLookup = level.registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT);

            return registryEntryLookup.getOrThrow(enchant);
        } catch (IllegalStateException illegalStateException) {
            return null;
        }
    }

    /**
     * Retrieves the {@link Enchantment} holder from the registry in the given world.
     *
     * @param level     The {@link Level} instance from which to access the enchantment registry.
     * @param namespace The namespace of the enchantment.
     * @param path      enchantment path in data.
     * @return A {@link Holder} containing the requested enchantment.
     * {@code Null} if not found any
     */
    @Nullable
    public static Holder<Enchantment> getEnchant(Level level, String namespace, String path) {
        try {
            return level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                    .getHolder(ResourceLocation.fromNamespaceAndPath(namespace, path))
                    .orElse(null);
        } catch (IllegalStateException illegalStateException) {
            return null;
        }
    }

    /**
     * Retrieves the {@link Enchantment} holder from the registry in the given world.
     *
     * @param level   The {@link Level} instance from which to access the enchantment registry.
     * @param stack   The {@link ItemStack} itemStack want to check.
     * @param enchant The {@link ResourceKey} representing the enchantment to retrieve.
     * @return {@code true} if has target Enchantment, {@code false} otherwise.
     */
    public static boolean hasEnchantment(Level level, ItemStack stack, ResourceKey<Enchantment> enchant) {
        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        Holder<Enchantment> enchantmentHolder = getEnchant(level, enchant);

        if (enchantmentHolder == null)
            return false;

        return itemEnchantmentsComponent.getLevel(enchantmentHolder) > 0;
    }

    /**
     * Retrieves the {@link Enchantment} holder from the registry in the given world.
     *
     * @param level     The {@link Level} instance from which to access the enchantment registry.
     * @param stack     The {@link ItemStack} itemStack want to check.
     * @param namespace The namespace of the enchantment.
     * @param path      The id of enchantment.
     * @return {@code true} if has target Enchantment, {@code false} otherwise.
     */
    public static boolean hasEnchantment(Level level, ItemStack stack, String namespace, String path) {
        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        Holder<Enchantment> enchantmentHolder = getEnchant(level, namespace, path);

        if (enchantmentHolder == null)
            return false;

        return itemEnchantmentsComponent.getLevel(enchantmentHolder) > 0;
    }

    /**
     * Retrieves the {@link Enchantment}'s level of the target {@link ItemStack}.
     *
     * @param level   The {@link Level} instance from which to access the enchantment registry.
     * @param stack   The {@link ItemStack} itemStack want to check.
     * @param enchant The {@link ResourceKey} representing the enchantment to retrieve.
     * @return {@code level > 0} if has target Enchantment, {@code 0} otherwise.
     */
    public static int getEnchantLevelFromItem(Level level, ItemStack stack, ResourceKey<Enchantment> enchant) {
        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        Holder<Enchantment> enchantmentHolder = getEnchant(level, enchant);

        if (enchantmentHolder == null)
            return 0;

        return itemEnchantmentsComponent.getLevel(enchantmentHolder);
    }

    /**
     * Retrieves the {@link Enchantment}'s level if the target {@link ItemStack}
     *
     * @param level     The {@link Level} instance from which to access the enchantment registry.
     * @param stack     The {@link ItemStack} itemStack want to check.
     * @param namespace The namespace of the enchantment.
     * @param path      The id of enchantment.
     * @return {@code level > 0} if has target Enchantment, {@code 0} otherwise.
     */
    public static int getEnchantLevelFromItem(Level level, ItemStack stack, String namespace, String path) {
        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        Holder<Enchantment> enchantmentHolder = getEnchant(level, namespace, path);

        if (enchantmentHolder == null)
            return 0;

        return itemEnchantmentsComponent.getLevel(enchantmentHolder);
    }
}
