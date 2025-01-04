package com.creativemd.littletiles.common.tileentity;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.parent.ParentTileList;
import com.creativemd.littletiles.common.tile.registry.LittleTileRegistry;
import com.creativemd.littletiles.common.utils.grid.IGridBased;
import cpw.mods.fml.relauncher.SideOnly;
import com.creativemd.creativecore.common.utils.CubeObject;
import com.creativemd.littletiles.common.parent.IParentTileList;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.type.PairProxy;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.small.LittleTileBox;
import com.creativemd.littletiles.common.utils.small.LittleTileVec;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.creativemd.littletiles.client.render.LittleBlockVertex;
import com.creativemd.littletiles.utils.TileList;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileEntityLittleTilesProxy extends TileEntityCreativeProxy implements IGridBased {

    protected final TileEntityInteractor interactor = new TileEntityInteractor();
    protected TileList tiles = new TileList();
    private boolean hasLoaded = false;
    protected LittleGridContext context = LittleGridContext.getMin();

    public TileEntityLittleTilesProxy(World world, int meta) {
        this.setWorldObj(world);

        this.init();
    }

    public void setLoaded() {
        hasLoaded = true;
    }

    public void setTiles(TileList tiles) {
        this.tiles = tiles;
//        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) updateCustomRenderer();
    }

    private void init() {
        tiles = new TileList(this);
    }


    public int tilesCount() {
        return tiles.size();
    }

    public TileList getTiles() {
        return tiles;
    }

    public ArrayList<LittleTile> customRenderingTiles = new ArrayList<>();

    public boolean needsRenderingUpdate;

    public int lightValue;

    public ArrayList<LittleBlockVertex> lastRendered;

    public boolean isRendering;

    public boolean needFullRenderUpdate;

    public void markFullRenderUpdate() {
        this.needFullRenderUpdate = true;
//        updateRender();
    }

    //    @Override
    public LittleGridContext getContext() {
        return context;
    }

    public boolean needFullUpdate = false;

    public boolean removeTile(LittleTile tile) {
        boolean result = tiles.remove(tile);
        updateTiles();
        return result;
    }

    public void addTiles(ArrayList<LittleTile> tiles) {
        this.tiles.addAll(tiles);
        updateTiles();
    }

    public boolean addTile(LittleTile tile) {
        boolean result = tiles.add(tile);
        updateTiles();
        return result;
    }

    // @TODO update this to 1.12 code maybe?
    public void updateTiles() {
        if (worldObj != null) {
            update();
            updateNeighbor();
        }
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) updateCustomRenderer();

    }

    // @TODO update this to 1.12 code maybe?
    @SideOnly(Side.CLIENT)
    public void updateCustomRenderer() {
        customRenderingTiles.clear();
        for (LittleTile tile : tiles) {
            if (tile.needCustomRendering()) customRenderingTiles.add(tile);
        }
    }

    // @TODO update this to 1.12 code maybe?
    public void updateNeighbor() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) markFullRenderUpdate();
        for (LittleTile tile : tiles) {
            tile.onNeighborChangeInside();
        }
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, LittleTiles.blockTile);
    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public double getMaxRenderDistanceSquared() {
//        double renderDistance = 0;
//        for (LittleTile tile : tiles) {
//            renderDistance = Math.max(renderDistance, tile.getMaxRenderDistanceSquared());
//        }
//        return renderDistance;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public AxisAlignedBB getRenderBoundingBox() {
//        double minX = xCoord;
//        double minY = yCoord;
//        double minZ = zCoord;
//        double maxX = xCoord + 1;
//        double maxY = yCoord + 1;
//        double maxZ = zCoord + 1;
//        for (LittleTile tile : tiles) {
//            AxisAlignedBB box = tile.getRenderBoundingBox();
//            minX = Math.min(box.minX, minX);
//            minY = Math.min(box.minY, minY);
//            minZ = Math.min(box.minZ, minZ);
//            maxX = Math.max(box.maxX, maxX);
//            maxY = Math.max(box.maxY, maxY);
//            maxZ = Math.max(box.maxZ, maxZ);
//        }
//        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
//    }
//
//    // public boolean needFullUpdate = true;
//
    /**
     * Used for
     **/
    public LittleTile loadedTile = null;
//

    /**
     * Used for placing a tile and can be used if a "cable" can connect to a direction
     */
    public boolean isSpaceForLittleTile(CubeObject cube) {
        return isSpaceForLittleTile(cube.getAxis());
    }

    public boolean isSpaceForLittleTile(LittleBox box) {
        return isSpaceForLittleTile(box, (Predicate<LittleTile>) null);
    }

    //
//
    public boolean isSpaceForLittleTile(LittleBox box, BiPredicate<IParentTileList, LittleTile> predicate) {
//        @TODO implement all tiles
//        for (Pair<IParentTileList, LittleTile> pair : tiles.allTiles()) {
//            if (predicate != null && !predicate.test(pair.key, pair.value))
//                continue;
//            if (pair.value.intersectsWith(box))
//                return false;
//
//        }
        return true;
    }

    //
    public boolean isSpaceForLittleTile(LittleBox box, Predicate<LittleTile> predicate) {
        //        @TODO implement all tiles
//        for (Pair<IParentTileList, LittleTile> pair : tiles.allTiles()) {
//            if (predicate != null && !predicate.test(pair.value))
//                continue;
//            if (pair.value.intersectsWith(box))
//                return false;
//
//        }
        return true;
    }

    //
//    /** Used for placing a tile and can be used if a "cable" can connect to a direction */
    public boolean isSpaceForLittleTile(AxisAlignedBB alignedBB, LittleTile ignoreTile) {
//        for (LittleTile tile : tiles) {
//            for (int j = 0; j < tile.boundingBoxes.size(); j++) {
//                if (ignoreTile != tile && alignedBB.intersectsWith(tile.boundingBoxes.get(j).getBox())) return false;
//            }
//
//        }
        return true;
    }
//

    /**
     * Used for placing a tile and can be used if a "cable" can connect to a direction
     */
    public boolean isSpaceForLittleTile(AxisAlignedBB alignedBB) {
        return isSpaceForLittleTile(alignedBB, null);
    }

    public boolean isSpaceForLittleTile(LittleTileBox box) {
        return isSpaceForLittleTile(box.getBox());
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (tiles != null) tiles.clear();
        tiles = new TileList();
        int count = nbt.getInteger("tilesCount");
        for (int i = 0; i < count; i++) {
            NBTTagCompound tileNBT = nbt.getCompoundTag("t" + i);
            LittleTile tile = LittleTile.CreateandLoadTile(this, worldObj, tileNBT);
            if (tile != null) tiles.add(tile);
        }
        updateTiles();
        // update();
    }

    protected void customTilesUpdate() {
//        if (worldObj.isRemote)
//            return;
//        boolean rendered = tiles.hasRendered();
//        boolean ticking = tiles.hasTicking();
//        if (ticking != isTicking() || rendered != isRendered()) {
//            TileEntityLittleTiles newTe;
//            if (rendered)
//                if (ticking)
//                    newTe = new TileEntityLittleTilesTickingRendered();
//                else
//                    newTe = new TileEntityLittleTilesRendered();
//            else if (ticking)
//                newTe = new TileEntityLittleTilesTicking();
//            else
//                newTe = new TileEntityLittleTiles();
//
//            newTe.assign(this);
//            newTe.tiles.te = newTe;
//
//            preventUnload = true;
//            world.setBlockState(pos, BlockTile.getState(ticking, rendered), 2);
//            world.setTileEntity(pos, newTe);
//            invalidate();
//        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        for (int i = 0; i < tiles.size(); i++) {
            NBTTagCompound tileNBT = new NBTTagCompound();
            tiles.get(i).saveTile(tileNBT);
            nbt.setTag("t" + i, tileNBT);
        }
        nbt.setInteger("tilesCount", tiles.size());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        // writeToNBT(nbt);
        for (int i = 0; i < tiles.size(); i++) {
            NBTTagCompound tileNBT = new NBTTagCompound();
            NBTTagCompound packet = new NBTTagCompound();
            tiles.get(i).saveTile(tileNBT);
            tiles.get(i).updatePacket(packet);
            // tileNBT.setByte("x", tiles.get(i).cornerVec.x);
            // tileNBT.setByte("y", tiles.get(i).cornerVec.y);
            // tileNBT.setByte("z", tiles.get(i).cornerVec.z);
            tileNBT.setTag("update", packet);
            nbt.setTag("t" + i, tileNBT);
            if (needFullUpdate) nbt.setBoolean("f" + i, true);
        }
        nbt.setInteger("tilesCount", tiles.size());
        needFullUpdate = false;
        // if(needFullUpdate)
        // {
        // nbt.setBoolean("fullUpdate", true);
        // needFullUpdate = false;
        // }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, nbt);
    }

    public LittleTile getTile(LittleTileVec vec) {
        return getTile((byte) vec.x, (byte) vec.y, (byte) vec.z);
    }


    public LittleTile getTile(byte minX, byte minY, byte minZ) {
        for (LittleTile tile : tiles) {
            if (tile.cornerVec.x == minX && tile.cornerVec.y == minY && tile.cornerVec.z == minZ) return tile;
        }
        return null;
    }

    //
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
//        /*
//         * if(pkt.func_148857_g().getBoolean("fullUpdate")) { tiles = new ArrayList<LittleTile>(); int count =
//         * pkt.func_148857_g().getInteger("tilesCount"); for (int i = 0; i < count; i++) { NBTTagCompound tileNBT = new
//         * NBTTagCompound(); tileNBT = pkt.func_148857_g().getCompoundTag("t" + i); LittleTile tile =
//         * LittleTile.CreateandLoadTile(worldObj, tileNBT); if(tile != null) tiles.add(tile); } }else{
//         */
//
//        ArrayList<LittleTile> exstingTiles = new ArrayList<>(tiles);
//        int count = pkt.func_148857_g().getInteger("tilesCount");
//        for (int i = 0; i < count; i++) {
//            NBTTagCompound tileNBT = pkt.func_148857_g().getCompoundTag("t" + i);
//            LittleTile tile = getTile(tileNBT.getByte("cVecx"), tileNBT.getByte("cVecy"), tileNBT.getByte("cVecz"));
//            if (tile != null && tile.getID().equals(tileNBT.getString("tID"))
//                    && !pkt.func_148857_g().getBoolean("f" + i)) {
//                tile.receivePacket(tileNBT.getCompoundTag("update"), net);
//                exstingTiles.remove(tile);
//            } else {
//                tile = LittleTile.CreateandLoadTile(this, worldObj, tileNBT);
//                if (tile != null) tiles.add(tile);
//                // else
//                // System.out.println("Failed to load tileentity nbt=" + tileNBT.toString());
//            }
//        }
//        for (LittleTile exstingTile : exstingTiles) {
//            tiles.remove(exstingTile);
//        }
//        // }
//        updateTiles();
//        // markFullRenderUpdate();
//        /*
//         * if(tiles.size() == 0) { System.out.println("===============================");
//         * System.out.println("Receiving littleTiles packet x=" + xCoord + ",y=" + yCoord + ",z" + zCoord);
//         * System.out.println(pkt.func_148857_g().toString()); System.out.println("-------------------------------");
//         * System.out.println("Loaded " + tiles.size() + " tiles"); }
//         */
//    }
//
    public MovingObjectPosition getMoving(EntityPlayer player) {
        return getMoving(player, false);
    }

    public MovingObjectPosition getMoving(EntityPlayer player, boolean loadTile) {
        MovingObjectPosition hit = null;

        Vec3 pos = player.getPosition(1);
        double d0 = player.capabilities.isCreativeMode ? 5.0F : 4.5F;
        Vec3 look = player.getLook(1.0F);
        Vec3 vec32 = pos.addVector(look.xCoord * d0, look.yCoord * d0, look.zCoord * d0);
        return getMoving(pos, vec32, loadTile);
    }

    public MovingObjectPosition getMoving(Vec3 pos, Vec3 look, boolean loadTile) {
        MovingObjectPosition hit = null;
        for (LittleTile tile : tiles) {
            for (int j = 0; j < tile.boundingBoxes.size(); j++) {
                MovingObjectPosition Temphit = tile.boundingBoxes.get(j).getBox()
                    .getOffsetBoundingBox(xCoord, yCoord, zCoord).calculateIntercept(pos, look);
                if (Temphit != null) {
                    if (hit == null || hit.hitVec.distanceTo(pos) > Temphit.hitVec.distanceTo(pos)) {
                        hit = Temphit;
                        if (loadTile) loadedTile = tile;
                    }
                }
            }
        }
        return hit;
    }

    //
    public boolean updateLoadedTile(EntityPlayer player) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) return false;
        loadedTile = null;
        getMoving(player, true);
        return loadedTile != null;
    }

    public boolean updateLoadedTileServer(Vec3 pos, Vec3 look) {
        loadedTile = null;
        getMoving(pos, look, true);
        return loadedTile != null;
    }
//
//    @SideOnly(Side.CLIENT)
//    public void checkClientLoadedTile(double distance) {
//        Minecraft mc = Minecraft.getMinecraft();
//        Vec3 pos = mc.thePlayer.getPosition(1);
//        if (mc.objectMouseOver.hitVec.distanceTo(pos) < distance) loadedTile = null;
//    }
//
//    @Override
//    public boolean shouldRenderInPass(int pass) {
//        if (super.shouldRenderInPass(pass)) {
//            return customRenderingTiles.size() > 0;
//        }
//        return false;
//    }

    public void update() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) markFullRenderUpdate();

        worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @SideOnly(Side.CLIENT)
    public void updateRender() {
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            if (needsRenderingUpdate) {
                updateRender();
                // System.out.println("Chunk update!");
                needsRenderingUpdate = false;
            }
        }

        for (LittleTile tile : tiles) {
            tile.updateEntity();
        }
        if (!worldObj.isRemote && tiles.size() == 0) worldObj.setBlockToAir(xCoord, yCoord, zCoord);
    }

    public ChunkCoordinates getCoord() {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    //
    public void combineTiles(LittleStructure structure) {
        // ArrayList<LittleTile> newTiles = new ArrayList<>();
        int size = 0;
        while (size != tiles.size()) {
            size = tiles.size();
            int i = 0;
            while (i < tiles.size()) {
                if (tiles.get(i).structure != structure) {
                    i++;
                    continue;
                }

                int j = 0;

                while (j < tiles.size()) {
                    if (tiles.get(j).structure != structure) {
                        j++;
                        continue;
                    }

                    if (i != j && tiles.get(i).boundingBoxes.size() == 1
                        && tiles.get(j).boundingBoxes.size() == 1
                        && tiles.get(i).canBeCombined(tiles.get(j))
                        && tiles.get(j).canBeCombined(tiles.get(i))) {
                        LittleTileBox box = tiles.get(i).boundingBoxes.get(0)
                            .combineBoxes(tiles.get(j).boundingBoxes.get(0));
                        if (box != null) {
                            tiles.get(i).boundingBoxes.set(0, box);
                            tiles.get(i).combineTiles(tiles.get(j));
                            tiles.get(i).updateCorner();
                            tiles.remove(j);
                            if (i > j) i--;
                            continue;
                        }
                    }
                    j++;
                }
                i++;
            }
        }
        update();
    }

    public void combineTiles() {
        // ArrayList<LittleTile> newTiles = new ArrayList<>();
        int size = 0;
        while (size != tiles.size()) {
            size = tiles.size();
            int i = 0;
            while (i < tiles.size()) {
                int j = 0;
                while (j < tiles.size()) {
                    if (i != j && tiles.get(i).boundingBoxes.size() == 1
                        && tiles.get(j).boundingBoxes.size() == 1
                        && tiles.get(i).canBeCombined(tiles.get(j))
                        && tiles.get(j).canBeCombined(tiles.get(i))) {
                        LittleTileBox box = tiles.get(i).boundingBoxes.get(0)
                            .combineBoxes(tiles.get(j).boundingBoxes.get(0));
                        if (box != null) {
                            tiles.get(i).boundingBoxes.set(0, box);
                            tiles.get(i).combineTiles(tiles.get(j));
                            tiles.get(i).updateCorner();
                            tiles.remove(j);
                            if (i > j) i--;
                            continue;
                        }
                    }
                    j++;
                }
                i++;
            }
        }
        update();
    }

    //    @Override
    public void handleUpdate(NBTTagCompound nbt, boolean chunkUpdate) {
//        if (isClientSide())
//            render.beforeClientReceivesUpdate();

        readFromNBT(nbt);
        if (!chunkUpdate)
            updateTiles();

//        if (isClientSide())
//            render.afterClientReceivesUpdate();
    }

    // 1.12 method
//    public PairProxy<IParentTileList, LittleTile> getFocusedTile(EntityPlayer player, float partialTickTime) {
//        if (!isClientSide())
//            return null;
////        Vector3d pos = player.getPositionEyes(partialTickTime);
////        double d0 = player.capabilities.isCreativeMode ? 5.0F : 4.5F;
////        Vector3d look = player.getLook(partialTickTime);
////        Vector3d vec32 = pos.addVector(look.x * d0, look.y * d0, look.z * d0);
////
////        if (world != player.world && world instanceof CreativeWorld) {
////            pos = ((CreativeWorld) world).getOrigin().transformPointToFakeWorld(pos);
////            vec32 = ((CreativeWorld) world).getOrigin().transformPointToFakeWorld(vec32);
////        }
//        MovingObjectPosition rayTraceResult = rayTrace(player, true);
//        rayTraceResult.
//
//        return getFocusedTile(pos, vec32);
//    }

    public MovingObjectPosition getFocusedTile(/*EntityPlayer player, float partialTickTime*/) {
        return Minecraft.getMinecraft().objectMouseOver;

    }

    @Nullable
    public static MovingObjectPosition rayTrace(Entity entity, double dist, boolean useLiquids) {
        Vec3 start = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3 look = entity.getLookVec();
        Vec3 end = start.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist);
        return entity.worldObj.func_147447_a(start, end, useLiquids, !useLiquids, false);
    }

//    @Nullable
//    public static MovingObjectPosition rayTrace(EntityPlayer player, boolean useLiquids) {
//        double playerReach = player.worldObj.isRemote
//            ? Minecraft.getMinecraft().playerController.getBlockReachDistance()
//            : (player instanceof EntityPlayerMP playerMP ? playerMP.theItemInWorldManager.getBlockReachDistance() : 5.0D);
//        return rayTrace(player, playerReach, useLiquids);
//    }

//    public PairProxy<IParentTileList, LittleTile> getFocusedTile(Vector3d pos, Vector3d look) {
//        IParentTileList parent = null;
//        LittleTile tileFocus = null;
//        RayTraceResult hit = null;
//        double distance = 0;
//        for (Pair<IParentTileList, LittleTile> pair : tiles.allTiles()) {
//            RayTraceResult Temphit = pair.value.rayTrace(context, getPos(), pos, look);
//            if (Temphit != null) {
//                if (hit == null || distance > Temphit.hitVec.distanceTo(pos)) {
//                    distance = Temphit.hitVec.distanceTo(pos);
//                    hit = Temphit;
//                    parent = pair.key;
//                    tileFocus = pair.value;
//                }
//            }
//        }
//        return new PairProxy(parent, tileFocus);
//    }

    public void fillUsedIds(BitSet usedIds) {
        tiles.fillUsedIds(usedIds);
    }

    @Override
    public void convertToSmallest() {
        int size = LittleGridContext.minSize;
        for (PairProxy<IParentTileList, LittleTile> pair : tiles.allTiles())
            size = Math.max(size, pair.value.getSmallestContext(context));

        if (size < context.size)
            convertTo(LittleGridContext.get(size));
    }

    @Override
    public void convertTo(LittleGridContext newContext) {
        for (PairProxy<IParentTileList, LittleTile> pair : tiles.allTiles())
            pair.getValue().convertTo(context, newContext);

        this.context = newContext;
    }

    public boolean combineTilesSecretly(/*int structureIndex*/) {
//        if (getStructure(structureIndex) == null)
//            return false;
//        boolean changed = BasicCombiner.combine((StructureTileList) getStructure(structureIndex));
        convertToSmallest();
        return true;
//        return changed;
    }

    /** Tries to convert the TileEntity to a vanilla block
     *
     * @return whether it could convert it or not */
    public boolean convertBlockToVanilla() {
        LittleTile firstTile = null;
        if (tiles.isCompletelyEmpty()) {
            worldObj.setBlockToAir(getPos().x, getPos().y, getPos().z);
            return true;
        }

//        if (worldObj instanceof IOrientatedWorld || tiles.countStructures() > 0)
//            return false;

        if (tiles.size() == 1) {
            if (!tiles.first().canBeConvertedToVanilla() || !tiles.first().doesFillEntireBlock(context))
                return false;
            firstTile = tiles.first();
        } else {
            boolean[][][] filled = new boolean[context.size][context.size][context.size];
            for (LittleTile tile : tiles) {
                if (firstTile == null) {
                    if (!tile.canBeConvertedToVanilla())
                        return false;

                    firstTile = tile;
                } else if (!firstTile.canBeCombined(tile) || !tile.canBeCombined(firstTile))
                    return false;

                tile.fillInSpace(filled);
            }

            for (int x = 0; x < filled.length; x++)
                for (int y = 0; y < filled[x].length; y++)
                    for (int z = 0; z < filled[x][y].length; z++)
                        if (!filled[x][y][z])
                            return false;
        }

//        worldObj.setBlockState(pos, firstTile.getBlockState());

        return true;
    }

    /** @param box
     * @param cutout
     *            filled with all boxes which are cutout by tiles
     * @return all boxes which are not cutout by other tiles */
    public List<LittleBox> cutOut(LittleBox box, List<LittleBox> cutout/*, @Nullable LittleBoxReturnedVolume volume*/) {
        List<LittleBox> cutting = new ArrayList<>();
        for (PairProxy<IParentTileList, LittleTile> pair : tiles.allTiles())
            pair.value.getIntersectingBox(box, cutting);
        return box.cutOut(cutting, cutout/*, volume*/);
    }

    public PairProxy<IParentTileList, LittleTile> intersectingTile(LittleBox box) {
        for (PairProxy<IParentTileList, LittleTile> pair : tiles.allTiles())
            if (pair.value.intersectsWith(box))
                return pair;
        return null;
    }

    /** Block will not update */
    public void updateTilesSecretly(Consumer<TileEntityInteractor> action) {
        action.accept(interactor);
    }

    public class TileEntityInteractor {

        public Iterable<IParentTileList> groups() {
            return new Iterable<IParentTileList>() {

                @Override
                public Iterator<IParentTileList> iterator() {
                    return new Iterator<IParentTileList>() {

                        IParentTileList current = (IParentTileList) tiles;
//                        Iterator<StructureTileList> children = structures().iterator();

                        @Override
                        public boolean hasNext() {
                            if (current != null)
                                return true;
//                            if (!children.hasNext())
//                                return false;
//                            current = children.next();
                            return false;
                        }

                        @Override
                        public IParentTileList next() {
                            IParentTileList result = current;
                            current = null;
                            return result;
                        }
                    };
                }
            };
        }

        public ParentTileList get(IParentTileList list) {
            return (ParentTileList) list;
        }

//        public StructureTileList get(IStructureTileList list) {
//            return (StructureTileList) list;
//        }

        public ParentTileList noneStructureTiles() {
            return tiles;
        }

//        public Iterable<StructureTileList> structures() {
//            return tiles.structuresReal();
//        }

//        public StructureTileList getStructure(int index) {
//            return tiles.getStructure(index);
//        }

//        public boolean removeStructure(int index) {
//            return tiles.removeStructure(index);
//        }

//        public StructureTileList addStructure(int index, int attribute) {
//            return tiles.addStructure(index, attribute);
//        }

        public void clearEverything() {
            tiles.clearEverything();
        }

    }

}
