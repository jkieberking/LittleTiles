package com.creativemd.littletiles.common.utils.place;

//import com.creativemd.creativecore.common.utils.type.Pair;
//import com.creativemd.littletiles.common.action.LittleActionException;
//import com.creativemd.littletiles.common.structure.LittleStructure;
//import com.creativemd.littletiles.common.tile.LittleTile;
//import com.creativemd.littletiles.common.tile.parent.IParentTileList;
//import com.creativemd.littletiles.common.util.place.Placement.PlacementBlock;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.parent.IParentTileList;
import com.creativemd.littletiles.common.type.PairProxy;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlaceModeNormal extends PlacementMode {

    public PlaceModeNormal(String name, PreviewMode mode, boolean placeInside) {
        super(name, mode, placeInside);
    }

//    @Override
//    public PlacementMode place() {
//        if (GuiScreen.isCtrlKeyDown())
//            return PlacementMode.fill;
//        return super.place();
//    }

    @Override
    public List<BlockPos> getCoordsToCheck(Set<BlockPos> splittedTiles, BlockPos pos) {
        List<BlockPos> coords = new ArrayList<>();
        coords.add(pos);
        return coords;
    }

    @Override
    public List<LittleTile> placeTile(Placement placement, Placement.PlacementBlock block, IParentTileList parent/*, LittleStructure structure*/, LittleTile tile, boolean requiresCollisionTest) throws LittleActionException {
        List<LittleTile> tiles = new ArrayList<>();
        PairProxy<IParentTileList, LittleTile> intersecting = null;
        if (!requiresCollisionTest || (intersecting = block.getTe().intersectingTile(tile.getBox())) == null)
            tiles.add(tile);
        else if (this instanceof PlaceModeAll) {
//            if (intersecting.key == parent)
//                System.out.println("Structure is not valid ... some tiles will be left out");
//            else
                throw new LittleActionException("Could not place all tiles");
        } else
            placement.unplaceableTiles.addTile(parent, tile);
        return tiles;
    }
}
