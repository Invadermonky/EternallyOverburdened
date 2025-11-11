package com.invadermonky.eternallyoverburdened.config;

import com.invadermonky.eternallyoverburdened.registry.ModEnchantsEO;
import com.invadermonky.eternallyoverburdened.utils.helpers.LogHelper;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeightSettings {
    private static final Set<ResourceLocation> CAPABILITY_BLACKLIST = new HashSet<>();
    private static Object2DoubleMap<ResourceLocation> ITEM_WEIGHTS;
    private static Object2DoubleMap<Fluid> FLUID_WEIGHTS;
    private static Object2DoubleMap<ResourceLocation> ARMOR_ADJUSTMENTS;
    private static Object2DoubleMap<Enchantment> ENCHANTMENT_ADJUSTMENTS;
    private static Object2DoubleMap<Potion> POTION_ADJUSTMENTS;

    public static double getItemWeight(ItemStack stack) {
        if(!stack.isEmpty()) {
            return ITEM_WEIGHTS.getOrDefault(stack.getItem().getRegistryName(), ConfigHandlerEO.itemSettings.defaultItemWeight);
        }
        return 0;
    }

    public static double getSingleItemWeight(ItemStack stack) {
        if(!stack.isEmpty()) {
            return getItemWeight(stack) + getFluidHandlerCapabilityWeight(stack) + getItemHandlerCapabilityWeight(stack);
        }
        return 0;
    }

    public static double getItemStackWeight(ItemStack stack) {
        if(!stack.isEmpty()) {
            return getSingleItemWeight(stack) * stack.getCount();
        }
        return 0;
    }

    public static double getFluidWeight(@Nullable Fluid fluid) {
        return fluid != null ? FLUID_WEIGHTS.getOrDefault(fluid, ConfigHandlerEO.itemSettings.defaultFluidWeight) : 0;
    }

    public static double getFluidStackWeight(@Nullable FluidStack fluidStack) {
        return fluidStack != null ? getFluidWeight(fluidStack.getFluid()) * fluidStack.amount : 0;
    }

    public static double getItemHandlerCapabilityWeight(ItemStack stack) {
        double weight = 0;
        if(!CAPABILITY_BLACKLIST.contains(stack.getItem().getRegistryName()) && stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if(handler != null) {
                for(int i = 0; i < handler.getSlots(); i++) {
                    ItemStack slotStack = handler.getStackInSlot(i);
                    weight += getItemStackWeight(slotStack);
                }
            }
        }
        return weight;
    }

    public static double getFluidHandlerCapabilityWeight(ItemStack stack) {
        double weight = 0;
        if(!CAPABILITY_BLACKLIST.contains(stack.getItem().getRegistryName()) && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            if(handler != null) {
                for(IFluidTankProperties props : handler.getTankProperties()) {
                    weight += getFluidStackWeight(props.getContents());
                }
            }
        }
        return weight;
    }

    public static double getArmorAdjustment(ItemStack stack) {
        double adjustment = ARMOR_ADJUSTMENTS.getOrDefault(stack.getItem().getRegistryName(), 0.0);
        adjustment += getEnchantmentAdjustments(stack);
        return adjustment;
    }

    public static double getEnchantmentAdjustments(ItemStack stack) {
        double adjustment = 0;
        if(stack.isItemEnchanted()) {
            for(Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
                adjustment += getEnchantmentAdjustment(entry.getKey(), entry.getValue());
            }
        }
        return adjustment;
    }

    public static double getEnchantmentAdjustment(Enchantment enchantment, int level) {
        return ENCHANTMENT_ADJUSTMENTS.getOrDefault(enchantment, 0.0) * (double) level;
    }

    public static double getPotionAdjustment(PotionEffect effect) {
        return POTION_ADJUSTMENTS.getOrDefault(effect.getPotion(), 0.0) * (double) effect.getAmplifier();
    }

    public static void syncConfig() {
        parseCapabilityBlacklist();
        parseItemWeights();
        parseFluidWeights();
        parseArmorAdjustments();
        parseEnchantmentAdjustments();
        parsePotionAdjustments();
    }

    private static void parseCapabilityBlacklist() {
        CAPABILITY_BLACKLIST.clear();
        for(String str : ConfigHandlerEO.itemSettings.capabilityBlacklist) {
            CAPABILITY_BLACKLIST.add(new ResourceLocation(str));
        }
    }

    private static void parseItemWeights() {
        Map<ResourceLocation, Double> map = new HashMap<>();
        Pattern pattern = Pattern.compile("^(.+?:.+?)=(-?\\d*\\.?\\d*)$");
        for(String configStr : ConfigHandlerEO.itemSettings.itemWeights) {
            try {
                Matcher matcher = pattern.matcher(configStr);
                if (matcher.find()) {
                    ResourceLocation loc = new ResourceLocation(matcher.group(1));
                    double weight = Double.parseDouble(matcher.group(2));
                    map.put(loc, weight);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                LogHelper.error("Failed to parse config string: " + configStr);
            }
        }
        ITEM_WEIGHTS = new Object2DoubleOpenHashMap<>(map);
    }

    private static void parseFluidWeights() {
        Map<Fluid, Double> map = new HashMap<>();
        Pattern pattern = Pattern.compile("^(.+?)=(-?\\d*\\.?\\d*)$");
        for(String configStr : ConfigHandlerEO.itemSettings.fluidWeights) {
            try {
                Matcher matcher = pattern.matcher(configStr);
                if (matcher.find()) {
                    String fluidName = matcher.group(1);
                    double weight = Double.parseDouble(matcher.group(2));
                    Fluid fluid = FluidRegistry.getFluid(fluidName);
                    if (fluid != null) {
                        map.put(fluid, weight);
                    } else {
                        LogHelper.error("No valid fluid found for: " + fluidName);
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                LogHelper.error("Failed to parse config string: " + configStr);
            }
        }
        FLUID_WEIGHTS = new Object2DoubleOpenHashMap<>(map);
    }

    private static void parseArmorAdjustments() {
        Map<ResourceLocation, Double> map = new HashMap<>();
        Pattern pattern = Pattern.compile("^(.+?:.+?)=(-?\\d*\\.?\\d*)$");
        for(String configStr : ConfigHandlerEO.itemSettings.armorAdjustments) {
            try {
                Matcher matcher = pattern.matcher(configStr);
                if (matcher.find()) {
                    ResourceLocation loc = new ResourceLocation(matcher.group(1));
                    double weight = Double.parseDouble(matcher.group(2));
                    map.put(loc, weight);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                LogHelper.error("Failed to parse config string: " + configStr);
            }
        }
        ARMOR_ADJUSTMENTS = new Object2DoubleOpenHashMap<>(map);
    }

    private static void parseEnchantmentAdjustments() {
        Map<Enchantment, Double> map = new HashMap<>();
        Pattern pattern = Pattern.compile("^(.+?:.+?)=(-?\\d*\\.?\\d*)$");
        for(String configStr : ConfigHandlerEO.itemSettings.enchantmentAdjustments) {
            try {
                Matcher matcher = pattern.matcher(configStr);
                if (matcher.find()) {
                    ResourceLocation loc = new ResourceLocation(matcher.group(1));
                    double weight = Double.parseDouble(matcher.group(2));
                    Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(loc);
                    if(enchantment != null) {
                        map.put(enchantment, weight);
                    } else {
                        LogHelper.error("No valid enchantment found for: " + loc);
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                LogHelper.error("Failed to parse config string: " + configStr);
            }
        }
        if(ConfigHandlerEO.enchantmentSettings.packMule.enablePackMule) {
            map.put(ModEnchantsEO.PACK_MULE, ConfigHandlerEO.enchantmentSettings.packMule.carryWeightBonus);
        }
        ENCHANTMENT_ADJUSTMENTS = new Object2DoubleOpenHashMap<>(map);
    }

    private static void parsePotionAdjustments() {
        Map<Potion, Double> map = new HashMap<>();
        Pattern pattern = Pattern.compile("^(.+?:.+?)=(-?\\d*\\.?\\d*)$");
        for(String configStr : ConfigHandlerEO.itemSettings.potionAdjustments) {
            try {
                Matcher matcher = pattern.matcher(configStr);
                if (matcher.find()) {
                    ResourceLocation loc = new ResourceLocation(matcher.group(1));
                    double weight = Double.parseDouble(matcher.group(2));
                    Potion potion = ForgeRegistries.POTIONS.getValue(loc);
                    if(potion != null) {
                        map.put(potion, weight);
                    } else {
                        LogHelper.error("No valid potion found for: " + loc);
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                LogHelper.error("Failed to parse config string: " + configStr);
            }
        }
        POTION_ADJUSTMENTS = new Object2DoubleOpenHashMap<>(map);
    }
}
