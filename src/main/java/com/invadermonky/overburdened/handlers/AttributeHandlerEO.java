package com.invadermonky.overburdened.handlers;

import com.invadermonky.overburdened.EternallyOverburdened;
import com.invadermonky.overburdened.config.ConfigHandlerEO;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = EternallyOverburdened.MOD_ID)
public class AttributeHandlerEO {
    public static final UUID CARRY_WEIGHT_UUID = UUID.fromString("6d8a8016-8d36-475c-b8c9-8239e83fce7b");
    public static final IAttribute CARRY_WEIGHT = new RangedAttribute(null,
            EternallyOverburdened.MOD_ID + ":carry_weight",
            ConfigHandlerEO.playerSettings.maxCarryWeight, 0, Double.MAX_VALUE)
            .setDescription("Carry Weight")
            .setShouldWatch(true);

    @SubscribeEvent
    public static void onEntityConstruction(EntityEvent.EntityConstructing event) {
        if(event.getEntity() instanceof EntityPlayer) {
            registerCarryWeight((EntityPlayer) event.getEntity());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if(event.isWasDeath() && !event.isCanceled()) {
            EntityPlayer original = event.getOriginal();
            IAttributeInstance originalCarryWeight = original.getEntityAttribute(CARRY_WEIGHT);
            if(originalCarryWeight != null) {
                setBaseCarryWeight(event.getEntityPlayer(), originalCarryWeight.getBaseValue());
            }
        }
    }

    public static double getCarryWeight(EntityPlayer player) {
        IAttributeInstance attributeInstance = player.getEntityAttribute(CARRY_WEIGHT);
        return attributeInstance != null ? attributeInstance.getAttributeValue() : ConfigHandlerEO.playerSettings.maxCarryWeight;
    }

    private static void registerCarryWeight(EntityPlayer player) {
        setBaseCarryWeight(player, getCarryWeight(player));
    }

    private static void setBaseCarryWeight(EntityPlayer player, double baseValue) {
        IAttributeInstance attributeInstance = player.getEntityAttribute(CARRY_WEIGHT);
        if(attributeInstance == null) {
            player.getAttributeMap().registerAttribute(CARRY_WEIGHT).setBaseValue(baseValue);
        } else {
            attributeInstance.setBaseValue(baseValue);
        }
    }
}
