package com.creativemd.littletiles.common.utils.math.geo;


import com.creativemd.littletiles.common.tile.math.LittleUtils;
import com.creativemd.littletiles.common.tile.math.vec.VectorFan;
import com.creativemd.littletiles.common.utils.math.VectorUtilsProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import org.joml.Vector3f;

public class NormalPlaneProxy {

    public final Vector3f normal;
    public final Vector3f origin;

    public NormalPlaneProxy(Vector3f origin, Vector3f normal) {
        this.origin = origin;
        this.normal = new Vector3f(normal);
        this.normal.normalize();
    }

    public NormalPlaneProxy(Axis axis, float value, EnumFacingProxy facing) {
        this.origin = new Vector3f();
        VectorUtilsProxy.set(origin, value, axis);
        this.normal = new Vector3f();
        VectorUtilsProxy.set(normal, facing.getAxisDirection().getOffset(), facing.getAxis());
    }

    public Boolean isInFront(Vector3f vec) {
        return isInFront(vec, 1.0E-7F);
    }

    public Boolean isInFront(Vector3f vec, float epsilon) {
        Vector3f temp = new Vector3f(vec);
        temp.sub(origin);
        float result = normal.dot(temp);
        if (result < 0 ? (result > -epsilon) : (result < epsilon))
            return null;
        return result > 0;
    }

    public boolean isInvalid() {
        return Float.isNaN(normal.x) || Float.isNaN(normal.y) || Float.isNaN(normal.z);
    }

    public boolean cuts(VectorFan strip) {
        boolean front = false;
        boolean back = false;
        for (int i = 0; i < strip.count(); i++) {
            Boolean result = isInFront(strip.get(i));

            if (result == null)
                return true;

            if (result)
                front = true;
            if (!result)
                back = true;

            if (front && back)
                return true;
        }
        return false;
    }

    public Vector3f intersect(Vector3f start, Vector3f end) {
        Vector3f lineOrigin = start;
        Vector3f lineDirection = new Vector3f(end);
        lineDirection.sub(lineOrigin);
        lineDirection.normalize();

        if (normal.dot(lineDirection) == 0)
            return null;

        float t = (normal.dot(origin) - normal.dot(lineOrigin)) / normal.dot(lineDirection);
        Vector3f point = new Vector3f(lineDirection);
        LittleUtils.scaleInPlace(point, t);
        point.add(lineOrigin);
        return point;
    }



    public Vector3f intersect(Ray3fProxy ray) {
        if (normal.dot(ray.direction) == 0)
            return null;

        float t = (normal.dot(origin) - normal.dot(ray.origin)) / normal.dot(ray.direction);
        Vector3f point = new Vector3f(ray.direction);
        point.scale(t);
        point.add(ray.origin);
        return point;
    }

    public Float project(Axis one, Axis two, Axis axis, float valueOne, float valueTwo) {
        Vector3f lineOrigin = new Vector3f();
        VectorUtils.set(lineOrigin, valueOne, one);
        VectorUtils.set(lineOrigin, valueTwo, two);

        Vector3f lineDirection = new Vector3f();
        VectorUtils.set(lineDirection, 1, axis);

        if (normal.dot(lineDirection) == 0)
            return null;

        float t = (normal.dot(origin) - normal.dot(lineOrigin)) / normal.dot(lineDirection);
        return VectorUtils.get(axis, lineOrigin) + VectorUtils.get(axis, lineDirection) * t;
    }

    @Override
    public String toString() {
        return "[o:" + origin + ",n:" + normal + "]";
    }

}
