package com.aki.akisutils.commands.commandtree;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandOreDict extends CommandBase {

    @Override
    public String getName() {
        return "ore_dict";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.akisutils.ore_dict.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer entityplayer = null;
        if(args.length > 0) {
            entityplayer = getPlayer(server, sender, args[0]);
            ItemStack stack = entityplayer.getHeldItemMainhand();
            String DictName = "";

            b:for(String s : OreDictionary.getOreNames()) {
                for(ItemStack stack1 : OreDictionary.getOres(s)) {
                    if(stack1.getItem().getRegistryName() == stack.getItem().getRegistryName()) {
                        DictName = s;
                        break b;
                    }
                }
            }
            if(DictName != "") {
                entityplayer.sendMessage(new TextComponentString("OreDictItem in: " + stack.getDisplayName() + ",\n --DictName: " + DictName));
            } else {
                entityplayer.sendMessage(new TextComponentString("NoOreDictItem in: " + stack.getDisplayName()));
            }
        } else if(sender.getCommandSenderEntity() instanceof EntityPlayer) {
            entityplayer = (EntityPlayer) sender.getCommandSenderEntity();
            ItemStack stack = entityplayer.getHeldItemMainhand();
            String DictName = "";

            b:for(String s : OreDictionary.getOreNames()) {
                for(ItemStack stack1 : OreDictionary.getOres(s)) {
                    if(stack1.getItem().getRegistryName() == stack.getItem().getRegistryName()) {
                        DictName = s;
                        break b;
                    }
                }
            }
            if(DictName != "") {
                entityplayer.sendMessage(new TextComponentString("OreDictItem in: " + stack.getDisplayName() + ",\n --DictName: " + DictName));
            } else {
                entityplayer.sendMessage(new TextComponentString("NoOreDictItem in: " + stack.getDisplayName()));
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return Collections.emptyList();
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
