package com.creativemd.littletiles.common.utils.interpolation;

import com.creativemd.littletiles.common.utils.math.vec.VecProxy;

public class LinearInterpolationProxy<T extends VecProxy> extends InterpolationProxy<T> {

    public LinearInterpolationProxy(double[] times, T[] points) {
        super(times, points);
    }

    public LinearInterpolationProxy(T... points) {
        super(points);
    }

    @Override
    public double valueAt(double mu, int pointIndex, int pointIndexNext, int dim) {
        return (getValue(pointIndexNext, dim) - getValue(pointIndex, dim)) * mu + getValue(pointIndex, dim);
    }

}
