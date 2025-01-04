package com.creativemd.littletiles.client.render.block;

import com.creativemd.littletiles.client.render.DefaultVertexFormatsProxy;
import com.creativemd.littletiles.client.render.LittleRenderUtils;
import com.creativemd.littletiles.client.render.VertexFormatProxy;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// This is from forgeg 1.12
@SideOnly(Side.CLIENT)
public class BakedQuadProxy implements IVertexProducerProxy
{
    protected final int[] vertexData;
    protected final int tintIndex;
    protected final EnumFacing face;
    protected final TextureAtlasSprite sprite;

    /**
     * @deprecated Use constructor with the format argument.
     */
    @Deprecated
    public BakedQuadProxy(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn)
    {
        this(vertexDataIn, tintIndexIn, faceIn, spriteIn, true, DefaultVertexFormatsProxy.ITEM);
    }

    public BakedQuadProxy(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn, boolean applyDiffuseLighting, VertexFormatProxy format)
    {
        this.format = format;
        this.applyDiffuseLighting = applyDiffuseLighting;
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
        this.sprite = spriteIn;
    }

    public TextureAtlasSprite getSprite()
    {
        return this.sprite;
    }

    public int[] getVertexData()
    {
        return this.vertexData;
    }

    public boolean hasTintIndex()
    {
        return this.tintIndex != -1;
    }

    public int getTintIndex()
    {
        return this.tintIndex;
    }

    public EnumFacing getFace()
    {
        return this.face;
    }

    protected final VertexFormatProxy format;
    protected final boolean applyDiffuseLighting;

    @Override
    public void pipe(IVertexConsumer consumer)
    {
        LittleRenderUtils.putBakedQuad(consumer, this);
    }

    public VertexFormatProxy getFormat()
    {
        return format;
    }

    public boolean shouldApplyDiffuseLighting()
    {
        return applyDiffuseLighting;
    }
}
