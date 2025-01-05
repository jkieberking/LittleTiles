package com.creativemd.littletiles.common.utils.math.vec;

import net.minecraft.util.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector3i;

import javax.annotation.Nullable;
public class Vec3dProxy
{
    public static final Vec3dProxy ZERO = new Vec3dProxy(0.0D, 0.0D, 0.0D);
    public final double x;
    public final double y;
    public final double z;

    public Vec3dProxy(double xIn, double yIn, double zIn)
    {
        if (xIn == -0.0D)
        {
            xIn = 0.0D;
        }

        if (yIn == -0.0D)
        {
            yIn = 0.0D;
        }

        if (zIn == -0.0D)
        {
            zIn = 0.0D;
        }

        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }

    public Vec3dProxy(Vector3i vector)
    {
        this((double)vector.x(), (double)vector.y(), (double)vector.z());
    }

    public Vec3dProxy subtractReverse(Vec3dProxy vec)
    {
        return new Vec3dProxy(vec.x - this.x, vec.y - this.y, vec.z - this.z);
    }

    public Vec3dProxy normalize()
    {
        double d0 = (double) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return d0 < 1.0E-4D ? ZERO : new Vec3dProxy(this.x / d0, this.y / d0, this.z / d0);
    }

    public double dotProduct(Vec3dProxy vec)
    {
        return this.x * vec.x + this.y * vec.y + this.z * vec.z;
    }

    public Vec3dProxy crossProduct(Vec3dProxy vec)
    {
        return new Vec3dProxy(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x);
    }

    public Vec3dProxy subtract(Vec3dProxy vec)
    {
        return this.subtract(vec.x, vec.y, vec.z);
    }

    public Vec3dProxy subtract(double x, double y, double z)
    {
        return this.addVector(-x, -y, -z);
    }

    public Vec3dProxy add(Vec3dProxy vec)
    {
        return this.addVector(vec.x, vec.y, vec.z);
    }

    public Vec3dProxy addVector(double x, double y, double z)
    {
        return new Vec3dProxy(this.x + x, this.y + y, this.z + z);
    }

    public double distanceTo(Vec3dProxy vec)
    {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;
        return (double)Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double squareDistanceTo(Vec3dProxy vec)
    {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double squareDistanceTo(double xIn, double yIn, double zIn)
    {
        double d0 = xIn - this.x;
        double d1 = yIn - this.y;
        double d2 = zIn - this.z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public Vec3dProxy scale(double factor)
    {
        return new Vec3dProxy(this.x * factor, this.y * factor, this.z * factor);
    }

    public double lengthVector()
    {
        return (double)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Nullable
    public Vec3dProxy getIntermediateWithXValue(Vec3dProxy vec, double x)
    {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;

        if (d0 * d0 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (x - this.x) / d0;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vec3dProxy(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
        }
    }

    @Nullable
    public Vec3dProxy getIntermediateWithYValue(Vec3dProxy vec, double y)
    {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;

        if (d1 * d1 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (y - this.y) / d1;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vec3dProxy(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
        }
    }

    @Nullable
    public Vec3dProxy getIntermediateWithZValue(Vec3dProxy vec, double z)
    {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;

        if (d2 * d2 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (z - this.z) / d2;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vec3dProxy(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
        }
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof Vec3dProxy))
        {
            return false;
        }
        else
        {
            Vec3dProxy vec3DProxy = (Vec3dProxy)p_equals_1_;

            if (Double.compare(vec3DProxy.x, this.x) != 0)
            {
                return false;
            }
            else if (Double.compare(vec3DProxy.y, this.y) != 0)
            {
                return false;
            }
            else
            {
                return Double.compare(vec3DProxy.z, this.z) == 0;
            }
        }
    }

    public int hashCode()
    {
        long j = Double.doubleToLongBits(this.x);
        int i = (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(this.y);
        i = 31 * i + (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(this.z);
        i = 31 * i + (int)(j ^ j >>> 32);
        return i;
    }

    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public Vec3dProxy rotatePitch(float pitch)
    {
        float f = MathHelper.cos(pitch);
        float f1 = MathHelper.sin(pitch);
        double d0 = this.x;
        double d1 = this.y * (double)f + this.z * (double)f1;
        double d2 = this.z * (double)f - this.y * (double)f1;
        return new Vec3dProxy(d0, d1, d2);
    }

    public Vec3dProxy rotateYaw(float yaw)
    {
        float f = MathHelper.cos(yaw);
        float f1 = MathHelper.sin(yaw);
        double d0 = this.x * (double)f + this.z * (double)f1;
        double d1 = this.y;
        double d2 = this.z * (double)f - this.x * (double)f1;
        return new Vec3dProxy(d0, d1, d2);
    }

    public static Vec3dProxy fromPitchYawVector(Vector2f p_189984_0_)
    {
        return fromPitchYaw(p_189984_0_.x, p_189984_0_.y);
    }

    public static Vec3dProxy fromPitchYaw(float p_189986_0_, float p_189986_1_)
    {
        float f = MathHelper.cos(-p_189986_1_ * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-p_189986_1_ * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-p_189986_0_ * 0.017453292F);
        float f3 = MathHelper.sin(-p_189986_0_ * 0.017453292F);
        return new Vec3dProxy((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
}
