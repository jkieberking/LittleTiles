package com.creativemd.littletiles.common.utils.math;

import com.creativemd.creativecore.common.utils.RotationUtils.Axis;
import com.creativemd.littletiles.common.tile.math.vec.VectorFan;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import org.joml.Vector3d;
import org.joml.Vector3i;

import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

public class VectorUtilsProxy {

    public static void set(Tuple3d vec, double value, Axis axis) {
        switch (axis) {
            case Xaxis:
                vec.x = value;
                break;
            case Yaxis:
                vec.y = value;
                break;
            case Zaxis:
                vec.z = value;
                break;
        }
    }

    public static void set(Tuple3f vec, float value, Axis axis) {
        switch (axis) {
            case Xaxis:
                vec.x = value;
                break;
            case Yaxis:
                vec.y = value;
                break;
            case Zaxis:
                vec.z = value;
                break;
        }
    }

    public static Vector3d set(Vector3d vec, double value, Axis axis) {
        switch (axis) {
            case Xaxis:
                return new Vector3d(value, vec.y, vec.z);
            case Yaxis:
                return new Vector3d(vec.x, value, vec.z);
            case Zaxis:
                return new Vector3d(vec.x, vec.y, value);
        }
        return null;
    }

    public static Vector3d set(Vector3d vec, int value, Axis axis) {
        switch (axis) {
            case Xaxis:
                return new Vector3d(value, vec.y(), vec.z());
            case Yaxis:
                return new Vector3d(vec.x(), value, vec.z());
            case Zaxis:
                return new Vector3d(vec.x(), vec.y(), value);
        }
        return null;
    }

    public static double get(Axis axis, Tuple3d vec) {
        return get(axis, vec.x, vec.y, vec.z);
    }

    public static float get(Axis axis, Tuple3f vec) {
        return get(axis, vec.x, vec.y, vec.z);
    }

    public static double get(Axis axis, Vector3d vec) {
        return get(axis, vec.x(), vec.y, vec.z);
    }

    public static double get(EnumFacingProxy.Axis axis, Vector3d vec) {
        return get(axis, vec.x(), vec.y, vec.z);
    }

    public static int get(Axis axis, Vector3i vec) {
        return get(axis, vec.x(), vec.y(), vec.z());
    }

    public static float get(Axis axis, float x, float y, float z) {
        switch (axis) {
            case Xaxis:
                return x;
            case Yaxis:
                return y;
            case Zaxis:
                return z;
        }
        return 0;
    }

    public static float get(EnumFacingProxy.Axis axis, float x, float y, float z) {
        switch (axis) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
        }
        return 0;
    }

    public static double get(EnumFacingProxy.Axis axis, double x, double y, double z) {
        switch (axis) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
        }
        return 0;
    }

    public static BlockPos set(BlockPos vec, int value, Axis axis) {
        switch (axis) {
            case Xaxis:
                return new BlockPos(value, vec.getY(), vec.getZ());
            case Yaxis:
                return new BlockPos(vec.getX(), value, vec.getZ());
            case Zaxis:
                return new BlockPos(vec.getX(), vec.getY(), value);
        }
        return null;
    }

    public static double get(Axis axis, double x, double y, double z) {
        switch (axis) {
            case Xaxis:
                return x;
            case Yaxis:
                return y;
            case Zaxis:
                return z;
            }
        return 0;
    }

    public static int get(Axis axis, int x, int y, int z) {
        switch (axis) {
            case Xaxis:
                return x;
            case Yaxis:
                return y;
            case Zaxis:
                return z;
            }
        return 0;
    }

    public static boolean isZero(double number) {
        return number > -VectorFan.EPSILON && number < VectorFan.EPSILON;
    }

    public static boolean isZero(float number) {
        return number > -VectorFan.EPSILON && number < VectorFan.EPSILON;
    }

    public static boolean equals(double number, double number2) {
        return number - number2 > -VectorFan.EPSILON && number - number2 < VectorFan.EPSILON;
    }

    public static boolean equals(float number, float number2) {
        return number - number2 > -VectorFan.EPSILON && number - number2 < VectorFan.EPSILON;
    }

}
