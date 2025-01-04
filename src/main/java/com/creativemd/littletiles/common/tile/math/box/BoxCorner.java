package com.creativemd.littletiles.common.tile.math.box;


import com.creativemd.creativecore.common.utils.RotationUtils.Axis;
import com.creativemd.littletiles.utils.EnumFacingProxy;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.ForgeDirection;
import org.joml.Vector3d;

import static com.creativemd.creativecore.common.utils.RotationUtils.Axis.*;

public enum BoxCorner {

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

    public BoxCorner neighborOne;
    public BoxCorner neighborTwo;
    public BoxCorner neighborThree;

    private static BoxCorner[][] facingCorners = new BoxCorner[][] { { EDN, EDS, WDN, WDS }, { EUN, EUS, WUN, WUS }, { EUN, EDN, WUN, WDN }, { EUS, EDS, WUS, WDS }, { WUN, WUS, WDN, WDS },
            { EUN, EUS, EDN, EDS } };

    private BoxCorner(EnumFacingProxy x, EnumFacingProxy y, EnumFacingProxy z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private void init() {
        neighborOne = getCorner(x == EnumFacingProxy.EAST ? EnumFacingProxy.WEST : EnumFacingProxy.EAST, y, z);
        neighborTwo = getCorner(x, y == EnumFacingProxy.DOWN ? EnumFacingProxy.UP : EnumFacingProxy.DOWN, z);
        neighborThree = getCorner(x, y, z == EnumFacingProxy.NORTH ? EnumFacingProxy.SOUTH : EnumFacingProxy.NORTH);
    }

//    public Vector3d getVector(AxisAlignedBB box) {
//        return new Vector3d(CreativeAxisAlignedBB.getCornerX(box, this), CreativeAxisAlignedBB.getCornerY(box, this), CreativeAxisAlignedBB.getCornerZ(box, this));
//    }
//
//    public boolean isFacing(EnumFacingProxy facing) {
//        return getFacing(facing.getAxis()) == facing;
//    }

    public boolean isFacingPositive(EnumFacingProxy.Axis axis) {
        return getFacing(axis).getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE;
    }

    public EnumFacingProxy getFacing(Axis axis) {
        switch (axis) {
            case Xaxis:
                return x;
            case Yaxis:
                return y;
            case Zaxis:
                return z;
        }
        throw new RuntimeException("null axis not permitted");
    }

    public EnumFacingProxy getFacing(EnumFacingProxy.Axis axis) {
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

    public BoxCorner flip(Axis axis) {
        switch (axis) {
            case Xaxis:
                return getCorner(x.getOpposite(), y, z);
            case Yaxis:
                return getCorner(x, y.getOpposite(), z);
            case Zaxis:
                return getCorner(x, y, z.getOpposite());
            }
        throw new RuntimeException("null axis not permitted");
    }
//
//    public BoxCorner rotate(Rotation rotation) {
//        int normalX = x.getAxisDirection().getOffset();
//        int normalY = y.getAxisDirection().getOffset();
//        int normalZ = z.getAxisDirection().getOffset();
//        return getCorner(EnumFacingProxy.getFacingFromAxis(rotation.getMatrix().getX(normalX, normalY, normalZ) > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, Axis.X), EnumFacing
//            .getFacingFromAxis(rotation.getMatrix().getY(normalX, normalY, normalZ) > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, Axis.Y), EnumFacing
//                .getFacingFromAxis(rotation.getMatrix().getZ(normalX, normalY, normalZ) > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, Axis.Z));
//    }
//
    public static BoxCorner getCornerUnsorted(EnumFacingProxy facing, EnumFacingProxy facing2, EnumFacingProxy facing3) {
        return getCorner(facing.getAxis() != EnumFacingProxy.Axis.X ? facing2.getAxis() != EnumFacingProxy.Axis.X ? facing3 : facing2 : facing, facing
            .getAxis() != EnumFacingProxy.Axis.Y ? facing2.getAxis() != EnumFacingProxy.Axis.Y ? facing3 : facing2 : facing, facing.getAxis() != EnumFacingProxy.Axis.Z ? facing2.getAxis() != EnumFacingProxy.Axis.Z ? facing3 : facing2 : facing);
    }

    public static BoxCorner getCorner(EnumFacingProxy x, EnumFacingProxy y, EnumFacingProxy z) {
        for (BoxCorner corner : BoxCorner.values()) {
            if (corner.x == x && corner.y == y && corner.z == z)
                return corner;
        }
        return null;
    }

    public static BoxCorner[] faceCorners(EnumFacingProxy facing) {
        return facingCorners[facing.ordinal()];
    }

    static {
        for (BoxCorner corner : BoxCorner.values())
            corner.init();
    }
}
