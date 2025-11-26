package com.invadermonky.overburdened.api.custom;

import com.invadermonky.overburdened.api.OverburdenedAPI;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class SpawnEggItemWeight implements ICustomItemWeight {
    private static final double cowWeight = 12.0;

    @Override
    public boolean matches(ItemStack stack) {
        if(stack.getItem() == Items.SPAWN_EGG && stack.getTagCompound() != null) {
            NBTTagCompound tag = stack.getTagCompound();
            if(tag.hasKey("EntityTag")) {
                return tag.getCompoundTag("EntityTag").getString("id").equals("minecraft:cow");
            }
        }
        return false;
    }

    @Override
    public double getSingleItemWeight(ItemStack stack) {
        return this.getItemWeight(stack) + cowWeight;
    }

    @Override
    public void getCustomTooltipWeights(ItemStack stack, List<String> extraWeights) {
        //Normally a localized key would be used, but this is a basic example to showcase how additional tooltips are added.
        extraWeights.add("Entity Weight: " + OverburdenedAPI.getFormatedWeight(cowWeight, true));

        //With localization
        //  Language Key:
        //      tooltip.entity_weight.info=Entity Weight: %s
        //  Tooltip Code:
        //      extraWeights.add(I18n.format("tooltip.entity_weight.info", OverburdenedAPI.getFormatedWeight(cowWeight, true)));
    }
}
