package com.creativemd.littletiles.common.utils.math;

import com.creativemd.littletiles.common.tile.math.vec.VectorFan;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Ray2dProxy {

    public double originOne;
    public double originTwo;

    public double directionOne;
    public double directionTwo;

    public Axis one;
    public Axis two;
//
//    public Ray2dProxy(Axis one, Axis two, Vector3d origin, double directionOne, double directionTwo) {
//        this.one = one;
//        this.two = two;
//        this.originOne = VectorUtilsProxy.get(one, origin);
//        this.originTwo = VectorUtilsProxy.get(two, origin);
//        this.directionOne = directionOne;
//        this.directionTwo = directionTwo;
//    }
//
//    public Ray2dProxy(Axis one, Axis two, IVecInt origin, double directionOne, double directionTwo) {
//        this.one = one;
//        this.two = two;
//        this.originOne = origin.get(one);
//        this.originTwo = origin.get(two);
//        this.directionOne = directionOne;
//        this.directionTwo = directionTwo;
//    }
//
    public Ray2dProxy(Axis one, Axis two, double startOne, double startTwo, double endOne, double endTwo) {
        set(one, two, startOne, startTwo, endOne, endTwo);
    }

    public double getOrigin(Axis axis) {
        if (one == axis)
            return originOne;
        return originTwo;
    }

    public double getDirection(Axis axis) {
        if (one == axis)
            return directionOne;
        return directionTwo;
    }

    public Axis getOther(Axis axis) {
        if (one == axis)
            return two;
        return one;
    }

    public void set(EnumFacingProxy.Axis one, EnumFacingProxy.Axis two, double startOne, double startTwo, double endOne, double endTwo) {
        this.one = one;
        this.two = two;

        this.originOne = startOne;
        this.originTwo = startTwo;
        this.directionOne = endOne - startOne;
        this.directionTwo = endTwo - startTwo;
    }
//
//    public void set(Axis one, Axis two, Vector3f first, Vector3f second) {
//        this.one = one;
//        this.two = two;
//
//        this.originOne = VectorUtilsProxy.get(one, first);
//        this.originTwo = VectorUtilsProxy.get(two, first);
//        this.directionOne = VectorUtilsProxy.get(one, second) - originOne;
//        this.directionTwo = VectorUtilsProxy.get(two, second) - originTwo;
//    }

    public double getT(Axis axis, double value) {
        return (value - getOrigin(axis)) / getDirection(axis);
    }

    public double get(Axis axis, double value) {
        Axis other = getOther(axis);
        return getOrigin(other) + getDirection(other) * (value - getOrigin(axis)) / getDirection(axis);
    }

    public Vector2d get(double t) {
        return new Vector2d(originOne + directionOne * t, originTwo + directionTwo * t);
    }

    public Vector2f getFloat(double t) {
        return new Vector2f((float) (originOne + directionOne * t), (float) (originTwo + directionTwo * t));
    }

    public Double getWithLimits(Axis axis, double value) {
        return getWithLimits(axis, value, 0, 1);
    }

    public Double getWithLimits(Axis axis, double value, double min, double max) {
        Axis other = getOther(axis);
        double position = (value - getOrigin(axis)) / getDirection(axis);
        if (position < min || position > max)
            return null;
        return getOrigin(other) + getDirection(other) * position;
    }

    public boolean isCoordinateOnLine(int one, int two) {
        return get(this.one, one) == two;
    }

    public boolean isCoordinateOnLine(double one, double two) {
        if (directionOne == 0)
            return VectorUtilsProxy.equals(originOne, one);
        else if (directionTwo == 0)
            return VectorUtilsProxy.equals(originTwo, two);
        return VectorUtilsProxy.equals(get(this.one, one), two);
    }

    public boolean isCoordinateToTheRight(int one, int two) {
        double tempOne = one - originOne;
        double tempTwo = two - originTwo;
        return directionOne * tempTwo - directionTwo * tempOne < 0;
    }

    public Boolean isCoordinateToTheRight(double one, double two) {
        double tempOne = one - originOne;
        double tempTwo = two - originTwo;
        double result = directionOne * tempTwo - directionTwo * tempOne;
        if (result > - VectorFan.EPSILON && result < VectorFan.EPSILON)
            return null;
        return result < 0;
    }

    public Vector3f intersect(Vector3f start, Vector3f end, float thirdValue) {
        float lineOriginOne = VectorUtilsProxy.get(one, start);
        float lineOriginTwo = VectorUtilsProxy.get(two, start);
        float lineDirectionOne = VectorUtilsProxy.get(one, end) - VectorUtilsProxy.get(one, start);
        float lineDirectionTwo = VectorUtilsProxy.get(two, end) - VectorUtilsProxy.get(two, start);

        if (VectorUtilsProxy.isZero(directionOne * lineDirectionTwo - directionTwo * lineDirectionOne))
            return null;

        Vector3f vec = new Vector3f(thirdValue, thirdValue, thirdValue);
        double t = ((lineOriginTwo - originTwo) * lineDirectionOne + originOne * lineDirectionTwo - lineOriginOne * lineDirectionTwo) / (lineDirectionOne * directionTwo - directionOne * lineDirectionTwo);
        VectorUtilsProxy.set(vec, (float) (originOne + t * directionOne), one);
        VectorUtilsProxy.set(vec, (float) (originTwo + t * directionTwo), two);
        return vec;
    }

    public double intersectWhen(Ray2dProxy line) throws VectorFan.ParallelException {
        if (VectorUtilsProxy.isZero(directionOne * line.directionTwo - directionTwo * line.directionOne))
            if (isCoordinateOnLine(line.originOne, line.originTwo))
                throw new VectorFan.ParallelException();
            else
                return -1;
        return ((line.originTwo - originTwo) * line.directionOne + originOne * line.directionTwo - line.originOne * line.directionTwo) / (line.directionOne * directionTwo - directionOne * line.directionTwo);
    }
//
//    public Vector3d intersect(Ray2dProxy line, int thirdValue) {
//        if (VectorUtilsProxy.isZero(directionOne * line.directionTwo - directionTwo * line.directionOne))
//            return null;
//
//        Vector3d vec = new Vector3d(thirdValue, thirdValue, thirdValue);
//        double t = ((line.originTwo - originTwo) * line.directionOne + originOne * line.directionTwo - line.originOne * line.directionTwo) / (line.directionOne * directionTwo - directionOne * line.directionTwo);
//        VectorUtilsProxy.set(vec, originOne + t * directionOne, one);
//        VectorUtilsProxy.set(vec, originTwo + t * directionTwo, two);
//        return vec;
//    }

//    @Override
//    public String toString() {
//        return one + "," + two + ",[" + originOne + "," + originTwo + "],[" + directionOne + "," + directionTwo + "]";
//    }
}
