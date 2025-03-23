package com.aki.akisutils.commands.commandtree;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;

public class CommandWorldInfo extends CommandBase {
    @Override
    public String getName() {
        return "world_info";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.akisutils.world_info.usage";
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
        s = "";
        s = getAddingString("","---------World info----------------");
        s = getAddingString(s, "WorldName: " + sender.getEntityWorld().getWorldInfo().getWorldName() + ", World Seed: " + world.provider.getSeed());
        s = getAddingString(s, "WorldTime: " + sender.getEntityWorld().getWorldInfo().getWorldTime() + ", TotalTime" + world.getTotalWorldTime());
        s = getAddingString(s, "WorldSaveFolder: " + world.provider.getSaveFolder());
        s = getAddingString(s, "SpawnPoint: " + world.provider.getSpawnPoint().toString() + ", SpawnCoordinate: " + world.provider.getSpawnCoordinate());
        s = getAddingString(s, "Dimension: " + world.provider.getDimensionType().getName() + ", (Id: " + dim + ")");
        s = getAddingString(s, "WorldType: " + world.getWorldType().getName() + ", Id: " + world.getWorldType().getId() + ", Version: " + world.getWorldType().getVersion());
        s = getAddingString(s, "LoadedEntities_Size: " + world.loadedEntityList.size());
        s = getAddingString(s, "LoadedTileEntities_Size: " + world.loadedTileEntityList.size());
        s = getAddingString(s, "PlayerEntities: " + new ArrayList<>(world.playerEntities));
        sender.sendMessage(new TextComponentString(s));
    }

    public String getAddingString(String s1, String s2) {
        return s1 + "\n" + s2;
    }
}
