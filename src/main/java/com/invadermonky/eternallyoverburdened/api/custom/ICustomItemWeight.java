package com.invadermonky.eternallyoverburdened.api.custom;

import com.invadermonky.eternallyoverburdened.config.WeightSettings;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * An interface used to define custom item weights for items with complex handling. This interface
 * should only be used when the ItemStack has weight sources baked into its tag data.
 * <p>
 * This class should only be used if {@link ICustomCapabilityHandler} is not capable of handling
 * the weight logic.
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
        return WeightSettings.getDefaultItemWeight(stack);
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
     * @param additionalWeights A list containing any additional weight sources that will
     *                                 be added to the detailed weight tooltip
     */
    default void getCustomTooltipWeights(ItemStack stack, List<String> additionalWeights) {}
}
