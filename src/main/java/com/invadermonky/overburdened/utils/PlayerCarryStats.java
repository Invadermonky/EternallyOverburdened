package com.invadermonky.overburdened.utils;

import com.invadermonky.overburdened.config.ConfigHandlerEO;
import com.invadermonky.overburdened.event.CarryWeightChangedEvent;
import com.invadermonky.overburdened.utils.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class PlayerCarryStats {
    protected UUID playerId;
    protected double maxCarryWeight;
    protected double currentCarryWeight;

    public PlayerCarryStats(EntityPlayer player) {
        this.playerId = player.getUniqueID();
        this.maxCarryWeight = PlayerHelper.getMaxCarryWeight(player);
        this.currentCarryWeight = PlayerHelper.getCurrentCarryWeight(player);
    }

    public UUID getPlayerUUID() {
        return playerId;
    }

    @Nullable
    public EntityPlayer getEntityPlayer() {
        return PlayerHelper.getEntityPlayer(this.playerId);
    }

    /**
     * Gets the player's calculated maximum carry weight including any adjustments from equipment or effects.
     *
     * @param includeOverburdenedWeight Whether this value should also include the weight over the overburdened limit.
     * @return The maximum carrying capacity of the player.
     */
    public double getMaxCarryWeight(boolean includeOverburdenedWeight) {
        if(includeOverburdenedWeight) {
            return this.maxCarryWeight + ConfigHandlerEO.potionSettings.overburdened.overburdenedThreshold * 4;
        }
        return this.maxCarryWeight;
    }

    /** Sets the player max carry weight. */
    public void setMaxCarryWeight(double maxCarryWeight) {
        this.maxCarryWeight = maxCarryWeight;
    }

    /** Gets the current amount of weight the player is carrying. */
    public double getCurrentCarryWeight() {
        return currentCarryWeight;
    }

    /** Sets the player current carry weight. */
    public void setCurrentCarryWeight(double currentCarryWeight) {
        this.currentCarryWeight = currentCarryWeight;
    }

    /**
     * Gets the remaining weight players can use to hold additional items.
     * @param includeOverburdened whether this value should also include overburdened weight
     * @return The remaining amount of weight a player can carry
     */
    public double getRemainingWeight(boolean includeOverburdened) {
        double max = this.getMaxCarryWeight(includeOverburdened);
        double rem = max - this.currentCarryWeight;
        return rem > 0 ? rem : 0;
    }

    /** Returns if the player is overburdened. */
    public boolean isOverburdened() {
        return this.currentCarryWeight > this.maxCarryWeight;
    }

    /** Updates the player's current and maximum carry weight values. */
    public void updateCarryStats(EntityPlayer player) {
        double newMaxWeight = PlayerHelper.getMaxCarryWeight(player);
        double newCurrWeight = PlayerHelper.getCurrentCarryWeight(player);
        CarryWeightChangedEvent event = new CarryWeightChangedEvent(player, this.maxCarryWeight, this.currentCarryWeight, newMaxWeight, newCurrWeight);
        boolean isCanceled = MinecraftForge.EVENT_BUS.post(event);
        if(!isCanceled) {
            this.maxCarryWeight = event.getNewMaxCarryWeight();
            this.currentCarryWeight = event.getNewCarryWeight();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlayerCarryStats)) return false;
        PlayerCarryStats that = (PlayerCarryStats) object;
        return Objects.equals(getPlayerUUID(), that.getPlayerUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPlayerUUID());
    }
}
