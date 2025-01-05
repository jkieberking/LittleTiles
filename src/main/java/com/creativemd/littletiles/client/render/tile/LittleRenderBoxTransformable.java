package com.creativemd.littletiles.client.render.tile;

import com.creativemd.littletiles.client.render.face.IFaceRenderType;
import com.creativemd.littletiles.common.tile.math.box.AlignedBox;
import com.creativemd.littletiles.common.tile.math.box.LittleTransformableBox;
import com.creativemd.littletiles.common.tile.math.box.LittleTransformableBox.VectorFanCache;
import com.creativemd.littletiles.common.tile.math.box.LittleTransformableBox.VectorFanFaceCache;
import com.creativemd.littletiles.common.tile.math.vec.VectorFan;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class LittleRenderBoxTransformable extends LittleRenderBox {

    private float scale;
    private float inverseScale;
    public VectorFanCache cache;

    public LittleRenderBoxTransformable(AlignedBox cube, LittleGridContext context, LittleTransformableBox box, Block block, int meta) {
        super(cube, box, block, meta);
        this.cache = box.requestCache();
        this.scale = (float) context.pixelSize;
        this.inverseScale = context.size;
    }

    @Override
    public void add(float x, float y, float z) {
        super.add(x, y, z);
        cache.add(x * inverseScale, y * inverseScale, z * inverseScale);
    }

    @Override
    public void sub(float x, float y, float z) {
        super.sub(x, y, z);
        cache.sub(x * inverseScale, y * inverseScale, z * inverseScale);
    }

    @Override
    public void scale(float scale) {
        super.scale(scale);
        cache.scale(scale);
    }

    public VectorFanFaceCache getFaceCache(EnumFacingProxy facing) {
        if (cache != null)
            return cache.get(facing);
        return null;
    }

    @Override
    public boolean renderSide(EnumFacingProxy facing) {
        VectorFanFaceCache cache = getFaceCache(facing);
        if (cache == null)
            return false;
        if (cache.hasAxisStrip() && super.renderSide(facing))
            return true;
        return cache.hasTiltedStripsRendering();
    }

    @Override
    protected Object getRenderQuads(EnumFacingProxy facing) {
        if (getType(facing).hasCachedFans())
            return getType(facing).getCachedFans();
        VectorFanFaceCache cache = getFaceCache(facing);

        if (cache.hasTiltedStripsRendering()) {
            if (super.renderSide(facing) && cache.hasAxisStrip()) {
                List<VectorFan> strips = new ArrayList<>(cache.axisStrips);
                cache.collectAllTiltedStripsRendering(strips);
                return strips;
            }

            if (cache.hasSingleTiltedStripRendering())
                return cache.getSingleTiltedStripRendering();

            List<VectorFan> strips = new ArrayList<>();

            cache.collectAllTiltedStripsRendering(strips);
            return strips;
        }
        if (super.renderSide(facing))
            return cache.axisStrips;
        return null;
    }

    @Override
    public float getPreviewOffX() {
        return 0;
    }

    @Override
    public float getPreviewOffY() {
        return 0;
    }

    @Override
    public float getPreviewOffZ() {
        return 0;
    }

    @Override
    public float getPreviewScaleX() {
        return scale;
    }

    @Override
    public float getPreviewScaleY() {
        return scale;
    }

    @Override
    public float getPreviewScaleZ() {
        return scale;
    }

    @Override
    protected boolean scaleAndOffsetQuads(EnumFacingProxy facing) {
        return true;
    }

    @Override
    protected boolean onlyScaleOnceNoOffset(EnumFacingProxy facing) {
        return true;
    }

    @Override
    protected float getOverallScale(EnumFacingProxy facing) {
        IFaceRenderType type = getType(facing);
        if (type.hasCachedFans())
            return type.getScale();
        return scale;
    }
}