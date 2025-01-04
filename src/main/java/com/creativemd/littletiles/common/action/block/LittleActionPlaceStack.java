package com.creativemd.littletiles.common.action.block;

import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.api.ILittlePlacer;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxesSimple;
import com.creativemd.littletiles.common.tile.preview.LittleAbsolutePreviews;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import com.creativemd.littletiles.common.utils.PlacementHelper;
import com.creativemd.littletiles.common.utils.place.*;
import cpw.mods.fml.common.FMLLog;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class LittleActionPlaceStack extends LittleAction {

    public LittleBoxes boxes;
    public LittleAbsolutePreviews destroyed;
    public PlacementPosition position;
    public boolean centered;
    public boolean fixed;
    public PlacementMode mode;
    public LittlePreviews previews;
    public ItemStack stack;

    public PlacementResult placedTiles;

    public LittleActionPlaceStack(ItemStack stack, LittlePreviews previews, PlacementPosition position, boolean centered, boolean fixed, PlacementMode mode) {
        super();
        this.position = position;
        this.centered = centered;
        this.fixed = fixed;
        this.mode = mode;
        this.previews = previews;
        this.stack = stack;
    }

    public LittleActionPlaceStack() {
        super();
    }

    public void checkMode(LittlePreviews previews) {
//        if (previews.hasStructure() && !mode.canPlaceStructures()) {
//            System.out.println("Using invalid mode for placing structure. mode=" + mode.name);
//            this.mode = PlacementMode.getStructureDefault();
//        }
    }
//
//    public void checkMode(LittleStructure structure) {
//        if (structure != null && !mode.canPlaceStructures()) {
//            System.out.println("Using invalid mode for placing structure. mode=" + mode.name);
//            this.mode = PlacementMode.getStructureDefault();
//        }
//    }
//
    @Override
    public boolean canBeReverted() {
        return true;
    }
//
//    @Override
//    public LittleAction revert(EntityPlayer player) {
//        boxes.convertToSmallest();
//
//        if (destroyed != null) {
//            destroyed.convertToSmallest();
//            return new LittleActionCombined(new LittleActionDestroyBoxes(boxes), new LittleActionPlaceAbsolute(destroyed, PlacementMode.normal, true));
//        }
//        return new LittleActionDestroyBoxes(boxes);
//    }
//
//
//    @Override
    protected boolean action(EntityPlayer player) throws LittleActionException {
        ItemStack stack = player.getHeldItem();
        World world = player.worldObj;

        if (!isAllowedToInteract(world, player, position.getPos(), true, EnumFacing.EAST)) {
            sendBlockResetToClient(world, player, position.getPos());
            return false;
        }

        if (PlacementHelper.getLittlePlacerInterface(stack) != null) {
            PlacementResult tiles = placeTile(player, stack, player.worldObj, position, centered, fixed, mode);

            if (!player.worldObj.isRemote) {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                Slot slot = playerMP.openContainer.getSlotFromInventory(playerMP.inventory, playerMP.inventory.currentItem);
                playerMP.playerNetServerHandler.sendPacket(
                    new S2FPacketSetSlot(
                        playerMP.openContainer.windowId,
                        slot.slotNumber,
                        playerMP.inventory.getCurrentItem()));            }
            return tiles != null;
        }
        return false;
    }
//
//    @Override
    public void writeBytes(ByteBuf buf) {
        position.writeToBytes(buf);
        buf.writeBoolean(centered);
        buf.writeBoolean(fixed);
        writePlacementMode(mode, buf);
        writePreviews(previews, buf);
        writeItemStack(buf, stack);

    }

    @Override
    public void readBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        position = PlacementPosition.readFromBytes(buf);
        centered = buffer.readBoolean();
        fixed = buffer.readBoolean();
        mode = readPlacementMode( buf);
        previews = readPreviews(buf);
        stack = readItemStack(buf);
    }

    @Override
    public void executeClient(EntityPlayer player) {
        //@TODO implement

    }

    @Override
    public void executeServer(EntityPlayer player) {
        if (PlacementHelper.getLittlePlacerInterface(stack) != null) {
            try {
                PlacementResult tiles = placeTile(player, stack, player.worldObj, position, centered, fixed, mode);
            } catch (LittleActionException e) {
                FMLLog.log(Level.ERROR, "failed to place tile on server!");
                throw new RuntimeException(e);
            }
        }

    }
//
//    @Override
//    public void readBytes(ByteBuf buf) {
//        this.position = PlacementPosition.readFromBytes(buf);
//        this.centered = buf.readBoolean();
//        this.fixed = buf.readBoolean();
//        this.mode = readPlacementMode(buf);
//        this.previews = readPreviews(buf);
//    }
//
    public PlacementResult placeTile(EntityPlayer player, ItemStack stack, World world, PlacementPosition position, boolean centered, boolean fixed, PlacementMode mode) throws LittleActionException, LittleActionException {
        ILittlePlacer iTile = (ILittlePlacer) PlacementHelper.getLittlePlacerInterface(stack);
        checkMode(previews);

        PlacementPreview result = PlacementHelper.getPreviews(world, previews, iTile.getPreviewsContext(stack), stack, position, centered, fixed, false, mode);

        if (result == null)
            return null;

        ItemStack toPlace = stack.copy();

//        LittleInventory inventory = new LittleInventory(player);
//
//        if (needIngredients(player))
//            if (!iTile.containsIngredients(stack))
//                canTake(player, inventory, this.mode.getBeforePlaceIngredients(result.previews));

        Placement placement = new Placement(player, result).setStack(toPlace);
        placedTiles = placement.place();

        if (placedTiles != null) {
            boxes = placedTiles.placedBoxes;

//            if (needIngredients(player)) {
//                checkAndGive(player, inventory, placement.overflow());
//
//                if (iTile.containsIngredients(stack)) {
//                    stack.shrink(1);
//                    checkAndGive(player, inventory, getIngredients(placement.unplaceableTiles));
//                } else {
//                    LittleIngredients ingredients = LittleIngredient.extractStructureOnly(previews);
//                    ingredients.add(getIngredients(placedTiles.placedPreviews));
//                    take(player, inventory, ingredients);
//                }
//            }

//            if (!placement.removedTiles.isEmpty())
//                destroyed = placement.removedTiles.copy();
        } else
            boxes = new LittleBoxesSimple(position.getPos(), result.context);

        return placedTiles;
    }
//
//    @Override
//    public LittleAction flip(Axis axis, LittleAbsoluteBox box) {
//        if (placedTiles == null)
//            return null;
//        return new LittleActionPlaceAbsolute(placedTiles.placedPreviews.copy(), mode);
//    }

}
