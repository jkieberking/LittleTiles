package com.creativemd.littletiles.common.utils.place.fixed;

import com.creativemd.creativecore.common.utils.RotationUtils;
import com.creativemd.creativecore.common.utils.RotationUtils.Axis;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.world.World;

public class InsideFixedHandler extends SecondModeHandler {

    protected void updateBox(RotationUtils.Axis axis, LittleGridContext context, LittleBox box) {
        int offset = 0;
        if (box.getSize(axis) <= context.size) {
            if (box.getMin(axis) < 0)
                offset = -box.getMin(axis);

            else if (box.getMax(axis) > context.maxPos)
                offset = context.maxPos - box.getMax(axis);
            LittleVec vec = new LittleVec(0, 0, 0);
            vec.set(axis, offset);
            box.add(vec);
        }
    }

    @Override
    public LittleBox getBox(World world, BlockPos pos, LittleGridContext context, LittleBox suggested) {
        updateBox(Axis.Xaxis, context, suggested);
        updateBox(Axis.Yaxis, context, suggested);
        updateBox(Axis.Zaxis, context, suggested);
        return suggested;
    }

}
