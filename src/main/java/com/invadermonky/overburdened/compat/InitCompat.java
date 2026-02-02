package com.invadermonky.overburdened.compat;

import com.invadermonky.overburdened.api.OverburdenedAPI;
import com.invadermonky.overburdened.api.custom.ShulkerBoxCapabilityHandler;
import com.invadermonky.overburdened.compat.actuallyadditions.ItemBagCapabilityHandler;
import com.invadermonky.overburdened.compat.hbm.HBMCapabilityHandler;
import com.invadermonky.overburdened.compat.immersiveengineering.IECapabilityHandler;
import com.invadermonky.overburdened.compat.ironchests.IronChestCapabilityHandler;
import com.invadermonky.overburdened.compat.travelersbackpack.TravelersBackpackCapabilityHandler;
import com.invadermonky.overburdened.utils.libs.ModIds;

public class InitCompat {
    public static void registerCustomHandlers() {
        //Minecraft
        OverburdenedAPI.registerCustomCapabilityHandler(new ShulkerBoxCapabilityHandler());
        if (ModIds.actually_additions.isLoaded)
            OverburdenedAPI.registerCustomCapabilityHandler(new ItemBagCapabilityHandler());
        if (ModIds.hbm_nuclear.isLoaded)
            OverburdenedAPI.registerCustomCapabilityHandler(new HBMCapabilityHandler());
        if (ModIds.immersive_engineering.isLoaded)
            OverburdenedAPI.registerCustomCapabilityHandler(new IECapabilityHandler());
        if (ModIds.iron_chests.isLoaded)
            OverburdenedAPI.registerCustomCapabilityHandler(new IronChestCapabilityHandler());
        if (ModIds.travelers_backpack.isLoaded)
            OverburdenedAPI.registerCustomCapabilityHandler(new TravelersBackpackCapabilityHandler());
    }
}
