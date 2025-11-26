package com.invadermonky.overburdened.api.custom;

import com.invadermonky.overburdened.api.OverburdenedAPI;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * An interface used to define custom item weights for items with complex handling. This interface
 * should only be used when the ItemStack has weight sources baked into its tag data.
 * <p>
 * This class should only be used if {@link ICustomCapabilityHandler} is not capable of handling
 * the weight logic.
 * <p>
 * For an example of this interface in use, see {@link SpawnEggItemWeight}.
 */
public interface ICustomItemWeight {
    /**
     * Returns true when the passed ItemStack matches this custom item weight
     */
    boolean matches(ItemStack stack);

    /**
     * Returns the weight of a basic item of this type. This is useful if item variants are defined
     * using ItemStack tag data.
     */
    default double getItemWeight(ItemStack stack) {
        return OverburdenedAPI.getItemWeight(stack);
    }

    /**
     * Returns the weight of a single item of this ItemStack. This value should include any internal
     * weights such from things as fluids, contained items, or other weight sources.
     */
    double getSingleItemWeight(ItemStack stack);

    /**
     * Returns the total weight of the ItemStack. This value is usually {@link ICustomItemWeight#getSingleItemWeight(ItemStack)}
     * multiplied by the stack count.
     */
    default double getItemStackWeight(ItemStack stack) {
        return this.getSingleItemWeight(stack) * stack.getCount();
    }

    /**
     * Additional weight sources that will appear on the detailed weight tooltip that appears when
     * holding shift.
     *
     * @param stack The ItemStack being queried for the tooltip
     * @param extraWeights A list of translated and formatted strings containing any additional
     *                     weight sources that will be added to the detailed weight tooltip
     */
    @SideOnly(Side.CLIENT)
    default void getCustomTooltipWeights(ItemStack stack, List<String> extraWeights) {}
}
