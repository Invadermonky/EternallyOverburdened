package com.invadermonky.overburdened.compat.hbm;

import com.hbm.blocks.generic.BlockStorageCrate;
import com.hbm.items.tool.ItemAmmoBag;
import com.hbm.tileentity.IPersistentNBT;
import com.invadermonky.overburdened.api.custom.ICustomCapabilityHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class HBMCapabilityHandler implements ICustomCapabilityHandler {
    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() instanceof ItemAmmoBag || Block.getBlockFromItem(stack.getItem()) instanceof BlockStorageCrate;
    }

    @Override
    public NonNullList<ItemStack> getContainedItems(ItemStack stack) {
        NonNullList<ItemStack> contents = NonNullList.create();
        if(stack.getItem() instanceof ItemAmmoBag) {
            this.getAmmoBagItems(stack, contents);
        } else if(Block.getBlockFromItem(stack.getItem()) instanceof BlockStorageCrate) {
            this.getStorageCrateItems(stack, contents);
        }
        return contents;
    }

    @Override
    public List<FluidStack> getContainedFluids(ItemStack stack) {
        return Collections.emptyList();
    }

    private void getAmmoBagItems(ItemStack bagStack, NonNullList<ItemStack> contents) {
        ItemAmmoBag.InventoryAmmoBag inventory = new ItemAmmoBag.InventoryAmmoBag(bagStack);
        for(int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if(!stack.isEmpty()) {
                contents.add(stack);
            }
        }
    }

    private void getStorageCrateItems(ItemStack crateStack, NonNullList<ItemStack> contents) {
        BlockStorageCrate crate = (BlockStorageCrate) Block.getBlockFromItem(crateStack.getItem());
        int slots = crate.getSlots();
        if(crateStack.getTagCompound() != null && crateStack.getTagCompound().hasKey(IPersistentNBT.NBT_PERSISTENT_KEY)) {
            NBTTagCompound tagCompound = crateStack.getTagCompound().getCompoundTag(IPersistentNBT.NBT_PERSISTENT_KEY);
            for(int i = 0; i < slots; i++) {
                if(tagCompound.hasKey("slot" + i)) {
                    ItemStack stack = new ItemStack(tagCompound.getCompoundTag("slot" + i));
                    if(!stack.isEmpty()) {
                        contents.add(stack);
                    }
                }
            }
        }
    }
}
