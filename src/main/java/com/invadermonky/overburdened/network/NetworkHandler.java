package com.invadermonky.overburdened.network;

import com.invadermonky.overburdened.EternallyOverburdened;
import com.invadermonky.overburdened.network.packets.PacketUpdateClientCarryWeight;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE;

    public static void preInit() {
        int id = 0;
        INSTANCE.registerMessage(PacketUpdateClientCarryWeight.class, PacketUpdateClientCarryWeight.class, id++, Side.CLIENT);
    }

    static {
        INSTANCE = new SimpleNetworkWrapper(EternallyOverburdened.MOD_ID);
    }
}
