package com.creativemd.littletiles.common.tile.math.box;

import com.creativemd.littletiles.common.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.math.LittleUtils;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.grid.IGridBased;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import org.joml.Vector3i;

public abstract class LittleBoxes implements IGridBased {

    public BlockPos pos;
    public LittleGridContext context;

    public LittleBoxes(BlockPos pos, LittleGridContext context) {
        this.pos = pos;
        this.context = context;
    }

    public abstract void add(LittleBox box);

    public LittleBox addBox(IParentTileList parent, LittleTile tile) {
        return addBox(parent.getContext(), parent.getPos(), tile.getBox().copy());
    }

    public LittleBox addBox(LittleGridContext context, BlockPos pos, LittleBox box) {
        if (this.context != context) {
            if (this.context.size > context.size) {
                box.convertTo(context, this.context);
                context = this.context;
            } else
                convertTo(context);
        }

        box.add(new LittleVec(context, LittleUtils.subtractVectors(pos, new Vector3i(this.pos.x(), this.pos.y(), this.pos.z()))));
        add(box);
        return box;
    }

    @Override
    public LittleGridContext getContext() {
        return context;
    }

    @Override
    public abstract void convertTo(LittleGridContext to);
//
//    @Override
//    public abstract void convertToSmallest();

    public abstract void clear();

    public abstract boolean isEmpty();

    public abstract int size();

    public abstract LittleBox getSurroundingBox();

//    public abstract HashMapList<BlockPos, LittleBox> generateBlockWise();

    public abstract Iterable<LittleBox> all();

//    public abstract void flip(Axis axis, LittleAbsoluteBox absoluteBox);

    public abstract LittleBoxes copy();

    public abstract void combineBoxesBlocks();

}
