package com.creativemd.littletiles.common.world;

import java.util.Collection;
import java.util.HashSet;

import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

// this is from creativecore
public class NeighborUpdateCollectorProxy {

    protected final World world;
    private final HashSet<BlockPos> blocksToUpdate;

    public NeighborUpdateCollectorProxy(World world, Collection<BlockPos> positions) {
        this.world = world;
        blocksToUpdate = new HashSet<>(positions);
    }

    public NeighborUpdateCollectorProxy(World world) {
        this.world = world;
        blocksToUpdate = new HashSet<>();
    }

    public void add(BlockPos pos) {
        blocksToUpdate.add(pos);
    }

    public void add(TileEntity te) {
        blocksToUpdate.add(new BlockPos(te.xCoord, te.yCoord, te.zCoord));
    }

    public void add(Collection<BlockPos> positions) {
        blocksToUpdate.addAll(positions);
    }

    protected void processPosition(BlockPos pos, HashSet<BlockPos> notifiedBlocks) {
        Block origin = world.getBlock(pos.x, pos.y, pos.z);

        for (int i = 0; i < 6; i++) {
            BlockPos neighbour = (BlockPos) pos.offset(EnumFacingProxy.toForgeDirection(EnumFacingProxy.fromEnumFacing(EnumFacing.values()[i])));
            if (!notifiedBlocks.contains(neighbour) && !blocksToUpdate.contains(neighbour)) {
//                world.getBlock(neighbour.x, neighbour.y, neighbour.z).neighborChanged(world, neighbour, origin.getBlock(), pos);
                notifiedBlocks.add(neighbour);
            }
        }
    }

    public void process() {
        HashSet<BlockPos> notifiedBlocks = new HashSet<>();
        for (BlockPos pos : blocksToUpdate)
            processPosition(pos, notifiedBlocks);
        blocksToUpdate.clear();
    }

}
