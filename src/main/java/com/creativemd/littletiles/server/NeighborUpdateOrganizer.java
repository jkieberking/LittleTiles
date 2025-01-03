//package com.creativemd.littletiles.server;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map.Entry;
//
//import com.creativemd.creativecore.common.packet.PacketHandler;
//
//import com.creativemd.littletiles.common.type.HashMapListProxy;
//import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldServer;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.world.WorldEvent;
//
//public class NeighborUpdateOrganizer {
//
//    private HashMapListProxy<World, BlockPos> positions = new HashMapListProxy<>();
//
//    public NeighborUpdateOrganizer() {
//        MinecraftForge.EVENT_BUS.register(this);
//    }
//
//    public void add(World world, BlockPos pos) {
//        if (world instanceof IOrientatedWorld)
//            return;
//        if (!positions.contains(world, pos))
//            positions.add(world, pos);
//    }
//
//    @SubscribeEvent
//    public void tick(ServerTickEvent event) {
//        if (event.phase == Phase.END) {
//            for (Entry<World, ArrayList<BlockPos>> entry : positions.entrySet()) {
//                World world = entry.getKey();
//                if (world instanceof WorldServer) {
//                    HashMapList<ChunkPos, BlockPos> chunks = new HashMapList<>();
//                    for (BlockPos pos : entry.getValue())
//                        chunks.add(new ChunkPos(pos), pos);
//
//                    for (EntityPlayer player : world.playerEntities) {
//                        List<BlockPos> collected = new ArrayList<>();
//                        for (Entry<ChunkPos, ArrayList<BlockPos>> chunk : chunks.entrySet()) {
//                            if (((WorldServer) world).getPlayerChunkMap().isPlayerWatchingChunk((EntityPlayerMP) player, chunk.getKey().x, chunk.getKey().z))
//                                collected.addAll(chunk.getValue());
//                        }
//
//                        if (!collected.isEmpty())
//                            PacketHandler.sendPacketToPlayer(new LittleNeighborUpdatePacket(world, collected), (EntityPlayerMP) player);
//                    }
//
//                } else if (world instanceof SubWorld)
//                    PacketHandler.sendPacketToTrackingPlayers(new LittleNeighborUpdatePacket(world, entry.getValue()), ((SubWorld) world).parent, null);
//            }
//
//            positions.clear();
//        }
//    }
//
//    @SubscribeEvent
//    public void unload(WorldEvent.Unload event) {
//        positions.removeKey(event.getWorld());
//    }
//
//}
