package com.invadermonky.overburdened.compat.gtce;

import com.invadermonky.overburdened.api.custom.ICustomCapabilityHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class GTCECapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        return false;
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        return null;
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        return Collections.emptyList();
    }
}
