package com.invadermonky.eternallyoverburdened.compat;

import com.invadermonky.eternallyoverburdened.api.OverburdenedAPI;
import com.invadermonky.eternallyoverburdened.api.custom.ShulkerBoxCapabilityHandler;
import com.invadermonky.eternallyoverburdened.compat.actuallyadditions.ItemBagCapabilityHandler;
import com.invadermonky.eternallyoverburdened.compat.immersiveengineering.IECapabilityHandler;
import com.invadermonky.eternallyoverburdened.compat.travelersbackpack.TravelersBackpackCapabilityHandler;
import com.invadermonky.eternallyoverburdened.utils.libs.ModIds;

public class InitCompat {
    public static void registerCustomHandlers() {
        //Minecraft
        OverburdenedAPI.registerCustomCapabilityHandler(new ShulkerBoxCapabilityHandler());
        if (ModIds.actually_additions.isLoaded)
            OverburdenedAPI.registerCustomCapabilityHandler(new ItemBagCapabilityHandler());
        if(ModIds.immersive_engineering.isLoaded)
            OverburdenedAPI.registerCustomCapabilityHandler(new IECapabilityHandler());
        if(ModIds.travelers_backpack.isLoaded)
            OverburdenedAPI.registerCustomCapabilityHandler(new TravelersBackpackCapabilityHandler());
    }
}
