package com.creativemd.littletiles.common.tile.place;

import com.creativemd.littletiles.client.render.tile.LittleRenderBox;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.parent.IParentTileList;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxReturnedVolume;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.type.HashMapListProxy;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.place.Placement;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public class PlacePreview {

    public LittleBox box;
    public LittlePreview preview;

    public PlacePreview(LittleBox box, LittlePreview preview) {
        this.box = box;
        this.preview = preview;
    }
//
//    /** NEEDS TO BE OVERRIDEN! ALWAYS! **/
    public PlacePreview copy() {
        return new PlacePreview(box.copy(), preview.copy());
    }

    /** If false it will be placed after all regular tiles have been placed **/
    public boolean needsCollisionTest() {
        return true;
    }
//
    public List<LittleRenderBox> getPreviews(LittleGridContext context) {
        ArrayList<LittleRenderBox> previews = new ArrayList<>();
        previews.add(box.getRenderingCube(context, null, 0));
        return previews;
    }

    public List<LittleTile> placeTile(Placement placement, Placement.PlacementBlock block, IParentTileList parent, /*LittleStructure structure,*/ boolean requiresCollisionTest) throws LittleActionException {
        LittleTile littleTile = preview.getLittleTile();

        if (littleTile == null)
            return Collections.EMPTY_LIST;

        littleTile.setBox(box.copy());
        return placement.mode.placeTile(placement, block, parent/*, structure*/, littleTile, requiresCollisionTest);
    }
//
    public PlacePreview copyWithBox(LittleBox box) {
        PlacePreview tile = this.copy();
        tile.box = box;
        return tile;
    }

    public boolean split(LittleGridContext context, HashMapListProxy<BlockPos, PlacePreview> tiles, BlockPos pos, LittleBoxReturnedVolume volume) {
        if (!requiresSplit()) {
            tiles.add(pos, this);
            return true;
        }

        HashMapListProxy<BlockPos, LittleBox> boxes = new HashMapListProxy<>();
        this.box.split(context, pos, boxes, volume);
        for (Entry<BlockPos, ArrayList<LittleBox>> entry : boxes.entrySet()) {
            for (LittleBox box : entry.getValue()) {
                tiles.add(entry.getKey(), this.copyWithBox(box));
            }
        }

        return true;
    }

    public boolean requiresSplit() {
        return true;
    }

    public void add(LittleVec vec) {
        box.add(vec);
    }

    public void convertTo(LittleGridContext context, LittleGridContext to) {
        box.convertTo(context, to);
    }
//
    public int getSmallestContext(LittleGridContext context) {
        return box.getSmallestContext(context);
    }

}
