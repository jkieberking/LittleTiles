package com.creativemd.littletiles.common.utils;

import com.creativemd.littletiles.utils.EnumFacingProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumFaceDirectionProxy
{
    DOWN(new EnumFaceDirectionProxy.VertexInformation[]{new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX)}),
    UP(new EnumFaceDirectionProxy.VertexInformation[]{new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX)}),
    NORTH(new EnumFaceDirectionProxy.VertexInformation[]{new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX)}),
    SOUTH(new EnumFaceDirectionProxy.VertexInformation[]{new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX)}),
    WEST(new EnumFaceDirectionProxy.VertexInformation[]{new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.WEST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX)}),
    EAST(new EnumFaceDirectionProxy.VertexInformation[]{new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.SOUTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.DOWN_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX), new EnumFaceDirectionProxy.VertexInformation(EnumFaceDirectionProxy.Constants.EAST_INDEX, EnumFaceDirectionProxy.Constants.UP_INDEX, EnumFaceDirectionProxy.Constants.NORTH_INDEX)});

    private static final EnumFaceDirectionProxy[] FACINGS = new EnumFaceDirectionProxy[6];
    private final EnumFaceDirectionProxy.VertexInformation[] vertexInfos;

    public static EnumFaceDirectionProxy getFacing(EnumFacingProxy facing)
    {
        return FACINGS[facing.getIndex()];
    }

    private EnumFaceDirectionProxy(EnumFaceDirectionProxy.VertexInformation[] vertexInfosIn)
    {
        this.vertexInfos = vertexInfosIn;
    }

    public EnumFaceDirectionProxy.VertexInformation getVertexInformation(int index)
    {
        return this.vertexInfos[index];
    }

    static
    {
        FACINGS[EnumFaceDirectionProxy.Constants.DOWN_INDEX] = DOWN;
        FACINGS[EnumFaceDirectionProxy.Constants.UP_INDEX] = UP;
        FACINGS[EnumFaceDirectionProxy.Constants.NORTH_INDEX] = NORTH;
        FACINGS[EnumFaceDirectionProxy.Constants.SOUTH_INDEX] = SOUTH;
        FACINGS[EnumFaceDirectionProxy.Constants.WEST_INDEX] = WEST;
        FACINGS[EnumFaceDirectionProxy.Constants.EAST_INDEX] = EAST;
    }

    @SideOnly(Side.CLIENT)
    public static final class Constants
        {
            public static final int SOUTH_INDEX = EnumFacingProxy.SOUTH.getIndex();
            public static final int UP_INDEX = EnumFacingProxy.UP.getIndex();
            public static final int EAST_INDEX = EnumFacingProxy.EAST.getIndex();
            public static final int NORTH_INDEX = EnumFacingProxy.NORTH.getIndex();
            public static final int DOWN_INDEX = EnumFacingProxy.DOWN.getIndex();
            public static final int WEST_INDEX = EnumFacingProxy.WEST.getIndex();
        }

    @SideOnly(Side.CLIENT)
    public static class VertexInformation
        {
            public final int xIndex;
            public final int yIndex;
            public final int zIndex;

            private VertexInformation(int xIndexIn, int yIndexIn, int zIndexIn)
            {
                this.xIndex = xIndexIn;
                this.yIndex = yIndexIn;
                this.zIndex = zIndexIn;
            }
        }
}
