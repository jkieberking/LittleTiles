package com.creativemd.littletiles.common.tileentity;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TileEntityCreativeProxy extends TileEntity {

    private World tempWorld;

    public boolean isClientSide() {
        if (worldObj != null)
            return worldObj.isRemote;
        return tempWorld.isRemote;
    }

    public World getTempWorld() {
        return tempWorld;
    }

//    @Override
    protected void setWorldCreate(World worldIn) {
        tempWorld = worldIn;
    }

    public void deleteTempWorld() {
        tempWorld = null;
    }

//    @Override
//    public SPacketUpdateTileEntity getUpdatePacket() {
//        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
//    }

//    @Override
//    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
//        handleUpdate(pkt.getNbtCompound(), false);
//    }
//
//    @Override
//    public void handleUpdateTag(NBTTagCompound tag) {
//        handleUpdate(tag, true);
//    }
//
//    public abstract void handleUpdate(NBTTagCompound nbt, boolean chunkUpdate);
//
//    @Override
//    public NBTTagCompound getUpdateTag() {
//        return writeToNBT(new NBTTagCompound());
//    }
//
//    public double getDistance(BlockPos coord) {
//        return Math.sqrt(pos.distanceSq(coord));
//    }
//
//    @SideOnly(Side.CLIENT)
//    public void updateRender() {
//        worldObj.markBlockRangeForRenderUpdate(new BlockPos(), pos);
//    }

    public void updateBlock() {
        if (!worldObj.isRemote) {
            BlockPos pos = this.getPos();
//            IBlockState state = world.getBlockState(pos);
            worldObj.markBlockForUpdate(pos.x(), pos.y(), pos.z());
//            worldObj.markChunkDirty(getPos(), this);
        }
    }

    public BlockPos getPos()
    {
        return new BlockPos(this.xCoord, this.yCoord, this.zCoord);
    }


}
