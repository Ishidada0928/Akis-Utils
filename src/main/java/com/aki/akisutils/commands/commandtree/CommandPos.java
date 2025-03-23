package com.aki.akisutils.commands.commandtree;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CommandPos extends CommandBase {
    @Override
    public String getName() {
        return "pos";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.akisutils.pos.usage";
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
        int dim = sender.getEntityWorld().provider.getDimension();
        World world = DimensionManager.getWorld(dim);
        ChunkPos chunkPos = new ChunkPos(sender.getPosition());
        String s = "";
        for(EntityPlayer p : world.playerEntities) {
            s = getAddingString("", "Name: " + p.getName() + ", Display: " + p.getDisplayName().getUnformattedText());
            s = getAddingString(s, "PosX: " + p.posX + " PosY: " + p.posY + " PosZ: " + p.posZ);
            s = getAddingString(s, "ChunkCoordX: " + p.chunkCoordX + " ChunkCoordY: " + p.chunkCoordY + " ChunkCoordZ: " + p.chunkCoordZ);
            s = getAddingString(s, "Chunk: [X:" + chunkPos.x + ", Z:" + chunkPos.z + "]");
            s = getAddingString(s, "DimName: " + sender.getEntityWorld().provider.getDimensionType().getName() + "(ID: " + dim + ")");
            s = getAddingString(s, "Rotation: Yaw: " + p.rotationYaw + ", Pitch: " + p.rotationPitch);
        }
        sender.sendMessage(new TextComponentString(s));
    }

    public String getAddingString(String s1, String s2) {
        return s1 + "\n" + s2;
    }
}
