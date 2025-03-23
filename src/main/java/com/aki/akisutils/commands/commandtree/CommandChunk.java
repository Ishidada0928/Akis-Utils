package com.aki.akisutils.commands.commandtree;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;

public class CommandChunk extends CommandBase {
    @Override
    public String getName() {
        return "chunk";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.akisutils.chunk.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    /**
     * Stepを実装してもいいかも
     * */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
            Entity entity = sender.getCommandSenderEntity();
            if(entity != null) {
                sender.sendMessage(new TextComponentString("chunk [regenerate]"));
                sender.sendMessage(new TextComponentString("chunk [regenerate] [pos_x] [pos_z]"));
                /*sender.sendMessage(new TextComponentString("chunk [save]"));
                sender.sendMessage(new TextComponentString("chunk [save] [pos_x] [pos_z]"));
                sender.sendMessage(new TextComponentString("chunk [load]"));
                sender.sendMessage(new TextComponentString("chunk [load] [pos_x] [pos_z]"));
                sender.sendMessage(new TextComponentString("chunk [load] [file_name + '.json']"));*/
            }
        } else {
            String first = args[0];
            World world = sender.getEntityWorld();
            switch (first) {
                case "regenerate":
                    IChunkProvider provider = server.getEntityWorld().getChunkProvider();
                    if(args.length == 3) {
                        try {
                            BlockPos pos = sender.getPosition();
                            int posX = (int)parseDouble(pos.getX(), args[1], true) >> 4;
                            int posZ = (int)parseDouble(pos.getZ(), args[2], true) >> 4;
                            Chunk chunk = world.getChunk(posX, posZ);
                            if(provider instanceof ChunkProviderServer && world instanceof WorldServer) {
                                PlayerChunkMap chunkMap = ((WorldServer) world).playerChunkMap;
                                PlayerChunkMapEntry entry = chunkMap.getEntry(posX, posZ);
                                if(entry != null)
                                    chunkMap.removeEntry(entry);
                                chunk.onUnload();
                                ((ChunkProviderServer) provider).loadedChunks.remove(ChunkPos.asLong(posX, posZ));
                                chunk = ((ChunkProviderServer) provider).chunkGenerator.generateChunk(posX, posZ);
                                chunk.onLoad();
                                chunk.populate(provider, ((ChunkProviderServer) provider).chunkGenerator);
                                ((ChunkProviderServer) provider).loadedChunks.put(ChunkPos.asLong(posX, posZ), chunk);
                                chunkMap.getOrCreateEntry(posX, posZ);
                                sender.sendMessage(new TextComponentString("Chunk Regenerate Done").setStyle(new Style().setColor(TextFormatting.AQUA)));
                            } else {
                                sender.sendMessage(new TextComponentString("Unknown ChunkProviderServer Provider: " + provider).setStyle(new Style().setColor(TextFormatting.RED)));
                            }
                        } catch (NumberFormatException ignored) {
                            sender.sendMessage(new TextComponentString("Unknown error").setStyle(new Style().setColor(TextFormatting.RED)));
                        }
                    } else if(args.length == 1) {

                    }
            }
        }
    }
}
