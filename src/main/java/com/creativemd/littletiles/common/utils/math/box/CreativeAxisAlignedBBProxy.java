package com.creativemd.littletiles.common.utils.math.box;

import javax.annotation.Nullable;

import com.creativemd.littletiles.common.tile.math.LittleUtils;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import org.joml.Vector3d;

public class CreativeAxisAlignedBBProxy extends AxisAlignedBB {

    public CreativeAxisAlignedBBProxy(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
    }

    public boolean contains(Vector3d vec) {
        if (vec.x > this.minX && vec.x < this.maxX) {
            if (vec.y > this.minY && vec.y < this.maxY) {
                return vec.z > this.minZ && vec.z < this.maxZ;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public double calculateYOffsetStepUp(AxisAlignedBB other, AxisAlignedBB otherY, double offset) {
        return calculateYOffset(other, offset);
    }

    protected double getValueOfFacing(EnumFacingProxy facing) {
        switch (facing) {
        case EAST:
            return maxX;
        case WEST:
            return minX;
        case UP:
            return maxY;
        case DOWN:
            return minY;
        case SOUTH:
            return maxZ;
        case NORTH:
            return minZ;

        }
        return 0;
    }

    public Vector3d getCorner(BoxCornerProxy corner) {
        return new Vector3d(getCornerX(corner), getCornerY(corner), getCornerZ(corner));
    }

    public Vector3d getCornerVector3d(BoxCornerProxy corner) {
        return new Vector3d(getCornerX(corner), getCornerY(corner), getCornerZ(corner));
    }

    public double getCornerValue(BoxCornerProxy corner, Axis axis) {
        return getValueOfFacing(corner.getFacing(axis));
    }

    public double getCornerX(BoxCornerProxy corner) {
        return getValueOfFacing(corner.x);
    }

    public double getCornerY(BoxCornerProxy corner) {
        return getValueOfFacing(corner.y);
    }

    public double getCornerZ(BoxCornerProxy corner) {
        return getValueOfFacing(corner.z);
    }

    public Vector3d getSize() {
        return new Vector3d(maxX - minX, maxY - minY, maxZ - minZ);
    }

    public Vector3d getSize3d() {
        return new Vector3d(maxX - minX, maxY - minY, maxZ - minZ);
    }

    public double getVolume() {
        return (maxX - minX) * (maxY - minY) * (maxZ - minZ);
    }

    public double getIntersectionVolume(AxisAlignedBB other) {
        double d0 = Math.max(this.minX, other.minX);
        double d1 = Math.max(this.minY, other.minY);
        double d2 = Math.max(this.minZ, other.minZ);
        double d3 = Math.min(this.maxX, other.maxX);
        double d4 = Math.min(this.maxY, other.maxY);
        double d5 = Math.min(this.maxZ, other.maxZ);
        if (d0 < d3 && d1 < d4 && d2 < d5)
            return Math.abs((d3 - d0) * (d4 - d1) * (d5 - d2));
        return 0;
    }

    public double getSize(Axis axis) {
        switch (axis) {
        case X:
            return maxX - minX;
        case Y:
            return maxY - minY;
        case Z:
            return maxZ - minZ;
        }
        return 0;
    }

    public double getMin(Axis axis) {
        switch (axis) {
        case X:
            return minX;
        case Y:
            return minY;
        case Z:
            return minZ;
        }
        return 0;
    }

    public double getMax(Axis axis) {
        switch (axis) {
        case X:
            return maxX;
        case Y:
            return maxY;
        case Z:
            return maxZ;
        }
        return 0;
    }

    public static double getValueOfFacing(AxisAlignedBB bb, EnumFacingProxy facing) {
        switch (facing) {
        case EAST:
            return bb.maxX;
        case WEST:
            return bb.minX;
        case UP:
            return bb.maxY;
        case DOWN:
            return bb.minY;
        case SOUTH:
            return bb.maxZ;
        case NORTH:
            return bb.minZ;

        }
        return 0;
    }

    public static double getMin(AxisAlignedBB bb, Axis axis) {
        switch (axis) {
        case X:
            return bb.minX;
        case Y:
            return bb.minY;
        case Z:
            return bb.minZ;
        }
        return 0;
    }

    public static double getMax(AxisAlignedBB bb, Axis axis) {
        switch (axis) {
        case X:
            return bb.maxX;
        case Y:
            return bb.maxY;
        case Z:
            return bb.maxZ;
        }
        return 0;
    }

    public static Vector3d getCorner(AxisAlignedBB bb, BoxCornerProxy corner) {
        return new Vector3d(getCornerX(bb, corner), getCornerY(bb, corner), getCornerZ(bb, corner));
    }

    public static double getCornerValue(AxisAlignedBB bb, BoxCornerProxy corner, Axis axis) {
        return getValueOfFacing(bb, corner.getFacing(axis));
    }

    public static double getCornerX(AxisAlignedBB bb, BoxCornerProxy corner) {
        return getValueOfFacing(bb, corner.x);
    }

    public static double getCornerY(AxisAlignedBB bb, BoxCornerProxy corner) {
        return getValueOfFacing(bb, corner.y);
    }

    public static double getCornerZ(AxisAlignedBB bb, BoxCornerProxy corner) {
        return getValueOfFacing(bb, corner.z);
    }

    public static boolean isClosest(Vector3d p_186661_1_, @Nullable Vector3d p_186661_2_, Vector3d p_186661_3_) {
        return p_186661_2_ == null || LittleUtils.squareDistanceTo(p_186661_1_, p_186661_3_) < LittleUtils.squareDistanceTo(p_186661_1_, p_186661_2_);
    }
}
