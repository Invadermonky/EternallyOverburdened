package com.invadermonky.eternallyoverburdened.compat.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.helper.Alias;
import com.cleanroommc.groovyscript.registry.NamedRegistry;
import com.invadermonky.eternallyoverburdened.EternallyOverburdened;
import com.invadermonky.eternallyoverburdened.api.OverburdenedAPI;
import com.invadermonky.eternallyoverburdened.api.custom.IngredientCustomWeight;

@RegistryDescription(
        linkGenerator = EternallyOverburdened.MOD_ID,
        reloadability = RegistryDescription.Reloadability.DISABLED
)
public class WeightsGS extends NamedRegistry {
    @GroovyBlacklist
    public WeightsGS() {
        super(Alias.generateOf("weights"));
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION, example = @Example("item('minecraft:stone'), 1.0"))
    public void setWeight(IIngredient ingredient, double weight) {
        OverburdenedAPI.registerCustomItemWeight(new IngredientCustomWeight(ingredient.toMcIngredient(), weight));
    }
}
