package com.creativemd.littletiles.common.utils.shape;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.math.box.LittleTransformableBox;
import com.creativemd.littletiles.common.tile.math.box.slice.LittleSlice;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.nbt.NBTTagCompound;

public class LittleShapeSlice extends LittleShape {

    public LittleShapeSlice() {
        super(2);
    }

    @Override
    protected void addBoxes(LittleBoxes boxes, ShapeSelection selection, boolean lowResolution) {
        LittleBox box = selection.getOverallBox();
        boxes.add(new LittleTransformableBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, LittleSlice.values()[selection.getNBT().getInteger("slice")]));
    }

//    @Override
//    public void addExtraInformation(NBTTagCompound nbt, List<String> list) {
//
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
//        return new ArrayList<>();
//    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {}
//
//    @Override
//    public void rotate(NBTTagCompound nbt, Rotation rotation) {
//        LittleSlice slice = LittleSlice.values()[nbt.getInteger("slice")];
//        slice = slice.rotate(rotation);
//        nbt.setInteger("slice", slice.ordinal());
//    }
//
//    @Override
//    public void flip(NBTTagCompound nbt, Axis axis) {
//        LittleSlice slice = LittleSlice.values()[nbt.getInteger("slice")];
//        slice = slice.flip(axis);
//        nbt.setInteger("slice", slice.ordinal());
//    }

}
