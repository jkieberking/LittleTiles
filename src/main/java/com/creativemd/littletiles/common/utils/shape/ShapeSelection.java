package com.creativemd.littletiles.common.utils.shape;

//import com.creativemd.creativecore.common.gui.container.SubGui;
//import com.creativemd.creativecore.common.utils.math.Rotation;
//import com.creativemd.creativecore.common.utils.math.VectorUtils;
//import com.creativemd.creativecore.common.utils.mc.TickUtils;
//import com.creativemd.littletiles.client.gui.SubGuiMarkShapeSelection;
import com.creativemd.littletiles.common.api.ILittlePlacer;
import com.creativemd.littletiles.common.api.ILittleTool;
//import com.creativemd.littletiles.common.block.BlockTile;
//import com.creativemd.littletiles.common.tile.math.box.LittleBox;
//import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
//import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
//import com.creativemd.littletiles.common.util.grid.IGridBased;
//import com.creativemd.littletiles.common.util.grid.LittleGridContext;
//import com.creativemd.littletiles.common.util.place.IMarkMode;
//import com.creativemd.littletiles.common.util.place.PlacementPosition;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.RenderGlobal;
//import net.minecraft.entity.player.EntityPlayer;
import com.creativemd.littletiles.common.blocks.BlockTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.utils.grid.IGridBased;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.place.IMarkMode;
import com.creativemd.littletiles.common.utils.place.PlacementPosition;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumFacing.Axis;
//import net.minecraft.util.EnumFacing.AxisDirection;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.util.math.Vec3d;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;

// @TODO add implements API
public class ShapeSelection implements Iterable<ShapeSelection.ShapeSelectPos>, IMarkMode, IGridBased {

    public ItemStack stack;
    public ILittleTool tool;
    private final List<ShapeSelectPos> positions = new ArrayList<>();

    public final boolean inside;

    protected LittleShape shape;
    protected String shapeKey;

    protected BlockPos pos;
    private ShapeSelectPos last;
    protected LittleGridContext context = LittleGridContext.getMin();

    protected LittleBox overallBox;
    protected ShapeSelectCache cache;

    private boolean marked;
    private int markedPosition;
    public boolean allowLowResolution = true;

    public ShapeSelection(ItemStack stack, boolean inside) {
        this.inside = inside;
        this.tool = (ILittleTool) stack.getItem();
        this.stack = stack;

        shapeKey = getNBT().getString("shape");
        shape = ShapeRegistry.getShape(shapeKey);
    }

    public NBTTagCompound getNBT() {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        if (stack.getItem() instanceof ILittlePlacer) {
            ILittlePlacer placer = (ILittlePlacer) stack.getItem();
            if (!placer.getIsShapeInitialized()) {
                placer.setShape(stack, ShapeRegistry.getShape("slice"));
                placer.setShapeInitialized();
            }
        }
        return stack.stackTagCompound;
    }

    protected boolean requiresCacheUpdate() {
        if (cache == null)
            return true;

        if (cache.context != context)
            return true;

        NBTTagCompound nbt = getNBT();
        if (!shapeKey.equals(nbt.getString("shape"))) {
            shapeKey = nbt.getString("shape");
            shape = ShapeRegistry.getShape(shapeKey);
            if (!cache.shapeKey.equals(shapeKey))
                return true;
        }

        if (countPositions() != cache.positions.size())
            return true;

        int i = 0;
        for (ShapeSelectPos pos : this) {
            if (!pos.equals(cache.positions.get(i)))
                return true;
            i++;
        }

        return false;
    }

    public LittleBox getOverallBox() {
        return overallBox.copy();
    }

    protected ShapeSelectCache getCache() {
        if (requiresCacheUpdate()) {
            LittleBox[] pointBoxes = new LittleBox[countPositions()];
            List<ShapeSelectPos> positions = new ArrayList<>(pointBoxes.length);
            int i = 0;
            for (ShapeSelectPos pos : ShapeSelection.this) {
                if (pos.pos.getPosX() == 0 || ShapeSelection.this.pos.x == 0) {
                    int a = 0;
                }
                pointBoxes[i] = new LittleBox(pos.pos.getRelative(ShapeSelection.this.pos));
                positions.add(pos.copy());
                i++;
            }

            overallBox = new LittleBox(pointBoxes);
            cache = new ShapeSelectCache(context, positions);
        }
        return cache;
    }

    public BlockPos getPos() {
        return pos;
    }
//
    @Override
    public LittleGridContext getContext() {
        return context;
    }

    public LittleBoxes getBoxes(boolean allowLowResolution) {
        if (this.allowLowResolution && allowLowResolution)
            return getCache().get(true);
        return getCache().get(false);
    }

    public void deleteCache() {
        cache = null;
    }

    @SideOnly(Side.CLIENT)
    public boolean addAndCheckIfPlace(EntityPlayer player, PlacementPosition position, MovingObjectPosition movingObjectPosition) {
        if (marked)
            return true;
        ShapeSelectPos pos = new ShapeSelectPos(player, position, movingObjectPosition);
        if ((shape.pointsBeforePlacing > positions.size() + 1 /*|| GuiScreen.isCtrlKeyDown())*/ && (shape.maxAllowed() == -1 || shape.maxAllowed() > positions.size() + 1))) {
            positions.add(pos);
            ensureSameContext(pos);
            return false;
        }
        last = pos;
        ensureSameContext(last);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void setLast(EntityPlayer player, ItemStack stack, PlacementPosition position , MovingObjectPosition objectPosition ) {
        this.stack = stack;
        if (objectPosition == null)
            return;
        if (positions.isEmpty())
            pos = position.getPos();
        if (position.getPosX() == 0) {
            int a = 0;
        }
        last = new ShapeSelectPos(player, position , objectPosition );
        ensureSameContext(last);
    }

    private void ensureSameContext(ShapeSelectPos pos) {
        if (context.size > pos.getContext().size)
            pos.convertTo(context);
        else if (context.size < pos.getContext().size)
            convertTo(pos.getContext());
    }
//
    public void toggleMark() {
        if (marked) {
            while (shape.maxAllowed() != -1 && positions.size() >= shape.maxAllowed())
                positions.remove(positions.size() - 1);
            markedPosition = positions.size() - 1;
            marked = false;
        } else {
            markedPosition = positions.size();
            positions.add(last);
            marked = true;
        }
    }

    @Override
    public boolean allowLowResolution() {
        return allowLowResolution;
    }

    @Override
    public PlacementPosition getPosition() {
        ShapeSelectPos retPos = positions.get(markedPosition);
        return retPos.pos.copy();
    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public SubGui getConfigurationGui() {
//        return new SubGuiMarkShapeSelection(this);
//    }
//
//    @Override
//    public void move(LittleGridContext context, EnumFacing facing) {
//        positions.get(markedPosition).move(context, facing);
//        deleteCache();
//    }
//
    @Override
    public void done() {
        toggleMark();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(LittleGridContext context, double x, double y, double z) {
        if (marked) {
            for (int i = 0; i < positions.size(); i++)
                positions.get(i).render(context, x, y, z, markedPosition == i);

        }
    }
//
//    public void rotate(EntityPlayer player, ItemStack stack, Rotation rotation) {
//        shape.rotate(getNBT(), rotation);
//        deleteCache();
//    }
//
//    public void flip(EntityPlayer player, ItemStack stack, Axis axis) {
//        shape.flip(getNBT(), axis);
//        deleteCache();
//    }

//    @SideOnly(Side.CLIENT)
//    public void click(EntityPlayer player) {
//        if (!marked)
//            return;
//        int index = -1;
//        double distance = Double.MAX_VALUE;
//        float partialTickTime = TickUtils.getPartialTickTime();
//        Vec3d pos = player.getPositionEyes(partialTickTime);
//        double d0 = player.capabilities.isCreativeMode ? 5.0F : 4.5F;
//        Vec3d look = player.getLook(partialTickTime);
//        Vec3d vec32 = pos.addVector(look.x * d0, look.y * d0, look.z * d0);
//        for (int i = 0; i < positions.size(); i++) {
//            RayTraceResult result = positions.get(i).getBox().calculateIntercept(pos, vec32);
//            if (result != null) {
//                double tempDistance = pos.squareDistanceTo(result.hitVec);
//                if (tempDistance < distance) {
//                    index = i;
//                    distance = tempDistance;
//                }
//            }
//        }
//        if (index != -1)
//            markedPosition = index;
//    }

    @Override
    public Iterator<ShapeSelectPos> iterator() {
        if (marked)
            return positions.iterator();
        return new Iterator<ShapeSelectPos>() {

            private Iterator<ShapeSelectPos> iter = positions.iterator();
            private boolean last = false;

            @Override
            public boolean hasNext() {
                return iter.hasNext() || !last;
            }

            @Override
            public ShapeSelectPos next() {
                if (iter.hasNext())
                    return iter.next();
                else if (!last) {
                    last = true;
                    return ShapeSelection.this.last;
                }
                throw new UnsupportedOperationException();
            }
        };
    }

    public ShapeSelectPos getFirst() {
        if (positions.size() > 0)
            return positions.get(0);
        return last;
    }

    public ShapeSelectPos getLast() {
        if (marked)
            return positions.get(positions.size() - 1);
        return last;
    }

    @Override
    public void convertTo(LittleGridContext to) {
        for (ShapeSelectPos other : positions)
            other.convertTo(to);
        context = to;
    }

    @Override
    public void convertToSmallest() {
        int smallest = LittleGridContext.getMin().size;
        for (int i = 0; i < positions.size(); i++)
            smallest = Math.max(smallest, positions.get(i).getSmallestContext());
        convertTo(LittleGridContext.get(smallest));
    }

    public int countPositions() {
        return positions.size() + (marked ? 0 : 1);
    }

    public class ShapeSelectCache {

        protected final List<ShapeSelectPos> positions;
        protected final LittleGridContext context;

        protected final LittleShape shape;
        protected final String shapeKey;
        protected final LittleBoxes cachedBoxesLowRes;
        protected LittleBoxes cachedBoxes;

        public ShapeSelectCache(LittleGridContext context, List<ShapeSelectPos> positions) {
            String shapeKey1;
            this.context = context;
            NBTTagCompound nbt = getNBT();
            String shapeKeyTmp = nbt.getString("shape");
            shapeKey = "slice"; //@TODO unhardcode
            Collection<LittleShape> shapes = ShapeRegistry.shapes();
            shape = ShapeRegistry.getShape(shapeKey);

            this.positions = positions;
            cachedBoxesLowRes = shape.getBoxes(ShapeSelection.this, true);
        }

        public LittleBoxes get(boolean allowLowResolution) {
            if (allowLowResolution)
                return cachedBoxesLowRes;
            if (cachedBoxes == null)
                cachedBoxes = shape.getBoxes(ShapeSelection.this, false);
            return cachedBoxes;
        }
    }

    public class ShapeSelectPos implements IGridBased {
        //
        public final PlacementPosition pos;
        public final MovingObjectPosition objectPosition;
//        public final BlockTile.TEResult result;
        //
        public ShapeSelectPos(EntityPlayer player, PlacementPosition position , MovingObjectPosition result) {
            this.pos = position;
            this.objectPosition = result;
//            this.result = BlockTile.loadTeAndTile(player.worldObj, new BlockPos(result.blockX, result.blockY, result.blockZ), player);
//            if (inside && result.sideHit.getAxisDirection() == AxisDirection.POSITIVE && context.isAtEdge(VectorUtils.get(result.sideHit.getAxis(), result.hitVec)))
//                pos.getVec().sub(result.sideHit);
        }

        public ShapeSelectPos(PlacementPosition position, MovingObjectPosition objectPosition/*, BlockTile.TEResult result*/) {
            this.pos = position;
            this.objectPosition = objectPosition;
//            this.result = result;
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
    public AxisAlignedBB getBox() {
        return pos.getBox(context);
    }

    public AxisAlignedBB growAxisAlignedBB(AxisAlignedBB box, double amount) {
        double d0 = box.minX - amount;
        double d1 = box.minY - amount;
        double d2 = box.minZ - amount;
        double d3 = box.maxX + amount;
        double d4 = box.maxY + amount;
        double d5 = box.maxZ + amount;

        return AxisAlignedBB.getBoundingBox(d0, d1, d2, d3, d4, d5);
    }

    @SideOnly(Side.CLIENT)
    public void render(LittleGridContext context, double x, double y, double z, boolean selected) {

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        AxisAlignedBB box = this.growAxisAlignedBB(getBox(), 0.002).offset(-x, -y, -z);

        GL11.glLineWidth(4.0F);
        RenderGlobal.drawOutlinedBoundingBox(box, 1 /*0.0F, 0.0F, 0.0F, 1F*/);

//        GlStateManager.disableDepth();
//        GlStateManager.glLineWidth(1.0F);
//        if (selected)
//            RenderGlobal.drawSelectionBoundingBox(box, 1F, 0.3F, 0.0F, 1F);
//        GlStateManager.enableDepth();

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

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
    @Override
    public void convertToSmallest() {
        pos.convertToSmallest();
    }

    public int getSmallestContext() {
        return pos.getSmallestContext();
    }

        public ShapeSelectPos copy() {
            return new ShapeSelectPos(pos.copy(), objectPosition );
        }
    }
}
