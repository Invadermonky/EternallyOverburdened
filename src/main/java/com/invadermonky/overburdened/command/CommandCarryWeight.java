package com.invadermonky.overburdened.command;

import com.invadermonky.overburdened.config.ConfigHandlerEO;
import com.invadermonky.overburdened.handlers.AttributeHandlerEO;
import com.invadermonky.overburdened.utils.helpers.StringHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CommandCarryWeight extends CommandBase {
    @Override
    public @NotNull String getName() {
        return "overburdened";
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return StringHelper.getTranslatedComponent("usage", "command").getUnformattedText();
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args) throws CommandException {
        if(args.length < 2 || args.length > 4) {
            throw new CommandException(StringHelper.getTranslationKey("invalid", "command"));
        }

        //Format:
        //  0 - player
        //  1 - action
        //  2 - amount
        //  3 - send message

        EntityPlayer player = getPlayer(server, sender, args[0]);
        IAttributeInstance attributeInstance = player.getEntityAttribute(AttributeHandlerEO.CARRY_WEIGHT);
        if(attributeInstance == null) {
            throw new CommandException(StringHelper.getTranslationKey("fatal", "command"), player.getName());
        }

        String action = args[1];
        if(action.equals("get")) {
            sender.sendMessage(new TextComponentTranslation(StringHelper.getTranslationKey("carry_weight", "command", "get"), StringHelper.DECIMAL_FORMAT.format(attributeInstance.getBaseValue())));
            return;
        }

        double carryWeight = attributeInstance.getBaseValue();
        switch (action) {
            case "set":
                carryWeight = parseDouble(args[2]);
                break;
            case "add":
                carryWeight += parseDouble(args[2]);
                break;
            case "mult":
                carryWeight *= parseDouble(args[2]);
                break;
            case "reset":
                carryWeight = ConfigHandlerEO.playerSettings.maxCarryWeight;
                break;
            default:
                throw new CommandException(StringHelper.getTranslationKey("unknown_action", "command"), action);
        }
        attributeInstance.setBaseValue(carryWeight);
        this.sendMessage(sender, carryWeight, action);
    }

    public void sendMessage(ICommandSender sender, double carryWeight, String action) {
        sender.sendMessage(new TextComponentTranslation(StringHelper.getTranslationKey("carry_weight", "command", action), StringHelper.DECIMAL_FORMAT.format(carryWeight)));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("eo");
    }

    @Override
    public @NotNull List<String> getTabCompletions(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if(args.length == 2) {
            return getListOfStringsMatchingLastWord(args, "get", "set", "add", "mult", "reset");
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public boolean isUsernameIndex(String @NotNull [] args, int index) {
        return index == 0;
    }
}
