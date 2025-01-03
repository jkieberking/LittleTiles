package com.creativemd.littletiles.common.utils.place.fixed;

import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.world.World;

public abstract class SecondModeHandler {

    public LittleBox getBox(World world, BlockPos pos, LittleGridContext context, LittleBox suggested) {
        return suggested;
    }

}
