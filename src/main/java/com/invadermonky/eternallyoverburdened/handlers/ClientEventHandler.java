package com.invadermonky.eternallyoverburdened.handlers;

import com.invadermonky.eternallyoverburdened.EternallyOverburdened;
import com.invadermonky.eternallyoverburdened.config.ConfigHandlerEO;
import com.invadermonky.eternallyoverburdened.config.WeightSettings;
import com.invadermonky.eternallyoverburdened.registry.ModPotionsEO;
import com.invadermonky.eternallyoverburdened.utils.PlayerCarryStats;
import com.invadermonky.eternallyoverburdened.utils.helpers.PlayerHelper;
import com.invadermonky.eternallyoverburdened.utils.helpers.StringHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = EternallyOverburdened.MOD_ID, value = Side.CLIENT)
public class ClientEventHandler {
    public static final ResourceLocation OVERLAY_WIDGETS = new ResourceLocation(EternallyOverburdened.MOD_ID, "textures/gui/widgets.png");
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0#");

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if(player != null) {
            ItemStack stack = event.getItemStack();
            List<String> tooltip = event.getToolTip();
            String units = ConfigHandlerEO.clientSettings.weightUnits;
            double carryWeightAdjustment = WeightSettings.getArmorAdjustment(stack);
            if(carryWeightAdjustment != 0) {
                tooltip.add(StringHelper.getTranslatedString("equipped", "tooltip", "desc"));
                String sign = carryWeightAdjustment > 0 ? "+" : "";
                tooltip.add(" " + TextFormatting.BLUE + I18n.format(StringHelper.getTranslationKey("carry_weight_adjustment", "tooltip", "info"), sign, DECIMAL_FORMAT.format(carryWeightAdjustment), units));
            }
            double stackWeight = WeightSettings.getItemStackWeight(stack);
            tooltip.add(I18n.format(StringHelper.getTranslationKey("stack_weight", "tooltip", "info"), DECIMAL_FORMAT.format(stackWeight), units));
            if (GuiScreen.isShiftKeyDown()) {
                double itemWeight = WeightSettings.getItemWeight(stack);
                double inventoryWeight = WeightSettings.getItemHandlerCapabilityWeight(stack);
                double fluidWeight = WeightSettings.getFluidHandlerCapabilityWeight(stack);

                tooltip.add(" ┠> " + I18n.format(StringHelper.getTranslationKey("item_weight", "tooltip", "info"), DECIMAL_FORMAT.format(itemWeight), units));
                if(inventoryWeight > 0) {
                    tooltip.add(" ┠> " + I18n.format(StringHelper.getTranslationKey("inventory_weight", "tooltip", "info"), DECIMAL_FORMAT.format(inventoryWeight), units));
                }
                if(fluidWeight > 0) {
                    tooltip.add(" ┠> " + I18n.format(StringHelper.getTranslationKey("fluid_weight", "tooltip", "info"), DECIMAL_FORMAT.format(fluidWeight), units));
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onFovUpdate(FOVUpdateEvent event) {
        EntityPlayer player = event.getEntity();
        if(player.isPotionActive(ModPotionsEO.OVERBURDENED) || player.isPotionActive(ModPotionsEO.INJURED)) {
            event.setNewfov(1.0f);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlayPost(RenderGameOverlayEvent.Post event) {
        //TODO: Maybe add an icon that includes negative and positive adjustment effects to the bar.

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        EntityPlayerSP playerSP = mc.player;
        if(playerSP == null)
            return;

        if(!mc.gameSettings.hideGUI && mc.playerController.gameIsSurvivalOrAdventure()) {
            if(event.getType() == RenderGameOverlayEvent.ElementType.ARMOR) {
                renderCarryWeightBar(mc, scaledResolution, playerSP);
            }
        }
    }

    private static void renderCarryWeightBar(Minecraft minecraft, ScaledResolution scaledResolution, EntityPlayer player) {
        PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
        boolean reversed = ConfigHandlerEO.clientSettings.overlayReverseDirection;
        boolean isInWater = !player.isCreative() && player.isInsideOfMaterial(Material.WATER) && ConfigHandlerEO.clientSettings.offsetInWater;
        int height = scaledResolution.getScaledHeight();
        int width = scaledResolution.getScaledWidth();
        long ticks = minecraft.ingameGUI.getUpdateCounter();
        Random rand = new Random();
        rand.setSeed(ticks * 312871);
        minecraft.renderEngine.bindTexture(OVERLAY_WIDGETS);
        GlStateManager.enableBlend();

        for(int i = 0; i < 10; i++) {
            int x = width / 2 + (reversed ? (-i * 8 + 72 + ConfigHandlerEO.clientSettings.overlayXOffset) : (i * 8 + ConfigHandlerEO.clientSettings.overlayXOffset));
            int y = height + ConfigHandlerEO.clientSettings.overlayYOffset;
            if(isInWater) {
                y -= 10;
            }
            if(stats.isOverburdened()) {
                PotionEffect effect = player.getActivePotionEffect(ModPotionsEO.OVERBURDENED);
                if(effect != null) {
                    int shakeTicks = 20 / (effect.getAmplifier() + 1);
                    if (ticks % shakeTicks == 0) {
                        y += rand.nextInt(3) - 1;
                    }
                }
            }
            getOverburdenedBarSprite(stats, i, reversed).drawSprite(minecraft, x, y);
        }
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        minecraft.renderEngine.bindTexture(Gui.ICONS);
    }

    private static WidgetSprite getOverburdenedBarSprite(PlayerCarryStats stats, int currentIndex, boolean reverse) {
        double currentWeight = stats.getCurrentCarryWeight();
        double maxWeight = stats.getMaxCarryWeight(false);
        if(stats.isOverburdened()) {
            double overWeight = currentWeight - maxWeight;
            double maxOverWeight = ConfigHandlerEO.potionSettings.overburdened.overburdenedThreshold * 4;
            int overWeightLevel = (int) Math.ceil(overWeight / maxOverWeight * 10.0);
            return currentIndex <= overWeightLevel ? WidgetSprite.BAG_OVER : WidgetSprite.BAG_FULL;
        } else {
            int weightLevel = (int) (Math.ceil(currentWeight / maxWeight * 20.0));
            int fullIndex = weightLevel / 2 - 1;
            int halfIndex = weightLevel % 2 == 1 ? fullIndex + 1 : -1;
            if (currentIndex <= fullIndex) {
                return WidgetSprite.BAG_FULL;
            } else if (currentIndex == halfIndex) {
                return reverse ? WidgetSprite.BAG_HALF_R : WidgetSprite.BAG_HALF;
            } else {
                return WidgetSprite.BAG_EMPTY;
            }
        }
    }

    private enum WidgetSprite {
        BAG_EMPTY(0,0, 9, 9),
        BAG_HALF(9, 0, 9, 9),
        BAG_HALF_R(18, 0, 9, 9),
        BAG_FULL(27, 0, 9, 9),
        BAG_OVER(36, 0, 9, 9);

        public final int texX;
        public final int texY;
        public final int width;
        public final int height;

        WidgetSprite(int texX, int texY, int width, int height) {
            this.texX = texX;
            this.texY = texY;
            this.width = width;
            this.height = height;
        }

        public void drawSprite(Minecraft minecraft, int x, int y) {
            minecraft.ingameGUI.drawTexturedModalRect(x, y, this.texX, this.texY, this.width, this.height);
        }
    }
}
