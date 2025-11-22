package com.invadermonky.eternallyoverburdened.api.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class ShulkerBoxCapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return block instanceof BlockShulkerBox && stack.getTagCompound() != null && stack.getTagCompound().hasKey("BlockEntityTag");
    }

    /**
     * Shulker Box inventory capability copied from {@link TileEntityShulkerBox#loadFromNbt(NBTTagCompound)}
     */
    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);
        if(stack.getTagCompound() != null) {
            NBTTagCompound tileTag = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if(tileTag.hasKey("Items", 9)) {
                ItemStackHelper.loadAllItems(tileTag, stacks);
            }
        }
        return stacks;
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        return Collections.emptyList();
    }
}
