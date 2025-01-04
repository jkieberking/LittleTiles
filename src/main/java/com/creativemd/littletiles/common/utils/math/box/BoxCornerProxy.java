package com.creativemd.littletiles.common.utils.math.box;

import javax.vecmath.Vector3d;


import com.creativemd.creativecore.common.utils.Rotation;
import com.creativemd.littletiles.common.utils.math.RotationProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

public enum BoxCornerProxy {

    EUN(EnumFacingProxy.EAST, EnumFacingProxy.UP, EnumFacingProxy.NORTH),
    EUS(EnumFacingProxy.EAST, EnumFacingProxy.UP, EnumFacingProxy.SOUTH),
    EDN(EnumFacingProxy.EAST, EnumFacingProxy.DOWN, EnumFacingProxy.NORTH),
    EDS(EnumFacingProxy.EAST, EnumFacingProxy.DOWN, EnumFacingProxy.SOUTH),
    WUN(EnumFacingProxy.WEST, EnumFacingProxy.UP, EnumFacingProxy.NORTH),
    WUS(EnumFacingProxy.WEST, EnumFacingProxy.UP, EnumFacingProxy.SOUTH),
    WDN(EnumFacingProxy.WEST, EnumFacingProxy.DOWN, EnumFacingProxy.NORTH),
    WDS(EnumFacingProxy.WEST, EnumFacingProxy.DOWN, EnumFacingProxy.SOUTH);

    public final EnumFacingProxy x;
    public final EnumFacingProxy y;
    public final EnumFacingProxy z;

    public BoxCornerProxy neighborOne;
    public BoxCornerProxy neighborTwo;
    public BoxCornerProxy neighborThree;

    private static BoxCornerProxy[][] facingCorners = new BoxCornerProxy[][] { { EDN, EDS, WDN, WDS }, { EUN, EUS, WUN, WUS }, { EUN, EDN, WUN, WDN }, { EUS, EDS, WUS, WDS }, { WUN, WUS, WDN, WDS },
            { EUN, EUS, EDN, EDS } };

    private BoxCornerProxy(EnumFacingProxy x, EnumFacingProxy y, EnumFacingProxy z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private void init() {
        neighborOne = getCorner(x.getOpposite(), y, z);
        neighborTwo = getCorner(x, y.getOpposite(), z);
        neighborThree = getCorner(x, y, z.getOpposite());
    }

    public Vector3d getVector(AxisAlignedBB box) {
        return new Vector3d(CreativeAxisAlignedBBProxy.getCornerX(box, this), CreativeAxisAlignedBBProxy.getCornerY(box, this), CreativeAxisAlignedBBProxy.getCornerZ(box, this));
    }

    public boolean isFacing(EnumFacingProxy facing) {
        return getFacing(facing.getAxis()) == facing;
    }

    public boolean isFacingPositive(Axis axis) {
        return getFacing(axis).getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE;
    }

    public EnumFacingProxy getFacing(Axis axis) {
        switch (axis) {
        case X:
            return x;
        case Y:
            return y;
        case Z:
            return z;
        }
        throw new RuntimeException("null axis not permitted");
    }

    public BoxCornerProxy flip(Axis axis) {
        switch (axis) {
        case X:
            return getCorner(x.getOpposite(), y, z);
        case Y:
            return getCorner(x, y.getOpposite(), z);
        case Z:
            return getCorner(x, y, z.getOpposite());
        }
        throw new RuntimeException("null axis not permitted");
    }

    public BoxCornerProxy rotate(RotationProxy rotation) {
        int normalX = x.getAxisDirection().getOffset();
        int normalY = y.getAxisDirection().getOffset();
        int normalZ = z.getAxisDirection().getOffset();
        return getCorner(EnumFacingProxy.getFacingFromAxis(rotation.getMatrix().getX(normalX, normalY, normalZ) > 0 ? EnumFacingProxy.AxisDirection.POSITIVE : EnumFacingProxy.AxisDirection.NEGATIVE, Axis.X), EnumFacingProxy
            .getFacingFromAxis(rotation.getMatrix().getY(normalX, normalY, normalZ) > 0 ? EnumFacingProxy.AxisDirection.POSITIVE : EnumFacingProxy.AxisDirection.NEGATIVE, Axis.Y), EnumFacingProxy
                .getFacingFromAxis(rotation.getMatrix().getZ(normalX, normalY, normalZ) > 0 ? EnumFacingProxy.AxisDirection.POSITIVE : EnumFacingProxy.AxisDirection.NEGATIVE, Axis.Z));
    }

    public static BoxCornerProxy getCornerUnsorted(EnumFacingProxy facing, EnumFacingProxy facing2, EnumFacingProxy facing3) {
        return getCorner(facing.getAxis() != Axis.X ? facing2.getAxis() != Axis.X ? facing3 : facing2 : facing, facing
            .getAxis() != Axis.Y ? facing2.getAxis() != Axis.Y ? facing3 : facing2 : facing, facing.getAxis() != Axis.Z ? facing2.getAxis() != Axis.Z ? facing3 : facing2 : facing);
    }

    public static BoxCornerProxy getCorner(EnumFacingProxy x, EnumFacingProxy y, EnumFacingProxy z) {
        for (BoxCornerProxy corner : BoxCornerProxy.values()) {
            if (corner.x == x && corner.y == y && corner.z == z)
                return corner;
        }
        return null;
    }

    public static BoxCornerProxy[] faceCorners(EnumFacingProxy facing) {
        return facingCorners[facing.ordinal()];
    }

    static {
        for (BoxCornerProxy corner : BoxCornerProxy.values())
            corner.init();
    }
}
