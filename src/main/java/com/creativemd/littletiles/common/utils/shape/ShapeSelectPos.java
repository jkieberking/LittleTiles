package com.creativemd.littletiles.common.utils.shape;

import com.creativemd.littletiles.common.utils.grid.IGridBased;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.place.PlacementPosition;
import net.minecraft.entity.player.EntityPlayer;

public class ShapeSelectPos implements IGridBased {
//
    public final PlacementPosition pos;
//    public final RayTraceResult ray;
//    public final BlockTile.TEResult result;
//
    public ShapeSelectPos(EntityPlayer player, PlacementPosition position /*, RayTraceResult result */) {
        this.pos = position;
//        this.ray = result;
//        this.result = BlockTile.loadTeAndTile(player.world, result.getBlockPos(), player);
//        if (inside && result.sideHit.getAxisDirection() == AxisDirection.POSITIVE && context.isAtEdge(VectorUtils.get(result.sideHit.getAxis(), result.hitVec)))
//            pos.getVec().sub(result.sideHit);
    }

    public ShapeSelectPos(PlacementPosition position /*, RayTraceResult ray, BlockTile.TEResult result*/) {
        this.pos = position;
//        this.ray = ray;
//        this.result = result;
    }
//
//    public void move(LittleGridContext context, EnumFacing facing) {
//        LittleVec vec = new LittleVec(facing);
//        vec.scale(GuiScreen.isCtrlKeyDown() ? context.size : 1);
//        pos.subVec(vec);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof ShapeSelectPos) {
//            if (!pos.equals(((ShapeSelectPos) obj).pos))
//                return false;
//            if (result.parent != ((ShapeSelectPos) obj).result.parent)
//                return false;
//            if (result.tile != ((ShapeSelectPos) obj).result.tile)
//                return false;
//            return true;
//        }
//        return false;
//    }
//
//    public AxisAlignedBB getBox() {
//        return pos.getBox(context);
//    }
//
//    @SideOnly(Side.CLIENT)
//    public void render(LittleGridContext context, double x, double y, double z, boolean selected) {
//        GlStateManager.enableBlend();
//        GlStateManager
//            .tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//
//        GlStateManager.disableTexture2D();
//        GlStateManager.depthMask(false);
//        AxisAlignedBB box = getBox().grow(0.002).offset(-x, -y, -z);
//
//        GlStateManager.glLineWidth(4.0F);
//        RenderGlobal.drawSelectionBoundingBox(box, 0.0F, 0.0F, 0.0F, 1F);
//
//        GlStateManager.disableDepth();
//        GlStateManager.glLineWidth(1.0F);
//        if (selected)
//            RenderGlobal.drawSelectionBoundingBox(box, 1F, 0.3F, 0.0F, 1F);
//        GlStateManager.enableDepth();
//
//        GlStateManager.depthMask(true);
//        GlStateManager.enableTexture2D();
//        GlStateManager.disableBlend();
//    }
//
    @Override
    public LittleGridContext getContext() {
        return pos.getContext();
    }
//
    @Override
    public void convertTo(LittleGridContext to) {
        pos.convertTo(to);
    }
//
//    @Override
//    public void convertToSmallest() {
//        pos.convertToSmallest();
//    }
//
//    public int getSmallestContext() {
//        return pos.getSmallestContext();
//    }
//
    public ShapeSelectPos copy() {
        return new ShapeSelectPos(pos.copy() /*, ray, result */);
    }
}
