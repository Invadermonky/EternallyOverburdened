package com.invadermonky.eternallyoverburdened.api;

import com.invadermonky.eternallyoverburdened.api.custom.ICustomCapabilityHandler;
import com.invadermonky.eternallyoverburdened.api.custom.ICustomItemWeight;
import com.invadermonky.eternallyoverburdened.config.WeightSettings;
import com.invadermonky.eternallyoverburdened.utils.helpers.StringHelper;
import net.minecraft.item.ItemStack;

public class OverburdenedAPI {
    /**
     * Returns the formatted decimal weight value as a string.
     *
     * @param weight The decimal weight
     * @param includeUnits whether the returned string should also include the weight units
     * @return A formatted weight string equivalent to the passed decimal value
     */
    public static String getFormatedWeight(double weight, boolean includeUnits) {
        return includeUnits ? StringHelper.getFormattedWeight(weight) : StringHelper.DECIMAL_FORMAT.format(weight);
    }

    /**
     * Returns the default configured item weight ignoring any custom weight values. Use this method to avoid circular
     * logic when retriving weights for {@link ICustomItemWeight} handlers.
     * <p>
     * <b>IMPORTANT</b> <u>DO NOT</u> attempt to access this method prior to Eternally Overburdened's FMLPostInit
     * phase. Doing so will result in a crash.
     */
    public static double getItemWeight(ItemStack stack) {
        return WeightSettings.getDefaultItemWeight(stack);
    }

    /**
     * Returns the default calculated single item weight, including any capability weight, but ignoring any custom
     * item weight values. Use this method to avoid circular logic when retriving weights for {@link ICustomItemWeight}
     * handlers.
     * <p>
     * <b>IMPORTANT</b> <u>DO NOT</u> attempt to access this method prior to Eternally Overburdened's FMLPostInit
     * phase. Doing so will result in a crash.
     */
    public static double getSingleItemWeight(ItemStack stack) {
        return WeightSettings.getDefaultSingleItemWeight(stack);
    }

    /**
     *
     * <b>IMPORTANT</b> <u>DO NOT</u> attempt to access this method prior to Eternally Overburdened's FMLPostInit
     * phase. Doing so will result in a crash.
     */
    public static double getItemStackWeight(ItemStack stack) {
        return WeightSettings.getDefaultItemStackWeight(stack);
    }

    /**
     * Returns an item's item handler capability weight. This value includes custom item weight and custom capability
     * handler values.
     * <p>
     * <b>IMPORTANT</b> <u>DO NOT</u> attempt to access this method prior to Eternally Overburdened's FMLPostInit
     * phase. Doing so will result in a crash.
     */
    public static double getItemHandlerCapabilityWeight(ItemStack stack) {
        ICustomCapabilityHandler customHandler = WeightSettings.getCustomCapabilityHandler(stack);
        return WeightSettings.getItemHandlerCapabilityWeight(stack, customHandler);
    }

    /**
     * Returns an item's fluid handler capability weight. This value includes custom item weight and custom capability
     * handler values.
     * <p>
     * <b>IMPORTANT</b> <u>DO NOT</u> attempt to access this method prior to Eternally Overburdened's FMLPostInit
     * phase. Doing so will result in a crash.
     */
    public static double getFluidHandlerCapabilityWeight(ItemStack stack) {
        ICustomCapabilityHandler customHandler = WeightSettings.getCustomCapabilityHandler(stack);
        return WeightSettings.getFluidHandlerCapabilityWeight(stack, customHandler);
    }

    /**
     * Registers a custom item weight handler. See {@link ICustomItemWeight} for more information.
     */
    public static void registerCustomItemWeight(ICustomItemWeight customWeight) {
        WeightSettings.registerCustomItemWeight(customWeight);
    }

    /**
     * Registers a custom capability handler. See {@link ICustomCapabilityHandler} for more information.
     */
    public static void registerCustomCapabilityHandler(ICustomCapabilityHandler customHandler) {
        WeightSettings.registerCustomCapabilityHandler(customHandler);
    }
}
