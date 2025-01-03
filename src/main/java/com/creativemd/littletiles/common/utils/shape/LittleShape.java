package com.creativemd.littletiles.common.utils.shape;

import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxesNoOverlap;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxesSimple;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

public abstract class LittleShape {

    public final int pointsBeforePlacing;

    public LittleShape(int pointsBeforePlacing) {
        this.pointsBeforePlacing = pointsBeforePlacing;
    }

    public String getKey() {
        return ShapeRegistry.getShapeName(this);
    }

//    @SideOnly(Side.CLIENT)
//    public String getLocalizedName() {
//        return GuiControl.translateOrDefault("shape." + getKey(), getKey());
//    }`

    public int maxAllowed() {
        return -1;
    }

    protected abstract void addBoxes(LittleBoxes boxes, ShapeSelection selection, boolean lowResolution);

    public LittleBoxes getBoxes(ShapeSelection selection, boolean lowResolution) {
        LittleBoxes boxes = requiresNoOverlap() ? new LittleBoxesNoOverlap(selection.getPos(), selection.getContext()) : new LittleBoxesSimple(selection.getPos(), selection.getContext());
        addBoxes(boxes, selection, lowResolution);
        return boxes;
    }

    public boolean requiresNoOverlap() {
        return false;
    }
//
//    public abstract void addExtraInformation(NBTTagCompound nbt, List<String> list);
//
//    @SideOnly(Side.CLIENT)
//    public abstract List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context);
//
//    @SideOnly(Side.CLIENT)
//    public abstract void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context);
//
//    public abstract void rotate(NBTTagCompound nbt, Rotation rotation);
//
//    public abstract void flip(NBTTagCompound nbt, Axis axis);

}
