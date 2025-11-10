package com.invadermonky.eternallyoverburdened;

import com.invadermonky.eternallyoverburdened.config.ConfigTags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = EternallyOverburdened.MOD_ID,
        name = EternallyOverburdened.MOD_NAME,
        version = EternallyOverburdened.MOD_VERSION
)
public class EternallyOverburdened {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = Tags.VERSION;

    @Mod.Instance
    public static EternallyOverburdened instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ConfigTags.syncConfig();
    }

}
