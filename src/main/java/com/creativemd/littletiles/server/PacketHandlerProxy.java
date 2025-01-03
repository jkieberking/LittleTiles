package com.creativemd.littletiles.server;

// this class is in creative core
public class PacketHandlerProxy {

//    private static ArrayList<CreativeSplittedMessageHandler> queuedMessages = new ArrayList<>();
//
//    public static void addQueueMessage(CreativeSplittedMessageHandler message) {
//        queuedMessages.add(message);
//    }
//
//    private static void sendQueuedMessage(EntityPlayer player) {
//        for (int i = 0; i < queuedMessages.size(); i++) {
//            sendMessage(queuedMessages.get(i).type, player, queuedMessages.get(i));
//        }
//        queuedMessages.clear();
//    }

//    public static void sendMessage(TrayIcon.MessageType type, EntityPlayer player, IMessage message) {
//        switch (type) {
//        case ToAllPlayer:
//            CreativeCore.network.sendToAll(message);
//            break;
//        case ToPlayer:
//            CreativeCore.network.sendTo(message, (EntityPlayerMP) player);
//            break;
//        case ToServer:
//            CreativeCore.network.sendToServer(message);
//            break;
//        }
//    }
//
//    public static void sendPacketToAllPlayers(CreativeCorePacket packet) {
//        CreativeCore.network.sendToAll(new CreativeMessageHandler(packet, MessageType.ToAllPlayer, null));
//        sendQueuedMessage(null);
//    }
//
//    public static void sendPacketToServer(CreativeCorePacket packet) {
//        CreativeCore.network.sendToServer(new CreativeMessageHandler(packet, MessageType.ToServer, null));
//        sendQueuedMessage(null);
//    }
//
//    public static void sendPacketsToAllPlayers(List<CreativeCorePacket> packets) {
//        for (int i = 0; i < packets.size(); i++) {
//            sendPacketToAllPlayers(packets.get(i));
//        }
//    }
//
//    public static void sendPacketToNearPlayers(World world, CreativeCorePacket packet, int distance, BlockPos pos) {
//        for (EntityPlayerMP entityplayermp : world.getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>() {
//            @Override
//            public boolean apply(@Nullable EntityPlayerMP p_apply_1_) {
//                return p_apply_1_.getDistanceSq(pos) < Math.pow(distance, 2);
//            }
//        })) {
//            sendPacketToPlayer(packet, entityplayermp);
//        }
//    }
//
//    public static void sendPacketToPlayer(CreativeCorePacket packet, EntityPlayerMP player) {
//        CreativeCore.network.sendTo(new CreativeMessageHandler(packet, MessageType.ToPlayer, player), player);
//        sendQueuedMessage(player);
//    }
//
//    public static void sendPacketToPlayers(CreativeCorePacket packet, Iterable<? extends EntityPlayer> players) {
//        for (EntityPlayer player : players) {
//            sendPacketToPlayer(packet, (EntityPlayerMP) player);
//        }
//    }
//
//    public static void sendPacketToPlayers(CreativeCorePacket packet, Iterable<? extends EntityPlayer> players, @Nullable Predicate<EntityPlayer> predicate) {
//        for (EntityPlayer player : players)
//            if (predicate == null || predicate.apply(player))
//                sendPacketToPlayer(packet, (EntityPlayerMP) player);
//    }
//
//    public static void sendPacketToTrackingPlayers(CreativeCorePacket packet, World world, BlockPos pos, @Nullable Predicate<EntityPlayer> predicate) {
//        if (world instanceof IOrientatedWorld) {
//            sendPacketToTrackingPlayers(packet, ((IOrientatedWorld) world).getTopEntity(), predicate);
//        } else {
//            try {
//                PlayerChunkMapEntry entry = ((WorldServer) world).getPlayerChunkMap().getEntry(pos.getX() >> 4, pos.getZ() >> 4);
//                if (entry != null)
//                    sendPacketToPlayers(packet, entry.getWatchingPlayers(), predicate);
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void sendPacketToTrackingPlayers(CreativeCorePacket packet, Entity entity, @Nullable Predicate<EntityPlayer> predicate) {
//        World world = entity.world;
//        if (world instanceof IOrientatedWorld)
//            sendPacketToTrackingPlayers(packet, ((IOrientatedWorld) world).getTopEntity(), predicate);
//        else
//            for (EntityPlayer player : ((WorldServer) world).getEntityTracker().getTrackingPlayers(entity))
//                if (predicate == null || predicate.apply(player))
//                    sendPacketToPlayer(packet, (EntityPlayerMP) player);
//    }
//
//    public static void sendPacketToTrackingPlayers(CreativeCorePacket packet, EntityPlayerMP player) {
//        Set<? extends EntityPlayer> players = player.getServerWorld().getEntityTracker().getTrackingPlayers(player);
//        for (Iterator iterator = players.iterator(); iterator.hasNext();) {
//            EntityPlayer entityPlayer = (EntityPlayer) iterator.next();
//            sendPacketToPlayer(packet, (EntityPlayerMP) entityPlayer);
//        }
//    }

}
