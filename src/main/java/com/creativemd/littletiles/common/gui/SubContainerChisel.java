package com.creativemd.littletiles.common.gui;

public class SubContainerChisel {

//    public SubContainerChisel(EntityPlayer player) {
//        super(player);
//    }

//    @Override
//    public void createControls() {
//        // TODO Auto-generated method stub
//
//    }

//    @Override
//    public void onGuiPacket(int controlID, NBTTagCompound nbt, EntityPlayer player) {
//        if (controlID == 0) {
//
//            int firstX = nbt.getInteger("x1");
//            int firstY = nbt.getInteger("y1");
//            int firstZ = nbt.getInteger("z1");
//            int secX = nbt.getInteger("x2");
//            int secY = nbt.getInteger("y2");
//            int secZ = nbt.getInteger("z2");
//            int minX = Math.min(firstX, secX);
//            int maxX = Math.max(firstX, secX);
//            int minY = Math.min(firstY, secY);
//            int maxY = Math.max(firstY, secY);
//            int minZ = Math.min(firstZ, secZ);
//            int maxZ = Math.max(firstZ, secZ);
//
//            boolean colorize = nbt.hasKey("color");
//            int color = nbt.getInteger("color");
//
//            Block filter = null;
//            int meta = -1;
//            if (nbt.hasKey("filterBlock")) {
//                filter = Block.getBlockFromName(nbt.getString("filterBlock"));
//                if (nbt.hasKey("filterMeta")) meta = nbt.getInteger("filterMeta");
//            }
//
//            Block replacement = null;
//            int metaReplacement = -1;
//            if (nbt.hasKey("replaceBlock")) {
//                replacement = Block.getBlockFromName(nbt.getString("replaceBlock"));
//                if (nbt.hasKey("replaceMeta")) metaReplacement = nbt.getInteger("replaceMeta");
//            }
//
//            int effected = 0;
//
//            for (int posX = minX; posX <= maxX; posX++) {
//                for (int posY = minY; posY <= maxY; posY++) {
//                    for (int posZ = minZ; posZ <= maxZ; posZ++) {
//                        TileEntity tileEntity = player.worldObj.getTileEntity(posX, posY, posZ);
//                        boolean hasChanged = false;
//                        if (tileEntity instanceof TileEntityLittleTilesProxy) {
//                            TileEntityLittleTilesProxy littleEntity = (TileEntityLittleTilesProxy) tileEntity;
//                            TileList tiles = littleEntity.getTiles();
//                            for (int i = 0; i < tiles.size(); i++) {
//                                LittleTile tile = tiles.get(i);
//                                boolean shouldEffect = tile.getClass() == LittleTile.class
//                                        || tile instanceof LittleTileColored;
//                                if (filter != null) {
//                                    if (((LittleTile) tile).block != filter) shouldEffect = false;
//                                    if (meta != -1 && ((LittleTile) tile).meta != meta) shouldEffect = false;
//                                }
//
//                                if (shouldEffect) {
//                                    hasChanged = true;
//
//                                    if (replacement != null) {
//                                        ((LittleTile) tile).block = replacement;
//                                        if (metaReplacement != -1) ((LittleTile) tile).meta = metaReplacement;
//                                        littleEntity.needFullUpdate = true;
//                                    }
//
//                                    if (colorize) {
//                                        LittleTile newTile = LittleTileColored
//                                                .setColor((LittleTile) tile, color);
//
//                                        if (newTile != null) tiles.set(i, newTile);
//                                    }
//                                    effected++;
//                                }
//                            }
//                            if (hasChanged) littleEntity.update();
//                        }
//                    }
//                }
//            }
//            player.addChatMessage(new ChatComponentText("Done! Effected " + effected + " tiles!"));
//        }
//    }

}
