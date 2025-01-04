package com.creativemd.littletiles.common.tile.math;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class LittleUtils {

    public static final float EPSILON = 0.01F;
    public static final int scale = 5;

    public static boolean smallerThanAndEquals(double a, double b) {
        return a < b || equals(a, b);
    }

    public static boolean greaterThanAndEquals(double a, double b) {
        return a > b || equals(a, b);
    }

    public static boolean equals(double a, double b) {
        return a == b ? true : Math.abs(a - b) < EPSILON;
    }

    public static boolean equals(float a, float b) {
        return a == b ? true : Math.abs(a - b) < EPSILON;
    }

    public static double round(double valueToRound, int numberOfDecimalPlaces) {
        double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
        double interestedInZeroDPs = valueToRound * multipicationFactor;
        return Math.round(interestedInZeroDPs) / multipicationFactor;
    }

    public static BlockPos subtractVectors(Vector3i vec1, Vector3i vec2)
    {
        return LittleUtils.addVectors(vec1, -vec2.x(), -vec2.y(), -vec2.z());
    }

    public static BlockPos subtractVectors(Vector3i vec1, int x, int y, int z)
    {
        return LittleUtils.addVectors(vec1, -x, -y, -z);
    }

    public static BlockPos subtractVectors(Vector3d vec1, int x, int y, int z)
    {
        return LittleUtils.addVectors(vec1, -x, -y, -z);
    }

    public static BlockPos addVectors(Vector3i vec1, int x, int y, int z)
    {
        return x == 0 && y == 0 && z == 0 ? new BlockPos(vec1.x, vec1.y, vec1.z) : new BlockPos(vec1.x() + x, vec1.y() + y, vec1.z() + z);
    }

    public static BlockPos addVectors(Vector3d vec1, int x, int y, int z)
    {
        // @TODO rounding here might be wrong
        return x == 0 && y == 0 && z == 0 ? new BlockPos((int) Math.round(vec1.x()), (int) Math.round(vec1.y()), (int) Math.round(vec1.z())) :
            new BlockPos((int) Math.round(vec1.x() + x), (int) Math.round(vec1.y() + y), (int) Math.round(vec1.z() + z));
    }

    public static float round(float valueToRound, int numberOfDecimalPlaces) {
        float multipicationFactor = (float) Math.pow(10, numberOfDecimalPlaces);
        float interestedInZeroDPs = valueToRound * multipicationFactor;
        return Math.round(interestedInZeroDPs) / multipicationFactor;
    }

    public static double round(double value) {
        return round(value, scale);
    }

    public static float round(float value) {
        return round(value, scale);
    }

    public static void scaleInPlace(Vector3f vector, float s) {
        vector.x *= s;
        vector.y *= s;
        vector.z *= s;
    }

    public static void scaleInPlace(Vector3f vector, double s) {
        vector.x *= s;
        vector.y *= s;
        vector.z *= s;
    }

    public static void scaleInPlace(Vector3d vector, double s) {
        vector.x *= s;
        vector.y *= s;
        vector.z *= s;
    }

    public static Vector3d scaleNotInPlace(Vector3d vector, double s) {
        return new Vector3d(
        vector.x * s,
        vector.y * s,
        vector.z * s
        );
    }
    }


    public static double squareDistanceTo(Vector3d from, Vector3d to)
    {
        double d0 = to.x - from.x;
        double d1 = to.y - from.y;
        double d2 = to.z - from.z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public static boolean epsilonEquals(Vector3f vector, Vector3f secondVector, double epsilonAmount) {
        double d = vector.x - secondVector.x;
        if ((d < 0.0D ? -d : d) > epsilonAmount) {
            return false;
        }
        d = vector.y - secondVector.y;
        if ((d < 0.0D ? -d : d) > epsilonAmount) {
            return false;
        }
        d = vector.z - secondVector.z;
        if ((d < 0.0D ? -d : d) > epsilonAmount) {
            return false;
        }
        return true;
    }
}
