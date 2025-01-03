package com.creativemd.littletiles.common.tile.math.box;


import com.creativemd.creativecore.common.utils.RotationUtils.Axis;
import com.creativemd.littletiles.utils.EnumFacingProxy;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.ForgeDirection;
import org.joml.Vector3d;

import static com.creativemd.creativecore.common.utils.RotationUtils.Axis.*;

public enum BoxCorner {

    EUN(EnumFacing.EAST, EnumFacing.UP, EnumFacing.NORTH),
    EUS(EnumFacing.EAST, EnumFacing.UP, EnumFacing.SOUTH),
    EDN(EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.NORTH),
    EDS(EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.SOUTH),
    WUN(EnumFacing.WEST, EnumFacing.UP, EnumFacing.NORTH),
    WUS(EnumFacing.WEST, EnumFacing.UP, EnumFacing.SOUTH),
    WDN(EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.NORTH),
    WDS(EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.SOUTH);

    public final EnumFacing x;
    public final EnumFacing y;
    public final EnumFacing z;

    public BoxCorner neighborOne;
    public BoxCorner neighborTwo;
    public BoxCorner neighborThree;

    private static BoxCorner[][] facingCorners = new BoxCorner[][] { { EDN, EDS, WDN, WDS }, { EUN, EUS, WUN, WUS }, { EUN, EDN, WUN, WDN }, { EUS, EDS, WUS, WDS }, { WUN, WUS, WDN, WDS },
            { EUN, EUS, EDN, EDS } };

    private BoxCorner(EnumFacing x, EnumFacing y, EnumFacing z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private void init() {
        neighborOne = getCorner(x == EnumFacing.EAST ? EnumFacing.WEST : EnumFacing.EAST, y, z);
        neighborTwo = getCorner(x, y == EnumFacing.DOWN ? EnumFacing.UP : EnumFacing.DOWN, z);
        neighborThree = getCorner(x, y, z == EnumFacing.NORTH ? EnumFacing.SOUTH : EnumFacing.NORTH);
    }

//    public Vector3d getVector(AxisAlignedBB box) {
//        return new Vector3d(CreativeAxisAlignedBB.getCornerX(box, this), CreativeAxisAlignedBB.getCornerY(box, this), CreativeAxisAlignedBB.getCornerZ(box, this));
//    }
//
//    public boolean isFacing(EnumFacing facing) {
//        return getFacing(facing.getAxis()) == facing;
//    }

    public boolean isFacingPositive(Axis axis) {
        return getFacing(axis).getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE;
    }

    public EnumFacingProxy getFacing(Axis axis) {
        switch (axis) {
            case Xaxis:
                return EnumFacingProxy.fromEnumFacing(x);
            case Yaxis:
                return EnumFacingProxy.fromEnumFacing(y);
            case Zaxis:
                return EnumFacingProxy.fromEnumFacing(z);
        }
        throw new RuntimeException("null axis not permitted");
    }

    public EnumFacing getFacing(EnumFacingProxy.Axis axis) {
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
                return getCorner(
                    EnumFacingProxy.toEnumFacing(
                        EnumFacingProxy.fromEnumFacing(x).getOpposite()
                    ), y, z);
            case Yaxis:
                return getCorner(x, EnumFacingProxy.toEnumFacing(
                    EnumFacingProxy.fromEnumFacing(y).getOpposite()
                ), z);
            case Zaxis:
                return getCorner(x, y, EnumFacingProxy.toEnumFacing(
                    EnumFacingProxy.fromEnumFacing(z).getOpposite()
                ));
            }
        throw new RuntimeException("null axis not permitted");
    }
//
//    public BoxCorner rotate(Rotation rotation) {
//        int normalX = x.getAxisDirection().getOffset();
//        int normalY = y.getAxisDirection().getOffset();
//        int normalZ = z.getAxisDirection().getOffset();
//        return getCorner(EnumFacing.getFacingFromAxis(rotation.getMatrix().getX(normalX, normalY, normalZ) > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, Axis.X), EnumFacing
//            .getFacingFromAxis(rotation.getMatrix().getY(normalX, normalY, normalZ) > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, Axis.Y), EnumFacing
//                .getFacingFromAxis(rotation.getMatrix().getZ(normalX, normalY, normalZ) > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, Axis.Z));
//    }
//
    public static BoxCorner getCornerUnsorted(EnumFacing facing, EnumFacing facing2, EnumFacing facing3) {
        EnumFacingProxy facingProxy = EnumFacingProxy.fromEnumFacing(facing);
        EnumFacingProxy facingProxy2 = EnumFacingProxy.fromEnumFacing(facing2);
        EnumFacingProxy facingProxy3 = EnumFacingProxy.fromEnumFacing(facing3);

        return getCorner(facingProxy.getAxis() != EnumFacingProxy.Axis.X ? facingProxy2.getAxis() != EnumFacingProxy.Axis.X ? facing3 : facing2 : facing, facingProxy
            .getAxis() != EnumFacingProxy.Axis.Y ? facingProxy2.getAxis() != EnumFacingProxy.Axis.Y ? facing3 : facing2 : facing, facingProxy.getAxis() != EnumFacingProxy.Axis.Z ? facingProxy2.getAxis() != EnumFacingProxy.Axis.Z ? facing3 : facing2 : facing);
    }

    public static BoxCorner getCorner(EnumFacing x, EnumFacing y, EnumFacing z) {
        for (BoxCorner corner : BoxCorner.values()) {
            if (corner.x == x && corner.y == y && corner.z == z)
                return corner;
        }
        return null;
    }

    public static BoxCorner[] faceCorners(EnumFacing facing) {
        return facingCorners[facing.ordinal()];
    }

    static {
        for (BoxCorner corner : BoxCorner.values())
            corner.init();
    }
}
