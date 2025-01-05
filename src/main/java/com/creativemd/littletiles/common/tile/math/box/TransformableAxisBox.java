package com.creativemd.littletiles.common.tile.math.box;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;

import com.creativemd.littletiles.common.tile.math.box.LittleTransformableBox.VectorFanCache;
import com.creativemd.littletiles.common.tile.math.box.LittleTransformableBox.VectorFanFaceCache;

import com.creativemd.littletiles.common.tile.math.vec.VectorFan;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.math.VectorUtilsProxy;
import com.creativemd.littletiles.common.utils.math.box.CreativeAxisAlignedBBProxy;
import com.creativemd.littletiles.common.utils.math.geo.NormalPlaneProxy;
import com.creativemd.littletiles.common.utils.math.vec.Vec3dProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import paulscode.sound.Vector3D;

public class TransformableAxisBox extends CreativeAxisAlignedBBProxy {

    private LittleGridContext context;
    private LittleBox box;

    public TransformableAxisBox(LittleBox box, LittleGridContext context, double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
        this.box = box;
        this.context = context;
    }

//    @Override
    public TransformableAxisBox setMaxY(double y2) {
        return new TransformableAxisBox(box, context, this.minX, this.minY, this.minZ, this.maxX, y2, this.maxZ);
    }

    @Override
    public boolean equals(Object object) {
        if (object.getClass() == TransformableAxisBox.class) {
            return ((TransformableAxisBox) object).minX == this.minX && ((TransformableAxisBox) object).minY == this.minY && ((TransformableAxisBox) object).minZ == this.minZ && ((TransformableAxisBox) object).maxX == this.maxX && ((TransformableAxisBox) object).maxY == this.maxY && ((TransformableAxisBox) object).maxZ == this.maxZ && ((TransformableAxisBox) object).context == this.context && ((TransformableAxisBox) object).box == this.box;
        }
        return false;
    }

    @Override
    public TransformableAxisBox contract(double p_191195_1_, double p_191195_3_, double p_191195_5_) {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (p_191195_1_ < 0.0D) {
            d0 -= p_191195_1_;
        } else if (p_191195_1_ > 0.0D) {
            d3 -= p_191195_1_;
        }

        if (p_191195_3_ < 0.0D) {
            d1 -= p_191195_3_;
        } else if (p_191195_3_ > 0.0D) {
            d4 -= p_191195_3_;
        }

        if (p_191195_5_ < 0.0D) {
            d2 -= p_191195_5_;
        } else if (p_191195_5_ > 0.0D) {
            d5 -= p_191195_5_;
        }

        return new TransformableAxisBox(box, context, d0, d1, d2, d3, d4, d5);
    }

    @Override
    public TransformableAxisBox expand(double x, double y, double z) {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (x < 0.0D) {
            d0 += x;
        } else if (x > 0.0D) {
            d3 += x;
        }

        if (y < 0.0D) {
            d1 += y;
        } else if (y > 0.0D) {
            d4 += y;
        }

        if (z < 0.0D) {
            d2 += z;
        } else if (z > 0.0D) {
            d5 += z;
        }

        return new TransformableAxisBox(box, context, d0, d1, d2, d3, d4, d5);
    }

//    @Override
    public AxisAlignedBB grow(double x, double y, double z) {
        double d0 = this.minX - x;
        double d1 = this.minY - y;
        double d2 = this.minZ - z;
        double d3 = this.maxX + x;
        double d4 = this.maxY + y;
        double d5 = this.maxZ + z;
        return new TransformableAxisBox(box, context, d0, d1, d2, d3, d4, d5);
    }

//    @Override
    public TransformableAxisBox intersect(AxisAlignedBB p_191500_1_) {
        double d0 = Math.max(this.minX, p_191500_1_.minX);
        double d1 = Math.max(this.minY, p_191500_1_.minY);
        double d2 = Math.max(this.minZ, p_191500_1_.minZ);
        double d3 = Math.min(this.maxX, p_191500_1_.maxX);
        double d4 = Math.min(this.maxY, p_191500_1_.maxY);
        double d5 = Math.min(this.maxZ, p_191500_1_.maxZ);
        return new TransformableAxisBox(box, context, d0, d1, d2, d3, d4, d5);
    }

    @Override
    public AxisAlignedBB offset(double x, double y, double z) {
        return new TransformableAxisBox(box, context, this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

//    @Override
    public AxisAlignedBB offset(BlockPos pos) {
        return new TransformableAxisBox(box, context, this.minX + pos.getX(), this.minY + pos.getY(), this.minZ + pos.getZ(), this.maxX + pos.getX(), this.maxY + pos
                .getY(), this.maxZ + pos.getZ());
    }

    @Override
    public double calculateYOffsetStepUp(AxisAlignedBB other, AxisAlignedBB otherY, double offset) {
        double newOffset = calculateYOffset(otherY, offset);
        if (offset > 0) {
            if (newOffset < offset)
                return newOffset / 2;
        } else {
            if (newOffset > offset)
                return newOffset / 2;
        }

        return newOffset;
    }

    public VectorFanFaceCache getFaceCache(EnumFacingProxy facing) {
        VectorFanCache cache = ((LittleTransformableBox) box).requestCache();
        if (cache != null)
            return cache.get(facing);
        return null;
    }

    public double calculateOffset(AxisAlignedBB other, Axis axis, double offset) {
        if (offset == 0)
            return offset;
        if (Math.abs(offset) < 1.0E-7D)
            return offset;
        boolean positive = offset > 0;
        EnumFacingProxy direction = EnumFacingProxy.getFacingFromAxis(positive ? EnumFacingProxy.AxisDirection.POSITIVE : EnumFacingProxy.AxisDirection.NEGATIVE, axis);

        Axis one = EnumFacingProxy.Axis.getOne(axis);
        Axis two = EnumFacingProxy.Axis.getTwo(axis);
        double minOne = getMin(other, one);
        minOne -= Math.floor(getMin(one));
        minOne *= context.size;
        double minTwo = getMin(other, two);
        minTwo -= Math.floor(getMin(two));
        minTwo *= context.size;
        double maxOne = getMax(other, one);
        maxOne -= Math.floor(getMin(one));
        maxOne *= context.size;
        double maxTwo = getMax(other, two);
        maxTwo -= Math.floor(getMin(two));
        maxTwo *= context.size;

        double otherAxis = (offset > 0 ? getMax(other, axis) : getMin(other, axis));
        otherAxis -= Math.floor(getMin(axis));
        otherAxis *= context.size;

        NormalPlaneProxy[] cuttingPlanes = new NormalPlaneProxy[] { new NormalPlaneProxy(one, (float) minOne, EnumFacingProxy
                .getFacingFromAxis(EnumFacingProxy.AxisDirection.NEGATIVE, one)), new NormalPlaneProxy(two, (float) minTwo, EnumFacingProxy
                        .getFacingFromAxis(EnumFacingProxy.AxisDirection.NEGATIVE, two)), new NormalPlaneProxy(one, (float) maxOne, EnumFacingProxy
                                .getFacingFromAxis(EnumFacingProxy.AxisDirection.POSITIVE, one)), new NormalPlaneProxy(two, (float) maxTwo, EnumFacingProxy.getFacingFromAxis(EnumFacingProxy.AxisDirection.POSITIVE, two)) };

        VectorFan tempFan = new VectorFan(/* @TODO can we pass null here? */(org.joml.Vector3f[]) null);
        VectorFanFaceCache front = getFaceCache(direction.getOpposite());
        if (front.hasAxisStrip()) {
            for (VectorFan vectorFan : front.axisStrips) {
                tempFan.set(vectorFan);
                if (tempFan.cutWithoutCopy(cuttingPlanes)) {
                    if (offset > 0.0D && getMax(other, axis) <= getMin(axis)) {
                        double d1 = getMin(axis) - getMax(other, axis);

                        if (d1 < offset)
                            return d1;
                    } else if (offset < 0.0D && getMin(other, axis) >= getMax(axis)) {
                        double d0 = getMax(axis) - getMin(other, axis);

                        if (d0 > offset)
                            return d0;

                    }
                    return offset;
                }
            }
        }

        double distance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < EnumFacingProxy.values().length; i++) {
            EnumFacingProxy facing = EnumFacingProxy.values()[i];
            if (facing == direction)
                continue;

            VectorFanFaceCache face = getFaceCache(facing);
            if (!face.hasTiltedStrip())
                continue;

            for (VectorFan vectorFan : face.tilted()) {
                tempFan.set(vectorFan);
                tempFan.cutWithoutCopy(cuttingPlanes);
                if (tempFan.isEmpty())
                    continue;

                for (int j = 0; j < tempFan.count(); j++) {
                    org.joml.Vector3f vec = tempFan.get(j);
                    double tempDistance = positive ? VectorUtilsProxy.get(axis, vec) - otherAxis : otherAxis - VectorUtilsProxy.get(axis, vec);

                    if (tempDistance < 0 && !OrientatedBoundingBox.equals(tempDistance, 0))
                        return offset;

                    if (tempDistance < distance)
                        distance = tempDistance;
                }
            }
        }

        if (Double.isInfinite(distance))
            return offset;

        distance *= context.pixelSize;

        if (offset > 0.0D) {
            if (distance < offset)
                return distance;
            return offset;
        } else if (offset < 0.0D) {
            if (-distance > offset)
                return -distance;
            return offset;
        }
        return offset;
    }

    private static Vector3f[] reverse(Vector3f[] array) {
        Vector3f[] b = new Vector3f[array.length];
        int j = array.length;
        for (int i = 0; i < array.length; i++) {
            b[j - 1] = array[i];
            j = j - 1;
        }
        return b;
    }

    @Override
    public double calculateXOffset(AxisAlignedBB other, double offsetX) {
        return calculateOffset(other, Axis.X, offsetX);
    }

    @Override
    public double calculateYOffset(AxisAlignedBB other, double offsetY) {
        return calculateOffset(other, Axis.Y, offsetY);
    }

    @Override
    public double calculateZOffset(AxisAlignedBB other, double offsetZ) {
        return calculateOffset(other, Axis.Z, offsetZ);
    }

    @Nullable
    protected Vec3dProxy collideWithPlane(Axis axis, double value, Vec3dProxy vecA, Vec3dProxy vecB) {
        Vec3dProxy vec3dProxy = axis != Axis.X ? axis != Axis.Y ? vecA.getIntermediateWithZValue(vecB, value) : vecA.getIntermediateWithYValue(vecB, value) : vecA
                .getIntermediateWithXValue(vecB, value);
        return vec3dProxy != null && intersectsWithAxis(axis, vec3dProxy) ? vec3dProxy : null;
    }

    public boolean intersectsWithAxis(Axis axis, Vec3dProxy vec) {
        switch (axis) {
        case X:
            return box.intersectsWithYZ(context, vec);
        case Y:
            return box.intersectsWithXZ(context, vec);
        case Z:
            return box.intersectsWithXY(context, vec);
        }
        return false;
    }

    @Override
    public String toString() {
        return "tbb[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

}