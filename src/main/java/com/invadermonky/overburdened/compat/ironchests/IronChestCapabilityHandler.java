package com.invadermonky.overburdened.compat.ironchests;

import com.invadermonky.overburdened.api.custom.ICustomCapabilityHandler;
import cpw.mods.ironchest.common.items.shulker.ItemIronShulkerBox;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class IronChestCapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() instanceof ItemIronShulkerBox;
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        NonNullList<ItemStack> contents = NonNullList.create();
        if(stack.getTagCompound() != null) {
            NBTTagCompound tileTag = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            int size = tileTag.getInteger("ShulkerBoxSize");
            if(size > 0) {
                contents = NonNullList.withSize(size, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(tileTag, contents);
            }
        }
        return contents;
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        return Collections.emptyList();
    }
}
