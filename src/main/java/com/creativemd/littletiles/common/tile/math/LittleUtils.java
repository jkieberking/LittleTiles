package com.creativemd.littletiles.common.tile.math;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import org.joml.Vector3d;
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

}
