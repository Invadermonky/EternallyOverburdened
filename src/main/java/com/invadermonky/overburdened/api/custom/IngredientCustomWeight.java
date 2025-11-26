package com.invadermonky.overburdened.api.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * A custom weight class used to define ingredient item weights. Only used for scripting
 * mods such as CraftTweaker or GroovyScript.
 * <p>
 * Use the configuration weight handler over this class whenever possible.
 */
public class IngredientCustomWeight implements ICustomItemWeight {
    private final Ingredient ingredient;
    private final double weight;

    public IngredientCustomWeight(Ingredient ingredient, double weight) {
        this.ingredient = ingredient;
        this.weight = weight;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return ingredient.apply(stack);
    }

    @Override
    public double getItemWeight(ItemStack stack) {
        return this.weight;
    }

    @Override
    public double getSingleItemWeight(ItemStack stack) {
        return this.getItemWeight(stack);
    }
}
