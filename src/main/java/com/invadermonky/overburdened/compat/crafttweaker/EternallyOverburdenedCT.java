package com.invadermonky.overburdened.compat.crafttweaker;

import com.invadermonky.overburdened.EternallyOverburdened;
import com.invadermonky.overburdened.api.OverburdenedAPI;
import com.invadermonky.overburdened.api.custom.IngredientCustomWeight;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + EternallyOverburdened.MOD_ID + ".Weights")
public class EternallyOverburdenedCT {
    @ZenMethod
    public static void setWeight(IIngredient ingredient, double weight) {
        OverburdenedAPI.registerCustomItemWeight(new IngredientCustomWeight(CraftTweakerMC.getIngredient(ingredient), weight));
    }
}
