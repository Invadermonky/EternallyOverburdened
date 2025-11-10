package com.invadermonky.eternallyoverburdened.potions;

import com.invadermonky.eternallyoverburdened.EternallyOverburdened;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEOPotion extends Potion {
    protected ResourceLocation iconLocation;

    protected AbstractEOPotion(String unlocName, ResourceLocation iconLocation, boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        this.setRegistryName(EternallyOverburdened.MOD_ID, unlocName);
        this.setPotionName(this.getRegistryName().toString());
        this.iconLocation = iconLocation;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(@NotNull PotionEffect effect, @NotNull Gui gui, int x, int y, float z) {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.currentScreen != null) {
            mc.getTextureManager().bindTexture(this.iconLocation);
            Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(@NotNull PotionEffect effect, @NotNull Gui gui, int x, int y, float z, float alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.iconLocation);
        Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
    }
}
