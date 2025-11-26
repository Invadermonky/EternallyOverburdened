package com.invadermonky.overburdened.utils;

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
        if(this.canUpdateMoveDistance()) {
            double distX = this.entityLiving.posX - this.posX;
            double distY = this.entityLiving.posY - this.posY;
            double distZ = this.entityLiving.posZ - this.posZ;
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

    private boolean canUpdateMoveDistance() {
        return this.entityLiving.isEntityAlive()
                && this.entityLiving.onGround
                && !this.entityLiving.isRiding()
                && !this.entityLiving.isElytraFlying();
    }

    private void updateCurrentPosition() {
        this.posX = this.entityLiving.posX;
        this.posY = this.entityLiving.posY;
        this.posZ = this.entityLiving.posZ;
    }

}
