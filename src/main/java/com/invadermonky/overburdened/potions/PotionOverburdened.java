package com.invadermonky.overburdened.potions;

import com.invadermonky.overburdened.EternallyOverburdened;
import com.invadermonky.overburdened.config.ConfigHandlerEO;
import com.invadermonky.overburdened.registry.ModPotionsEO;
import com.invadermonky.overburdened.utils.MoveDistanceHolder;
import com.invadermonky.overburdened.utils.helpers.StringHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PotionOverburdened extends AbstractEOPotion {
    public static final ResourceLocation ICON = new ResourceLocation(EternallyOverburdened.MOD_ID, "textures/potions/overburdened.png");
    public static final UUID OVERBURDENED_UUID = UUID.fromString("3ce2db81-089b-4448-b55a-02d13c43fadb");
    private static final Map<UUID, MoveDistanceHolder> MOVE_DISTANCE = new HashMap<>();

    public PotionOverburdened() {
        super("overburdened", ICON, true, 0x0);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, OVERBURDENED_UUID.toString(), -0.15, Constants.AttributeModifierOperation.MULTIPLY);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void performEffect(@NotNull EntityLivingBase entityLiving, int amplifier) {
        if(!entityLiving.world.isRemote) {
            MoveDistanceHolder holder = this.getDistanceHolder(entityLiving);
            if (amplifier >= 2) {
                holder.updateDistanceMoved();
                if (holder.getDistanceMoved() >= ConfigHandlerEO.potionSettings.overburdened.movementInjuryDistance) {
                    int chance = ConfigHandlerEO.potionSettings.overburdened.movementInjuryChance;
                    if (chance > 0) {
                        chance *= (amplifier - 1);
                        if (chance > 0 && entityLiving.world.rand.nextInt(100) < chance) {
                            String messageKey = entityLiving.isPotionActive(ModPotionsEO.INJURED) ? "injured_again" : "injured";
                            entityLiving.addPotionEffect(new PotionEffect(ModPotionsEO.INJURED, 9600, 0, true, false));
                            entityLiving.attackEntityFrom(DamageSource.GENERIC, 1.0f);
                            if (entityLiving instanceof EntityPlayer) {
                                ((EntityPlayer) entityLiving).sendStatusMessage(StringHelper.getTranslatedComponent(messageKey, "chat"), true);
                            }
                        }
                        holder.resetDistanceMoved();
                    }
                }
            } else {
                holder.resetDistanceMoved();
            }
        }
        if(!entityLiving.onGround && entityLiving.isInWater()) {
            entityLiving.addVelocity(0, -(0.01 + (amplifier * 0.007)), 0);
        }
    }

    @Override
    public double getAttributeModifierAmount(int amplifier, @NotNull AttributeModifier modifier) {
        return amplifier < 4 ? super.getAttributeModifierAmount(amplifier, modifier) : -1.0;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public @NotNull List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }

    private MoveDistanceHolder getDistanceHolder(EntityLivingBase entityLiving) {
        if (!MOVE_DISTANCE.containsKey(entityLiving.getUniqueID())) {
            MoveDistanceHolder holder = new MoveDistanceHolder(entityLiving);
            MOVE_DISTANCE.put(entityLiving.getUniqueID(), holder);
            return holder;
        }
        return MOVE_DISTANCE.get(entityLiving.getUniqueID());
    }
}
