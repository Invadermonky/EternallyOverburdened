package com.invadermonky.eternallyoverburdened.network.packets;

import com.invadermonky.eternallyoverburdened.utils.PlayerCarryStats;
import com.invadermonky.eternallyoverburdened.utils.helpers.PlayerHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUpdateClientCarryWeight implements IMessage, IMessageHandler<PacketUpdateClientCarryWeight, IMessage> {
    private double currentCarryWeight;
    private double maxCarryWeight;

    public PacketUpdateClientCarryWeight(PlayerCarryStats stats) {
        this.currentCarryWeight = stats.getCurrentCarryWeight();
        this.maxCarryWeight = stats.getMaxCarryWeight(false);
    }

    public PacketUpdateClientCarryWeight() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        this.currentCarryWeight = buf.readDouble();
        this.maxCarryWeight = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.currentCarryWeight);
        buf.writeDouble(this.maxCarryWeight);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketUpdateClientCarryWeight message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            EntityPlayer player = Minecraft.getMinecraft().player;
            PlayerCarryStats stats = PlayerHelper.getPlayerCarryStats(player);
            stats.setCurrentCarryWeight(message.currentCarryWeight);
            stats.setMaxCarryWeight(message.maxCarryWeight);
        });
        return null;
    }
}
