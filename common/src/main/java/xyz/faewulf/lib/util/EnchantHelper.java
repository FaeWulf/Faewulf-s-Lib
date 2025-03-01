package xyz.faewulf.lib.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
     * @param level   The {@link Level} instance from which to access the enchantment registry.
     * @param stack   The {@link ItemStack} itemStack want to check.
     * @param enchant The {@link ResourceKey} representing the enchantment to retrieve.
     * @return {@code true} if has target Enchantment, {@code false} otherwise.
     */
    public static boolean hasEnchantment(Level level, ItemStack stack, Enchantment enchant) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchant, stack) > 0;
    }
}
