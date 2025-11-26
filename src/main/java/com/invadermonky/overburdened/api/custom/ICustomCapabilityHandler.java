package com.invadermonky.overburdened.api.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * An interface used to define custom capability handlers used for weight calculation.
 * This interface should be used for ItemStacks that store tile entity data such as
 * Shulker Boxes or Fluid Tanks.
 * <p>
 * For an example of this interface in use, see {@link ShulkerBoxCapabilityHandler}.
 */
public interface ICustomCapabilityHandler {
    /**
     * Returns true if the ItemStack matches this custom handler.
     */
    boolean matches(ItemStack stack);

    /**
     * Returns a list of all items contained in this ItemStack's internal inventory.
     */
    NonNullList<ItemStack> getContainedItems(ItemStack stack);

    /**
     * Returns a list of all FluidStacks contained in this ItemStacks internal fluid tanks.
     */
    List<FluidStack> getContainedFluids(ItemStack stack);
}
