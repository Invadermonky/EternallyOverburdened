package com.invadermonky.eternallyoverburdened.handlers;

import com.invadermonky.eternallyoverburdened.EternallyOverburdened;
import com.invadermonky.eternallyoverburdened.config.ConfigHandlerEO;
import com.invadermonky.eternallyoverburdened.config.WeightSettings;
import com.invadermonky.eternallyoverburdened.network.NetworkHandler;
import com.invadermonky.eternallyoverburdened.network.packets.PacketUpdateClientCarryWeight;
import com.invadermonky.eternallyoverburdened.registry.ModPotionsEO;
import com.invadermonky.eternallyoverburdened.utils.PlayerCarryStats;
import com.invadermonky.eternallyoverburdened.utils.helpers.PlayerHelper;
import com.invadermonky.eternallyoverburdened.utils.helpers.StringHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
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
    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!player.world.isRemote) {
                if (player.ticksExisted % ConfigHandlerEO.generalSettings.updateInterval == 0) {
                    PlayerHelper.updatePlayerCarryStats(player);
                    PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
                    NetworkHandler.INSTANCE.sendTo(new PacketUpdateClientCarryWeight(stats), (EntityPlayerMP) player);
                }
                PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
                if (stats.isOverburdened()) {
                    if (player.isSprinting()) {
                        player.setSprinting(false);
                    }
                    if (player.ticksExisted % 10 == 0) {
                        double overLimit = stats.getCurrentCarryWeight() - stats.getMaxCarryWeight(false);
                        int amplifier = Math.min((int) (overLimit / ConfigHandlerEO.potionSettings.overburdened.overburdenedThreshold), 3);
                        if(stats.getCurrentCarryWeight() > stats.getMaxCarryWeight(true)) {
                            amplifier = 4;
                        }
                        PotionEffect effect = player.getActivePotionEffect(ModPotionsEO.OVERBURDENED);
                        if (effect != null && effect.getAmplifier() > amplifier) {
                            player.removePotionEffect(ModPotionsEO.OVERBURDENED);
                        }
                        player.addPotionEffect(new PotionEffect(ModPotionsEO.OVERBURDENED, 215, amplifier, true, false));
                    }
                } else {
                    if (player.isPotionActive(ModPotionsEO.OVERBURDENED)) {
                        player.removePotionEffect(ModPotionsEO.OVERBURDENED);
                    }
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
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
                    int injuryChance = ConfigHandlerEO.potionSettings.overburdened.fallInjuryChance * (1 + effect.getAmplifier());
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

    @SuppressWarnings("ConstantConditions")
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

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            if(entityLiving.isPotionActive(ModPotionsEO.INJURED) && ConfigHandlerEO.potionSettings.injured.restrictJumping) {
                entityLiving.motionY *= 0.7;
                return;
            }
            PotionEffect effect = entityLiving.getActivePotionEffect(ModPotionsEO.OVERBURDENED);
            if(effect != null) {
                int level = effect.getAmplifier();
                if(level > 2) {
                    entityLiving.motionY = 0.0;
                } else if(ConfigHandlerEO.potionSettings.overburdened.restrictJump) {
                    float modifier = ((float) level) * 0.15f + 0.12f;
                    entityLiving.motionY *= (1.0f - modifier);
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
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
        if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
            EntityItem entityItem = event.getItem();
            ItemStack stack = entityItem.getItem();
            double itemWeight = WeightSettings.getSingleItemWeight(stack);
            double stackWeight = itemWeight * stack.getCount();
            double remaining = stats.getRemainingWeight(false);
            if(remaining < stackWeight) {
                if(remaining >= itemWeight) {
                    int toAdd = (int) (remaining / itemWeight);
                    ItemStack copy = stack.copy();
                    copy.setCount(toAdd);

                    if(!player.inventory.addItemStackToInventory(copy)) {
                        if (copy.getCount() != toAdd) {
                            toAdd -= copy.getCount();
                        } else {
                            toAdd = 0;
                        }
                    }
                    if(toAdd > 0) {
                        stack.shrink(toAdd);
                        stats.updateCarryStats(player);
                        NetworkHandler.INSTANCE.sendTo(new PacketUpdateClientCarryWeight(stats), (EntityPlayerMP) player);
                    }
                }
                isCanceled = true;
            }
            if(isCanceled) {
                player.sendStatusMessage(StringHelper.getTranslatedComponent("pickup_failed", "chat"), true);
                event.setCanceled(true);
            }
        }
    }

    /*
     *  Minecraft EntityItem handling went full retard so I'm forced to do this horrid shit to make item clicking work.
     */

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        handleEntityItemInteraction(event);
    }

    private static void handleEntityItemInteraction(PlayerInteractEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        RayTraceResult trace = PlayerHelper.getClosestItemRayTrace(player, false);
        if(trace != null && trace.typeOfHit == RayTraceResult.Type.ENTITY && trace.entityHit instanceof EntityItem) {
            PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
            EntityItem entityItem = (EntityItem) trace.entityHit;
            ItemStack stack = entityItem.getItem();
            Item item = stack.getItem();
            int count = stack.getCount();
            double itemWeight = WeightSettings.getSingleItemWeight(stack);
            double stackWeight = itemWeight * stack.getCount();
            double remaining = stats.getRemainingWeight(true);

            if(stackWeight <= remaining) {
                if(!player.world.isRemote) {
                    if (player.inventory.addItemStackToInventory(stack)) {
                        player.onItemPickup(entityItem, count);
                        player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
                        entityItem.setDead();
                    }
                    if(stack.isEmpty() || stack.getCount() < count) {
                        player.addStat(StatList.getObjectsPickedUpStats(item), count);
                        PlayerHelper.updatePlayerCarryStats(player);
                        NetworkHandler.INSTANCE.sendTo(new PacketUpdateClientCarryWeight(stats), (EntityPlayerMP) player);
                    }
                }
                event.setCancellationResult(EnumActionResult.SUCCESS);
                if(event.isCancelable()) {
                    event.setCanceled(true);
                }
            } else if(remaining >= itemWeight) {
                if(!player.world.isRemote) {
                    if (remaining >= itemWeight) {
                        int toAdd = (int) (remaining / itemWeight);
                        ItemStack copy = stack.copy();
                        copy.setCount(toAdd);
                        if (!player.inventory.addItemStackToInventory(copy)) {
                            if (copy.getCount() < toAdd) {
                                toAdd -= copy.getCount();
                            } else {
                                toAdd = 0;
                            }
                        }
                        if (toAdd > 0) {
                            stack.shrink(toAdd);
                            player.addStat(StatList.getObjectsPickedUpStats(item), count);
                            PlayerHelper.updatePlayerCarryStats(player);
                            NetworkHandler.INSTANCE.sendTo(new PacketUpdateClientCarryWeight(stats), (EntityPlayerMP) player);
                        }
                    }
                }
            } else {
                player.sendStatusMessage(StringHelper.getTranslatedComponent("overburdened_pickup_failed", "chat"), true);
            }

            if(event.isCancelable()) {
                event.setCancellationResult(EnumActionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
