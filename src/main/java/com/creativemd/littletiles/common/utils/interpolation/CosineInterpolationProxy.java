package com.creativemd.littletiles.common.utils.interpolation;


import com.creativemd.littletiles.common.utils.math.vec.VecProxy;

public class CosineInterpolationProxy<T extends VecProxy> extends InterpolationProxy<VecProxy> {

    public CosineInterpolationProxy(T... points) {
        super(points);
    }

    @Override
    public double valueAt(double mu, int pointIndex, int pointIndexNext, int dim) {
        double mu2 = (1 - Math.cos(mu * Math.PI)) / 2;
        return (getValue(pointIndex, dim) * (1 - mu2) + getValue(pointIndexNext, dim) * mu2);
    }
}
