package com.invadermonky.eternallyoverburdened.compat.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public class GSContainer extends GroovyPropertyContainer {
    public final WeightsGS WEIGHTS = new WeightsGS();

    public GSContainer() {
        this.addProperty(WEIGHTS);
    }
}
