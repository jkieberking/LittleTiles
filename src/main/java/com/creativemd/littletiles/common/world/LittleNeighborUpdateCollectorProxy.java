package com.creativemd.littletiles.common.world;

import java.util.Collection;
import java.util.HashSet;

import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LittleNeighborUpdateCollectorProxy extends NeighborUpdateCollectorProxy {

    public static final LittleNeighborUpdateCollectorProxy EMPTY = new LittleNeighborUpdateCollectorProxy(null) {

        @Override
        public void add(BlockPos pos) {}

        @Override
        public void add(TileEntity te) {}

        @Override
        public void add(Collection<BlockPos> positions) {}

        @Override
        protected void processPosition(BlockPos pos, HashSet<BlockPos> notifiedBlocks) {}

        @Override
        public void process() {}
    };

    public LittleNeighborUpdateCollectorProxy(World world, Collection<BlockPos> positions) {
        super(world, positions);
    }

    public LittleNeighborUpdateCollectorProxy(World world) {
        super(world);
    }

    @Override
    protected void processPosition(BlockPos pos, HashSet<BlockPos> notifiedBlocks) {
        TileEntity te = world.getTileEntity(pos.x, pos.y, pos.z);
        if (te instanceof TileEntityLittleTilesProxy)
            ((TileEntityLittleTilesProxy) te).updateTiles();
        super.processPosition(pos, notifiedBlocks);
    }

}
