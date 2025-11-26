package com.invadermonky.overburdened.config;

import com.invadermonky.overburdened.api.custom.ICustomCapabilityHandler;
import com.invadermonky.overburdened.api.custom.ICustomItemWeight;
import com.invadermonky.overburdened.registry.ModEnchantsEO;
import com.invadermonky.overburdened.utils.ItemHolder;
import com.invadermonky.overburdened.utils.helpers.LogHelper;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
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
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeightSettings {
    private static final Set<ResourceLocation> CAPABILITY_BLACKLIST = new HashSet<>();
    private static final Set<ICustomItemWeight> CUSTOM_ITEM_WEIGHTS = new HashSet<>();
    private static final Set<ICustomCapabilityHandler> CUSTOM_CAPABILITY_HANDLERS = new HashSet<>();
    private static Object2DoubleMap<ItemHolder> ITEM_WEIGHTS;
    private static Object2DoubleMap<Fluid> FLUID_WEIGHTS;
    private static Object2DoubleMap<ItemHolder> ARMOR_ADJUSTMENTS;
    private static Object2DoubleMap<Enchantment> ENCHANTMENT_ADJUSTMENTS;
    private static Object2DoubleMap<Potion> POTION_ADJUSTMENTS;

    //##################################################
    //  Default Item Weights (No custom handling)
    //##################################################

    public static double getDefaultItemWeight(ItemStack stack) {
        if(!stack.isEmpty()) {
            ItemHolder stackHolder = new ItemHolder(stack);
            if(ITEM_WEIGHTS.containsKey(stackHolder) || ITEM_WEIGHTS.containsKey(stackHolder.setMetaData(ItemHolder.WILDCARD_VALUE))) {
                return ITEM_WEIGHTS.get(stackHolder);
            } else {
                return ConfigHandlerEO.itemSettings.defaultItemWeight;
            }
        }
        return 0;
    }

    public static double getDefaultSingleItemWeight(ItemStack stack) {
        double weight = 0;
        ICustomCapabilityHandler customHandler = getCustomCapabilityHandler(stack);
        weight += getItemWeight(stack);
        weight += getFluidHandlerCapabilityWeight(stack, customHandler);
        weight += getItemHandlerCapabilityWeight(stack, customHandler);
        return weight;
    }

    public static double getDefaultItemStackWeight(ItemStack stack) {
        return getDefaultSingleItemWeight(stack) * stack.getCount();
    }

    //##################################################
    //  Item Weights (Includes custom handling)
    //##################################################

    public static double getItemWeight(ItemStack stack) {
        if(!stack.isEmpty()) {
            ICustomItemWeight customWeight = getCustomItemWeight(stack);
            if(customWeight != null) {
                return customWeight.getItemWeight(stack);
            }
        }
        return getDefaultItemWeight(stack);
    }

    public static double getSingleItemWeight(ItemStack stack) {
        if(!stack.isEmpty()) {
            ICustomItemWeight customWeight = getCustomItemWeight(stack);
            if(customWeight != null) {
                return customWeight.getSingleItemWeight(stack);
            } else {
                return getDefaultSingleItemWeight(stack);
            }
        }
        return 0;
    }

    public static double getItemStackWeight(ItemStack stack) {
        if(!stack.isEmpty()) {
            ICustomItemWeight customWeight = getCustomItemWeight(stack);
            if(customWeight != null) {
                return customWeight.getItemStackWeight(stack);
            } else {
                return getSingleItemWeight(stack) * stack.getCount();
            }
        }
        return 0;
    }

    //##################################################
    //  Fluid Weights
    //##################################################

    public static double getFluidWeight(@Nullable Fluid fluid) {
        return fluid != null ? FLUID_WEIGHTS.getOrDefault(fluid, ConfigHandlerEO.itemSettings.defaultFluidWeight) : 0;
    }

    public static double getFluidStackWeight(@Nullable FluidStack fluidStack) {
        return fluidStack != null ? getFluidWeight(fluidStack.getFluid()) * fluidStack.amount : 0;
    }

    //##################################################
    //  Capability Weights
    //##################################################

    public static double getItemHandlerCapabilityWeight(ItemStack stack, @Nullable ICustomCapabilityHandler customHandler) {
        double weight = 0;
        if(!CAPABILITY_BLACKLIST.contains(stack.getItem().getRegistryName())) {
            if(customHandler != null) {
                NonNullList<ItemStack> contents = customHandler.getContainedItems(stack);
                for(ItemStack invStack : contents) {
                    weight += getItemStackWeight(invStack);
                }
            } else if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack slotStack = handler.getStackInSlot(i);
                        weight += getItemStackWeight(slotStack);
                    }
                }
            }
        }
        return weight;
    }

    public static double getFluidHandlerCapabilityWeight(ItemStack stack, @Nullable ICustomCapabilityHandler customHandler) {
        double weight = 0;
        if(!CAPABILITY_BLACKLIST.contains(stack.getItem().getRegistryName())) {
            if(customHandler != null) {
                List<FluidStack> fluidStacks = customHandler.getContainedFluids(stack);
                for(FluidStack fluidStack : fluidStacks) {
                    weight += getFluidStackWeight(fluidStack);
                }
            } else if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                if (handler != null) {
                    for (IFluidTankProperties props : handler.getTankProperties()) {
                        weight += getFluidStackWeight(props.getContents());
                    }
                }
            }
        }
        return weight;
    }

    //##################################################
    //  Custom Handlers
    //##################################################

    @Nullable
    public static ICustomItemWeight getCustomItemWeight(ItemStack stack) {
        return CUSTOM_ITEM_WEIGHTS.stream().filter(customWeight -> customWeight.matches(stack)).findFirst().orElse(null);
    }

    public static void registerCustomItemWeight(ICustomItemWeight customWeight) {
        CUSTOM_ITEM_WEIGHTS.add(customWeight);
    }

    @Nullable
    public static ICustomCapabilityHandler getCustomCapabilityHandler(ItemStack stack) {
        return CUSTOM_CAPABILITY_HANDLERS.stream().filter(customHandler -> customHandler.matches(stack)).findFirst().orElse(null);
    }

    public static void registerCustomCapabilityHandler(ICustomCapabilityHandler customHandler) {
        CUSTOM_CAPABILITY_HANDLERS.add(customHandler);
    }

    //##################################################
    //  Carry Weight Adjustments
    //##################################################

    public static double getArmorAdjustment(ItemStack stack, boolean includeEnchants) {
        double adjustment = 0;
        if(!stack.isEmpty()) {
            ItemHolder stackHolder = new ItemHolder(stack);
            if(ARMOR_ADJUSTMENTS.containsKey(stackHolder) || ARMOR_ADJUSTMENTS.containsKey(stackHolder.setMetaData(ItemHolder.WILDCARD_VALUE))) {
                adjustment += ARMOR_ADJUSTMENTS.get(stackHolder);
            }
            if(includeEnchants) {
                adjustment += getEnchantmentAdjustments(stack);
            }
        }
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
        return getEnchantmentAdjustment(enchantment) * (double) level;
    }

    public static double getEnchantmentAdjustment(Enchantment enchantment) {
        return ENCHANTMENT_ADJUSTMENTS.getOrDefault(enchantment, 0.0);
    }

    public static double getPotionAdjustment(PotionEffect effect) {
        return getPotionAdjustment(effect.getPotion()) * (double) effect.getAmplifier();
    }

    public static double getPotionAdjustment(Potion potion) {
        return POTION_ADJUSTMENTS.getOrDefault(potion, 0.0);
    }

    //##################################################
    //  Configuration Handling
    //##################################################

    public static void syncConfig() {
        parseCapabilityBlacklist();
        parseItemWeights();
        parseFluidWeights();
        parseEquipmentAdjustments();
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
        Map<ItemHolder, Double> map = new HashMap<>();
        Pattern configPattern = Pattern.compile("^([^=]+?)=(-?\\d*\\.?\\d*)$");
        Pattern itemPattern = Pattern.compile("^(.+?:.+?):?(-?\\d*)$");
        Pattern orePattern = Pattern.compile("^(\\w+)$");
        for(String configStr : ConfigHandlerEO.itemSettings.itemWeights) {
            try {
                Matcher configMatcher = configPattern.matcher(configStr);
                if (configMatcher.find()) {
                    double weight = Double.parseDouble(configMatcher.group(2));
                    Matcher matcher = itemPattern.matcher(configMatcher.group(1));
                    if(matcher.find()) {
                        ResourceLocation loc = new ResourceLocation(matcher.group(1));
                        if(!matcher.group(2).isEmpty()) {
                            int meta = Integer.parseInt(matcher.group(2));
                            map.put(new ItemHolder(loc, meta), weight);
                        } else {
                            map.put(new ItemHolder(loc), weight);
                        }
                        continue;
                    } else {
                        matcher = orePattern.matcher(configMatcher.group(1));
                        if(matcher.find()) {
                            String oreDict = matcher.group(1);
                            for(ItemStack stack : OreDictionary.getOres(oreDict)) {
                                map.put(new ItemHolder(stack.getItem().getRegistryName(), stack.getMetadata()), weight);
                            }
                            continue;
                        }
                    }
                }
                throw new IllegalArgumentException();
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

    private static void parseEquipmentAdjustments() {
        Map<ItemHolder, Double> map = new HashMap<>();
        Pattern pattern = Pattern.compile("^(.+?:.+?):?(-?\\d*)=(-?\\d*\\.?\\d*)$");
        for(String configStr : ConfigHandlerEO.itemSettings.equipmentAdjustments) {
            try {
                Matcher matcher = pattern.matcher(configStr);
                if (matcher.find()) {
                    ResourceLocation loc = new ResourceLocation(matcher.group(1));
                    double weight = Double.parseDouble(matcher.group(3));
                    if(!matcher.group(2).isEmpty()) {
                        int meta = Integer.parseInt(matcher.group(2));
                        map.put(new ItemHolder(loc, meta), weight);
                    } else {
                        map.put(new ItemHolder(loc), weight);
                    }
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
