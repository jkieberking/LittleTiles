package com.creativemd.littletiles.client.render.world;

import javax.vecmath.Vector3d;

import com.creativemd.littletiles.common.tile.math.vec.IVecOriginProxy;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IOrientatedWorld {

    public boolean hasParent();

    public World getParent();

    public World getRealWorld();

    public IVecOriginProxy getOrigin();

    public void setOrigin(Vector3d center);

    public Entity getParentEntity();

    public default Entity getTopEntity() {
        World world = getParent();
        if (world instanceof IOrientatedWorld)
            return ((IOrientatedWorld) world).getTopEntity();
        return getParentEntity();
    }
}
