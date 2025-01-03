package com.creativemd.littletiles.common.utils.shape.type;

import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.utils.shape.LittleShapeSelectable;
import com.creativemd.littletiles.common.utils.shape.ShapeSelection.ShapeSelectPos;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

public class LittleShapeTile extends LittleShapeSelectable {

    public LittleShapeTile() {
        super(1);
    }

    @Override
    protected void addBoxes(LittleBoxes boxes, com.creativemd.littletiles.common.utils.shape.ShapeSelection selection, boolean lowResolution) {
        for (ShapeSelectPos pos : selection) {
//            if (pos.result.isComplete())
//                addBox(boxes, selection.inside, selection.getContext(), pos.result.parent, pos.result.tile.getBox(), pos.pos.facing);
//            else
                addBox(boxes, selection.inside, selection.getContext(), new BlockPos(pos.objectPosition.blockX, pos.objectPosition.blockY, pos.objectPosition.blockZ), pos.pos.facing);
        }
    }

//    @Override
//    public void addExtraInformation(NBTTagCompound nbt, List<String> list) {}

//    @Override
//    @SideOnly(Side.CLIENT)
//    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
//        return Collections.EMPTY_LIST;
//    }

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
//
//    }

}
