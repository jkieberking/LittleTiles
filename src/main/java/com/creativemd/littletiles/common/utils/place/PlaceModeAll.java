package com.creativemd.littletiles.common.utils.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.client.gui.GuiScreen;

public class PlaceModeAll extends PlaceModeNormal {

    public PlaceModeAll(String name, PreviewMode mode) {
        super(name, mode, false);
    }

    @Override
    public boolean canPlaceStructures() {
        return true;
    }

    @Override
    public PlacementMode place() {
        if (GuiScreen.isCtrlKeyDown())
            return PlacementMode.overwrite;
        return super.place();
    }

    @Override
    public List<BlockPos> getCoordsToCheck(Set<BlockPos> splittedTiles, BlockPos pos) {
        return new ArrayList<>(splittedTiles);
    }
}
