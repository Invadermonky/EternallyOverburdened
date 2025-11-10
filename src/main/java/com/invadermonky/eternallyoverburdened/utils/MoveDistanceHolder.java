package com.invadermonky.eternallyoverburdened.utils;

import net.minecraft.entity.EntityLivingBase;

public class MoveDistanceHolder {
    private final EntityLivingBase entityLiving;
    private double posX;
    private double posY;
    private double posZ;
    private double distMoved;

    public MoveDistanceHolder(EntityLivingBase entityLiving) {
        this.entityLiving = entityLiving;
        this.updateCurrentPosition();
    }

    public double getDistanceMoved() {
        return this.distMoved;
    }

    public void updateDistanceMoved() {
        double distance = 0;
        if(entityLiving.isEntityAlive() && !entityLiving.isRiding()) {
            double distX = entityLiving.posX - this.posX;
            double distY = entityLiving.posY - this.posY;
            double distZ = entityLiving.posZ - this.posZ;
            distance = Math.sqrt((distX * distX) + (distY * distY) + (distZ * distZ));
            distance = distance <= 8.0 ? distance : 0;
        }
        this.distMoved += distance;
        this.updateCurrentPosition();
    }

    public void resetDistanceMoved() {
        this.updateCurrentPosition();
        this.distMoved = 0;
    }

    private void updateCurrentPosition() {
        this.posX = entityLiving.posX;
        this.posY = entityLiving.posY;
        this.posZ = entityLiving.posZ;
    }

}
