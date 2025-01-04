package com.creativemd.littletiles.common.items;

import java.util.List;

import com.cleanroommc.modularui.factory.ClientGUI;
import com.creativemd.littletiles.client.render.PreviewRenderer;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.api.ILittlePlacer;
import com.creativemd.littletiles.common.gui.GuiChisel;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.preview.LittleAbsolutePreviews;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.registry.LittleTileRegistry;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.place.IMarkMode;
import com.creativemd.littletiles.common.utils.place.PlacementMode;
import com.creativemd.littletiles.common.utils.place.PlacementPosition;
import com.creativemd.littletiles.common.utils.place.PlacementPreview;
import com.creativemd.littletiles.common.utils.shape.LittleShape;
import com.creativemd.littletiles.common.utils.shape.ShapeRegistry;
import com.creativemd.littletiles.common.utils.shape.ShapeSelection;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.creativemd.littletiles.LittleTiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemLittleChisel extends Item implements ILittlePlacer {
    private static final Logger log = LogManager.getLogger(ItemLittleChisel.class);

    public static ShapeSelection selection;
    public static PlacementMode currentMode = PlacementMode.fill;
    public static boolean initializedShape = false;

    public ItemLittleChisel() {
        setCreativeTab(CreativeTabs.tabTools);
        hasSubtypes = true;
        setMaxStackSize(1);
    }

    public static void setShape(ItemStack stack, LittleShape shape) {
        self:initializedShape = true;
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        stack.getTagCompound().setString("shape", shape.getKey());
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getIconString() {
        return LittleTiles.modid + ":LTChisel";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();

        if (stack.stackTagCompound.hasKey("x1")) list.add(
                "1: x=" + stack.stackTagCompound.getInteger("x1")
                        + ",y="
                        + stack.stackTagCompound.getInteger("y1")
                        + ",z="
                        + stack.stackTagCompound.getInteger("z1"));
        else list.add("1: undefinded");

        if (stack.stackTagCompound.hasKey("x2")) list.add(
                "2: x=" + stack.stackTagCompound.getInteger("x2")
                        + ",y="
                        + stack.stackTagCompound.getInteger("y2")
                        + ",z="
                        + stack.stackTagCompound.getInteger("z2"));
        else list.add("2: undefinded");

        list.add("creative mode only");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMarkMode onMark(EntityPlayer player, ItemStack stack, PlacementPosition position , MovingObjectPosition result, PlacementPreview previews  ) {
        if (selection != null)
            selection.toggleMark();
        return selection;
    }

    @Override
    public void onClickAir(EntityPlayer player, ItemStack stack) {
        if (selection != null) {
//            selection.click(player);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onClickBlock(World world, EntityPlayer player, ItemStack stack, PlacementPosition position, MovingObjectPosition movingObjectPosition) {
//        if (selection != null)
//            selection.click(player);
        return false;
    }

    @Override
    public boolean onRightClick(World world, EntityPlayer player, ItemStack stack, PlacementPosition position, MovingObjectPosition movingObjectPosition) {
        if (LittleAction.isUsingSecondMode(player)) {
            selection = null;
            PreviewRenderer.marked = null;
        } else if (selection != null)
            return selection.addAndCheckIfPlace(player, getPosition(position, movingObjectPosition, currentMode), movingObjectPosition);
        return false;
    }

    @Override
    public void onDeselect(World world, ItemStack stack, EntityPlayer player) {
        selection = null;
    }

//    @Override
    @SideOnly(Side.CLIENT)
    public void tick(EntityPlayer player, ItemStack stack, PlacementPosition position , MovingObjectPosition movingObjectPosition ) {
        if (!initializedShape) {
            self:setShape(stack, ShapeRegistry.getShape("slice"));
        }
        NBTTagCompound a = stack.stackTagCompound;
        if (selection == null)
            selection = new ShapeSelection(stack, false);
        selection.setLast(player, stack, getPosition(position, movingObjectPosition, currentMode), movingObjectPosition );
    }

    protected static PlacementPosition getPosition(PlacementPosition position, MovingObjectPosition movingObjectPosition, PlacementMode mode) {
        if (position.getPosX() == 0) {
            int a = 0;
        }
        position = position.copy();

        EnumFacingProxy facing = EnumFacingProxy.fromEnumFacing(position.facing);
        if (mode.placeInside)
            facing = facing.getOpposite();
        if (facing.getAxisDirection() == EnumFacingProxy.AxisDirection.NEGATIVE)
            position.getVec().add(position.getVec());


        return position;
    }

    @Override
    public boolean hasLittlePreview(ItemStack stack) {
        return true;
    }

    @Override
    public PlacementMode getPlacementMode(ItemStack stack) {
        return currentMode;
    }

    public static LittlePreview getPreview(ItemStack stack) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        if (stack.getTagCompound().hasKey("preview"))
            return LittleTileRegistry.loadPreview(stack.getTagCompound().getCompoundTag("preview"));

//        IBlockState state = stack.getTagCompound().hasKey("state") ? Block.getStateById(stack.getTagCompound().getInteger("state")) : Blocks.STONE.getDefaultState();
        LittleTile tile = new LittleTile(Blocks.stone, 0/*state.getBlock().getMetaFromState(state)*/); /*stack.getTagCompound().hasKey("color") ? new LittleTileColored(state.getBlock(), state.getBlock().getMetaFromState(state), stack.getTagCompound()
            .getInteger("color")) : new LittleTile(state.getBlock(), state.getBlock().getMetaFromState(state));*/

        LittleGridContext context = LittleGridContext.get();
        tile.setBox(new LittleBox(0, 0, 0, context.size, context.size, context.size));
        LittlePreview preview = tile.getPreviewTile();
        setPreview(stack, preview);
        return preview;
    }

    public static void setPreview(ItemStack stack, LittlePreview preview) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbt = new NBTTagCompound();
        preview.writeToNBT(nbt);
        stack.getTagCompound().setTag("preview", nbt);
    }

    @Override
    public LittleAbsolutePreviews getLittlePreview(ItemStack stack) {
        return null;
    }

    @Override
    public LittleAbsolutePreviews getLittlePreview(ItemStack stack, boolean allowLowResolution) {
        if (selection != null) {
            LittleBoxes boxes = selection.getBoxes(allowLowResolution);

            LittleAbsolutePreviews previews = new LittleAbsolutePreviews(boxes.pos, boxes.context);

            LittlePreview preview = getPreview(stack);
            for (LittleBox box : boxes.all()) {
                LittlePreview newPreview = preview.copy();
                newPreview.box = box.copy();
                previews.addWithoutCheckingPreview(newPreview);
            }

            return previews;
        }
        return null;
    }
}
