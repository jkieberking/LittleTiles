// @TODO figure out a better way than just mostly copy/pasting 1.12's EntityFacing
package com.creativemd.littletiles.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import org.joml.Vector3i;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public enum EnumFacingProxy {
    DOWN(0, 1, -1, "down", EnumFacingProxy.AxisDirection.NEGATIVE, EnumFacingProxy.Axis.Y, new Vector3i(0, -1, 0)),
    UP(1, 0, -1, "up", EnumFacingProxy.AxisDirection.POSITIVE, EnumFacingProxy.Axis.Y, new Vector3i(0, 1, 0)),
    NORTH(2, 3, 2, "north", EnumFacingProxy.AxisDirection.NEGATIVE, EnumFacingProxy.Axis.Z, new Vector3i(0, 0, -1)),
    SOUTH(3, 2, 0, "south", EnumFacingProxy.AxisDirection.POSITIVE, EnumFacingProxy.Axis.Z, new Vector3i(0, 0, 1)),
    WEST(4, 5, 1, "west", EnumFacingProxy.AxisDirection.NEGATIVE, EnumFacingProxy.Axis.X, new Vector3i(-1, 0, 0)),
    EAST(5, 4, 3, "east", EnumFacingProxy.AxisDirection.POSITIVE, EnumFacingProxy.Axis.X, new Vector3i(1, 0, 0));

    public static final EnumFacingProxy[] horizontalFacings = {EnumFacingProxy.SOUTH, EnumFacingProxy.WEST, EnumFacingProxy.NORTH,
        EnumFacingProxy.EAST};

    /**
     * Ordering index for D-U-N-S-W-E
     */
    private final int index;
    /**
     * Index of the opposite Facing in the VALUES array
     */
    private final int opposite;
    /**
     * Ordering index for the HORIZONTALS field (S-W-N-E)
     */
    private final int horizontalIndex;
    private final String name;
    private final EnumFacingProxy.Axis axis;
    private final EnumFacingProxy.AxisDirection axisDirection;
    /**
     * Normalized Vector that points in the direction of this Facing
     */
    private final Vector3i directionVec;
    /**
     * All facings in D-U-N-S-W-E order
     */
    public static final EnumFacingProxy[] VALUES = new EnumFacingProxy[6];
    /**
     * All Facings with horizontal axis in order S-W-N-E
     */
    public static final EnumFacingProxy[] HORIZONTALS = new EnumFacingProxy[4];
    private static final Map<String, EnumFacingProxy> NAME_LOOKUP = Maps.<String, EnumFacingProxy>newHashMap();

    private EnumFacingProxy(int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn, EnumFacingProxy.AxisDirection axisDirectionIn, EnumFacingProxy.Axis axisIn, Vector3i directionVecIn) {
        this.index = indexIn;
        this.horizontalIndex = horizontalIndexIn;
        this.opposite = oppositeIn;
        this.name = nameIn;
        this.axis = axisIn;
        this.axisDirection = axisDirectionIn;
        this.directionVec = directionVecIn;
    }

    // this takes in the MovingObjectPosition.sideHit int and returns a EnumFacingProxy
    //    sideHit's int is: -1 then it went the full length of the ray trace. Bottom = 0, Top = 1, East = 2, West
    //     * = 3, North = 4, South = 5.
    public static EnumFacingProxy fromSideHitInt(int sideHit) {
        switch (sideHit) {
            case 0:
                return DOWN;
            case 1:
                return UP;
            case 2:
                return EAST;
            case 3:
                return WEST;
            case 4:
                return NORTH;
            case 5:
                return SOUTH;
        }
        throw new RuntimeException("no enum facing found for sideHit");
    }
    public static EnumFacingProxy fromEnumFacing(EnumFacing facing) {
        switch (facing) {
            case UP:
                return UP;
            case DOWN:
                return DOWN;
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }
        throw new RuntimeException("no enum facing");
    }

    public static EnumFacing toEnumFacing(EnumFacing facing) {
        switch (facing) {
            case UP:
                return EnumFacing.UP;
            case DOWN:
                return EnumFacing.DOWN;
            case NORTH:
                return EnumFacing.NORTH;
            case SOUTH:
                return EnumFacing.SOUTH;
            case WEST:
                return EnumFacing.WEST;
            case EAST:
                return EnumFacing.EAST;
        }
        throw new RuntimeException("no enum facing");
    }

    public static EnumFacing toEnumFacing(EnumFacingProxy facing) {
        switch (facing) {
            case UP:
                return EnumFacing.UP;
            case DOWN:
                return EnumFacing.DOWN;
            case NORTH:
                return EnumFacing.NORTH;
            case SOUTH:
                return EnumFacing.SOUTH;
            case WEST:
                return EnumFacing.WEST;
            case EAST:
                return EnumFacing.EAST;
        }
        throw new RuntimeException("no enum facing");
    }

    public static ForgeDirection toForgeDirection(EnumFacingProxy facing) {
        switch (facing) {
            case UP:
                return ForgeDirection.UP;
            case DOWN:
                return ForgeDirection.DOWN;
            case NORTH:
                return ForgeDirection.NORTH;
            case SOUTH:
                return ForgeDirection.SOUTH;
            case WEST:
                return ForgeDirection.WEST;
            case EAST:
                return ForgeDirection.EAST;
        }
        throw new RuntimeException("no enum facing");
    }

    /**
     * Get the Index of this Facing (0-5). The order is D-U-N-S-W-E
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Get the index of this horizontal facing (0-3). The order is S-W-N-E
     */
    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }

    /**
     * Get the AxisDirection of this Facing.
     */
    public EnumFacingProxy.AxisDirection getAxisDirection() {
        return this.axisDirection;
    }

    /**
     * Get the opposite Facing (e.g. DOWN => UP)
     */
    public EnumFacingProxy getOpposite() {
        /**
         * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E. Named getFront for legacy reasons.
         */
        return getFront(this.opposite);
    }

    /**
     * Rotate this Facing around the given axis clockwise. If this facing cannot be rotated around the given axis,
     * returns this facing without rotating.
     */
    public EnumFacingProxy rotateAround(EnumFacingProxy.Axis axis) {
        switch (axis) {
            case X:

                if (this != WEST && this != EAST) {
                    return this.rotateX();
                }

                return this;
            case Y:

                if (this != UP && this != DOWN) {
                    return this.rotateY();
                }

                return this;
            case Z:

                if (this != NORTH && this != SOUTH) {
                    return this.rotateZ();
                }

                return this;
            default:
                throw new IllegalStateException("Unable to get CW facing for axis " + axis);
        }
    }

    /**
     * Rotate this Facing around the Y axis clockwise (NORTH => EAST => SOUTH => WEST => NORTH)
     */
    public EnumFacingProxy rotateY() {
        switch (this) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            default:
                throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        }
    }

    /**
     * Rotate this Facing around the X axis (NORTH => DOWN => SOUTH => UP => NORTH)
     */
    private EnumFacingProxy rotateX() {
        switch (this) {
            case NORTH:
                return DOWN;
            case EAST:
            case WEST:
            default:
                throw new IllegalStateException("Unable to get X-rotated facing of " + this);
            case SOUTH:
                return UP;
            case UP:
                return NORTH;
            case DOWN:
                return SOUTH;
        }
    }

    /**
     * Rotate this Facing around the Z axis (EAST => DOWN => WEST => UP => EAST)
     */
    private EnumFacingProxy rotateZ() {
        switch (this) {
            case EAST:
                return DOWN;
            case SOUTH:
            default:
                throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
            case WEST:
                return UP;
            case UP:
                return EAST;
            case DOWN:
                return WEST;
        }
    }

    /**
     * Rotate this Facing around the Y axis counter-clockwise (NORTH => WEST => SOUTH => EAST => NORTH)
     */
    public EnumFacingProxy rotateYCCW() {
        switch (this) {
            case NORTH:
                return WEST;
            case EAST:
                return NORTH;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            default:
                throw new IllegalStateException("Unable to get CCW facing of " + this);
        }
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetX() {
        return this.axis == EnumFacingProxy.Axis.X ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetY() {
        return this.axis == EnumFacingProxy.Axis.Y ? this.axisDirection.getOffset() : 0;
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetZ() {
        return this.axis == EnumFacingProxy.Axis.Z ? this.axisDirection.getOffset() : 0;
    }

    /**
     * Same as getName, but does not override the method from Enum.
     */
    public String getName2() {
        return this.name;
    }

    public EnumFacingProxy.Axis getAxis() {
        return this.axis;
    }

    /**
     * Get the facing specified by the given name
     */
    @Nullable
    public static EnumFacingProxy byName(String name) {
        return name == null ? null : (EnumFacingProxy) NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E. Named getFront for legacy reasons.
     */
    public static EnumFacingProxy getFront(int index) {
        return VALUES[MathHelper.abs_int(index % values().length)];
    }

    /**
     * Get a Facing by it's horizontal index (0-3). The order is S-W-N-E.
     */
    public static EnumFacingProxy getHorizontal(int horizontalIndexIn) {
        return horizontalFacings[horizontalIndexIn];

    }

    /**
     * Get the Facing corresponding to the given angle (0-360). An angle of 0 is SOUTH, an angle of 90 would be WEST.
     */
    public static EnumFacingProxy fromAngle(double angle) {
        /**
         * Get a Facing by it's horizontal index (0-3). The order is S-W-N-E.
         */
        return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 3);
    }

    public float getHorizontalAngle() {
        return (float) ((this.horizontalIndex & 3) * 90);
    }

    /**
     * Choose a random Facing using the given Random
     */
    public static EnumFacingProxy random(Random rand) {
        return values()[rand.nextInt(values().length)];
    }

    public static EnumFacingProxy getFacingFromVector(float x, float y, float z) {
        EnumFacingProxy enumfacing = NORTH;
        float f = Float.MIN_VALUE;

        for (EnumFacingProxy enumfacing1 : values()) {
            float f1 = x * (float) enumfacing1.directionVec.x() + y * (float) enumfacing1.directionVec.y() + z * (float) enumfacing1.directionVec.z();

            if (f1 > f) {
                f = f1;
                enumfacing = enumfacing1;
            }
        }

        return enumfacing;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public static EnumFacingProxy getFacingFromAxis(EnumFacingProxy.AxisDirection axisDirectionIn, EnumFacingProxy.Axis axisIn) {
        for (EnumFacingProxy enumfacing : values()) {
            if (enumfacing.getAxisDirection() == axisDirectionIn && enumfacing.getAxis() == axisIn) {
                return enumfacing;
            }
        }

        throw new IllegalArgumentException("No such direction: " + axisDirectionIn + " " + axisIn);
    }

//    public static EnumFacing getDirectionFromEntityLiving(BlockPos p_190914_0_, EntityLivingBase p_190914_1_)
//    {
//        if (Math.abs(p_190914_1_.posX - (double)((float)p_190914_0_.getX() + 0.5F)) < 2.0D && Math.abs(p_190914_1_.posZ - (double)((float)p_190914_0_.getZ() + 0.5F)) < 2.0D)
//        {
//            double d0 = p_190914_1_.posY + (double)p_190914_1_.getEyeHeight();
//
//            if (d0 - (double)p_190914_0_.getY() > 2.0D)
//            {
//                return UP;
//            }
//
//            if ((double)p_190914_0_.getY() - d0 > 0.0D)
//            {
//                return DOWN;
//            }
//        }
//
//        return p_190914_1_.getHorizontalFacing().getOpposite();
//    }

    /**
     * Get a normalized Vector that points in the direction of this Facing.
     */
    public Vector3i getDirectionVec() {
        return this.directionVec;
    }

    static {
        for (EnumFacingProxy enumfacing : values()) {
            VALUES[enumfacing.index] = enumfacing;

            if (enumfacing.getAxis().isHorizontal()) {
                HORIZONTALS[enumfacing.horizontalIndex] = enumfacing;
            }

            NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(Locale.ROOT), enumfacing);
        }
    }

    public static enum Axis implements Predicate<EnumFacingProxy> {
        X("x", EnumFacingProxy.Plane.HORIZONTAL),
        Y("y", EnumFacingProxy.Plane.VERTICAL),
        Z("z", EnumFacingProxy.Plane.HORIZONTAL);

        private static final Map<String, EnumFacingProxy.Axis> NAME_LOOKUP = Maps.<String, EnumFacingProxy.Axis>newHashMap();
        private final String name;
        private final EnumFacingProxy.Plane plane;

        private Axis(String name, EnumFacingProxy.Plane plane) {
            this.name = name;
            this.plane = plane;
        }

        /**
         * Get the axis specified by the given name
         */
        @Nullable
        public static EnumFacingProxy.Axis byName(String name) {
            return name == null ? null : (EnumFacingProxy.Axis) NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
        }

        /**
         * Like getName but doesn't override the method from Enum.
         */
        public String getName2() {
            return this.name;
        }

        /**
         * If this Axis is on the vertical plane (Only true for Y)
         */
        public boolean isVertical() {
            return this.plane == EnumFacingProxy.Plane.VERTICAL;
        }

        /**
         * If this Axis is on the horizontal plane (true for X and Z)
         */
        public boolean isHorizontal() {
            return this.plane == EnumFacingProxy.Plane.HORIZONTAL;
        }

        public String toString() {
            return this.name;
        }

        public boolean apply(@Nullable EnumFacingProxy p_apply_1_) {
            return p_apply_1_ != null && p_apply_1_.getAxis() == this;
        }

        /**
         * Get this Axis' Plane (VERTICAL for Y, HORIZONTAL for X and Z)
         */
        public EnumFacingProxy.Plane getPlane() {
            return this.plane;
        }

        public String getName() {
            return this.name;
        }

        static {
            for (EnumFacingProxy.Axis enumfacing$axis : values()) {
                NAME_LOOKUP.put(enumfacing$axis.getName2().toLowerCase(Locale.ROOT), enumfacing$axis);
            }
        }
    }

    public static enum AxisDirection {
        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        private final int offset;
        private final String description;

        private AxisDirection(int offset, String description) {
            this.offset = offset;
            this.description = description;
        }

        /**
         * Get the offset for this AxisDirection. 1 for POSITIVE, -1 for NEGATIVE
         */
        public int getOffset() {
            return this.offset;
        }

        public String toString() {
            return this.description;
        }
    }

    public static enum Plane implements Predicate<EnumFacingProxy>, Iterable<EnumFacingProxy> {
        HORIZONTAL,
        VERTICAL;

        /**
         * All EnumFacing values for this Plane
         */
        public EnumFacingProxy[] facings() {
            switch (this) {
                case HORIZONTAL:
                    return new EnumFacingProxy[]{EnumFacingProxy.NORTH, EnumFacingProxy.EAST, EnumFacingProxy.SOUTH, EnumFacingProxy.WEST};
                case VERTICAL:
                    return new EnumFacingProxy[]{EnumFacingProxy.UP, EnumFacingProxy.DOWN};
                default:
                    throw new Error("Someone\'s been tampering with the universe!");
            }
        }

        /**
         * Choose a random Facing from this Plane using the given Random
         */
        public EnumFacingProxy random(Random rand) {
            EnumFacingProxy[] aenumfacing = this.facings();
            return aenumfacing[rand.nextInt(aenumfacing.length)];
        }

        public boolean apply(@Nullable EnumFacingProxy p_apply_1_) {
            return p_apply_1_ != null && p_apply_1_.getAxis().getPlane() == this;
        }

        public Iterator<EnumFacingProxy> iterator() {
            return Iterators.<EnumFacingProxy>forArray(this.facings());
        }
    }
}
