package com.invadermonky.overburdened.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

public class ItemHolder {
    public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;
    public final ResourceLocation registryName;
    public int metaData;

    public ItemHolder(ResourceLocation registryName, int metaData) {
        this.registryName = registryName;
        this.metaData = metaData;
    }

    public ItemHolder(ResourceLocation registryName) {
        this(registryName, WILDCARD_VALUE);
    }

    public ItemHolder(ItemStack stack) {
        this(stack.getItem().getRegistryName(), stack.getMetadata());
    }

    public ItemHolder setMetaData(int metaData) {
        this.metaData = metaData;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemHolder)) return false;
        ItemHolder that = (ItemHolder) object;
        return metaData == that.metaData && Objects.equals(registryName, that.registryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryName, metaData);
    }
}
