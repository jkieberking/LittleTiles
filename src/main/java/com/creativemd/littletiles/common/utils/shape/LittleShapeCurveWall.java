package com.creativemd.littletiles.common.utils.shape;

import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.interpolation.CubicInterpolationProxy;
import com.creativemd.littletiles.common.utils.interpolation.HermiteInterpolationProxy;
import com.creativemd.littletiles.common.utils.interpolation.InterpolationProxy;
import com.creativemd.littletiles.common.utils.interpolation.LinearInterpolationProxy;
import com.creativemd.littletiles.common.utils.math.VectorUtilsProxy;
import com.creativemd.littletiles.common.utils.math.vec.Vec2Proxy;
import com.creativemd.littletiles.utils.EnumFacingProxy.Axis;
import com.creativemd.littletiles.utils.RotationUtilsProxy;
import org.joml.Interpolationd;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;


public class LittleShapeCurveWall extends LittleShape {

    private static String[] interpolationTypes = new String[] { "hermite", "cubic", "linear" };

    public LittleShapeCurveWall() {
        super(2);
    }

    @Override
    protected void addBoxes(LittleBoxes boxes, ShapeSelection selection, boolean lowResolution) {
        int direction = selection.getNBT().getInteger("direction");

        LittleBox overallBox = selection.getOverallBox();
        Axis axis = direction == 0 ? Axis.Y : direction == 1 ? Axis.X : Axis.Z;
        Axis one = RotationUtilsProxy.getOne(axis);
        Axis two = RotationUtilsProxy.getTwo(axis);

        List<Vec2Proxy> points = new ArrayList<>();
        double halfPixelSize = selection.getContext().pixelSize * 0.5;
        for (ShapeSelection.ShapeSelectPos pos : selection) {
            LittleGridContext context = pos.getContext();
            points.add(new Vec2Proxy(pos.pos.getVanillaGrid(one) + halfPixelSize, pos.pos.getVanillaGrid(two)));
        }

        int thickness = Math.max(0, selection.getNBT().getInteger("thickness") - 1);

        if (points.size() <= 1) {
            LittleBox box = selection.getOverallBox();
            box.growCentered(thickness);
            boxes.add(box);
            return;
        }

        InterpolationProxy<Vec2Proxy> interpolation;
        switch (selection.getNBT().getInteger("interpolation")) {
        case 0:
            interpolation = new HermiteInterpolationProxy<>(points.toArray(new Vec2Proxy[0]));
            break;
        case 1:
            interpolation = new CubicInterpolationProxy<>(points.toArray(new Vec2Proxy[0]));
            break;
        default:
            interpolation = new LinearInterpolationProxy<>(points.toArray(new Vec2Proxy[0]));
            break;
        }

        Vec2Proxy origin = new Vec2Proxy(VectorUtilsProxy.get(one, boxes.pos.x,  boxes.pos.y,  boxes.pos.z), VectorUtilsProxy.get(two, boxes.pos.x, boxes.pos.y, boxes.pos.z));

        int amount = 0;
        double pointTime = 1D / (points.size() - 1);
        double currentTime = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            Vec2Proxy before = points.get(i);
            Vec2Proxy end = points.get(i + 1);
            Vec2Proxy middle = interpolation.valueAt(pointTime * (i + 0.5));

            double distance = before.distance(middle) + middle.distance(end);
            int stepCount = (int) Math.ceil(distance / boxes.context.pixelSize * 2);
            double stepSize = pointTime / (stepCount - 1);
            for (int j = 0; j < stepCount; j++) {
                Vec2Proxy vec = (Vec2Proxy) interpolation.valueAt(pointTime * i + stepSize * j).sub(origin);
                LittleVec point = new LittleVec(0, 0, 0);
                point.set(one, boxes.context.toGrid(vec.x));
                point.set(two, boxes.context.toGrid(vec.y));
                LittleBox box = new LittleBox(point);
                box.setMin(axis, overallBox.getMin(axis));
                box.setMax(axis, overallBox.getMax(axis));
                box.growCentered(thickness);
                boxes.add(box);
                amount++;
            }
        }
    }

    @Override
    public boolean requiresNoOverlap() {
        return true;
    }

//    @Override
//    public void addExtraInformation(NBTTagCompound nbt, List<String> list) {
//        list.add("interpolation: " + interpolationTypes[nbt.getInteger("interpolation")]);
//
//        int facing = nbt.getInteger("direction");
//        String text = "facing: ";
//        switch (facing) {
//        case 0:
//            text += "y";
//            break;
//        case 1:
//            text += "x";
//            break;
//        case 2:
//            text += "z";
//            break;
//        }
//        list.add(text);
//    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
//        List<GuiControl> controls = new ArrayList<>();
//        controls.add(new GuiSteppedSlider("thickness", 5, 5, 100, 14, nbt.getInteger("thickness"), 1, context.size));
//
//        controls
//            .add(new GuiStateButton("interpolation", nbt.getInteger("interpolation"), 60, 30, 40, 7, interpolationTypes));
//        controls.add(new GuiStateButton("direction", nbt.getInteger("direction"), 5, 27, "facing: y", "facing: x", "facing: z"));
//        return controls;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {
//        GuiSteppedSlider slider = (GuiSteppedSlider) gui.get("thickness");
//        nbt.setInteger("thickness", (int) slider.value);
//
//        GuiStateButton state = (GuiStateButton) gui.get("direction");
//        nbt.setInteger("direction", state.getState());
//
//        nbt.setInteger("interpolation", ((GuiStateButton) gui.get("interpolation")).getState());
//    }
//
//    @Override
//    public void rotate(NBTTagCompound nbt, Rotation rotation) {
//        int direction = nbt.getInteger("direction");
//        if (rotation.axis != Axis.Y)
//            direction = 0;
//        else {
//            if (direction == 1)
//                direction = 2;
//            else
//                direction = 1;
//        }
//
//        nbt.setInteger("direction", direction);
//    }
//
//    @Override
//    public void flip(NBTTagCompound nbt, Axis axis) {}

}
