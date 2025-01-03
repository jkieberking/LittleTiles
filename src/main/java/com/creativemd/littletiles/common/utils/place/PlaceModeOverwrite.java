package com.creativemd.littletiles.common.utils.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.creativemd.littletiles.common.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxReturnedVolume;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;


public class PlaceModeOverwrite extends PlacementMode {

    public PlaceModeOverwrite(String name, PreviewMode mode) {
        super(name, mode, false);
    }

    @Override
    public boolean shouldConvertBlock() {
        return true;
    }

    @Override
    public boolean canPlaceStructures() {
        return true;
    }

    @Override
    public boolean checkAll() {
        return false;
    }

    @Override
    public List<BlockPos> getCoordsToCheck(Set<BlockPos> splittedTiles, BlockPos pos) {
        return new ArrayList<>(splittedTiles);
    }

    @Override
    public List<LittleTile> placeTile(Placement placement, Placement.PlacementBlock block, IParentTileList parent/*, LittleStructure structure*/, LittleTile tile, boolean requiresCollisionTest) {
        List<LittleTile> tiles = new ArrayList<>();
        LittleGridContext context = block.getContext();
        LittleBoxReturnedVolume volume = new LittleBoxReturnedVolume();
        if (requiresCollisionTest)
//            for (LittleTile removedTile : LittleActionDestroyBoxes.removeBox(block.getTe(), context, tile.getBox(), false, volume)) {
//                placement.removedTiles.addTile(block.getTe().noneStructureTiles(), removedTile);
//                if (volume.has())
//                    placement.addRemovedIngredient(block, tile, volume);
//                volume.clear();
//            }
        block.getTe().convertTo(context);
        tiles.add(tile);
        return tiles;
    }

    @Override
    public void prepareBlock(Placement placement, Placement.PlacementBlock block, IParentTileList parent, boolean requiresCollisionTest) {
//        block.getTe().updateTilesSecretly((x) -> {
//            TileList parent = x.noneStructureTiles();
//            for (LittleTile toRemove : parent)
//                placement.removedTiles.addTile(parent, toRemove);
//            parent.clear();
//
//        });
    }

}
