package com.aki.akisutils.commands.commandtree;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandKillNotPlayer extends CommandBase {

    @Override
    public String getName() {
        return "npkill";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.akisutils.npkill.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        List<Entity> entities = getEntityList(server, sender, "@e[type=!player]");
        int Count = 0;
        int MaxCount = entities.size();
        for(Entity entity : entities) {
            if(entity != null) {
                Count++;
                entity.onKillCommand();
                notifyCommandListener(sender, this, "commands.kill.successful", new Object[]{entity.getDisplayName()});
            }
        }
        sender.sendMessage(new TextComponentString("Kill to: " + Count + "/" + MaxCount + " Entities"));
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
