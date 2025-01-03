package com.creativemd.littletiles.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.api.ILittlePlacer;
import com.creativemd.littletiles.common.api.ILittleTool;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.place.PlacementMode;
import com.creativemd.littletiles.common.utils.place.PlacementPosition;
import com.creativemd.littletiles.common.utils.place.PlacementPreview;
import com.creativemd.littletiles.common.utils.place.fixed.InsideFixedHandler;
import com.creativemd.littletiles.common.utils.place.fixed.SecondModeHandler;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.creativemd.littletiles.common.blocks.BlockTile;
import com.creativemd.littletiles.common.blocks.ILittleTile;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesProxy;
import com.creativemd.littletiles.common.utils.small.LittleTileBox;
import com.creativemd.littletiles.common.utils.small.LittleTileSize;
import com.creativemd.littletiles.common.utils.small.LittleTileVec;
import com.creativemd.littletiles.utils.InsideShiftHandler;
import com.creativemd.littletiles.utils.PreviewTile;
import com.creativemd.littletiles.utils.ShiftHandler;

import javax.annotation.Nullable;

/** This class does all calculations on where to place a block. Used for rendering preview and placing **/
public class PlacementHelper {

    private static PlacementHelper instance;
    private static LittlePreviews lastPreviews;
    private static NBTTagCompound lastCached;
    private static boolean lastLowResolution;

    public static PlacementHelper getInstance(EntityPlayer player) {
        if (instance == null) instance = new PlacementHelper(player);
        else {
            instance.player = player;
            instance.world = player.worldObj;
        }
        return instance;
    }

    public EntityPlayer player;
    public World world;

    public static void removeCache() {
        lastCached = null;
        lastPreviews = null;
    }

    public PlacementHelper(EntityPlayer player) {
        this.player = player;
        this.world = player.worldObj;
    }

    public static ILittleTile getLittleInterface(ItemStack stack) {
        if (stack == null) return null;
        if (stack.getItem() instanceof ILittleTile) return (ILittleTile) stack.getItem();
        if (Block.getBlockFromItem(stack.getItem()) instanceof ILittleTile)
            return (ILittleTile) Block.getBlockFromItem(stack.getItem());
        return null;
    }

    public static ILittlePlacer getLittlePlacerInterface(ItemStack stack) {
        if (stack == null)
            return null;
        if (stack.getItem() instanceof ILittlePlacer)
            return (ILittlePlacer) stack.getItem();
        if (Block.getBlockFromItem(stack.getItem()) instanceof ILittlePlacer)
            return (ILittlePlacer) Block.getBlockFromItem(stack.getItem());
        return null;
    }

    public static boolean isLittleBlock(ItemStack stack) {
        if (stack == null) return false;
        if (stack.getItem() instanceof ILittleTile)
            return ((ILittleTile) stack.getItem()).getLittlePreview(stack) != null;
        if (Block.getBlockFromItem(stack.getItem()) instanceof ILittleTile)
            return ((ILittleTile) Block.getBlockFromItem(stack.getItem())).getLittlePreview(stack) != null;
        return false;
    }

    public static boolean isLittlePlacer(ItemStack stack) {
        if (stack == null)
            return false;
        if (stack.getItem() instanceof ILittlePlacer)
            return ((ILittlePlacer) stack.getItem()).hasLittlePreview(stack);
        if (Block.getBlockFromItem(stack.getItem()) instanceof ILittlePlacer)
            return ((ILittlePlacer) Block.getBlockFromItem(stack.getItem())).hasLittlePreview(stack);
        return false;
    }

    public static LittleTileVec getInternalOffset(ArrayList<LittleTilePreview> tiles) {
        byte minX = LittleTile.maxPos;
        byte minY = LittleTile.maxPos;
        byte minZ = LittleTile.maxPos;
        for (LittleTilePreview tile : tiles) {
            if (tile == null) return new LittleTileVec(0, 0, 0);
            if (tile.box != null) {
                minX = (byte) Math.min(minX, tile.box.minX);
                minY = (byte) Math.min(minY, tile.box.minY);
                minZ = (byte) Math.min(minZ, tile.box.minZ);
            }
        }
        return new LittleTileVec(minX, minY, minZ);
    }

    public static LittleVec getInternalOffset(ILittlePlacer iTile, ItemStack stack, LittlePreviews tiles, LittleGridContext original) {
        LittleVec offset = iTile.getCachedOffset(stack);
        if (offset != null) {
            if (tiles.getContext() != original)
                offset.convertTo(original, tiles.getContext());
            return offset;
        }
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        for (LittlePreview preview : tiles.allPreviews()) {
            if (preview.box != null) {
                minX = Math.min(minX, preview.box.minX);
                minY = Math.min(minY, preview.box.minY);
                minZ = Math.min(minZ, preview.box.minZ);
            }
        }
        return new LittleVec(minX, minY, minZ);
    }

    public static LittleVec getSize(ILittlePlacer iTile, ItemStack stack, LittlePreviews tiles, boolean allowLowResolution, LittleGridContext original) {
        LittleVec cached = iTile.getCachedSize(stack);
        if (cached != null) {
            if (tiles.getContext() != original)
                cached.convertTo(original, tiles.getContext());
            return cached;
        }
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        LittleVec size = new LittleVec(0, 0, 0);
        for (LittlePreview preview : tiles.allPreviews()) {
            minX = Math.min(minX, preview.box.minX);
            minY = Math.min(minY, preview.box.minY);
            minZ = Math.min(minZ, preview.box.minZ);
            maxX = Math.max(maxX, preview.box.maxX);
            maxY = Math.max(maxY, preview.box.maxY);
            maxZ = Math.max(maxZ, preview.box.maxZ);
        }
        return new LittleVec(maxX - minX, maxY - minY, maxZ - minZ).max(size);
    }

    // @TODO fix this for the calling class
    public ArrayList<PreviewTile> getPreviewTiles(ItemStack stack, int x, int y, int z, Vec3 playerPos, Vec3 hitVec,
            ForgeDirection side, boolean customPlacement, boolean inside) // , ForgeDirection rotation, ForgeDirection
                                                                          // rotation2)
    {
        ArrayList<ShiftHandler> shifthandlers = new ArrayList<>();
        ArrayList<PreviewTile> preview = new ArrayList<>();
        ArrayList<LittleTilePreview> tiles = null;

        LittleTilePreview tempPreview = null;
        ILittleTile iTile = PlacementHelper.getLittleInterface(stack);

        if (iTile != null) tiles = iTile.getLittlePreview(stack);

        if (tiles != null) {
            // @TODO fix this for the calling class
//            LittleTileSize size = getSize(tiles);
            LittleTileSize size = new LittleTileSize(0, 0, 0);

            // size.rotateSize(rotation);
            // size.rotateSize(rotation2);
            // size.rotateSize(rotation2.getRotation(ForgeDirection.DOWN));

            // size.rotateSize(side);

            if (tiles.size() == 1) shifthandlers.addAll(tiles.get(0).shifthandlers);

            shifthandlers.add(new InsideShiftHandler());

            LittleTileBox box = getTilesBox(size, hitVec, x, y, z, side, customPlacement, inside);
            LittleTileVec internalOffset = getInternalOffset(tiles);
            internalOffset.invert();
            // box.addOffset(new LittleTileVec(-LittleTile.maxPos/2, -LittleTile.maxPos/2, -LittleTile.maxPos/2));

            boolean canPlaceNormal = false;

            if (!customPlacement && player.isSneaking()) {
                if (!inside && !canBePlacedInside(x, y, z, hitVec, side)) {
                    switch (side) {
                        case EAST:
                            x++;
                            break;
                        case WEST:
                            x--;
                            break;
                        case UP:
                            y++;
                            break;
                        case DOWN:
                            y--;
                            break;
                        case SOUTH:
                            z++;
                            break;
                        case NORTH:
                            z--;
                            break;
                        default:
                            break;
                    }
                }

                if (tiles.size() > 0 && tiles.get(0).box != null) {
                    Block block = world.getBlock(x, y, z);
                    if (block.isReplaceable(world, x, y, z) || block instanceof BlockTile) {
                        TileEntity te = world.getTileEntity(x, y, z);
                        canPlaceNormal = true;
                        if (te instanceof TileEntityLittleTilesProxy) {
                            TileEntityLittleTilesProxy teTiles = (TileEntityLittleTilesProxy) te;
                            for (LittleTilePreview tile : tiles) {
                                if (!teTiles.isSpaceForLittleTile(tile.box)) {
                                    canPlaceNormal = false;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!canPlaceNormal) {

                    for (ShiftHandler shiftHandler : shifthandlers) {
                        shiftHandler.init(world, x, y, z);
                    }

                    LittleTileVec hit = getHitVec(hitVec, x, y, z, side, customPlacement, inside);
                    ShiftHandler handler = null;
                    double distance = 2;
                    for (ShiftHandler shifthandler : shifthandlers) {
                        double tempDistance = shifthandler.getDistance(hit);
                        if (tempDistance < distance) {
                            distance = tempDistance;
                            handler = shifthandler;
                        }
                    }

                    if (handler != null) {
                        box = handler.getNewPosition(world, x, y, z, box);
                    }
                }
            }

            LittleTileVec offset = box.getMinVec();

            offset.addVec(internalOffset);

            for (LittleTilePreview tile : tiles) {
                if (tile != null) {
                    if (tile.box == null) {
                        preview.add(new PreviewTile(box.copy(), tile));
                    } else {
                        if (!canPlaceNormal) tile.box.addOffset(offset);
                        // tile.box.rotateBox(rotation);
                        // tile.box.rotateBox(rotation2);
                        // tile.box.rotateBox(rotation2.getRotation(ForgeDirection.DOWN));
                        preview.add(new PreviewTile(tile.box, tile));
                    }
                }
            }

            LittleStructure structure = iTile.getLittleStructure(stack);
            if (structure != null) {
                // ArrayList<LittleTileBox> highlightedBoxes = structure.getSpecialTiles();
                ArrayList<PreviewTile> newBoxes = structure.getSpecialTiles();

                for (PreviewTile newBox : newBoxes) {
                    if (!canPlaceNormal) newBox.box.addOffset(offset);
                }

                preview.addAll(newBoxes);

                /*
                 * for (int i = 0; i < highlightedBoxes.size(); i++) { if(!canPlaceNormal)
                 * highlightedBoxes.get(i).addOffset(offset); //tile.box.rotateBox(rotation);
                 * //tile.box.rotateBox(rotation2); //tile.box.rotateBox(rotation2.getRotation(ForgeDirection.DOWN));
                 * PreviewTile previewTile = new PreviewTile(highlightedBoxes.get(i), null); previewTile.color =
                 * Vec3.createVectorHelper(1, 0, 0); preview.add(previewTile); }
                 */
            }
        }

        return preview;
    }

    public static LittleBox getTilesBoxByPosition(LittleAbsoluteVec pos, LittleVec size, boolean centered, @Nullable EnumFacing facing, PlacementMode mode) {
        LittleVec temp = pos.getVec().copy();
        if (centered) {
            LittleVec center = size.calculateCenter();
            LittleVec centerInv = size.calculateInvertedCenter();

            // @TODO implement placeinside
            if (mode.placeInside)
                throw new RuntimeException("havent implemented place inside yet");
//                facing = facing.getOpposite();

            // Make hit the center of the Box
            switch (facing) {
                case EAST:
                    temp.x += center.x;
                    break;
                case WEST:
                    temp.x -= centerInv.x;
                    break;
                case UP:
                    temp.y += center.y;
                    break;
                case DOWN:
                    temp.y -= centerInv.y;
                    break;
                case SOUTH:
                    temp.z += center.z;
                    break;
                case NORTH:
                    temp.z -= centerInv.z;
                    break;
                default:
                    break;
            }
        }
        return new LittleBox(temp, size.x, size.y, size.z);
    }

    public LittleTileBox getTilesBox(LittleTileSize size, Vec3 hitVec, int x, int y, int z, ForgeDirection side,
            boolean customPlacement, boolean inside) {
        LittleTileVec hit = getHitVec(hitVec, x, y, z, side, customPlacement, inside);
        LittleTileVec center = size.calculateCenter();
        LittleTileVec centerInv = size.calculateInvertedCenter();
        // hit.addVec(center);
        // Make hit the center of the Box
        switch (side) {
            case EAST:
                hit.x += center.x;
                break;
            case WEST:
                hit.x -= centerInv.x;
                break;
            case UP:
                hit.y += center.y;
                break;
            case DOWN:
                hit.y -= centerInv.y;
                break;
            case SOUTH:
                hit.z += center.z;
                break;
            case NORTH:
                hit.z -= centerInv.z;
                break;
            default:
                break;
        }
        return new LittleTileBox(hit, size);
    }

    public boolean canBePlacedInsideBlock(int x, int y, int z) {
        TileEntity tileEntity = player.worldObj.getTileEntity(x, y, z);
        return tileEntity instanceof TileEntityLittleTilesProxy;
    }

    public boolean canBePlacedInside(int x, int y, int z, Vec3 hitVec, ForgeDirection side) {
        TileEntity tileEntity = player.worldObj.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityLittleTilesProxy) {
            switch (side) {
                case EAST:
                case WEST:
                    return (int) hitVec.xCoord != hitVec.xCoord;
                case UP:
                case DOWN:
                    return (int) hitVec.yCoord != hitVec.yCoord;
                case SOUTH:
                case NORTH:
                    return (int) hitVec.zCoord != hitVec.zCoord;
                default:
                    return false;
            }
        }
        /*
         * if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) { p_77648_7_ = 1; } else if
         * (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush &&
         * !block.isReplaceable(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) { }
         */
        return false;
    }

    public static LittleAbsoluteVec getAbsoluteHitVec(BlockPos blockPos, LittleGridContext context, boolean isInsideOfBlock) {
        LittleAbsoluteVec pos = new LittleAbsoluteVec(blockPos, context);

//        if (!isInsideOfBlock)
//            pos.getVec().set(result.sideHit.getAxis(), result.sideHit.getAxisDirection() == AxisDirection.POSITIVE ? 0 : context.size);

        return pos;
    }

    public LittleTileVec getHitVec(Vec3 hitVec, int x, int y, int z, ForgeDirection side, boolean customPlacement,
            boolean isInside) {
        if (customPlacement && !isInside) {
            double posX = hitVec.xCoord - x;
            double posY = hitVec.yCoord - y;
            double posZ = hitVec.zCoord - z;

            LittleTileVec vec = new LittleTileVec((int) (posX * 16), (int) (posY * 16), (int) (posZ * 16));
            if (!canBePlacedInside(x, y, z, hitVec, side)) {
                switch (side) {
                    case EAST:
                        vec.x -= 16;
                        break;
                    case WEST:
                        vec.x += 16;
                        break;
                    case UP:
                        vec.y -= 16;
                        break;
                    case DOWN:
                        vec.y += 16;
                        break;
                    case SOUTH:
                        vec.z -= 16;
                        break;
                    case NORTH:
                        vec.z += 16;
                        break;
                    default:
                        break;

                }
            }
            return vec;
        }
        double posX = hitVec.xCoord - x;
        // if(hitVec.xCoord < 0)
        // posX = 1-posX;
        double posY = hitVec.yCoord - y;
        // if(hitVec.yCoord < 0)
        // posY = 1-posY;
        double posZ = hitVec.zCoord - z;
        // if(hitVec.zCoord < 0)
        // posZ = 1-posZ;
        LittleTileVec vec = new LittleTileVec((int) (posX * 16), (int) (posY * 16), (int) (posZ * 16));
        if (!customPlacement && !canBePlacedInside(x, y, z, hitVec, side)) {
            switch (side) {
                case EAST:
                    // vec.x = 15;
                    vec.x = 0;
                    break;
                case WEST:
                    vec.x = 16;
                    // if(x < 0)
                    // vec.x = 0;
                    break;
                case UP:
                    // vec.y = 15;
                    vec.y = 0;
                    break;
                case DOWN:
                    vec.y = 16;
                    break;
                case SOUTH:
                    // vec.z = 15;
                    vec.z = 0;
                    break;
                case NORTH:
                    vec.z = 16;
                    // if(z < 0)
                    // vec.z = 0;
                    break;
                default:
                    break;

            }
        }
        return vec;
    }
    @SideOnly(Side.CLIENT)
    public PlacementPosition getPosition(World world, MovingObjectPosition moving, LittleGridContext context, ILittleTool tool, ItemStack stack) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ForgeDirection sideHit = ForgeDirection.getOrientation(moving.sideHit);
        int x = moving.blockX;
        int y = moving.blockY;
        int z = moving.blockZ;

        boolean canBePlacedInsideBlock = true;
        if (!canBePlacedInside(x, y, z, moving.hitVec, sideHit)) {
            switch (sideHit) {
                case EAST:
                    x++;
                    break;
                case WEST:
                    x--;
                    break;
                case UP:
                    y++;
                    break;
                case DOWN:
                    y--;
                    break;
                case SOUTH:
                    z++;
                    break;
                case NORTH:
                    z--;
                    break;
                default:
                    break;
            }

            canBePlacedInsideBlock = false;
        }

        BlockPos pos = new BlockPos(x, y, z);

        EnumFacing facing;
        switch (sideHit) {
            case EAST:
                facing = EnumFacing.EAST;
                break;
            case WEST:
                facing = EnumFacing.WEST;
                break;
            case UP:
                facing = EnumFacing.UP;
                break;
            case DOWN:
                facing = EnumFacing.DOWN;
                break;
            case SOUTH:
                facing = EnumFacing.SOUTH;
                break;
            case NORTH:
                facing = EnumFacing.NORTH;
                break;
            default:
                throw new RuntimeException("no detected side hit");
        }

        PlacementPosition result = new PlacementPosition(pos, getHitVec(moving, context, false).getVecContext(), facing);

        // @TODO implement this, not sure what it does right now
//        if (tool instanceof ILittlePlacer && stack != null && (LittleAction.isUsingSecondMode(player) != ((ILittlePlacer) tool).snapToGridByDefault(stack))) {
//            Vec3d position = player.getPositionEyes(TickUtils.getPartialTickTime());
//            double d0 = player.capabilities.isCreativeMode ? 5.0F : 4.5F;
//            Vec3d temp = player.getLook(TickUtils.getPartialTickTime());
//            Vec3d look = position.addVector(temp.x * d0, temp.y * d0, temp.z * d0);
//            position = position.subtract(pos.getX(), pos.getY(), pos.getZ());
//            look = look.subtract(pos.getX(), pos.getY(), pos.getZ());
//            List<LittleRenderBox> cubes = ((ILittlePlacer) tool).getPositingCubes(world, pos, stack);
//            if (cubes != null)
//                result.positingCubes = cubes;
//        }

        return result;
    }

     /*
     * @param centeredif the previews should be centered
     * @param facing if centered is true it will be used to apply the offset
     * @param fixed if the previews should keep its original boxes
     * */
    public static PlacementPreview getPreviews(World world, ItemStack stack, PlacementPosition position, boolean centered, boolean fixed, boolean allowLowResolution, PlacementMode mode) {
        ILittlePlacer iTile = PlacementHelper.getLittlePlacerInterface(stack);

        LittlePreviews tiles = allowLowResolution == lastLowResolution && iTile.shouldCache() && lastCached != null && lastCached.equals(stack.getTagCompound()) ? lastPreviews.copy() : null;
        if (tiles == null && iTile != null)
            tiles = iTile.getLittlePreview(stack, allowLowResolution);

        PlacementPreview result = getPreviews(world, tiles, iTile.getPreviewsContext(stack), stack, position, centered, fixed, allowLowResolution, mode);

        if (result != null) {
            if (stack.getTagCompound() == null) {
                lastCached = null;
                lastPreviews = null;
            } else {
//                lastLowResolution = allowLowResolution;
//                lastCached = stack.getTagCompound().copy();
                lastPreviews = tiles.copy();
            }
        }
        return result;
    }

    public static PlacementPreview getPreviews(World world, @Nullable LittlePreviews tiles, LittleGridContext original, ItemStack stack, PlacementPosition position, boolean centered, boolean fixed, boolean allowLowResolution, PlacementMode mode) {
        ILittlePlacer iTile = PlacementHelper.getLittlePlacerInterface(stack);

        if (tiles != null && (!tiles.isEmpty() || tiles.hasChildren())) {

            if (tiles.isAbsolute())
                return new PlacementPreview(world, tiles, mode, tiles.getSurroundingBox(), true, tiles.getBlockPos(), null, position.facing);

            tiles.forceContext(position);
            LittleGridContext context = tiles.getContext();

            LittleVec size = getSize(iTile, stack, tiles, allowLowResolution, original);

            List<SecondModeHandler> shifthandlers = new ArrayList<SecondModeHandler>();

            boolean singleMode = tiles.totalSize() == 1;

            if (singleMode) {
                shifthandlers.add(new InsideFixedHandler());
                centered = true;
            }

            LittleBox box = getTilesBoxByPosition(position, size, centered, position.facing, mode);

            boolean canBePlaceFixed = false;

            if (fixed) {
                canBePlaceFixed = !singleMode && LittleAction.canPlaceInside(tiles, world, position.getPos(), mode.placeInside);

                if (!canBePlaceFixed)
                    for (int i = 0; i < shifthandlers.size(); i++)
                        box = shifthandlers.get(i).getBox(world, position.getPos(), context, box);

            }

            LittleAbsoluteVec offset = new LittleAbsoluteVec(position.getPos(), context, box.getMinVec());
            LittleVec internalOffset = getInternalOffset(iTile, stack, tiles, original);
            internalOffset.invert();
            offset.getVec().add(internalOffset);

            // @TODO implement placeinside
//            if ((canBePlaceFixed || (fixed && singleMode)) && mode.placeInside)
//                if (position.getVec().get(position.facing.getAxis()) % context.size == 0)
//                    offset.getVec().add(position.facing.getOpposite());

            return new PlacementPreview(world, tiles, mode, box, canBePlaceFixed, offset.getPos(), offset.getVec(), position.facing);
        }

        return null;
    }

    public static LittleAbsoluteVec getHitVec(MovingObjectPosition movingObjectPosition, LittleGridContext context, boolean isInsideOfBlock) {
        LittleAbsoluteVec pos = new LittleAbsoluteVec(movingObjectPosition, context);

        if (!isInsideOfBlock)
            pos.getVec().set(EnumFacingProxy.fromSideHitInt(movingObjectPosition.sideHit).getAxis(), EnumFacingProxy.fromSideHitInt(movingObjectPosition.sideHit).getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE ? 0 : context.size);

        return pos;
    }

}
