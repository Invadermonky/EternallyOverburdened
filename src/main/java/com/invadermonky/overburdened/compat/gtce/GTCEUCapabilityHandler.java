package com.invadermonky.overburdened.compat.gtce;

import com.invadermonky.overburdened.api.custom.ICustomCapabilityHandler;
import gregtech.api.block.machines.BlockMachine;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collections;
import java.util.List;

public class GTCEUCapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return block instanceof BlockMachine && this.hasInventoryTag(stack);
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        NonNullList<ItemStack> list = NonNullList.create();
        if(stack.getTagCompound() != null) {
            ItemStackHandler handler = new ItemStackHandler();
            handler.deserializeNBT(stack.getTagCompound().getCompoundTag("Inventory"));
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack contents =  handler.getStackInSlot(i);
                if(!contents.isEmpty()) {
                    list.add(contents);
                }
            }
        }
        return list;
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        return Collections.emptyList();
    }

    private boolean hasInventoryTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey("Taped") && tag.hasKey("Inventory");
    }
}
