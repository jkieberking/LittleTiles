package com.creativemd.littletiles.common.utils.math.box;

import com.creativemd.littletiles.common.tile.math.LittleUtils;
import com.creativemd.littletiles.common.utils.math.VectorUtilsProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.creativemd.littletiles.utils.RotationUtilsProxy;
import org.joml.Vector3d;
import org.joml.Vector3f;


public enum BoxFaceProxy {

    EAST(
        EnumFacingProxy.EAST,
        new BoxCornerProxy[] { BoxCornerProxy.EUS, BoxCornerProxy.EDS, BoxCornerProxy.EDN, BoxCornerProxy.EUN },
        EnumFacingProxy.NORTH,
        EnumFacingProxy.DOWN),
    WEST(
        EnumFacingProxy.WEST,
        new BoxCornerProxy[] { BoxCornerProxy.WUN, BoxCornerProxy.WDN, BoxCornerProxy.WDS, BoxCornerProxy.WUS },
        EnumFacingProxy.SOUTH,
        EnumFacingProxy.DOWN),
    UP(
        EnumFacingProxy.UP,
        new BoxCornerProxy[] { BoxCornerProxy.WUN, BoxCornerProxy.WUS, BoxCornerProxy.EUS, BoxCornerProxy.EUN },
        EnumFacingProxy.EAST,
        EnumFacingProxy.SOUTH),
    DOWN(
        EnumFacingProxy.DOWN,
        new BoxCornerProxy[] { BoxCornerProxy.WDS, BoxCornerProxy.WDN, BoxCornerProxy.EDN, BoxCornerProxy.EDS },
        EnumFacingProxy.EAST,
        EnumFacingProxy.NORTH),
    SOUTH(
        EnumFacingProxy.SOUTH,
        new BoxCornerProxy[] { BoxCornerProxy.WUS, BoxCornerProxy.WDS, BoxCornerProxy.EDS, BoxCornerProxy.EUS },
        EnumFacingProxy.EAST,
        EnumFacingProxy.DOWN),
    NORTH(
        EnumFacingProxy.NORTH,
        new BoxCornerProxy[] { BoxCornerProxy.EUN, BoxCornerProxy.EDN, BoxCornerProxy.WDN, BoxCornerProxy.WUN },
        EnumFacingProxy.WEST,
        EnumFacingProxy.DOWN);

    public final EnumFacingProxy facing;
    public final BoxCornerProxy[] corners;

    private final Axis one;
    private final Axis two;

    private final EnumFacingProxy texU;
    private final EnumFacingProxy texV;

    private final BoxTriangle triangleFirst;
    private final BoxTriangle triangleFirstInv;
    private final BoxTriangle triangleSecond;
    private final BoxTriangle triangleSecondInv;

    BoxFaceProxy(EnumFacingProxy facing, BoxCornerProxy[] corners, EnumFacingProxy texU, EnumFacingProxy texV) {
        this.facing = facing;
        this.corners = corners;

        this.one = RotationUtilsProxy.getOne(facing.getAxis());
        this.two = RotationUtilsProxy.getTwo(facing.getAxis());

        this.texU = texU;
        this.texV = texV;

        this.triangleFirst = new BoxTriangle(new BoxCornerProxy[] { corners[0], corners[1], corners[2] });
        this.triangleFirstInv = new BoxTriangle(new BoxCornerProxy[] { corners[0], corners[1], corners[3] });
        this.triangleSecond = new BoxTriangle(new BoxCornerProxy[] { corners[0], corners[2], corners[3] });
        this.triangleSecondInv = new BoxTriangle(new BoxCornerProxy[] { corners[1], corners[2], corners[3] });
    }

    public BoxCornerProxy getCornerInQuestion(boolean first, boolean inverted) {
        if (first)
            if (inverted)
                return corners[1];
            else
                return corners[0];
        if (inverted)
            return corners[3];
        return corners[2];
    }

    private BoxTriangle getTriangle(boolean first, boolean inverted) {
        if (first)
            if (inverted)
                return triangleFirstInv;
            else
                return triangleFirst;
        else if (inverted)
            return triangleSecondInv;
        return triangleSecond;
    }

    public Axis getTexUAxis() {
        return texU.getAxis();
    }

    public Axis getTexVAxis() {
        return texV.getAxis();
    }

    public EnumFacingProxy getTexU() {
        return texU;
    }

    public EnumFacingProxy getTexV() {
        return texV;
    }

    public BoxCornerProxy[] getTriangleFirst(boolean inverted) {
        return getTriangle(true, inverted).corners;
    }

    public BoxCornerProxy[] getTriangleSecond(boolean inverted) {
        return getTriangle(false, inverted).corners;
    }

    public Vector3d first(Vector3d[] corners) {
        return corners[this.corners[0].ordinal()];
    }

    public Vector3d normal(Vector3d[] corners) {
        Vector3d origin = first(corners);
        Vector3d first = new Vector3d(corners[this.corners[1].ordinal()]);
        Vector3d second = new Vector3d(corners[this.corners[2].ordinal()]);
        first.sub(origin);
        second.sub(origin);
        return new Vector3d(first.y * second.z - first.z * second.y, first.z * second.x - first.x * second.z, first.x * second.y - first.y * second.x);
    }

    public Boolean isFacingOutwards(boolean first, boolean inverted, Vector3f normal) {
        BoxTriangle triangle = getTriangle(first, inverted);
        float valueOne = VectorUtilsProxy.get(one, normal);
        float valueTwo = VectorUtilsProxy.get(two, normal);
        Boolean outwardOne = valueOne == 0 ? null : valueOne > 0;
        Boolean outwardTwo = valueTwo == 0 ? null : valueTwo > 0;

        if (outwardOne == outwardTwo)
            return outwardOne;
        if (outwardOne != null && outwardTwo == null)
            return outwardOne;
        if (outwardOne == null && outwardTwo != null)
            return outwardTwo;

        if (valueOne == valueTwo)
            return null;
        if (valueOne > valueTwo)
            return outwardOne;
        return outwardTwo;
    }

    public static Vector3f getTraingleNormal(BoxCornerProxy[] triangle, Vector3f[] corners) {
        Vector3f a = new Vector3f(corners[triangle[1].ordinal()]);
        a.sub(corners[triangle[0].ordinal()]);

        Vector3f b = new Vector3f(corners[triangle[2].ordinal()]);
        b.sub(corners[triangle[0].ordinal()]);

        Vector3f normal = new Vector3f();
        normal.cross(a, b);
        return normal;
    }

    public static void ensureSameLength(Vector3f one, Vector3f two) {
        float normVecOne = one.x * one.x + one.y * one.y + one.z * one.z;
        float normVecTwo = two.x * two.x + two.y * two.y + two.z * two.z;
        LittleUtils.scaleInPlace(one, normVecTwo);
        LittleUtils.scaleInPlace(two, normVecOne);
    }

    public static BoxFaceProxy get(EnumFacingProxy facing) {
        switch (facing) {
        case EAST:
            return EAST;
        case WEST:
            return WEST;
        case UP:
            return UP;
        case DOWN:
            return DOWN;
        case SOUTH:
            return SOUTH;
        case NORTH:
            return NORTH;
        default:
            return null;
        }
    }

    public static BoxFaceProxy get(EnumFacingProxy.Axis axis, boolean direction) {
        switch (axis) {
        case X:
            return direction ? EAST : WEST;
        case Y:
            return direction ? UP : DOWN;
        case Z:
            return direction ? SOUTH : NORTH;
        default:
            return null;
        }
    }

    public static Vector3f[] getVecArray(BoxCornerProxy[] corners, Vector3f[] vecs) {
        Vector3f[] result = new Vector3f[corners.length];
        for (int i = 0; i < result.length; i++)
            result[i] = vecs[corners[i].ordinal()];
        return result;
    }

    private class BoxTriangle {

        private final BoxCornerProxy[] corners;
        private final boolean outwardDirectionOne;
        private final boolean outwardDirectionTwo;

        public BoxTriangle(BoxCornerProxy[] corners) {
            this.corners = corners;
            this.outwardDirectionOne = corners[1].getFacing(one).getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE;
            this.outwardDirectionTwo = corners[1].getFacing(two).getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE;
        }

    }
}
