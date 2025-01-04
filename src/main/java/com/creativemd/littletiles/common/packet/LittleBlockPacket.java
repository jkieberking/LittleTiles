package com.creativemd.littletiles.common.packet;

import java.util.UUID;

import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.parent.IParentTileList;
import com.creativemd.littletiles.common.type.PairProxy;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class LittleBlockPacket extends CreativeCorePacket {

    public UUID uuid;
    public BlockPos blockPos;
    public BlockPos pos;
    public Vec3 look;
//    public BlockPacketAction action;
    public World world;
    public NBTTagCompound nbt;

    public int x;
    public int y;
    public int z;
    public int action;

    public LittleBlockPacket() {

    }

    public LittleBlockPacket(int x, int y, int z, EntityPlayer player, int action) {
        this(x, y, z, player, action, new NBTTagCompound());
    }

    public LittleBlockPacket(int x, int y, int z, EntityPlayer player, int action, NBTTagCompound nbt) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.action = action;
        this.pos = new BlockPos(player.serverPosX, player.serverPosY, player.serverPosZ);
        double d0 = player.capabilities.isCreativeMode ? 5.0F : 4.5F;
        Vec3 look = player.getLook(1.0F);
        this.look = player.getLook(1.0F);
        this.nbt = nbt;
    }

    @Override
    public void writeBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        writeVec3(Vec3.createVectorHelper(pos.x, pos.y, pos.z), buf);
        writeVec3(look, buf);
        buf.writeInt(action);
        writeNBT(buf, nbt);
    }

    public static void writePos(ByteBuf buf, BlockPos pos) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static void writeFacing(ByteBuf buf, EnumFacing facing) {
        buf.writeInt(EnumFacingProxy.fromEnumFacing(facing).getIndex());
    }

    public static EnumFacing readFacing(ByteBuf buf) {
        return EnumFacing.getFront(buf.readInt());
    }

    public static BlockPos readPos(ByteBuf buf) {
        return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }


    @Override
    public void readBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        pos = new BlockPos((int) readVec3(buf).xCoord, (int)  readVec3(buf).yCoord, (int)  readVec3(buf).zCoord);
        look = readVec3(buf);
        action = buf.readInt();
        nbt = readNBT(buf);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void executeClient(EntityPlayer player) {

    }

    @Override
    public void executeServer(EntityPlayer player) {

        World world = player.worldObj;

        if (uuid != null) {
            // @TODO animation
//            EntityAnimation animation = WorldAnimationHandler.findAnimation(false, uuid);
//            if (animation == null)
//                return;

//            if (!LittleAction.isAllowedToInteract(player, animation, action.rightClick))
//                return;

//            world = animation.fakeWorld;
//            pos = animation.origin.transformPointToFakeWorld(pos);
//            look = animation.origin.transformPointToFakeWorld(look);
        }

        TileEntity tileEntity = world.getTileEntity(blockPos.x, blockPos.y, blockPos.z);
        if (tileEntity instanceof TileEntityLittleTilesProxy) {
            TileEntityLittleTilesProxy te = (TileEntityLittleTilesProxy) tileEntity;
//            PairProxy<IParentTileList, LittleTile> pair = te.getFocusedTile(/*pos, look*/);
            MovingObjectPosition movingObjectPosition= te.getFocusedTile(/*pos, look*/);

            if (!LittleAction.isAllowedToInteract(world, player, blockPos, true /*action.rightClick*/, EnumFacing.EAST)) {
                LittleAction.sendBlockResetToClient(world, player, blockPos);
                return;
            }

            if (movingObjectPosition != null) {
                ItemStack stack = player.getHeldItem();
                MovingObjectPosition moving = Minecraft.getMinecraft().objectMouseOver;
//                action.action(world, te, pair.key, pair.value, stack, player, moving, blockPos, nbt);

                if (!player.worldObj.isRemote) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                    Slot slot = playerMP.openContainer.getSlotFromInventory(playerMP.inventory, playerMP.inventory.currentItem);
//                    playerMP.connection.sendPacket(new SPacketSetSlot(playerMP.openContainer.windowId, slot.slotNumber, playerMP.inventory.getCurrentItem()));
                }
            }
        }
    }
}
