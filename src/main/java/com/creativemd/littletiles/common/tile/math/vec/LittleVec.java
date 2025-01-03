package com.creativemd.littletiles.common.tile.math.vec;

import com.creativemd.creativecore.common.utils.RotationUtils.Axis;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.math.RotationProxy;
import com.creativemd.littletiles.common.utils.math.VectorUtilsProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.security.InvalidParameterException;

public class LittleVec /* implements IVecInt */ {

    public static final LittleVec ZERO = new LittleVec(0, 0, 0);

    public int x;
    public int y;
    public int z;

    public LittleVec(String name, NBTTagCompound nbt) {
        if (nbt.getTag(name + "x") instanceof NBTTagByte) {
            set(nbt.getByte(name + "x"), nbt.getByte(name + "y"), nbt.getByte(name + "z"));
            writeToNBT(name, nbt);
        } else if (nbt.getTag(name + "x") instanceof NBTTagInt) {
            set(nbt.getInteger(name + "x"), nbt.getInteger(name + "y"), nbt.getInteger(name + "z"));
        }
        else if (nbt.getTag(name) instanceof NBTTagIntArray) {
            int[] array = nbt.getIntArray(name);
            if (array.length == 3)
                set(array[0], array[1], array[2]);
            else
                throw new InvalidParameterException("No valid coords given " + nbt);
        } else if (nbt.getTag(name) instanceof NBTTagString) {
            String[] coords = nbt.getString(name).split("\\.");
            try {
                set(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
            } catch (Exception e) {
                set(0, 0, 0);
            }
        }
    }

    public LittleVec(LittleGridContext context, MovingObjectPosition movingObjectPosition) {
        this(context, movingObjectPosition.hitVec);
    }

    public LittleVec(LittleGridContext context, Vector3d vec, EnumFacing facing) {
        this(context, vec);
        EnumFacingProxy enumFacingProxy = EnumFacingProxy.fromEnumFacing(facing);
        EnumFacingProxy.AxisDirection axisDirection = enumFacingProxy.getAxisDirection();
        EnumFacingProxy.Axis axis = enumFacingProxy.getAxis();
        if (axisDirection == EnumFacingProxy.AxisDirection.POSITIVE && !context.isAtEdge(VectorUtilsProxy.get(axis, vec)))
            set(axis, get(axis) + 1);
    }

    public LittleVec(LittleGridContext context, Vec3 vec) {
        this.x = context.toGrid(vec.xCoord);
        this.y = context.toGrid(vec.yCoord);
        this.z = context.toGrid(vec.zCoord);
    }

    public LittleVec(LittleGridContext context, Vector3d vec) {
        this.x = context.toGrid(vec.x);
        this.y = context.toGrid(vec.y);
        this.z = context.toGrid(vec.z);
    }

    public LittleVec(EnumFacing facing) {
        switch (facing) {
        case EAST:
            set(1, 0, 0);
            break;
        case WEST:
            set(-1, 0, 0);
            break;
        case UP:
            set(0, 1, 0);
            break;
        case DOWN:
            set(0, -1, 0);
            break;
        case SOUTH:
            set(0, 0, 1);
            break;
        case NORTH:
            set(0, 0, -1);
            break;
        default:
            set(0, 0, 0);
            break;
        }
    }

    public LittleVec(int x, int y, int z) {
        set(x, y, z);
    }

    public LittleVec(LittleGridContext context, Vector3i vec) {
        this(context.toGrid(vec.x()), context.toGrid(vec.y()), context.toGrid(vec.z()));
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(LittleGridContext context, Vector3i vec) {
        set(context.toGrid(vec.x()), context.toGrid(vec.y()), context.toGrid(vec.z()));
    }

    public int getSmallestContext(LittleGridContext context) {
        int size = LittleGridContext.minSize;
        size = Math.max(size, context.getMinGrid(x));
        size = Math.max(size, context.getMinGrid(y));
        size = Math.max(size, context.getMinGrid(z));
        return size;
    }

    public void convertTo(LittleGridContext from, LittleGridContext to) {
        if (from.size > to.size) {
            int ratio = from.size / to.size;
            x /= ratio;
            y /= ratio;
            z /= ratio;
        } else {
            int ratio = to.size / from.size;
            x *= ratio;
            y *= ratio;
            z *= ratio;
        }
    }

    public BlockPos getBlockPos(LittleGridContext context) {
        return new BlockPos((int) Math.floor(context.toVanillaGrid(x)), (int) Math.floor(context.toVanillaGrid(y)), (int) Math.floor(context.toVanillaGrid(z)));
    }

    public Vector3d getVec(LittleGridContext context) {
        return new Vector3d(context.toVanillaGrid(x), context.toVanillaGrid(y), context.toVanillaGrid(z));
    }

    public Vector3d getVector(LittleGridContext context) {
        return new Vector3d(context.toVanillaGrid(x), context.toVanillaGrid(y), context.toVanillaGrid(z));
    }

    public double getPosX(LittleGridContext context) {
        return context.toVanillaGrid(x);
    }

    public double getPosY(LittleGridContext context) {
        return context.toVanillaGrid(y);
    }

    public double getPosZ(LittleGridContext context) {
        return context.toVanillaGrid(z);
    }

    public void add(EnumFacing facing) {
        EnumFacingProxy enumFacingProxy = EnumFacingProxy.fromEnumFacing(facing);
        set(enumFacingProxy.getAxis(), get(enumFacingProxy.getAxis()) + enumFacingProxy.getAxisDirection().getOffset());
    }

    public void add(LittleVec vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }

    public void add(BlockPos pos, LittleGridContext context) {
        this.x += context.toGrid(pos.x());
        this.y += context.toGrid(pos.y());
        this.z += context.toGrid(pos.z());
    }

    public void sub(EnumFacingProxy facing) {
        set(facing.getAxis(), get(facing.getAxis()) - facing.getAxisDirection().getOffset());
    }

    public void sub(LittleVec vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
    }

    public void sub(BlockPos pos, LittleGridContext context) {
        this.x -= context.toGrid(pos.getX());
        this.y -= context.toGrid(pos.getY());
        this.z -= context.toGrid(pos.getZ());
    }

    public void flip(Axis axis) {
        set(axis, -get(axis));
    }

    public void rotateVec(RotationProxy rotation) {
        int tempX = x;
        int tempY = y;
        int tempZ = z;
        this.x = rotation.getMatrix().getX(tempX, tempY, tempZ);
        this.y = rotation.getMatrix().getY(tempX, tempY, tempZ);
        this.z = rotation.getMatrix().getZ(tempX, tempY, tempZ);
    }

    public double distanceTo(LittleVec vec) {
        return Math.sqrt(Math.pow(vec.x - this.x, 2) + Math.pow(vec.y - this.y, 2) + Math.pow(vec.z - this.z, 2));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof LittleVec)
            return x == ((LittleVec) object).x && y == ((LittleVec) object).y && z == ((LittleVec) object).z;
        return super.equals(object);
    }

    public LittleVec copy() {
        return new LittleVec(x, y, z);
    }

    public void writeToNBT(String name, NBTTagCompound nbt) {
        nbt.setIntArray(name, new int[] { x, y, z });
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }

    public void invert() {
        set(-x, -y, -z);
    }

    public void scale(int factor) {
        x *= factor;
        y *= factor;
        z *= factor;
    }

//    @Override
    public int getX() {
        return x;
    }

//    @Override
    public int getY() {
        return y;
    }

//    @Override
    public int getZ() {
        return z;
    }

//    @Override
    public void setX(int value) {
        x = value;
    }

//    @Override
    public void setY(int value) {
        y = value;
    }

//    @Override
    public void setZ(int value) {
        z = value;
    }

//    @Override
    public int get(Axis axis) {
        switch (axis) {
            case Xaxis:
                return x;
            case Yaxis:
                return y;
            case Zaxis:
                return z;
        }
        return 0;
    }

    public int get(EnumFacingProxy.Axis axis) {
        switch (axis) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
        }
        return 0;
    }
//
//    @Override
    public void set(Axis axis, int value) {
        switch (axis) {
            case Xaxis:
                x = value;
                break;
            case Yaxis:
                y = value;
                break;
            case Zaxis:
                z = value;
                break;
        }
    }

    public void set(EnumFacingProxy.Axis axis, int value) {
        switch (axis) {
            case X:
                x = value;
                break;
            case Y:
                y = value;
                break;
            case Z:
                z = value;
                break;
        }
    }

    public int getVolume() {
        return x * y * z;
    }

    /** @return the volume in percent to a size of a normal block */
    public double getPercentVolume(LittleGridContext context) {
        return (double) getVolume() / (double) (context.maxTilesPerBlock);
    }

    public LittleVec calculateInvertedCenter() {
        return new LittleVec((int) Math.ceil(this.x / 2D), (int) Math.ceil(this.y / 2D), (int) Math.ceil(this.z / 2D));
    }

    public LittleVec calculateCenter() {
        return new LittleVec((int) Math.floor(this.x / 2D), (int) Math.floor(this.y / 2D), (int) Math.floor(this.z / 2D));
    }

    public LittleVec max(LittleVec size) {
        this.x = Math.max(this.x, size.x);
        this.y = Math.max(this.y, size.y);
        this.z = Math.max(this.z, size.z);
        return this;
    }

    public Axis getLongestAxis() {
        if (Math.abs(x) > Math.abs(y))
            if (Math.abs(x) > Math.abs(z))
                return Axis.Xaxis;
            else
                return Axis.Zaxis;
        else if (Math.abs(z) > Math.abs(y))
            return Axis.Zaxis;
        return Axis.Yaxis;

    }

}
