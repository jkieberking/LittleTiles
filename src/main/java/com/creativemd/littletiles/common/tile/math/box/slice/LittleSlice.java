package com.creativemd.littletiles.common.tile.math.box.slice;


import com.creativemd.creativecore.core.CreativeCore;
import com.creativemd.littletiles.common.tile.math.box.BoxCorner;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.utils.math.RotationProxy;
import com.creativemd.littletiles.common.utils.math.RotationUtilProxy;
import com.creativemd.littletiles.common.utils.math.VectorUtilsProxy;
import com.creativemd.littletiles.common.utils.math.box.BoxCornerProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.util.EnumFacing;
import org.joml.Vector3d;
import org.joml.Vector3i;

public enum LittleSlice {

    X_US_DN_RIGHT(Axis.X, true, 2, 0, BoxCorner.EUS, BoxCorner.EDN),
    X_US_DN_LEFT(Axis.X, false, 0, 2, BoxCorner.EUS, BoxCorner.EDN),
    X_DS_UN_RIGHT(Axis.X, true, 0, 2, BoxCorner.EDS, BoxCorner.EUN),
    X_DS_UN_LEFT(Axis.X, false, 2, 0, BoxCorner.EDS, BoxCorner.EUN),

    Y_ES_WN_RIGHT(Axis.Y, true, 1, 0, BoxCorner.EUS, BoxCorner.WUN),
    Y_ES_WN_LEFT(Axis.Y, false, 0, 1, BoxCorner.EUS, BoxCorner.WUN),
    Y_WS_EN_RIGHT(Axis.Y, true, 0, 1, BoxCorner.WUS, BoxCorner.EUN),
    Y_WS_EN_LEFT(Axis.Y, false, 1, 0, BoxCorner.WUS, BoxCorner.EUN),

    Z_WU_ED_RIGHT(Axis.Z, true, 2, 0, BoxCorner.WUS, BoxCorner.EDS),
    Z_WU_ED_LEFT(Axis.Z, false, 0, 2, BoxCorner.WUS, BoxCorner.EDS),
    Z_WD_EU_RIGHT(Axis.Z, true, 0, 2, BoxCorner.WDS, BoxCorner.EUS),
    Z_WD_EU_LEFT(Axis.Z, false, 2, 0, BoxCorner.WDS, BoxCorner.EUS);
//
    public final Axis axis;
    public final Vector3i sliceVec;
    public final EnumFacingProxy emptySideOne;
    public final EnumFacingProxy emptySideTwo;
    public final boolean isRight;

    public final BoxCorner start;
    public final BoxCorner end;

    public final int traingleOrderPositive;
    public final int traingleOrderNegative;
    private final int[] normal;
//
//    public static LittleSlice getSliceFromNormal(int[] normal) {
//        for (LittleSlice slice : values()) {
//            if (Arrays.equals(slice.normal, normal))
//                return slice;
//        }
//        return null;
//    }
//
    public static int getDirectionBetweenFacing(EnumFacingProxy facing, EnumFacingProxy facing2) {
        if (facing.getAxisDirection() == facing2.getAxisDirection())
            return 0;
        return facing.getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE ? -1 : 1;
    }

    public static LittleSlice getSliceByID(int id) // Yes its a mess, unfortunately i screwed it up twice.
    {
        if (id < 12)
            return getOlderSlice(id);
        if (id < 24)
            return getOldSlice(id - 12);
        return LittleSlice.values()[id - 24];
    }

    private static void checkVersion() {
        if (ReflectionHelper.getPrivateValue(CreativeCore.class, null, "version").equals("1.7.4") || ReflectionHelper.getPrivateValue(CreativeCore.class, null, "version")
            .equals("1.9.8")) // Make
                                                                                                                                                                                                // CreativeCore
            throw new RuntimeException("Please update CreativeCore");
    }

    public static LittleSlice getOldSlice(int id) {
        checkVersion();
        LittleSlice wrongSlice = LittleSlice.values()[id];
        if (wrongSlice.axis == Axis.Yaxis) {
            for (LittleSlice slice : LittleSlice.values()) {
                if (slice.axis == wrongSlice.axis && slice.start.equals(wrongSlice.start) && slice.end.equals(wrongSlice.end) && slice.isRight != wrongSlice.isRight)
                    return slice;
            }

            throw new RuntimeException("Slice id=" + id + " could not be converted to the new slice format");
        }
        return wrongSlice;

    }

    public static LittleSlice getOlderSlice(int id) {
        checkVersion();
        LittleSlice wrongSlice = LittleSlice.values()[id];
        if (wrongSlice.axis == Axis.Xaxis) {
            for (LittleSlice slice : LittleSlice.values()) {
                if (slice.axis == wrongSlice.axis && slice.start.equals(wrongSlice.start) && slice.end.equals(wrongSlice.end) && slice.isRight != wrongSlice.isRight)
                    return slice;
            }

            throw new RuntimeException("Slice id=" + id + " could not be converted to the new slice format");
        }
        return wrongSlice;

    }
//
    private LittleSlice(Axis axis, boolean isRight, int traingleOrderPositive, int traingleOrderNegative, BoxCorner start, BoxCorner end) {
        this.axis = axis;
        this.isRight = isRight;
        this.start = start;
        this.end = end;
        this.traingleOrderPositive = traingleOrderPositive;
        this.traingleOrderNegative = traingleOrderNegative;
        this.sliceVec = new Vector3i(
            getDirectionBetweenFacing(start.x, end.x),
            getDirectionBetweenFacing(start.y, end.y),
            getDirectionBetweenFacing(start.z, end.z)
        );
        Vector3i temp = RotationUtilProxy.rotate(sliceVec, RotationProxy.getRotation(axis, isRight));
        this.normal = new int[] { temp.x(), temp.y(), temp.z() };
        EnumFacingProxy.Axis one = RotationUtilProxy.getOneEnumFacing(axis);
        EnumFacingProxy.Axis two = RotationUtilProxy.getTwoEnumFacing(axis);
        this.emptySideOne = EnumFacingProxy.getFacingFromAxis(normal[one.ordinal()] == 1 ? EnumFacingProxy.AxisDirection.POSITIVE : EnumFacingProxy.AxisDirection.NEGATIVE, one);
        this.emptySideTwo = EnumFacingProxy.getFacingFromAxis(normal[two.ordinal()] == 1 ? EnumFacingProxy.AxisDirection.POSITIVE : EnumFacingProxy.AxisDirection.NEGATIVE, two);
    }
//
//    public LittleSlice getOpposite() {
//        return getSliceFromNormal(new int[] { -normal[0], -normal[1], -normal[2] });
//    }
//
//    public int getDirectionScale(Axis axis) {
//        return VectorUtils.get(axis, sliceVec);
//    }
//
//    public boolean isFacingPositive(Axis axis) {
//        return normal[axis.ordinal()] > 0;
//    }
//
//    public EnumFacing getEmptySide(Axis axis) {
//        return EnumFacing.getFacingFromAxis(normal[axis.ordinal()] > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE, axis);
//    }
//
//    public int[] getNormal() {
//        return normal;
//    }
//
//    public Vec3d getNormalVec() {
//        return new Vec3d(normal[0], normal[1], normal[2]).normalize();
//    }
//
//    public LittleSlice rotate(Rotation rotation) {
//        return getSliceFromNormal(new int[] { rotation.getMatrix().getX(normal), rotation.getMatrix().getY(normal), rotation.getMatrix().getZ(normal) });
//    }
//
//    public LittleSlice flip(Axis axis) {
//        int[] newNormal = Arrays.copyOf(normal, normal.length);
//        newNormal[axis.ordinal()] = -newNormal[axis.ordinal()];
//        return getSliceFromNormal(newNormal);
//    }
//
    public EnumFacingProxy getPreferedSide(LittleVec size) {
        int sizeOne = size.get(emptySideOne.getAxis());
        int sizeTwo = size.get(emptySideTwo.getAxis());
        if (sizeOne > sizeTwo)
            return emptySideTwo;
        else if (sizeOne < sizeTwo)
            return emptySideOne;

        return emptySideOne.getAxis() == EnumFacingProxy.Axis.Y ? emptySideOne : emptySideTwo;
    }

    public EnumFacing getPreferedSide(Vector3d size) {
        double sizeOne = VectorUtilsProxy.get(emptySideOne.getAxis(), size);
        double sizeTwo = VectorUtilsProxy.get(emptySideTwo.getAxis(), size);
        if (sizeOne > sizeTwo)
            return EnumFacingProxy.toEnumFacing(emptySideTwo);
        else if (sizeOne < sizeTwo)
            return EnumFacingProxy.toEnumFacing(emptySideOne);

        return emptySideOne.getAxis() == EnumFacingProxy.Axis.Y ? EnumFacingProxy.toEnumFacing(emptySideOne) : EnumFacingProxy.toEnumFacing(emptySideTwo);
    }
//
//    public EnumFacing getPreferedSide(Vec3d size) {
//        double sizeOne = VectorUtils.get(emptySideOne.getAxis(), size);
//        double sizeTwo = VectorUtils.get(emptySideTwo.getAxis(), size);
//        if (sizeOne > sizeTwo)
//            return emptySideTwo;
//        else if (sizeOne < sizeTwo)
//            return emptySideOne;
//
//        return emptySideOne.getAxis() == Axis.Y ? emptySideOne : emptySideTwo;
//    }
//
//    public boolean shouldRenderSide(EnumFacing facing, LittleVec size) {
//        if (normal[facing.getAxis().ordinal()] == facing.getAxisDirection().getOffset()) {
//            Axis otherAxis = RotationUtils.getOne(axis);
//            if (otherAxis == facing.getAxis())
//                otherAxis = RotationUtils.getTwo(axis);
//
//            int sizeOne = size.get(facing.getAxis());
//            int sizeTwo = size.get(otherAxis);
//            if (sizeOne > sizeTwo)
//                return false;
//            else if (sizeOne < sizeTwo)
//                return true;
//            else
//                return axis == Axis.Y ? RotationUtils.getOne(axis) == facing.getAxis() : otherAxis != Axis.Y;
//        }
//        return true;
//    }
//
//    public boolean shouldRenderSide(EnumFacing facing, Vec3d size) {
//        if (normal[facing.getAxis().ordinal()] == facing.getAxisDirection().getOffset()) {
//            Axis otherAxis = RotationUtils.getOne(axis);
//            if (otherAxis == facing.getAxis())
//                otherAxis = RotationUtils.getTwo(axis);
//
//            double sizeOne = VectorUtils.get(facing.getAxis(), size);
//            double sizeTwo = VectorUtils.get(otherAxis, size);
//            if (sizeOne > sizeTwo)
//                return false;
//            else if (sizeOne < sizeTwo)
//                return true;
//            else
//                return axis == Axis.Y ? RotationUtils.getOne(axis) == facing.getAxis() : otherAxis != Axis.Y;
//        }
//        return true;
//    }
//
//    /** Theoretically there are two corners, but it will always return the one with a
//     * positive direction */
//    public BoxCorner getFilledCorner() {
//        return BoxCorner.getCornerUnsorted(RotationUtils.getFacing(axis), emptySideOne.getOpposite(), emptySideTwo.getOpposite());
//    }
//
    /** Theoretically there are two corners, but it will always return the one with a
     * positive direction */
    public BoxCornerProxy getEmptyCorner() {
        return BoxCornerProxy.getCornerUnsorted(RotationUtilProxy.getFacing(axis), emptySideOne, emptySideTwo);
    }
//
//    public boolean isCornerAffected(BoxCorner corner) {
//        return (corner.x == emptySideOne || corner.y == emptySideOne || corner.z == emptySideOne) && (corner.x == emptySideTwo || corner.y == emptySideTwo || corner.z == emptySideTwo);
//    }
//
//    public int getSliceID() {
//        return this.ordinal() + 24;
//    }
//
//    /*
//     * public void sliceVector(LittleCorner corner, Vector3f vec, CubeObject cube,
//     * LittleTileSize size) { if(isCornerAffected(corner)) { EnumFacing side =
//     * getPreferedSide(size); switch(side.getAxis()) { case X: vec.x =
//     * cube.getVertexInformationPosition(side.getOpposite().ordinal()); break; case
//     * Y: vec.y = cube.getVertexInformationPosition(side.getOpposite().ordinal());
//     * break; case Z: vec.z =
//     * cube.getVertexInformationPosition(side.getOpposite().ordinal()); break; } } }
//     */
}
