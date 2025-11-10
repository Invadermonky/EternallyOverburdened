package com.invadermonky.eternallyoverburdened.handlers;

import com.invadermonky.eternallyoverburdened.EternallyOverburdened;
import com.invadermonky.eternallyoverburdened.config.ConfigHandlerEO;
import com.invadermonky.eternallyoverburdened.config.ConfigTags;
import com.invadermonky.eternallyoverburdened.registry.ModPotionsEO;
import com.invadermonky.eternallyoverburdened.utils.PlayerCarryStats;
import com.invadermonky.eternallyoverburdened.utils.helpers.PlayerHelper;
import com.invadermonky.eternallyoverburdened.utils.helpers.StringHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = EternallyOverburdened.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if(player.ticksExisted % ConfigHandlerEO.settings.updateInterval == 0) {
                PlayerHelper.updatePlayerCarryStats(player);
            }
            PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
            if(stats.isOverburdened()) {
                if (player.isSprinting()) {
                    player.setSprinting(false);
                }
                if (player.ticksExisted % 10 == 0) {
                    double overLimit = stats.getCurrentOverLimitWeight();
                    int amplifier = Math.min((int) (overLimit / ConfigHandlerEO.potions.overburdened.overburdenedThreshold), 3);
                    PotionEffect effect = player.getActivePotionEffect(ModPotionsEO.OVERBURDENED);
                    if(effect != null && effect.getAmplifier() > amplifier) {
                        player.removePotionEffect(ModPotionsEO.OVERBURDENED);
                    }
                    player.addPotionEffect(new PotionEffect(ModPotionsEO.OVERBURDENED, 215, amplifier, true, false));
                }
            } else {
                if(player.isPotionActive(ModPotionsEO.OVERBURDENED)) {
                    player.removePotionEffect(ModPotionsEO.OVERBURDENED);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamaged(LivingDamageEvent event) {
        if(event.getSource() == DamageSource.FALL) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            float damage = event.getAmount();
            if(entityLiving.isPotionActive(ModPotionsEO.INJURED) && damage >= 2.0f) {
                entityLiving.addPotionEffect(new PotionEffect(ModPotionsEO.INJURED, 9600, 0, true, false));
                if(entityLiving instanceof EntityPlayer) {
                    ((EntityPlayer) entityLiving).sendStatusMessage(StringHelper.getTranslatedComponent("injured_again", "chat"), true);
                }
            } else {
                PotionEffect effect = entityLiving.getActivePotionEffect(ModPotionsEO.OVERBURDENED);
                if (effect != null) {
                    int injuryChance = ConfigHandlerEO.potions.overburdened.fallInjuryChance * (1 + effect.getAmplifier());
                    if (damage >= 2.0f) {
                        injuryChance += (int) ((damage - 1.0f) * 5);
                    }
                    if (entityLiving.world.rand.nextInt(100) < injuryChance) {
                        entityLiving.addPotionEffect(new PotionEffect(ModPotionsEO.INJURED, 9600, 0, true, false));
                        if(entityLiving instanceof EntityPlayer) {
                            ((EntityPlayer) entityLiving).sendStatusMessage(StringHelper.getTranslatedComponent("injured", "chat"), true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if(event.getEntityLiving() instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            float distance = event.getDistance();
            if(distance > 1.0f) {
                PotionEffect effect = entityLiving.getActivePotionEffect(ModPotionsEO.OVERBURDENED);
                if (effect != null) {
                    float distanceAddition = Math.min((0.8f * effect.getAmplifier()), 2.8f);
                    event.setDistance(distance + distanceAddition);
                } else if(entityLiving.isPotionActive(ModPotionsEO.INJURED)) {
                    event.setDistance(distance + 2.8f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            if(entityLiving.isPotionActive(ModPotionsEO.INJURED) && ConfigHandlerEO.potions.injured.restrictJumping) {
                entityLiving.motionY *= 0.7;
                return;
            }
            PotionEffect effect = entityLiving.getActivePotionEffect(ModPotionsEO.OVERBURDENED);
            if(effect != null) {
                int level = effect.getAmplifier();
                if(level > 2) {
                    entityLiving.motionY = 0.0;
                } else if(ConfigHandlerEO.potions.overburdened.restrictJump) {
                    float modifier = ((float) level) / 3.0f * 0.7f;
                    entityLiving.motionY *= (1.0f - modifier);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event) {
        if(event.getEntityLiving() instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            PotionEffect effect = entityLiving.getActivePotionEffect(ModPotionsEO.OVERBURDENED);
            if(effect != null) {
                float modifier = ((float) effect.getAmplifier() + 1.0f) / 4.0f;
                event.setStrength(event.getStrength() * (1.0f - modifier));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onItemPickup(EntityItemPickupEvent event) {
        boolean isCanceled = false;
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
            EntityItem entityItem = event.getItem();
            ItemStack stack = entityItem.getItem();
            int count = stack.getCount();
            double remaining = stats.getMaxCarryWeight() - stats.getCurrentCarryWeight();
            double stackWeight = ConfigTags.getItemStackWeight(stack);
            if(stackWeight > remaining) {
                if (tryToPickupItem(player, entityItem, remaining)) {
                    if (stack.isEmpty()) {
                        player.onItemPickup(entityItem, count - stack.getCount());
                    } else {
                        isCanceled = true;
                    }
                    PlayerHelper.updatePlayerCarryStats(player);
                } else {
                    isCanceled = true;
                }
            }

            if(isCanceled) {
                player.sendStatusMessage(StringHelper.getTranslatedComponent("pickup_failed", "chat"), true);
                event.setCanceled(true);
            }
        }
    }

    public static void attributeTest() {

    }

    /*
     *  Minecraft EntityItem handling went full retard so I'm forced to do this horrid shit to make item clicking work.
     */

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        handleEntityItemInteraction(event);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        handleEntityItemInteraction(event);
    }

    private static void handleEntityItemInteraction(PlayerInteractEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        RayTraceResult trace = PlayerHelper.getClosestItemRayTrace(player.world, player, false);
        if(trace != null && trace.typeOfHit == RayTraceResult.Type.ENTITY && trace.entityHit instanceof EntityItem) {
            PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
            double currentWeight = stats.getCurrentCarryWeight();
            double maxWeight = stats.getMaxCarryWeight();
            if(currentWeight >= maxWeight) {
                //The maximum amount of weight a player can carry at all
                double maxOverWeight = maxWeight + (ConfigHandlerEO.potions.overburdened.overburdenedThreshold * 4.0);
                double remainingWeight = maxOverWeight - currentWeight;
                EntityItem entityItem = (EntityItem) trace.entityHit;
                ItemStack stack = entityItem.getItem();
                Item item = stack.getItem();
                int count = stack.getCount();
                if(tryToPickupItem(player, entityItem, remainingWeight)) {
                    if(stack.isEmpty()) {
                        player.onItemPickup(entityItem, count);
                    } else {
                        count -= entityItem.getItem().getCount();
                    }
                    player.addStat(StatList.getObjectsPickedUpStats(item), count);
                    PlayerHelper.updatePlayerCarryStats(player);
                    event.setCancellationResult(EnumActionResult.SUCCESS);
                    if(event.isCancelable()) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private static boolean tryToPickupItem(EntityPlayer player, EntityItem entityItem, double remainingWeight) {
        ItemStack stack = entityItem.getItem();
        double stackWeight = ConfigTags.getItemStackWeight(stack);
        if(stackWeight <= remainingWeight) {
            if(player.inventory.addItemStackToInventory(stack)) {
                entityItem.setDead();
            }
            return true;
        } else {
            ItemStack copy = stack.copy();
            int count = copy.getCount();
            double itemWeight = ConfigTags.getItemWeight(stack)
                    + ConfigTags.getItemHandlerCapabilityWeight(stack)
                    + ConfigTags.getFluidHandlerCapabilityWeight(stack);
            if(remainingWeight >= itemWeight) {
                int maxInsert = (int) (remainingWeight / itemWeight);
                copy.setCount(maxInsert);
                if(!player.inventory.addItemStackToInventory(copy)) {
                    maxInsert -= copy.getCount();
                }
                stack.setCount(count - maxInsert);
                return true;
            }
        }
        return false;
    }
}
