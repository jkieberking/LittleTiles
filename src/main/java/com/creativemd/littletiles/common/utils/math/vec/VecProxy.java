package com.creativemd.littletiles.common.utils.math.vec;

import java.lang.reflect.InvocationTargetException;

public abstract class VecProxy<T extends VecProxy> {

    public VecProxy() {

    }

    public VecProxy(VecProxy vecProxy) {
        for (int i = 0; i < vecProxy.getDimensionCount(); i++) {
            this.setValueByDim(i, vecProxy.getValueByDim(i));
        }
    }

    public abstract void setValueByDim(int dim, double value);

    public abstract double getValueByDim(int dim);

    public abstract int getDimensionCount();

    public T copy() {
        return (T) copyVec(this);
    }

    public T add(VecProxy vecProxy) {
        T newVec = copy();
        for (int i = 0; i < vecProxy.getDimensionCount(); i++) {
            newVec.setValueByDim(i, newVec.getValueByDim(i) + vecProxy.getValueByDim(i));
        }
        return newVec;
    }

    public T sub(VecProxy vecProxy) {
        T newVec = copy();
        for (int i = 0; i < vecProxy.getDimensionCount(); i++) {
            newVec.setValueByDim(i, newVec.getValueByDim(i) - vecProxy.getValueByDim(i));
        }
        return newVec;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < getDimensionCount(); i++) {
            if (i > 0)
                builder.append(",");
            builder.append(getValueByDim(i));
        }
        builder.append("]");
        return builder.toString();
    }

    public double distance(VecProxy vecProxy) {
        if (vecProxy.getDimensionCount() != this.getDimensionCount())
            return 0;
        double value = 0;
        for (int i = 0; i < getDimensionCount(); i++)
            value += Math.pow(getValueByDim(i) - vecProxy.getValueByDim(i), 2);
        return Math.sqrt(value);
    }

    public static VecProxy copyVec(VecProxy vecProxy) {
        switch (vecProxy.getDimensionCount()) {
        case 1:
            return new Vec1Proxy(vecProxy);
        case 2:
            return new Vec2Proxy(vecProxy);
        case 3:
            return new Vec3Proxy(vecProxy);
        default:
            return null;
        }
    }

    public static VecProxy createEmptyVec(Class className) {
        try {
            return (VecProxy) className.getConstructor(VecProxy.class).newInstance(new Vec1Proxy(0));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
