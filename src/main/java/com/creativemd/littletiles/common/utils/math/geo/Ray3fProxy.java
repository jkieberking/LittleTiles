package com.creativemd.littletiles.common.utils.math.geo;

import com.creativemd.littletiles.common.utils.math.VectorUtilsProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import org.joml.Vector3f;

public class Ray3fProxy {

    public final Vector3f origin;
    public final Vector3f direction;

    public Ray3fProxy(Vector3f start, Vector3f end) {
        this.origin = start;
        this.direction = new Vector3f(end);
        this.direction.sub(origin);
        this.direction.normalize();
    }

    public Ray3fProxy(Vector3f origin, EnumFacingProxy facing) {
        this.origin = origin;
        this.direction = new Vector3f();
        VectorUtilsProxy.set(direction, facing.getAxisDirection().getOffset(), facing.getAxis());
    }

    public void set(float x, float y, float z, float x2, float y2, float z2) {
        origin.set(x, y, z);
        direction.set(x2, y2, z2);
        direction.sub(origin);
    }

    public double getT(EnumFacingProxy.Axis axis, double value) {
        return (value - VectorUtilsProxy.get(axis, origin.x, origin.y, origin.z)) / VectorUtilsProxy.get(axis, direction.x, direction.y, direction.z);
    }

    public Vector3f get(float t) {
        return new Vector3f(origin.x + direction.x * t, origin.y + direction.y * t, origin.z + direction.z * t);
    }

}
