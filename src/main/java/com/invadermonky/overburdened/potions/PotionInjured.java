package com.invadermonky.overburdened.potions;

import com.invadermonky.overburdened.EternallyOverburdened;
import com.invadermonky.overburdened.config.ConfigHandlerEO;
import com.invadermonky.overburdened.registry.ModItemsEO;
import com.invadermonky.overburdened.utils.MoveDistanceHolder;
import com.invadermonky.overburdened.utils.helpers.StringHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PotionInjured extends AbstractEOPotion {
    public static final ResourceLocation ICON = new ResourceLocation(EternallyOverburdened.MOD_ID, "textures/potions/injured.png");
    public static final UUID INJURED_UUID = UUID.fromString("ef3fe858-fb10-4491-a212-07e59c3ed548");
    private static final Map<UUID, MoveDistanceHolder> MOVE_DISTANCE = new HashMap<>();

    public PotionInjured() {
        super("injured", ICON, true, 0x0);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, INJURED_UUID.toString(), -0.6, Constants.AttributeModifierOperation.MULTIPLY);
    }

    @Override
    public void performEffect(@NotNull EntityLivingBase entityLiving, int amplifier) {
        if(!entityLiving.world.isRemote) {
            MoveDistanceHolder holder = this.getDistanceHolder(entityLiving);
            holder.updateDistanceMoved();
            if (holder.getDistanceMoved() >= ConfigHandlerEO.potionSettings.injured.injuryDamageDistance) {
                int chance = ConfigHandlerEO.potionSettings.injured.injuryDamageChance;
                if (chance > 0 && entityLiving.world.rand.nextInt(100) < chance) {
                    entityLiving.attackEntityFrom(DamageSource.GENERIC, 1.0f);
                    PotionEffect effect = entityLiving.getActivePotionEffect(this);
                    if(effect != null) {
                        entityLiving.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration() + 300, effect.getAmplifier(), true, false));
                    }
                    if(entityLiving instanceof EntityPlayer) {
                        ((EntityPlayer) entityLiving).sendStatusMessage(StringHelper.getTranslatedComponent("injured_increased", "chat"), true);
                    }
                }
                holder.resetDistanceMoved();
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull List<ItemStack> getCurativeItems() {
        return Collections.singletonList(new ItemStack(ModItemsEO.SPLINT));
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
