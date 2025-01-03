package com.creativemd.littletiles.common.utils.interpolation;

import com.creativemd.littletiles.common.utils.math.vec.VecProxy;

public class HermiteInterpolationProxy<T extends VecProxy> extends CubicInterpolationProxy<T> {

    public Tension tension;
    public double bias;

    public HermiteInterpolationProxy(double[] times, T[] points, double bias, Tension tension) {
        super(times, points);
        this.bias = bias;
        this.tension = tension;
    }

    public HermiteInterpolationProxy(double[] times, T[] points, Tension tension) {
        this(times, points, 0, tension);
    }

    public HermiteInterpolationProxy(double[] times, T[] points) {
        this(times, points, Tension.Normal);
    }

    public HermiteInterpolationProxy(double bias, Tension tension, T... points) {
        super(points);
        this.bias = bias;
        this.tension = tension;
    }

    public HermiteInterpolationProxy(Tension tension, T... points) {
        this(0, tension, points);
    }

    public HermiteInterpolationProxy(T... points) {
        this(Tension.Normal, points);
    }

    @Override
    public double valueAt(double mu, int pointIndex, int pointIndexNext, int dim) {
        double m0, m1, mu2, mu3;
        double a0, a1, a2, a3;

        double v0 = getValue(pointIndex - 1, dim);
        double v1 = getValue(pointIndex, dim);
        double v2 = getValue(pointIndexNext, dim);
        double v3 = getValue(pointIndexNext + 1, dim);

        mu2 = mu * mu;
        mu3 = mu2 * mu;
        m0 = (v1 - v0) * (1 + bias) * (1 - tension.value) / 2;
        m0 += (v2 - v1) * (1 - bias) * (1 - tension.value) / 2;
        m1 = (v2 - v1) * (1 + bias) * (1 - tension.value) / 2;
        m1 += (v3 - v2) * (1 - bias) * (1 - tension.value) / 2;
        a0 = 2 * mu3 - 3 * mu2 + 1;
        a1 = mu3 - 2 * mu2 + mu;
        a2 = mu3 - mu2;
        a3 = -2 * mu3 + 3 * mu2;

        return (a0 * v1 + a1 * m0 + a2 * m1 + a3 * v2);
    }

    public static enum Tension {
        High(1),
        Normal(0),
        Low(-1);

        public final int value;

        Tension(int value) {
            this.value = value;
        }
    }

}
