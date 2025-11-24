package com.invadermonky.eternallyoverburdened.compat.groovyscript;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;
import com.invadermonky.eternallyoverburdened.EternallyOverburdened;

public class GSLinkGenerator extends BasicLinkGenerator {
    @Override
    public String id() {
        return EternallyOverburdened.MOD_ID;
    }

    @Override
    protected String domain() {
        return "https://github.com/Invadermonky/EternallyOverburdened/";
    }

    @Override
    protected String version() {
        return EternallyOverburdened.MOD_VERSION;
    }
}
