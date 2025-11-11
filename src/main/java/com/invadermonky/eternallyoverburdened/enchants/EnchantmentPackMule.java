package com.invadermonky.eternallyoverburdened.enchants;

import com.invadermonky.eternallyoverburdened.EternallyOverburdened;
import com.invadermonky.eternallyoverburdened.config.ConfigHandlerEO;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantmentPackMule extends Enchantment {
    public EnchantmentPackMule() {
        super(Rarity.COMMON, EnumEnchantmentType.ARMOR, new EntityEquipmentSlot[] {EntityEquipmentSlot.CHEST});
        this.setRegistryName(EternallyOverburdened.MOD_ID, "pack_mule");
        this.setName(this.getRegistryName().toString());
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + enchantmentLevel * ConfigHandlerEO.enchantmentSettings.packMule.minEnchantability;
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        if(stack.getItem() instanceof ItemArmor) {
            return ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
        }
        return super.canApply(stack);
    }
}
