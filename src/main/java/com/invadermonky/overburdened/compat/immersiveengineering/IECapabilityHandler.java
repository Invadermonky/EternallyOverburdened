package com.invadermonky.overburdened.compat.immersiveengineering;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.Utils;
import com.invadermonky.overburdened.api.custom.ICustomCapabilityHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class IECapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        if(stack.getTagCompound() != null) {
            Block block = Block.getBlockFromItem(stack.getItem());
            int meta = stack.getMetadata();
            return (block == IEContent.blockWoodenDevice0 && (meta == 0 || meta == 1 || meta == 5)) || (block == IEContent.blockMetalDevice0 && meta == 4);
        }
        return false;
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("inventory")) {
            Block block = Block.getBlockFromItem(stack.getItem());
            int meta = stack.getMetadata();
            if (block == IEContent.blockWoodenDevice0 && (meta == 0 || meta == 5)) {
                return Utils.readInventory(stack.getTagCompound().getTagList("inventory", 10), 27);
            }
        }
        return NonNullList.create();
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        List<FluidStack> fluids = new ArrayList<>();
        int meta = stack.getMetadata();
        if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("tank")) {
            Block block = Block.getBlockFromItem(stack.getItem());
            if ((block == IEContent.blockWoodenDevice0 && meta == 1) || (block == IEContent.blockMetalDevice0 && meta == 4)) {
                NBTTagCompound tankTag = stack.getTagCompound().getCompoundTag("tank");
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tankTag);
                if(fluidStack != null && fluidStack.getFluid() != null && fluidStack.amount > 0) {
                    fluids.add(fluidStack);
                }
            }
        }
        return fluids;
    }
}
