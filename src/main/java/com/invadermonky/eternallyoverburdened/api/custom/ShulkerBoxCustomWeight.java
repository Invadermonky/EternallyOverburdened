package com.invadermonky.eternallyoverburdened.api.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class ShulkerBoxCustomWeight implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return block instanceof BlockShulkerBox && stack.getTagCompound() != null && stack.getTagCompound().hasKey("BlockEntityTag");
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);
        if(stack.getTagCompound() != null) {
            NBTTagCompound tileTag = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if(tileTag.hasKey("Items")) {
                NBTTagCompound itemsTag = tileTag.getCompoundTag("Items");
                ItemStackHelper.loadAllItems(itemsTag, stacks);
            }
        }
        return stacks;
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        return Collections.emptyList();
    }
}
