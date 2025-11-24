package com.invadermonky.eternallyoverburdened.compat.travelersbackpack;

import com.invadermonky.eternallyoverburdened.api.custom.ICustomCapabilityHandler;
import com.tiviacz.travelersbackpack.init.ModItems;
import com.tiviacz.travelersbackpack.util.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TravelersBackpackCapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() == ModItems.TRAVELERS_BACKPACK && stack.getTagCompound() != null;
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        NonNullList<ItemStack> contents = NonNullList.create();
        if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("Items")) {
            NonNullList<ItemStack> invContents = NonNullList.withSize(54, ItemStack.EMPTY);
            NonNullList<ItemStack> craftContents = NonNullList.withSize(9, ItemStack.EMPTY);
            ItemStackUtils.loadAllItems(stack.getTagCompound(), invContents, craftContents);
            contents.addAll(invContents);
            contents.addAll(craftContents);
        }
        return contents;
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        List<FluidStack> fluids = new ArrayList<>();
        if(stack.getTagCompound() != null) {
            addFluidIfValid(fluids, stack.getTagCompound().getCompoundTag("RightTank"));
            addFluidIfValid(fluids, stack.getTagCompound().getCompoundTag("LeftTank"));
        }
        return fluids;
    }

    private void addFluidIfValid(List<FluidStack> fluids, NBTTagCompound fluidTag) {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(fluidTag);
        if(fluidStack != null && fluidStack.getFluid() != null && fluidStack.amount > 0) {
            fluids.add(fluidStack);
        }
    }
}
