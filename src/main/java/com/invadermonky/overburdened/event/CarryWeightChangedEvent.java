package com.invadermonky.overburdened.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * CarryWeightChangedEvent is fired whenever the payer carry weight is updated.
 * This can be on a living update or when the player picks up an item.
 * <p>
 * This event is only fired on the server side.
 * <p>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}
 * <p>
 * This event is cancellable.
 * This event does not have an {@link Event.Result}.
 *
 */
public class CarryWeightChangedEvent extends PlayerEvent {
    private final double oldMaxCarryWeight;
    private final double oldCarryWeight;
    private double newMaxCarryWeight;
    private double newCarryWeight;

    public CarryWeightChangedEvent(EntityPlayer player, double oldMax, double oldWeight, double newMax, double newWeight) {
        super(player);
        this.oldMaxCarryWeight = oldMax;
        this.oldCarryWeight = oldWeight;
        this.newMaxCarryWeight = newMax;
        this.newCarryWeight = newWeight;
    }

    /**
     * Returns the old maximum player carry weight value
     */
    public double getOldMaxCarryWeight() {
        return oldMaxCarryWeight;
    }

    /**
     * Returns the old player carry weight value.
     */
    public double getOldCarryWeight() {
        return oldCarryWeight;
    }

    /**
     * Returns the newly calculated player maximum carry weight.
     */
    public double getNewMaxCarryWeight() {
        return newMaxCarryWeight;
    }

    /**
     * Sets the new player maximum carry weight value.
     */
    public void setNewMaxCarryWeight(double newMaxCarryWeight) {
        this.newMaxCarryWeight = newMaxCarryWeight;
    }

    /**
     * Gets the calculated new player carry weight value.
     */
    public double getNewCarryWeight() {
        return newCarryWeight;
    }

    /**
     * Sets the new player carry weight value.
     */
    public void setNewCarryWeight(double newCarryWeight) {
        this.newCarryWeight = newCarryWeight;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
