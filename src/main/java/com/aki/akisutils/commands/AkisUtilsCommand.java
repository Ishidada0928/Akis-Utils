package com.aki.akisutils.commands;

import com.aki.akisutils.commands.commandtree.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class AkisUtilsCommand extends CommandTreeBase {
    public AkisUtilsCommand()
    {
        super.addSubcommand(new CommandKillNotPlayer());
        super.addSubcommand(new CommandPos());
        super.addSubcommand(new CommandWorldInfo());
        //super.addSubcommand(new CommandGetDebugItems());
        super.addSubcommand(new CommandOreDict());
        super.addSubcommand(new CommandTick());
        super.addSubcommand(new CommandChunk());
        super.addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName()
    {
        return "akisutils";
    }

    /*@Override
    public void addSubcommand(ICommand command)
    {
        throw new UnsupportedOperationException("Don't add sub-commands to /mcu, create your own command.");
    }*/

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "commands.akisutils.usage";
    }
}
