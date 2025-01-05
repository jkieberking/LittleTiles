package com.creativemd.littletiles.client.render;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.world.World;

public interface IRenderChunkSupplier {

    public Object getRenderChunk(World world, BlockPos pos);

}
