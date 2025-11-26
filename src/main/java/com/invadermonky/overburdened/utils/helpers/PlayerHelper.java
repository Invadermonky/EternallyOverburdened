package com.invadermonky.overburdened.utils.helpers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.overburdened.config.WeightSettings;
import com.invadermonky.overburdened.handlers.AttributeHandlerEO;
import com.invadermonky.overburdened.utils.PlayerCarryStats;
import com.invadermonky.overburdened.utils.libs.ModIds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerHelper {
    /**
     * A map to hold calculated player carrying weights. This map will hold the values so other functions
     * can access the weight amounts without having to recalculate the value every time.
     */
    private static final Map<UUID, PlayerCarryStats> PLAYER_CARRY_STATS = new HashMap<>();

    public static PlayerCarryStats getPlayerCarryStats(EntityPlayer player) {
        if(!PLAYER_CARRY_STATS.containsKey(player.getUniqueID())) {
            updatePlayerCarryStats(player);
        }
        return PLAYER_CARRY_STATS.get(player.getUniqueID());
    }

    public static void updatePlayerCarryStats(EntityPlayer player) {
        if(!PLAYER_CARRY_STATS.containsKey(player.getUniqueID())) {
            PLAYER_CARRY_STATS.put(player.getUniqueID(), new PlayerCarryStats(player));
        } else {
            PLAYER_CARRY_STATS.get(player.getUniqueID()).updateCarryStats(player);
        }
    }

    public static double getMaxCarryWeight(EntityPlayer player) {
        double carryWeight = AttributeHandlerEO.getCarryWeight(player);
        for(ItemStack stack : player.getArmorInventoryList()) {
            carryWeight += WeightSettings.getArmorAdjustment(stack, true);
        }

        if(ModIds.baubles.isLoaded) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                carryWeight += WeightSettings.getArmorAdjustment(stack, true);
            }
        }
        //TODO: Maybe gamestage support?

        for(PotionEffect effect : player.getActivePotionEffects()) {
            carryWeight += WeightSettings.getPotionAdjustment(effect);
        }

        return carryWeight;
    }

    public static double getCurrentCarryWeight(EntityPlayer player) {
        double weight = 0;
        if(player.isCreative()) {
            return weight;
        }
        //Mouse Item weight
        weight += WeightSettings.getItemStackWeight(player.inventory.getItemStack());
        //Carry weight
        for(int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
            ItemStack slotStack = player.inventory.getStackInSlot(slot);
            weight += WeightSettings.getItemStackWeight(slotStack);
        }
        //Baubles Weight
        if(ModIds.baubles.isLoaded) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                weight += WeightSettings.getItemStackWeight(stack);
            }
        }
        return weight;
    }

    @Nullable
    public static EntityPlayer getEntityPlayer(UUID playerId) {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? null : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId);
    }

    @Nullable
    public static RayTraceResult getClosestItemRayTrace(EntityPlayer player, boolean useLiquids) {
        World world = player.world;
        double reach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d origin = player.getPositionEyes(1.0f);
        RayTraceResult result = rayTrace(world, player, reach, useLiquids);
        if(result != null) {
            Vec3d endPoint = result.hitVec;
            AxisAlignedBB searchVolume = new AxisAlignedBB(origin.x, origin.y, origin.z, endPoint.x, endPoint.y, endPoint.z);
            List<EntityItem> entityItems = world.getEntitiesWithinAABB(EntityItem.class, searchVolume);

            Entity closestHitEntity = null;
            Vec3d closestHitPosition = endPoint;
            AxisAlignedBB entityBounds;
            Vec3d intercept = null;

            for(EntityItem entityItem : entityItems) {
                entityBounds = entityItem.getEntityBoundingBox();
                if(entityBounds != null) {
                    float entityBorderSize = entityItem.getCollisionBorderSize();
                    if (entityBorderSize != 0) {
                        entityBounds = entityBounds.grow(entityBorderSize, entityBorderSize, entityBorderSize);
                    }

                    RayTraceResult hit = entityBounds.calculateIntercept(origin, endPoint);
                    if (hit != null) {
                        intercept = hit.hitVec;
                    }
                }

                if (intercept != null) {
                    float currentHitDistance = (float) intercept.distanceTo(origin);
                    float closestHitDistance = (float) closestHitPosition.distanceTo(origin);
                    if (currentHitDistance < closestHitDistance) {
                        closestHitEntity = entityItem;
                        closestHitPosition = intercept;
                    }
                }
            }
            if (closestHitEntity != null) {
                result = new RayTraceResult(closestHitEntity, closestHitPosition);
                result.typeOfHit = RayTraceResult.Type.ENTITY;
            }
        }
        return result;
    }

    public static RayTraceResult rayTrace(World world, EntityPlayer player, double reach, boolean useLiquids) {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        double posX = player.posX;
        double posY = player.posY + player.eyeHeight;
        double posZ = player.posZ;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3d vec3d1 = vec3d.add((double)f6 * reach, (double)f5 * reach, (double)f7 * reach);
        return world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
    }


}
