package com.creativemd.littletiles.common.parent;

import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesProxy;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.world.World;
public interface IParentTileList extends Iterable<LittleTile> {

    public LittleTile first();

    public LittleTile last();

    public int size();

    public int totalSize();

    public boolean isStructure();

//    public default boolean isStructureChildSafe(LittleStructure structure) {
//        try {
//            return isStructureChild(structure);
//        } catch (CorruptedConnectionException | NotYetConnectedException e) {
//            return false;
//        }
//    }

//    public boolean isStructureChild(LittleStructure structure) throws CorruptedConnectionException, NotYetConnectedException;

    public boolean isMain();

//    public LittleStructure getStructure() throws CorruptedConnectionException, NotYetConnectedException;

    public int getAttribute();

    public void setAttribute(int attribute);

    public boolean isClient();

    public TileEntityLittleTilesProxy getTe();

    public default World getWorld() {
        TileEntityLittleTilesProxy te = getTe();
        if (te.hasWorldObj())
            return te.getWorldObj();
        throw new RuntimeException("no world object");
//        return te.getTempWorld();
    }

    public default BlockPos getPos() {
        return getTe().getPos();
    }

    public default LittleGridContext getContext() {
        return getTe().getContext();
    }

//    @SideOnly(Side.CLIENT)
//    public default LittleRenderBox getTileRenderingCube(LittleTile tile, LittleGridContext context, BlockRenderLayer layer) {
//        LittleRenderBox box = tile.getRenderingCube(context, layer);
//        if (box != null && isStructure() && LittleStructureAttribute.emissive(getAttribute()))
//            box.emissive = true;
//        return box;
//    }

}
