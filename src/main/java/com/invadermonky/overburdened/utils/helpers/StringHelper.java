package com.invadermonky.overburdened.utils.helpers;

import com.invadermonky.overburdened.EternallyOverburdened;
import com.invadermonky.overburdened.config.ConfigHandlerEO;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

import java.text.DecimalFormat;

public class StringHelper {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0#");

    public static String getTranslationKey(String unloc, String type, String... params) {
        StringBuilder str = new StringBuilder(type + "." + EternallyOverburdened.MOD_ID + ":" + unloc);
        for (String param : params) {
            str.append(".").append(param);
        }
        return str.toString();
    }

    public static String getTranslatedString(String unloc, String type, String... params) {
        return I18n.format(getTranslationKey(unloc, type, params));
    }

    public static String getTranslatedString(String translationKey, Object... params) {
        return I18n.format(translationKey, params);
    }

    public static ITextComponent getTranslatedComponent(String unloc, String type, String... params) {
        return new TextComponentTranslation(getTranslationKey(unloc, type, params));
    }

    public static String getDimensionName(int dimensionId) {
        if (!DimensionManager.isDimensionRegistered(dimensionId)) {
            return Integer.toString(dimensionId);
        }
        DimensionType type = DimensionManager.getProviderType(dimensionId);
        if (type == null) {
            return Integer.toString(dimensionId);
        }
        String name = type.getName();
        int[] dims = DimensionManager.getDimensions(type);
        if (dims != null && dims.length > 1) {
            name += " " + dimensionId;
        }
        return name;
    }

    public static String getFormattedWeight(double weight) {
        return DECIMAL_FORMAT.format(weight) + " " + ConfigHandlerEO.clientSettings.weightUnits;
    }
}
