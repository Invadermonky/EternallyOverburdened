package com.invadermonky.overburdened;

import com.invadermonky.overburdened.command.CommandCarryWeight;
import com.invadermonky.overburdened.compat.InitCompat;
import com.invadermonky.overburdened.config.WeightSettings;
import com.invadermonky.overburdened.network.NetworkHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

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
        NetworkHandler.preInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        InitCompat.registerCustomHandlers();
        WeightSettings.syncConfig();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandCarryWeight());
    }
}
