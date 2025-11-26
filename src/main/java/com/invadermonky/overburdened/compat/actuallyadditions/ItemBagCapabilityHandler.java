package com.invadermonky.overburdened.compat.actuallyadditions;

import com.invadermonky.overburdened.api.custom.ICustomCapabilityHandler;
import de.ellpeck.actuallyadditions.mod.items.InitItems;
import de.ellpeck.actuallyadditions.mod.items.ItemBag;
import de.ellpeck.actuallyadditions.mod.items.ItemDrill;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class ItemBagCapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() == InitItems.itemBag && !((ItemBag) stack.getItem()).isVoid;
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        ItemStackHandlerAA handler = new ItemStackHandlerAA(28);
        ItemDrill.loadSlotsFromNBT(handler, stack);
        return handler.getItems();
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        return Collections.emptyList();
    }
}
