package com.creativemd.littletiles.common.utils;

import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.tile.math.box.AlignedBox;
import com.creativemd.littletiles.common.utils.small.LittleTileBox;
import com.creativemd.creativecore.common.utils.CubeObject;
import com.creativemd.littletiles.client.render.tile.LittleRenderBox;
import com.creativemd.littletiles.common.items.ItemBlockTiles;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tile.combine.ICombinable;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxReturnedVolume;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.registry.LittleTileRegistry;
import com.creativemd.littletiles.common.tile.registry.LittleTileType;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesProxy;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.small.LittleTileBox;
import com.creativemd.littletiles.common.utils.small.LittleTileCoord;
import com.creativemd.littletiles.common.utils.small.LittleTileSize;
import com.creativemd.littletiles.common.utils.small.LittleTileVec;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumFacing;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LittleTile implements ICombinable {

    // this classes "boundingBox" got updated to this LittleBox variable from 1.7 -> 1.12
    private LittleBox box;

    public TileEntityLittleTilesProxy te;
    public boolean invisible = false;
    public boolean glowing = false;

    private Block block;
    private int meta;

    private static final HashMap<Class<? extends LittleTile>, String> tileIDs = new HashMap<>();

    public static final int minPos = 0;
    public static final int maxPos = 16;


    //    private ISpecialBlockHandler handler;
    protected byte cachedTranslucent;
//    protected IBlockState state = null;

    public LittleTile() {
    }
    public LittleTile(Block block, int meta) {
        setBlock(block, meta);
    }

    public LittleTileType getType() {
        return LittleTileRegistry.getTileType(String.valueOf(this.getClass()));
    }

    // ================Block================
//
//    private void updateSpecialHandler() {
//        if (!(block instanceof BlockAir))
//            handler = SpecialBlockHandler.getSpecialBlockHandler(block, meta);
//        updateBlockState();
//    }
//
//    public boolean hasSpecialBlockHandler() {
//        return handler != null;
//    }
//
    protected void setBlock(String defaultName, Block block, int meta) {
        if (block == null || block instanceof BlockAir) {
            this.block = Blocks.air;
            this.meta = meta;
//            this.handler = MissingBlockHandler.getHandler(defaultName);
        } else
            setBlock(block, meta);
    }

    public void setBlock(Block block, int meta) {
        this.block = block;
        this.meta = meta;
//        updateSpecialHandler();
    }

    public void setMeta(int meta) {
        this.meta = meta;
//        updateSpecialHandler();
    }

    public void setBlock(Block block) {
        this.block = block;
//        updateSpecialHandler();
    }

    public Block getBlock() {
        return this.block;
    }

    public int getMeta() {
        return this.meta;
    }
//
//    public IBlockState getBlockState() {
//        if (state == null)
//            updateBlockState();
//        return state;
//    }
//
//    public void updateBlockState() {
//        state = BlockUtils.getState(block, meta);
//        if (state == null)
//            state = block.getDefaultState();
//    }
//
//    public boolean isTranslucent() {
//        if (cachedTranslucent == 0)
//            updateTranslucent();
//        return cachedTranslucent == 2;
//    }
//
//    public void updateTranslucent() {
//        if (!getBlockState().getMaterial().blocksLight() || !getBlockState().getMaterial().isSolid() || !getBlockState().isOpaqueCube())
//            cachedTranslucent = 2;
//        else
//            cachedTranslucent = 1;
//    }
//
//    // ================Position & Size================

    public int getSmallestContext(LittleGridContext context) {
        return box.getSmallestContext(context);
    }

    public void convertTo(LittleGridContext from, LittleGridContext to) {
        box.convertTo(from, to);
    }

    public boolean canBeConvertedToVanilla() {
        if (!box.isSolid())
            return false;
//        if (hasSpecialBlockHandler())
//            return handler.canBeConvertedToVanilla(this);
        return true;
    }

    @Override
    public LittleBox getBox() {
        return box;
    }

    @Override
    public void setBox(LittleBox box) {
        this.box = box;
    }

    public LittleVec getMinVec() {
        return box.getMinVec();
    }

    public int getMaxY() {
        return box.maxY;
    }
//
    public AxisAlignedBB getSelectedBox(BlockPos pos, LittleGridContext context) {
        return box.getSelectionBox(context, pos);
    }
//
//    public double getVolume() {
//        return box.getVolume();
//    }
//
//    public double getPercentVolume(LittleGridContext context) {
//        return box.getPercentVolume(context);
//    }

    public LittleVec getSize() {
        return box.getSize();
    }

    public boolean doesFillEntireBlock(LittleGridContext context) {
        return box.doesFillEntireBlock(context);
    }

//    public void fillFace(LittleBoxFace face, LittleGridContext context) {
//        LittleBox box = this.box;
//        if (face.context != context) {
//            box = box.copy();
//            box.convertTo(context, face.context);
//        }
//        box.fill(face);
//    }
//
    public boolean fillInSpace(boolean[][][] filled) {
        if (!box.isSolid())
            return false;

        boolean changed = false;
        for (int x = box.minX; x < box.maxX; x++) {
            for (int y = box.minY; y < box.maxY; y++) {
                for (int z = box.minZ; z < box.maxZ; z++) {
                    filled[x][y][z] = true;
                    changed = true;
                }
            }
        }
        return changed;
    }

    public boolean fillInSpaceInaccurate(LittleBox otherBox, boolean[][][] filled) {
        return box.fillInSpaceInaccurate(otherBox, filled);
    }

//    @Override
    public boolean fillInSpace(LittleBox otherBox, boolean[][][] filled) {
        return box.fillInSpace(otherBox, filled);
    }

    public boolean intersectsWith(LittleBox box) {
        return LittleBox.intersectsWith(this.box, box);
    }

    public List<LittleBox> cutOut(LittleBox box, @Nullable LittleBoxReturnedVolume volume) {
        return this.box.cutOut(box/*, volume*/);
    }

    public List<LittleBox> cutOut(List<LittleBox> boxes, List<LittleBox> cutout, @Nullable LittleBoxReturnedVolume volume) {
        return this.box.cutOut(boxes, cutout/*, volume*/);
    }

    public void getIntersectingBox(LittleBox box, List<LittleBox> boxes) {
        if (LittleBox.intersectsWith(box, this.box))
            boxes.add(this.box);
    }

    public LittleBox getCompleteBox() {
        return box;
    }

//    public LittleVec getCenter() {
//        return box.getCenter();
//    }
//
//    public RayTraceResult rayTrace(LittleGridContext context, BlockPos blockPos, Vec3d pos, Vec3d look) {
//        return box.calculateIntercept(context, blockPos, pos, look);
//    }

    public boolean equalsBox(LittleBox box) {
        return this.box.equals(box);
    }

    public boolean canBeCombined(LittleTile tile) {
        if (invisible != tile.invisible)
            return false;

        if (glowing != tile.glowing)
            return false;

        return block == tile.block && meta == tile.meta;
    }
//
    @Override
    public boolean canCombine(ICombinable combinable) {
        return this.canBeCombined((LittleTile) combinable) && ((LittleTile) combinable).canBeCombined(this);
    }
    public IIcon getIcon(int side) {
        return block.getIcon(side, meta);
    }

//
//    public boolean doesProvideSolidFace(EnumFacing facing) {
//        return !invisible && box.isFacePartiallyFilled(facing) && !isTranslucent() && block != Blocks.BARRIER;
//    }
//
//    @SideOnly(Side.CLIENT)
//    public boolean canBeRenderCombined(LittleTile tile) {
//        if (this.invisible != tile.invisible)
//            return false;
//
//        if (block == tile.block && meta == tile.meta && block != Blocks.BARRIER && tile.block != Blocks.BARRIER)
//            return true;
//
//        if (hasSpecialBlockHandler() && handler.canBeRenderCombined(this, tile))
//            return true;
//
//        return false;
//    }
//
//    public boolean doesTouch(LittleTile tile) {
//        return box.doesTouch(tile.box);
//    }
//
//    // ================Save & Loading================

    public NBTTagCompound startNBTGrouping() {
        NBTTagCompound nbt = new NBTTagCompound();
        saveTile(nbt);

        nbt.removeTag("box");

        NBTTagList list = new NBTTagList();
        list.appendTag(box.getNBTIntArray());
        nbt.setTag("boxes", list);

        return nbt;
    }

    public boolean canBeNBTGrouped(LittleTile tile) {
        return tile.canBeCombined(this) && this.canBeCombined(tile);
    }

    public void groupNBTTile(NBTTagCompound nbt, LittleTile tile) {
        NBTTagList list = nbt.getTagList("boxes", 11);
        list.appendTag(tile.box.getNBTIntArray());
    }

    public List<NBTTagCompound> extractNBTFromGroup(NBTTagCompound nbt) {
        List<NBTTagCompound> tags = new ArrayList<>();
        NBTTagList list = nbt.getTagList("boxes", 11);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound copy = (NBTTagCompound) nbt.copy();
            NBTTagList small = new NBTTagList();
            small.appendTag(list.getCompoundTagAt(i));
            copy.setTag("boxes", small);
            tags.add(copy);
        }
        return tags;
    }

    public void saveTile(NBTTagCompound nbt) {
        saveTileCore(nbt);
        saveTileExtra(nbt);
    }

    /** Used to save extra data like block-name, meta, color etc. everything
     * necessary for a preview **/
    public void saveTileExtra(NBTTagCompound nbt) {
        if (invisible)
            nbt.setBoolean("invisible", invisible);
        if (glowing)
            nbt.setBoolean("glowing", glowing);

        nbt.setString("block", Block.blockRegistry.getNameForObject(block)
                .toString() + (meta != 0 ? ":" + meta : ""));
    }

    public void saveTileCore(NBTTagCompound nbt) {
        LittleTileType type = getType();
        if (type.saveId)
            nbt.setString("tID", type.id);
        if (box != null) {
            nbt.setIntArray("box", box.getArray());
        }
    }

    public void loadTile(NBTTagCompound nbt) {
        loadTileCore(nbt);
        loadTileExtra(nbt);
    }

    public void loadTileExtra(NBTTagCompound nbt) {
        invisible = nbt.getBoolean("invisible");
        glowing = nbt.getBoolean("glowing");
        if (nbt.hasKey("meta"))
            setBlock(nbt.getString("block"), Block.getBlockFromName(nbt.getString("block")), nbt.getInteger("meta"));
        else {
            String[] parts = nbt.getString("block").split(":");
            if (parts.length == 3)
                setBlock(nbt.getString("block"), Block.getBlockFromName(parts[0] + ":" + parts[1]), Integer.parseInt(parts[2]));
            else
                setBlock(nbt.getString("block"), Block.getBlockFromName(parts[0] + ":" + parts[1]), 0);
        }
    }

    public void loadTileCore(NBTTagCompound nbt) {
        if (nbt.hasKey("bSize")) { // Old (used till 1.4)
            int count = nbt.getInteger("bSize");
            box = LittleBox.loadBox("bBox" + 0, nbt);
        } else if (nbt.hasKey("boxes")) { // Out of date (used in pre-releases of 1.5)
            NBTTagList list = nbt.getTagList("boxes", 11);
            // func_150306_c = getIntArrayAt()
            box = LittleBox.createBox(list.func_150306_c(0));
        } else if (nbt.hasKey("box")) { // Active one
            box = LittleBox.createBox(nbt.getIntArray("box"));
        }

    }

//    // ================Copy================
//
    @Override
    public LittleTile copy() {
        LittleTile tile = null;
        try {
            tile = this.getClass().getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("Invalid LittleTile class=" + this.getClass().getName());
            tile = null;
        }
        if (tile != null) {
            copyCore(tile);
            copyExtra(tile);
        }
        return tile;
    }
//
//    public void assignTo(LittleTile target) {
//        copyCore(target);
//        copyExtra(target);
//    }
//
    public void copyExtra(LittleTile tile) {
        tile.invisible = this.invisible;
        tile.glowing = this.glowing;
//        tile.handler = this.handler;
        tile.block = this.block;
        tile.meta = this.meta;
    }

    public void copyCore(LittleTile tile) {
        tile.box = box != null ? box.copy() : null;
    }
//
//    // ================Drop================

    public ItemStack getDrop(LittleGridContext context) {
        return getDropInternal(context);
    }

    protected ItemStack getDropInternal(LittleGridContext context) {
        return ItemBlockTiles.getStackFromPreview(context, getPreviewTile());
    }

    public LittlePreview getPreviewTile() {
//        if (hasSpecialBlockHandler()) {
//            LittlePreview preview = handler.getPreview(this);
//            if (preview != null)
//                return preview;
//        }

        NBTTagCompound nbt = new NBTTagCompound();
        saveTileExtra(nbt);
        LittleTileType type = getType();
        if (type.saveId)
            nbt.setString("tID", type.id);
        return new LittlePreview(box.copy(), nbt);
    }
//
//    // ================Rendering================
//
//    @SideOnly(Side.CLIENT)
//    public boolean shouldBeRenderedInLayer(BlockRenderLayer layer) {
//        if (FMLClientHandler.instance().hasOptifine() && block.canRenderInLayer(state, BlockRenderLayer.CUTOUT))
//            return layer == BlockRenderLayer.CUTOUT_MIPPED; // Should fix an Optifine bug
//
//        try {
//            return block.canRenderInLayer(getBlockState(), layer);
//        } catch (Exception e) {
//            try {
//                return block.getBlockLayer() == layer;
//            } catch (Exception e2) {
//                return layer == BlockRenderLayer.SOLID;
//            }
//        }
//    }
//
    @SideOnly(Side.CLIENT)
    public final LittleRenderBox getRenderingCube(LittleGridContext context) {
        if (invisible)
            return null;
        return getInternalRenderingCube(context);
    }

    @SideOnly(Side.CLIENT)
    protected LittleRenderBox getInternalRenderingCube(LittleGridContext context) {
        return box.getRenderingCube(context, block, meta);
    }

//    @Override
    public ArrayList<CubeObject> getRenderingCubes() {
        ArrayList<CubeObject> cubes = new ArrayList<>();
//        for (LittleTileBox boundingBox : boundingBoxes) {
//            CubeObject cube = box.getCube();
            AlignedBox alignedBox = box.getCube(te.getContext());
            CubeObject cube = new CubeObject(alignedBox.minX, alignedBox.minY, alignedBox.minZ, alignedBox.maxX, alignedBox.maxY, alignedBox.maxZ);
            cube.block = block;
            cube.meta = meta;
            cubes.add(cube);
//        }
        return cubes;
    }
//
//    // ================Sound================
//
//    public SoundType getSound() {
//        return block.getSoundType();
//    }
//
//    // ================Interaction================
//
    public boolean canSawResizeTile(EnumFacing facing, EntityPlayer player) {
        return true;
    }

    public boolean canBeMoved(EnumFacing facing) {
        return true;
    }
//
//    // ================Block Event================

    public float getExplosionResistance() {
        return block.getExplosionResistance(null);
    }

//    public void onTileExplodes(IParentTileList parent, Explosion explosion) {
//        if (hasSpecialBlockHandler())
//            handler.onTileExplodes(parent, this, explosion);
//    }
//
//    public void randomDisplayTick(IParentTileList parent, Random rand) {
//        if (hasSpecialBlockHandler())
//            handler.randomDisplayTick(parent, this, rand);
//        else
//            block.randomDisplayTick(getBlockState(), parent.getWorld(), parent.getPos(), rand);
//
//        if (block == Blocks.BARRIER)
//            spawnBarrierParticles(parent.getPos());
//    }
//
//    @SideOnly(Side.CLIENT)
//    private void spawnBarrierParticles(BlockPos pos) {
//        Minecraft mc = Minecraft.getMinecraft();
//        ItemStack itemstack = mc.player.getHeldItemMainhand();
//        if (mc.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock(Blocks.BARRIER))
//            mc.world.spawnParticle(EnumParticleTypes.BARRIER, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0.0D, 0.0D, 0.0D, new int[0]);
//    }
//
//    public boolean onBlockActivated(IParentTileList parent, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
//        if (hasSpecialBlockHandler())
//            return handler.onBlockActivated(parent, this, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
//        return block.onBlockActivated(parent.getWorld(), parent.getPos(), getBlockState(), playerIn, hand, side, hitX, hitY, hitZ);
//    }
//
//    public int getLightValue(IBlockAccess world, BlockPos pos) {
//        if (glowing)
//            return glowing ? 14 : 0;
//        return block.getLightValue(getBlockState());
//    }
//
//    public float getEnchantPowerBonus(World world, BlockPos pos) {
//        return block.getEnchantPowerBonus(world, pos);
//    }
//
//    public float getSlipperiness(IBlockAccess world, BlockPos pos, Entity entity) {
//        return block.getSlipperiness(getBlockState(), world, pos, entity);
//    }
//
//    public boolean isMaterial(Material material) {
//        if (hasSpecialBlockHandler())
//            return handler.isMaterial(this, material);
//        return material == block.getMaterial(state);
//    }
//
//    public boolean isLiquid() {
//        if (hasSpecialBlockHandler())
//            return handler.isLiquid(this);
//        return getBlockState().getMaterial().isLiquid();
//    }
//
//    public Vec3d getFogColor(IParentTileList parent, Entity entity, Vec3d originalColor, float partialTicks) {
//        if (hasSpecialBlockHandler())
//            return handler.getFogColor(parent, this, entity, originalColor, partialTicks);
//        return originalColor;
//    }
//
//    public Vec3d modifyAcceleration(IParentTileList parent, Entity entityIn, Vec3d motion) {
//        if (hasSpecialBlockHandler())
//            return handler.modifyAcceleration(parent, this, entityIn, motion);
//        return null;
//    }
//
//    // ================Collision================

    public boolean shouldCheckForCollision() {
        return true;
    }
//
//    public void onEntityCollidedWithBlock(IParentTileList parent, Entity entityIn) {
//        if (hasSpecialBlockHandler())
//            handler.onEntityCollidedWithBlock(parent, this, entityIn);
//    }
//
//    public boolean hasNoCollision() {
//        if (hasSpecialBlockHandler())
//            return handler.canWalkThrough(this);
//        return false;
//    }
//
//    public LittleBox getCollisionBox() {
//        if (hasSpecialBlockHandler())
//            return handler.getCollisionBox(this, box);
//
//        return box;
//    }
//
//    @Deprecated
//    public static class LittleTilePosition {
//
//        public BlockPos coord;
//        public LittleVec position;
//
//        public LittleTilePosition(BlockPos coord, LittleVec position) {
//            this.coord = coord;
//            this.position = position;
//        }
//
//        public LittleTilePosition(String id, NBTTagCompound nbt) {
//            coord = new BlockPos(nbt.getInteger(id + "coX"), nbt.getInteger(id + "coY"), nbt.getInteger(id + "coZ"));
//            position = new LittleVec(id + "po", nbt);
//        }
//
//        public LittleTilePosition(NBTTagCompound nbt) {
//            this("", nbt);
//        }
//
//        public void writeToNBT(String id, NBTTagCompound nbt) {
//            nbt.setInteger(id + "coX", coord.getX());
//            nbt.setInteger(id + "coY", coord.getY());
//            nbt.setInteger(id + "coZ", coord.getZ());
//            position.writeToNBT(id + "po", nbt);
//        }
//
//        public void writeToNBT(NBTTagCompound nbt) {
//            writeToNBT("", nbt);
//        }
//
//        @Override
//        public String toString() {
//            return "coord:" + coord + "|position:" + position;
//        }
//
//        public LittleTilePosition copy() {
//            return new LittleTilePosition(new BlockPos(coord), position.copy());
//        }
//
//    }
//
//    public static class MissingBlockHandler implements ISpecialBlockHandler {
//
//        private static HashMap<String, MissingBlockHandler> handlers = new HashMap<>();
//
//        public static MissingBlockHandler getHandler(String blockname) {
//            MissingBlockHandler handler = handlers.get(blockname);
//            if (handler != null)
//                return handler;
//            handler = new MissingBlockHandler(blockname);
//            handlers.put(blockname, handler);
//            return handler;
//        }
//
//        public final String blockname;
//
//        private MissingBlockHandler(String blockname) {
//            this.blockname = blockname;
//        }
//
//        public static void unload() {
//            handlers.clear();
//        }
//    }
//

    public static Class<? extends LittleTile> getClassByID(String id) {
        for (Map.Entry<Class<? extends LittleTile>, String> entry : tileIDs.entrySet()) {
            if (id.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String getIDByClass(Class<? extends LittleTile> LittleClass) {
        return tileIDs.get(LittleClass);
    }

    /** The id has to be unique and cannot be changed! **/
    public static void registerLittleTile(Class<? extends LittleTile> LittleClass, String id) {
        tileIDs.put(LittleClass, id);
    }

    public static LittleTile CreateandLoadTile(TileEntityLittleTilesProxy te, World world, NBTTagCompound nbt) {
        return CreateandLoadTile(te, world, nbt, false, null);
    }

    public static LittleTile CreateandLoadTile(TileEntityLittleTilesProxy te, World world, NBTTagCompound nbt,
                                                       boolean isPacket, NetworkManager net) {
        if (nbt.hasKey("tileID")) // If it's the old tileentity
        {
            if (nbt.hasKey("block")) {
                Block block = Block.getBlockFromName(nbt.getString("block"));
                int meta = nbt.getInteger("meta");
                LittleTileBox box = new LittleTileBox(new LittleTileVec("i", nbt), new LittleTileVec("a", nbt));
                box.addOffset(new LittleTileVec(8, 8, 8));
                LittleTile tile = new LittleTile(block, meta);
                tile.setBox(new LittleBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));
                tile.cornerVec = box.getMinVec();
                return tile;
            }
        } else {
            String id = nbt.getString("tID");
            Class<? extends LittleTile> TileClass = getClassByID(id);
            LittleTile tile = null;
            if (TileClass != null) {
                try {
                    tile = TileClass.getConstructor().newInstance();
                } catch (Exception e) {
                    System.out.println("Found invalid tileID=" + id);
                }
            }
            if (tile != null) if (isPacket) tile.receivePacket(nbt, net);
            else {
                try {
                    tile.loadTile(te, nbt);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return tile;
        }
        return null;
    }


    public String getID() {
        return getIDByClass(this.getClass());
    }

    // ================Position & Size================

    public LittleTileVec cornerVec;

    public AxisAlignedBB getSelectedBox() {
        return box.getBox(te.getContext());
    }

    public double getPercentVolume() {
        double percent = 0;
        // @TODO get volume
//        for (LittleTileBox boundingBox : boundingBoxes) {
//            percent += boundingBox.getSize().getPercentVolume();
//        }
        return percent;
    }

    public void combineTiles(LittleTile tile) {
//        if (isLoaded()) {
//            structure.getTiles().remove(tile);
//        }
    }

    // ================Packets================

    public void updatePacket(NBTTagCompound nbt) {

        nbt.setInteger("bSize", 1);
        // @TODO add back in multiple bounding boxes
        for (int i = 0; i < 1 /*boundingBoxes.size()*/; i++) {
            box.writeToNBT("bBox" + i, nbt);
        }
    }

    public void receivePacket(NBTTagCompound nbt, NetworkManager net) {
        int count = nbt.getInteger("bSize");
        box = null;
        for (int i = 0; i < count; i++) {
            LittleTileBox littleTileBox = new LittleTileBox("bBox" + i, nbt);
            box = new LittleBox(littleTileBox.minX, littleTileBox.minY, littleTileBox.minZ, littleTileBox.maxX, littleTileBox.maxY, littleTileBox.maxZ);
        }
        updateCorner();
    }

    // ================Save & Loading================

    public void loadTile(TileEntityLittleTilesProxy te, NBTTagCompound nbt) {
        this.te = te;
        loadTileCore(nbt);
        loadTileExtra(nbt);
    }


//    public void markForUpdate() {
//        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) te.update();
//        else te.updateRender();
//    }

    // ================Placing================
    /** stack may be null **/
    public void onPlaced(EntityPlayer player, ItemStack stack) {
        onNeighborChangeInside();
    }

    // changed from 1.7
    public void updateCorner() {
        if (box != null) {
            cornerVec = new LittleTileVec(box.minX, box.minY, box.minZ);
        } else cornerVec = new LittleTileVec(0, 0, 0);
    }

    public void place() {
//         LittleTileBox box = new LittleTileBox(getSelectedBox());
        updateCorner();
        te.addTile(this);
        // te.getWorldObj().playSoundEffect((double)((float)te.xCoord + 0.5F), (double)((float)te.yCoord + 0.5F),
        // (double)((float)te.zCoord + 0.5F), getSound().func_150496_b(), (getSound().getVolume() + 1.0F) / 2.0F,
        // getSound().getPitch() * 0.8F);
    }

    // ================Destroying================

    public void onDestoryed() {}

    public void destroy() {
/*        if (isStructureBlock) {
            if (!te.getWorldObj().isRemote && isLoaded()) structure.onLittleTileDestory();
        } else {*/
            te.removeTile(this);
//        }
    }

    // ================Copy================

    public void assign(LittleTile tile) {
        copyCore(tile);
        copyExtra(tile);
    }

    // ================Drop================

    public ArrayList<ItemStack> getDrops() {
        ArrayList<ItemStack> drops = new ArrayList<>();
        ItemStack stack = null;
        if (isStructureBlock) {
            if (isLoaded()) stack = structure.getStructureDrop();
        } /* @TODO get context for getDrop() call: else stack = getDrop();*/
        if (stack != null) drops.add(stack);

        return drops;
    }

    // ================Notifcations/Events================

    public void onNeighborChangeOutside() {
        onNeighborChange();
    }

    public void onNeighborChangeInside() {
        onNeighborChange();
    }

    public void onNeighborChange() {}

    // ================Rendering================

    /*
     * @SideOnly(Side.CLIENT) public boolean isRendering;
     * @SideOnly(Side.CLIENT) public ArrayList<LittleBlockVertex> lastRendered;
     */

    public boolean needCustomRendering() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void renderTick(double x, double y, double z, float partialTickTime) {}

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 4096;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB
            .getBoundingBox(te.xCoord, te.yCoord, te.zCoord, te.xCoord + 1, te.yCoord + 1, te.zCoord + 1);
    }
    // ================Tick================

    public static final int ticksBetweenRefresh = 1200;

    public int ticks = 0;

    public void updateEntity() {
        ticks++;
        if (ticks > ticksBetweenRefresh) {
            ticks = 0;
            if (isStructureBlock && isMainBlock && structure.tilesToLoad != null) {
                // System.out.println("Loading structure x=" + te.xCoord + " y=" + te.yCoord + " z=" + te.zCoord + "");
//                structure.loadTiles();
            }
        }
    }

    // ================Interaction================

//    public boolean canSawResizeTile(ForgeDirection direction, EntityPlayer player) {
//        return boundingBoxes.size() == 1 && !isStructureBlock && canSawResize(direction, player);
//    }

    // ================Block Event================

    public void randomDisplayTick(World world, int x, int y, int z, Random random) {}

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float moveX,
                                    float moveY, float moveZ) {
        if (isLoaded()) return structure.onBlockActivated(world, this, x, y, z, player, side, moveX, moveY, moveZ);
        return false;
    }

    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 0;
    }

    public double getEnchantPowerBonus(World world, int x, int y, int z) {
        return 0;
    }

    public boolean isLadder() {
        if (isLoaded()) return structure.isLadder();
        return false;
    }

    public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
        if (isLoaded()) return structure.isBed(world, x, y, z, player);
        return false;
    }

    // ================Structure================

    public boolean isStructureBlock = false;

    public LittleStructure structure;

    /*
     * Removed positions are now saved relative to the current position
     * @Deprecated public LittleTilePosition pos;
     */

    public LittleTileCoord coord;

    public boolean isMainBlock = false;

//    public boolean checkForStructure() {
//        if (structure != null) return true;
//        World world = te.getWorldObj();
//        // if(!world.isRemote)
//        // {
//        ChunkCoordinates absoluteCoord = coord.getAbsolutePosition(te);
//        Chunk chunk = world.getChunkFromBlockCoords(absoluteCoord.posX, absoluteCoord.posZ);
//        if (!(chunk instanceof EmptyChunk)) {
//            TileEntity tileEntity = world.getTileEntity(absoluteCoord.posX, absoluteCoord.posY, absoluteCoord.posZ);
//            if (tileEntity instanceof TileEntityLittleTilesProxy) {
//                LittleTile tile = ((TileEntityLittleTilesProxy) tileEntity).getTile(coord.position);
//                if (tile != null && tile.isStructureBlock) {
//                    if (tile.isMainBlock) {
//                        this.structure = tile.structure;
//                        if (this.structure != null && this.structure.getTiles() != null
//                            && !this.structure.getTiles().contains(this))
//                            this.structure.getTiles().add(this);
//                    }
//                }
//            }
//
//            if (structure == null) {
//                te.removeTile(this);
////                te.update();
//            }
//
//            // pos = null;
//
//            return structure != null;
//        }
//
//        // }
//        return false;
//    }

    public boolean isLoaded() {
        return isStructureBlock /*&& checkForStructure()*/;
    }

    @Deprecated
    public static class LittleTilePosition {

        public ChunkCoordinates coord;
        public LittleTileVec position;

        public LittleTilePosition(ChunkCoordinates coord, LittleTileVec position) {
            this.coord = coord;
            this.position = position;
        }

        public LittleTilePosition(String id, NBTTagCompound nbt) {
            coord = new ChunkCoordinates(
                nbt.getInteger(id + "coX"),
                nbt.getInteger(id + "coY"),
                nbt.getInteger(id + "coZ"));
            position = new LittleTileVec(id + "po", nbt);
        }

        public LittleTilePosition(NBTTagCompound nbt) {
            this("", nbt);
        }

        public void writeToNBT(String id, NBTTagCompound nbt) {
            nbt.setInteger(id + "coX", coord.posX);
            nbt.setInteger(id + "coY", coord.posY);
            nbt.setInteger(id + "coZ", coord.posZ);
            position.writeToNBT(id + "po", nbt);
        }

        public void writeToNBT(NBTTagCompound nbt) {
            writeToNBT("", nbt);
        }

        @Override
        public String toString() {
            return "coord:" + coord + "|position:" + position;
        }

        public LittleTile.LittleTilePosition copy() {
            return new LittleTile.LittleTilePosition(new ChunkCoordinates(coord), position.copy());
        }

    }

    // ================Block================
//    private void updateSpecialHandler() {
//        if (!(block instanceof BlockAir))
//            handler = SpecialBlockHandler.getSpecialBlockHandler(block, meta);
//        updateBlockState();
//    }

    public void fillUsedIds(BitSet usedIds) {
//        for (Integer id : structures.keySet())
//            if (id >= 0)
//                usedIds.set(id);
    }

//    @Override
    public boolean canBlockBeThreaded() {
            if (LittleTiles.isAngelicaLoaded) return false;
            if (LittleTiles.isGTNHlibLoaded) return false;
            return block.getRenderType() == 0 && !(block instanceof BlockGrass);
    }
}
