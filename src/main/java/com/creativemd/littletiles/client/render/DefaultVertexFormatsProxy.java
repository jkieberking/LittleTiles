package com.creativemd.littletiles.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DefaultVertexFormatsProxy
{
    public static final VertexFormatProxy BLOCK = new VertexFormatProxy();
    public static final VertexFormatProxy ITEM = new VertexFormatProxy();
    public static final VertexFormatProxy OLDMODEL_POSITION_TEX_NORMAL = new VertexFormatProxy();
    public static final VertexFormatProxy PARTICLE_POSITION_TEX_COLOR_LMAP = new VertexFormatProxy();
    public static final VertexFormatProxy POSITION = new VertexFormatProxy();
    public static final VertexFormatProxy POSITION_COLOR = new VertexFormatProxy();
    public static final VertexFormatProxy POSITION_TEX = new VertexFormatProxy();
    public static final VertexFormatProxy POSITION_NORMAL = new VertexFormatProxy();
    public static final VertexFormatProxy POSITION_TEX_COLOR = new VertexFormatProxy();
    public static final VertexFormatProxy POSITION_TEX_NORMAL = new VertexFormatProxy();
    public static final VertexFormatProxy POSITION_TEX_LMAP_COLOR = new VertexFormatProxy();
    public static final VertexFormatProxy POSITION_TEX_COLOR_NORMAL = new VertexFormatProxy();
    public static final VertexFormatElementProxy POSITION_3F = new VertexFormatElementProxy(0, VertexFormatElementProxy.EnumType.FLOAT, VertexFormatElementProxy.EnumUsage.POSITION, 3);
    public static final VertexFormatElementProxy COLOR_4UB = new VertexFormatElementProxy(0, VertexFormatElementProxy.EnumType.UBYTE, VertexFormatElementProxy.EnumUsage.COLOR, 4);
    public static final VertexFormatElementProxy TEX_2F = new VertexFormatElementProxy(0, VertexFormatElementProxy.EnumType.FLOAT, VertexFormatElementProxy.EnumUsage.UV, 2);
    public static final VertexFormatElementProxy TEX_2S = new VertexFormatElementProxy(1, VertexFormatElementProxy.EnumType.SHORT, VertexFormatElementProxy.EnumUsage.UV, 2);
    public static final VertexFormatElementProxy NORMAL_3B = new VertexFormatElementProxy(0, VertexFormatElementProxy.EnumType.BYTE, VertexFormatElementProxy.EnumUsage.NORMAL, 3);
    public static final VertexFormatElementProxy PADDING_1B = new VertexFormatElementProxy(0, VertexFormatElementProxy.EnumType.BYTE, VertexFormatElementProxy.EnumUsage.PADDING, 1);

    static
    {
        BLOCK.addElement(POSITION_3F);
        BLOCK.addElement(COLOR_4UB);
        BLOCK.addElement(TEX_2F);
        BLOCK.addElement(TEX_2S);
        ITEM.addElement(POSITION_3F);
        ITEM.addElement(COLOR_4UB);
        ITEM.addElement(TEX_2F);
        ITEM.addElement(NORMAL_3B);
        ITEM.addElement(PADDING_1B);
        OLDMODEL_POSITION_TEX_NORMAL.addElement(POSITION_3F);
        OLDMODEL_POSITION_TEX_NORMAL.addElement(TEX_2F);
        OLDMODEL_POSITION_TEX_NORMAL.addElement(NORMAL_3B);
        OLDMODEL_POSITION_TEX_NORMAL.addElement(PADDING_1B);
        PARTICLE_POSITION_TEX_COLOR_LMAP.addElement(POSITION_3F);
        PARTICLE_POSITION_TEX_COLOR_LMAP.addElement(TEX_2F);
        PARTICLE_POSITION_TEX_COLOR_LMAP.addElement(COLOR_4UB);
        PARTICLE_POSITION_TEX_COLOR_LMAP.addElement(TEX_2S);
        POSITION.addElement(POSITION_3F);
        POSITION_COLOR.addElement(POSITION_3F);
        POSITION_COLOR.addElement(COLOR_4UB);
        POSITION_TEX.addElement(POSITION_3F);
        POSITION_TEX.addElement(TEX_2F);
        POSITION_NORMAL.addElement(POSITION_3F);
        POSITION_NORMAL.addElement(NORMAL_3B);
        POSITION_NORMAL.addElement(PADDING_1B);
        POSITION_TEX_COLOR.addElement(POSITION_3F);
        POSITION_TEX_COLOR.addElement(TEX_2F);
        POSITION_TEX_COLOR.addElement(COLOR_4UB);
        POSITION_TEX_NORMAL.addElement(POSITION_3F);
        POSITION_TEX_NORMAL.addElement(TEX_2F);
        POSITION_TEX_NORMAL.addElement(NORMAL_3B);
        POSITION_TEX_NORMAL.addElement(PADDING_1B);
        POSITION_TEX_LMAP_COLOR.addElement(POSITION_3F);
        POSITION_TEX_LMAP_COLOR.addElement(TEX_2F);
        POSITION_TEX_LMAP_COLOR.addElement(TEX_2S);
        POSITION_TEX_LMAP_COLOR.addElement(COLOR_4UB);
        POSITION_TEX_COLOR_NORMAL.addElement(POSITION_3F);
        POSITION_TEX_COLOR_NORMAL.addElement(TEX_2F);
        POSITION_TEX_COLOR_NORMAL.addElement(COLOR_4UB);
        POSITION_TEX_COLOR_NORMAL.addElement(NORMAL_3B);
        POSITION_TEX_COLOR_NORMAL.addElement(PADDING_1B);
    }
}
