package com.invadermonky.eternallyoverburdened.utils;

import com.invadermonky.eternallyoverburdened.utils.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;

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

    public EntityPlayer getEntityPlayer() {
        return PlayerHelper.getEntityPlayer(this.playerId);
    }

    public void setPlayer(EntityPlayer player) {
        this.playerId = player.getUniqueID();
    }

    public double getMaxCarryWeight() {
        return maxCarryWeight;
    }

    public double getCurrentCarryWeight() {
        return currentCarryWeight;
    }

    public double getCurrentOverLimitWeight() {
        return this.isOverburdened() ? this.currentCarryWeight - this.maxCarryWeight : 0.0;
    }

    public boolean isOverburdened() {
        return this.currentCarryWeight > this.maxCarryWeight;
    }

    public void updateCarryStats(EntityPlayer player) {
        this.maxCarryWeight = PlayerHelper.getMaxCarryWeight(player);
        this.currentCarryWeight = PlayerHelper.getCurrentCarryWeight(player);
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
