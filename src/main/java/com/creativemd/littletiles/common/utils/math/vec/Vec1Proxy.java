package com.creativemd.littletiles.common.utils.math.vec;

public class Vec1Proxy extends VecProxy<Vec1Proxy> {

    public double x;

    public Vec1Proxy(VecProxy vec) {
        super(vec);
    }

    public Vec1Proxy(double x) {
        this.x = x;
    }

    @Override
    public double getValueByDim(int dim) {
        if (dim == 0)
            return x;
        return 0;
    }

    @Override
    public void setValueByDim(int dim, double value) {
        if (dim == 0)
            this.x = value;
    }

    @Override
    public int getDimensionCount() {
        return 1;
    }
}
