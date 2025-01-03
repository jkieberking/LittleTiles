package com.creativemd.littletiles.common.utils.shape;

import com.creativemd.littletiles.common.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class LittleShapeSelectable extends LittleShape {

    public LittleShapeSelectable(int pointsBeforePlacing) {
        super(pointsBeforePlacing);
    }

    @Override
    public boolean requiresNoOverlap() {
        return true;
    }

//    public static void addBox(LittleBoxes boxes, boolean inside, LittleGridContext context, IParentTileList parent, LittleBox box, EnumFacing facing) {
////     @TODO add if inside
////        if (inside)
////            boxes.addBox(parent.getContext(), parent.getPos(), box.copy());
////        else {
//            EnumFacingProxy facingProxy = EnumFacingProxy.fromEnumFacing(facing);
//            box = box.copy();
//            int size = 1;
//            if (parent.getContext().size > context.size) {
//                size = parent.getContext().size / context.size;
//                context = parent.getContext();
//            } else
//                box.convertTo(parent.getContext(), context);
//
//            if (facingProxy.getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE) {
//                int min = box.getMax(facingProxy.getAxis());
//                box.setMin(facingProxy.getAxis(), min);
//                box.setMax(facingProxy.getAxis(), min + 1);
//            } else {
//                int max = box.getMin(facingProxy.getAxis());
//                box.setMin(facingProxy.getAxis(), max - 1);
//                box.setMax(facingProxy.getAxis(), max);
//            }
//            boxes.addBox(context, parent.getPos(), box);
////        }
//    }

    public void addBox(LittleBoxes boxes, boolean inside, LittleGridContext context, BlockPos pos, EnumFacing facing) {
        EnumFacingProxy facingProxy = EnumFacingProxy.fromEnumFacing(facing);
        LittleBox box = new LittleBox(0, 0, 0, context.size, context.size, context.size);
        if (facingProxy.getAxisDirection() == EnumFacingProxy.AxisDirection.POSITIVE)
            box.setMax(facingProxy.getAxis(), 1);
        else
            box.setMin(facingProxy.getAxis(), context.size - 1);
        if (inside)
            boxes.addBox(context, pos, box);
        else
            boxes.addBox(context, (BlockPos) pos.offset(EnumFacingProxy.toForgeDirection(facingProxy)), box);
    }

}
