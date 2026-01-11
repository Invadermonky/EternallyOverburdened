package com.invadermonky.overburdened.config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import vazkii.quark.management.entity.EntityChestPassenger;

@Mod.EventBusSubscriber
public class Test {
    @SubscribeEvent
    public static void onEntityLogOut(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (event.getEntity() instanceof EntityPlayer && player.getRidingEntity() instanceof EntityBoat) {
                EntityBoat boat = (EntityBoat) player.getRidingEntity();
                for (Entity passenger : boat.getPassengers()) {
                    if (passenger instanceof EntityChestPassenger) {
                        passenger.setDropItemsWhenDead(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if(player.getRidingEntity() instanceof EntityBoat) {
            EntityBoat boat = (EntityBoat) player.getRidingEntity();
            for(Entity passenger : boat.getPassengers()) {
                if(passenger instanceof EntityChestPassenger) {
                    //passenger.setDropItemsWhenDead(false);
                }
            }
        }
    }

}
