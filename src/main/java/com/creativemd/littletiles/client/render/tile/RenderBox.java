package com.creativemd.littletiles.client.render.tile;

import com.cleanroommc.modularui.utils.Color;
import com.creativemd.creativecore.common.utils.ColorUtils;
import com.creativemd.littletiles.client.render.block.BakedQuadProxy;
import com.creativemd.littletiles.client.render.face.FaceRenderType;
import com.creativemd.littletiles.client.render.face.IFaceRenderType;
import com.creativemd.littletiles.common.tile.math.box.AlignedBox;
import com.creativemd.littletiles.common.tile.math.vec.VectorFan;
import com.creativemd.littletiles.common.utils.math.Ray2dProxy;
import com.creativemd.littletiles.common.utils.math.geo.NormalPlaneProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.block.Block;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderBox extends AlignedBox {

    private static final VectorFan DOWN = new VectorFanSimple(new Vector3f[] { new Vector3f(0, 0, 1), new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 0, 1) });
    private static final VectorFan UP = new VectorFanSimple(new Vector3f[] { new Vector3f(0, 1, 0), new Vector3f(0, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 0) });
    private static final VectorFan NORTH = new VectorFanSimple(new Vector3f[] { new Vector3f(1, 1, 0), new Vector3f(1, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0) });
    private static final VectorFan SOUTH = new VectorFanSimple(new Vector3f[] { new Vector3f(0, 1, 1), new Vector3f(0, 0, 1), new Vector3f(1, 0, 1), new Vector3f(1, 1, 1) });
    private static final VectorFan WEST = new VectorFanSimple(new Vector3f[] { new Vector3f(0, 1, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 1) });
    private static final VectorFan EAST = new VectorFanSimple(new Vector3f[] { new Vector3f(1, 1, 1), new Vector3f(1, 0, 1), new Vector3f(1, 0, 0), new Vector3f(1, 1, 0) });


    public Block block;
    public int meta = 0;
    public int color = -1;
//
//    public boolean keepVU = false;
//    public boolean allowOverlap = false;
//    public boolean doesNeedQuadUpdate = true;
//    public boolean needsResorting = false;
//    public boolean emissive = false;
//
    private IFaceRenderType renderEast = FaceRenderType.INSIDE_RENDERED;
    private IFaceRenderType renderWest = FaceRenderType.INSIDE_RENDERED;
    private IFaceRenderType renderUp = FaceRenderType.INSIDE_RENDERED;
    private IFaceRenderType renderDown = FaceRenderType.INSIDE_RENDERED;
    private IFaceRenderType renderSouth = FaceRenderType.INSIDE_RENDERED;
    private IFaceRenderType renderNorth = FaceRenderType.INSIDE_RENDERED;

    private Object quadEast = null;
    private Object quadWest = null;
    private Object quadUp = null;
    private Object quadDown = null;
    private Object quadSouth = null;
    private Object quadNorth = null;
//
//    public Object customData;
//
//    public RenderBox(AlignedBox cube, RenderBox box) {
//        super(cube);
//        this.block = box.block;
//        this.meta = box.meta;
//        this.color = box.color;
//        this.renderEast = box.renderEast;
//        this.renderWest = box.renderWest;
//        this.renderUp = box.renderUp;
//        this.renderDown = box.renderDown;
//        this.renderSouth = box.renderSouth;
//        this.renderNorth = box.renderNorth;
//    }
//
    public RenderBox(AlignedBox cube, Block block, int meta) {
        super(cube);
        this.block = block;
        this.meta = meta;
    }
//
//    public RenderBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Block block) {
//        super(minX, minY, minZ, maxX, maxY, maxZ);
//        this.block = block;
//    }
//
//    public RenderBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Block block, int meta) {
//        super(minX, minY, minZ, maxX, maxY, maxZ);
//        this.block = block;
//        this.meta = meta;
//    }

    public RenderBox setColor(int color) {
        this.color = color;
        return this;
    }
//
//    public RenderBox setKeepUV(boolean keep) {
//        this.keepVU = keep;
//        return this;
//    }
//
//    public void setQuad(EnumFacingProxy facing, List<BakedQuad> quads) {
//        Object quad = quads == null || quads.isEmpty() ? null : quads.size() == 1 ? quads.get(0) : quads;
//        switch (facing) {
//        case DOWN:
//            quadDown = quad;
//            break;
//        case EAST:
//            quadEast = quad;
//            break;
//        case NORTH:
//            quadNorth = quad;
//            break;
//        case SOUTH:
//            quadSouth = quad;
//            break;
//        case UP:
//            quadUp = quad;
//            break;
//        case WEST:
//            quadWest = quad;
//            break;
//        }
//    }
//
//    public Object getQuad(EnumFacingProxy facing) {
//        switch (facing) {
//        case DOWN:
//            return quadDown;
//        case EAST:
//            return quadEast;
//        case NORTH:
//            return quadNorth;
//        case SOUTH:
//            return quadSouth;
//        case UP:
//            return quadUp;
//        case WEST:
//            return quadWest;
//        }
//        return null;
//    }
//
//    public int countQuads() {
//        int quads = 0;
//        if (quadUp != null)
//            quads += quadUp instanceof List ? ((List) quadUp).size() : 1;
//        if (quadDown != null)
//            quads += quadDown instanceof List ? ((List) quadDown).size() : 1;
//        if (quadEast != null)
//            quads += quadEast instanceof List ? ((List) quadEast).size() : 1;
//        if (quadWest != null)
//            quads += quadWest instanceof List ? ((List) quadWest).size() : 1;
//        if (quadSouth != null)
//            quads += quadSouth instanceof List ? ((List) quadSouth).size() : 1;
//        if (quadNorth != null)
//            quads += quadNorth instanceof List ? ((List) quadNorth).size() : 1;
//        return quads;
//    }
//
//    public IBlockState getModelState(IBlockState state, IBlockAccess world, BlockPos pos) {
//        return block.getExtendedState(state, world, pos);
//    }
//
//    public IBlockState getBlockState() {
//        if (meta != -1)
//            return BlockUtils.getState(block, meta);
//        else
//            return block.getDefaultState();
//    }
//
//    public IBlockState getBlockState(Block block) {
//        if (meta != -1)
//            return BlockUtils.getState(block, meta);
//        else
//            return block.getDefaultState();
//    }
//
//    public void setType(EnumFacingProxy facing, IFaceRenderType renderer) {
//        switch (facing) {
//        case DOWN:
//            renderDown = renderer;
//            break;
//        case EAST:
//            renderEast = renderer;
//            break;
//        case NORTH:
//            renderNorth = renderer;
//            break;
//        case SOUTH:
//            renderSouth = renderer;
//            break;
//        case UP:
//            renderUp = renderer;
//            break;
//        case WEST:
//            renderWest = renderer;
//            break;
//        }
//    }
//
    public IFaceRenderType getType(EnumFacingProxy facing) {
        switch (facing) {
        case DOWN:
            return renderDown;
        case EAST:
            return renderEast;
        case NORTH:
            return renderNorth;
        case SOUTH:
            return renderSouth;
        case UP:
            return renderUp;
        case WEST:
            return renderWest;
        }
        return FaceRenderType.INSIDE_RENDERED;
    }

    public boolean renderSide(EnumFacingProxy facing) {
        switch (facing) {
        case DOWN:
            return renderDown.shouldRender();
        case EAST:
            return renderEast.shouldRender();
        case NORTH:
            return renderNorth.shouldRender();
        case SOUTH:
            return renderSouth.shouldRender();
        case UP:
            return renderUp.shouldRender();
        case WEST:
            return renderWest.shouldRender();
        }
        return true;
    }

//    public boolean intersectsWithFace(EnumFacingProxy facing, RenderInformationHolder holder, BlockPos offset) {
//        switch (facing.getAxis()) {
//        case X:
//            return holder.maxY > this.minY - offset.getY() && holder.minY < this.maxY - offset.getY() && holder.maxZ > this.minZ - offset.getZ() && holder.minZ < this.maxZ - offset.getZ();
//        case Y:
//            return holder.maxX > this.minX - offset.getX() && holder.minX < this.maxX - offset.getX() && holder.maxZ > this.minZ - offset.getZ() && holder.minZ < this.maxZ - offset.getZ();
//        case Z:
//            return holder.maxX > this.minX - offset.getX() && holder.minX < this.maxX - offset.getX() && holder.maxY > this.minY - offset.getY() && holder.minY < this.maxY - offset.getY();
//        }
//        return false;
//    }

    protected Object getRenderQuads(EnumFacingProxy facing) {
        if (getType(facing).hasCachedFans())
            return getType(facing).getCachedFans();
        switch (facing) {
        case DOWN:
            return DOWN;
        case EAST:
            return EAST;
        case NORTH:
            return NORTH;
        case SOUTH:
            return SOUTH;
        case UP:
            return UP;
        case WEST:
            return WEST;
        }
        return null;
    }

//    protected List<BakedQuad> getBakedQuad(IBlockAccess world, IBakedModel blockModel, IBlockState state, EnumFacingProxy facing, BlockPos pos, BlockRenderLayer layer, long rand) {
//        return OptifineHelper.getRenderQuads(blockModel.getQuads(state, facing, rand), world, state, pos, facing, layer, rand);
//    }

    protected float getOffsetX() {
        return minX;
    }

    protected float getOffsetY() {
        return minY;
    }

    protected float getOffsetZ() {
        return minZ;
    }

    protected float getOverallScale(EnumFacingProxy facing) {
        return getType(facing).getScale();
    }

    protected float getScaleX() {
        return maxX - minX;
    }

    protected float getScaleY() {
        return maxY - minY;
    }

    protected float getScaleZ() {
        return maxZ - minZ;
    }

    protected boolean scaleAndOffsetQuads(EnumFacingProxy facing) {
        return true;
    }

    protected boolean onlyScaleOnceNoOffset(EnumFacingProxy facing) {
        return getType(facing).hasCachedFans();
    }
//
//    public List<BakedQuad> getBakedQuad(IBlockAccess world, @Nullable BlockPos pos, BlockPos offset, IBlockState state, IBakedModel blockModel, EnumFacingProxy facing, BlockRenderLayer layer, long rand, boolean overrideTint, int defaultColor) {
//        List<BakedQuad> blockQuads = getBakedQuad(world, blockModel, state, facing, pos, layer, rand);
//
//        if (blockQuads.isEmpty())
//            return Collections.emptyList();
//
//        RenderInformationHolder holder = new RenderInformationHolder(facing, this.color != -1 ? this.color : defaultColor);
//        holder.offset = offset;
//
//        List<BakedQuad> quads = new ArrayList<>();
//        for (int i = 0; i < blockQuads.size(); i++) {
//
//            holder.setQuad(blockQuads.get(i), overrideTint, defaultColor);
//            if (!needsResorting && OptifineHelper.isEmissive(holder.quad.getSprite()))
//                needsResorting = true;
//
//            VertexFormat format = holder.quad.getFormat();
//            int[] data = holder.quad.getVertexData();
//
//            int index = 0;
//            int uvIndex = index + format.getUvOffsetById(0) / 4;
//            float tempMinX = Float.intBitsToFloat(data[index]);
//            float tempMinY = Float.intBitsToFloat(data[index + 1]);
//            float tempMinZ = Float.intBitsToFloat(data[index + 2]);
//
//            float tempU = Float.intBitsToFloat(data[uvIndex]);
//            float tempV = Float.intBitsToFloat(data[uvIndex + 1]);
//
//            holder.uvInverted = false;
//
//            index = 1 * format.getIntegerSize();
//            uvIndex = index + format.getUvOffsetById(0) / 4;
//            if (tempMinX != Float.intBitsToFloat(data[index])) {
//                if (tempU != Float.intBitsToFloat(data[uvIndex]))
//                    holder.uvInverted = Axis.X != RotationUtils.getUAxisFromFacing(facing);
//                else
//                    holder.uvInverted = Axis.X != RotationUtils.getVAxisFromFacing(facing);
//            } else if (tempMinY != Float.intBitsToFloat(data[index + 1])) {
//                if (tempU != Float.intBitsToFloat(data[uvIndex]))
//                    holder.uvInverted = Axis.Y != RotationUtils.getUAxisFromFacing(facing);
//                else
//                    holder.uvInverted = Axis.Y != RotationUtils.getVAxisFromFacing(facing);
//            } else {
//                if (tempU != Float.intBitsToFloat(data[uvIndex]))
//                    holder.uvInverted = Axis.Z != RotationUtils.getUAxisFromFacing(facing);
//                else
//                    holder.uvInverted = Axis.Z != RotationUtils.getVAxisFromFacing(facing);
//            }
//
//            index = 2 * format.getIntegerSize();
//            float tempMaxX = Float.intBitsToFloat(data[index]);
//            float tempMaxY = Float.intBitsToFloat(data[index + 1]);
//            float tempMaxZ = Float.intBitsToFloat(data[index + 2]);
//
//            holder.setBounds(tempMinX, tempMinY, tempMinZ, tempMaxX, tempMaxY, tempMaxZ);
//
//            // Check if it is intersecting, otherwise there is no need to render it
//            if (!intersectsWithFace(facing, holder, offset))
//                continue;
//
//            uvIndex = format.getUvOffsetById(0) / 4;
//            float u1 = Float.intBitsToFloat(data[uvIndex]);
//            float v1 = Float.intBitsToFloat(data[uvIndex + 1]);
//            uvIndex = 2 * format.getIntegerSize() + format.getUvOffsetById(0) / 4;
//            float u2 = Float.intBitsToFloat(data[uvIndex]);
//            float v2 = Float.intBitsToFloat(data[uvIndex + 1]);
//
//            if (holder.uvInverted) {
//                holder.sizeU = RotationUtils.getVFromFacing(facing, tempMinX, tempMinY, tempMinZ) < RotationUtils.getVFromFacing(facing, tempMaxX, tempMaxY, tempMaxZ) ? u2 - u1 : u1 - u2;
//                holder.sizeV = RotationUtils.getUFromFacing(facing, tempMinX, tempMinY, tempMinZ) < RotationUtils.getUFromFacing(facing, tempMaxX, tempMaxY, tempMaxZ) ? v2 - v1 : v1 - v2;
//            } else {
//                holder.sizeU = RotationUtils.getUFromFacing(facing, tempMinX, tempMinY, tempMinZ) < RotationUtils.getUFromFacing(facing, tempMaxX, tempMaxY, tempMaxZ) ? u2 - u1 : u1 - u2;
//                holder.sizeV = RotationUtils.getVFromFacing(facing, tempMinX, tempMinY, tempMinZ) < RotationUtils.getVFromFacing(facing, tempMaxX, tempMaxY, tempMaxZ) ? v2 - v1 : v1 - v2;
//            }
//
//            Object renderQuads = getRenderQuads(holder.facing);
//            if (renderQuads instanceof List)
//                for (int j = 0; j < ((List<VectorFan>) renderQuads).size(); j++)
//                    ((List<VectorFan>) renderQuads).get(j).generate(holder, quads);
//            else if (renderQuads instanceof VectorFan)
//                ((VectorFan) renderQuads).generate(holder, quads);
//        }
//        return quads;
//
//    }
//
//    public void deleteQuadCache() {
//        doesNeedQuadUpdate = true;
//        quadEast = null;
//        quadWest = null;
//        quadUp = null;
//        quadDown = null;
//        quadSouth = null;
//        quadNorth = null;
//    }
//
    protected boolean previewScalingAndOffset() {
        return true;
    }

    public float getPreviewOffX() {
        return minX;
    }

    public float getPreviewOffY() {
        return minY;
    }

    public float getPreviewOffZ() {
        return minZ;
    }

    public float getPreviewScaleX() {
        return maxX - minX;
    }

    public float getPreviewScaleY() {
        return maxY - minY;
    }

    public float getPreviewScaleZ() {
        return maxZ - minZ;
    }

    public void renderPreview(double x, double y, double z, int alpha) {
        int red = Color.getRed(color);
        int green = Color.getGreen(color);
        int blue = Color.getBlue(color);

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        if (previewScalingAndOffset()) {
            for (int i = 0; i < EnumFacingProxy.values().length; i++) {
                Object renderQuads = getRenderQuads(EnumFacingProxy.values()[i]);
                if (renderQuads instanceof List)
                    for (int j = 0; j < ((List<VectorFan>) renderQuads).size(); j++)
                        ((List<VectorFan>) renderQuads).get(j)
                            .renderPreview(getPreviewOffX(), getPreviewOffY(), getPreviewOffZ(), getPreviewScaleX(), getPreviewScaleY(), getPreviewScaleZ(), red, green, blue, alpha);
                else if (renderQuads instanceof VectorFan)
                    ((VectorFan) renderQuads)
                        .renderPreview(getPreviewOffX(), getPreviewOffY(), getPreviewOffZ(), getPreviewScaleX(), getPreviewScaleY(), getPreviewScaleZ(), red, green, blue, alpha);
            }
        } else {
            for (int i = 0; i < EnumFacingProxy.values().length; i++) {
                Object renderQuads = getRenderQuads(EnumFacingProxy.values()[i]);
                if (renderQuads instanceof List)
                    for (int j = 0; j < ((List<VectorFan>) renderQuads).size(); j++)
                        ((List<VectorFan>) renderQuads).get(j).renderPreview(red, green, blue, alpha);
                else if (renderQuads instanceof VectorFan)
                    ((VectorFan) renderQuads).renderPreview(red, green, blue, alpha);
            }
        }

        GL11.glPopMatrix();
    }
//
//    public void renderLines(double x, double y, double z, int alpha) {
//        int red = ColorUtils.getRed(color);
//        int green = ColorUtils.getGreen(color);
//        int blue = ColorUtils.getBlue(color);
//
//        if (red == 1 && green == 1 && blue == 1)
//            red = green = blue = 0;
//
//        GlStateManager.pushMatrix();
//        GlStateManager.translate(x, y, z);
//
//        if (previewScalingAndOffset()) {
//            for (int i = 0; i < EnumFacingProxy.VALUES.length; i++) {
//                Object renderQuads = getRenderQuads(EnumFacingProxy.VALUES[i]);
//                if (renderQuads instanceof List)
//                    for (int j = 0; j < ((List<VectorFan>) renderQuads).size(); j++)
//                        ((List<VectorFan>) renderQuads).get(j)
//                            .renderLines(getPreviewOffX(), getPreviewOffY(), getPreviewOffZ(), getPreviewScaleX(), getPreviewScaleY(), getPreviewScaleZ(), red, green, blue, alpha);
//                else if (renderQuads instanceof VectorFan)
//                    ((VectorFan) renderQuads)
//                        .renderLines(getPreviewOffX(), getPreviewOffY(), getPreviewOffZ(), getPreviewScaleX(), getPreviewScaleY(), getPreviewScaleZ(), red, green, blue, alpha);
//            }
//        } else {
//            for (int i = 0; i < EnumFacingProxy.VALUES.length; i++) {
//                Object renderQuads = getRenderQuads(EnumFacingProxy.VALUES[i]);
//                if (renderQuads instanceof List)
//                    for (int j = 0; j < ((List<VectorFan>) renderQuads).size(); j++)
//                        ((List<VectorFan>) renderQuads).get(j).renderLines(red, green, blue, alpha);
//                else if (renderQuads instanceof VectorFan)
//                    ((VectorFan) renderQuads).renderLines(red, green, blue, alpha);
//            }
//        }
//        GlStateManager.popMatrix();
//    }
//
//    public void renderLines(double x, double y, double z, int alpha, Vector3d center, double grow) {
//        int red = ColorUtils.getRed(color);
//        int green = ColorUtils.getGreen(color);
//        int blue = ColorUtils.getBlue(color);
//
//        if (red == 1 && green == 1 && blue == 1)
//            red = green = blue = 0;
//
//        GlStateManager.pushMatrix();
//        GlStateManager.translate(x, y, z);
//
//        if (previewScalingAndOffset()) {
//            for (int i = 0; i < EnumFacingProxy.VALUES.length; i++) {
//                Object renderQuads = getRenderQuads(EnumFacingProxy.VALUES[i]);
//                if (renderQuads instanceof List)
//                    for (int j = 0; j < ((List<VectorFan>) renderQuads).size(); j++)
//                        ((List<VectorFan>) renderQuads).get(j)
//                            .renderLines(getPreviewOffX(), getPreviewOffY(), getPreviewOffZ(), getPreviewScaleX(), getPreviewScaleY(), getPreviewScaleZ(), red, green, blue, alpha, center, grow);
//                else if (renderQuads instanceof VectorFan)
//                    ((VectorFan) renderQuads)
//                        .renderLines(getPreviewOffX(), getPreviewOffY(), getPreviewOffZ(), getPreviewScaleX(), getPreviewScaleY(), getPreviewScaleZ(), red, green, blue, alpha, center, grow);
//            }
//        } else {
//            for (int i = 0; i < EnumFacingProxy.VALUES.length; i++) {
//                Object renderQuads = getRenderQuads(EnumFacingProxy.VALUES[i]);
//                if (renderQuads instanceof List)
//                    for (int j = 0; j < ((List<VectorFan>) renderQuads).size(); j++)
//                        ((List<VectorFan>) renderQuads).get(j).renderLines(red, green, blue, alpha, center, grow);
//                else if (renderQuads instanceof VectorFan)
//                    ((VectorFan) renderQuads).renderLines(red, green, blue, alpha, center, grow);
//            }
//        }
//        GlStateManager.popMatrix();
//    }
//
//    public boolean isTranslucent() {
//        if (ColorUtils.isTransparent(color))
//            return true;
//        return !getBlockState().getMaterial().blocksLight() || !getBlockState().getMaterial().isSolid() || !getBlockState().isOpaqueCube();
//    }
//
    public class RenderInformationHolder {

        public final EnumFacingProxy facing;
        public final int color;
        public BlockPos offset;
        public boolean shouldOverrideColor;

        public BakedQuadProxy quad;

        public NormalPlaneProxy normal;
        public Ray2dProxy ray = new Ray2dProxy(EnumFacingProxy.Axis.X, Axis.Y, 0, 0, 0, 0);

        public final boolean scaleAndOffset;

        public final float offsetX;
        public final float offsetY;
        public final float offsetZ;

        public final float scaleX;
        public final float scaleY;
        public final float scaleZ;

        public float minX;
        public float minY;
        public float minZ;
        public float maxX;
        public float maxY;
        public float maxZ;

        public float sizeX;
        public float sizeY;
        public float sizeZ;

        public boolean uvInverted;
        public float sizeU;
        public float sizeV;

        public RenderInformationHolder(EnumFacingProxy facing, int color) {
            this.color = color;
            this.facing = facing;
            RenderBox box = getBox();
            scaleAndOffset = box.scaleAndOffsetQuads(facing);
            if (scaleAndOffset) {
                if (box.onlyScaleOnceNoOffset(facing)) {
                    this.offsetX = this.offsetY = this.offsetZ = 0;
                    this.scaleX = this.scaleY = this.scaleZ = box.getOverallScale(facing);
                } else {
                    this.offsetX = box.getOffsetX();
                    this.offsetY = box.getOffsetY();
                    this.offsetZ = box.getOffsetZ();
                    this.scaleX = box.getScaleX();
                    this.scaleY = box.getScaleY();
                    this.scaleZ = box.getScaleZ();
                }

            } else {
                this.offsetX = this.offsetY = this.offsetZ = 0;
                this.scaleX = this.scaleY = this.scaleZ = 0;
            }
        }

        public void setQuad(BakedQuadProxy quad, boolean overrideTint, int defaultColor) {
            this.quad = quad;
            this.shouldOverrideColor = overrideTint && (defaultColor == -1 || quad.hasTintIndex()) && color != -1;
        }

        public void setBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
            this.minX = Math.min(minX, maxX);
            this.minY = Math.min(minY, maxY);
            this.minZ = Math.min(minZ, maxZ);
            this.maxX = Math.max(minX, maxX);
            this.maxY = Math.max(minY, maxY);
            this.maxZ = Math.max(minZ, maxZ);

            this.sizeX = this.maxX - this.minX;
            this.sizeY = this.maxY - this.minY;
            this.sizeZ = this.maxZ - this.minZ;
        }

        public RenderBox getBox() {
            return RenderBox.this;
        }

        public boolean hasBounds() {
            switch (facing.getAxis()) {
            case X:
                return minY != 0 || maxY != 1 || minZ != 0 || maxZ != 1;
            case Y:
                return minX != 0 || maxX != 1 || minZ != 0 || maxZ != 1;
            case Z:
                return minX != 0 || maxX != 1 || minY != 0 || maxY != 1;
            }
            return false;
        }
    }

    private static class VectorFanSimple extends VectorFan {

        public VectorFanSimple(Vector3f[] coords) {
            super(coords);

        }

//        @Override
//        @SideOnly(Side.CLIENT)
//        public void generate(RenderInformationHolder holder, List<BakedQuad> quads) {
//            int index = 0;
//            while (index < coords.length - 3) {
//                generate(holder, coords[0], coords[index + 1], coords[index + 2], coords[index + 3], quads);
//                index += 2;
//            }
//            if (index < coords.length - 2)
//                generate(holder, coords[0], coords[index + 1], coords[index + 2], coords[index + 2], quads);
//        }

//        @Override
//        protected boolean doMinMaxLate() {
//            return true;
//        }

    }
}
