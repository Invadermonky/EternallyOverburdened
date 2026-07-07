package com.invadermonky.overburdened.compat.bundles;

import com.invadermonky.overburdened.api.custom.ICustomCapabilityHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import surreal.bundles.items.ItemBundle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BundlesCapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBundle;
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack itemStack) {
        NonNullList<ItemStack> list = NonNullList.create();
        ItemStack[] stacks = ItemBundle.getItems(itemStack);
        if (stacks != null) {
            Arrays.stream(stacks).filter(stack -> !stack.isEmpty()).forEach(list::add);
        }
        return list;
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack itemStack) {
        return Collections.emptyList();
    }
}

