package com.creativemd.littletiles.common.utils.math.vec;

public class Vec3Proxy extends VecProxy {

    public double x;
    public double y;
    public double z;

    public Vec3Proxy(VecProxy vec) {
        super(vec);
    }

    public Vec3Proxy(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getValueByDim(int dim) {
        if (dim == 0)
            return x;
        if (dim == 1)
            return y;
        if (dim == 2)
            return z;
        return 0;
    }

    @Override
    public void setValueByDim(int dim, double value) {
        if (dim == 0)
            this.x = value;
        if (dim == 1)
            this.y = value;
        if (dim == 2)
            this.z = value;
    }

    @Override
    public int getDimensionCount() {
        return 3;
    }
}
