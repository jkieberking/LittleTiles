package com.creativemd.littletiles.common.utils.shape;

import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

public class LittleShapeType extends LittleShapeSelectable {

    public LittleShapeType() {
        super(1);
    }

    @Override
    protected void addBoxes(LittleBoxes boxes, ShapeSelection selection, boolean lowResolution) {
        for (ShapeSelection.ShapeSelectPos pos : selection) {
//            if (pos.result.isComplete()) {
////                if (pos.result.parent.isStructure())
////                    continue;
//
//                LittleTile tile = pos.result.tile;
////                for (LittleTile toDestroy : pos.result.te.noneStructureTiles())
////                    if (tile.canBeCombined(toDestroy) && toDestroy.canBeCombined(tile))
////                        addBox(boxes, selection.inside, selection.getContext(), pos.result.te.noneStructureTiles(), toDestroy.getBox(), pos.pos.facing);
//
//            } else
            addBox(boxes, selection.inside, selection.getContext(), new BlockPos(pos.objectPosition.blockX, pos.objectPosition.blockY, pos.objectPosition.blockZ), pos.pos.facing);
        }
    }
//
//    @Override
//    public void addExtraInformation(NBTTagCompound nbt, List<String> list) {}
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
//        return Collections.EMPTY_LIST;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {}

//    @Override
//    public void rotate(NBTTagCompound nbt, Rotation rotation) {
//
//    }
//
//    @Override
//    public void flip(NBTTagCompound nbt, Axis axis) {
}
