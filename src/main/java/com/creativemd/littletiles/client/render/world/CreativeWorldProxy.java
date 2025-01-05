//package com.creativemd.littletiles.client.render.world;
//
//import com.creativemd.littletiles.client.render.IRenderChunkSupplier;
//import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
//import net.minecraft.entity.Entity;
//import net.minecraft.profiler.Profiler;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldProvider;
//import net.minecraft.world.storage.ISaveHandler;
//import net.minecraft.world.storage.WorldInfo;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
//public abstract class CreativeWorldProxy extends World implements IOrientatedWorld {
//
//    public Entity parent;
//    @SideOnly(Side.CLIENT)
//    public IRenderChunkSupplier renderChunkSupplier;
//    public boolean hasChanged = false;
//
//    public boolean preventNeighborUpdate = false;
//
//    protected CreativeWorldProxy(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
//        super(saveHandlerIn, info, providerIn, profilerIn, client);
//    }
//
////    @Override
////    public void neighborChanged(BlockPos pos, final Block blockIn, BlockPos fromPos) {
////        if (preventNeighborUpdate)
////            return;
////        if (this.isRemote) {
////            IBlockState iblockstate = this.getBlockState(pos);
////
////            try {
////                iblockstate.neighborChanged(this, pos, blockIn, fromPos);
////            } catch (Throwable throwable) {
////                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
////                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
////                crashreportcategory.addDetail("Source block type", new ICrashReportDetail<String>() {
////                    @Override
////                    public String call() throws Exception {
////                        try {
////                            return String
////                                .format("ID #%d (%s // %s // %s)", Block.getIdFromBlock(blockIn), blockIn.getUnlocalizedName(), blockIn.getClass().getName(), blockIn.getRegistryName());
////                        } catch (Throwable var2) {
////                            return "ID #" + Block.getIdFromBlock(blockIn);
////                        }
////                    }
////                });
////                CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
////                throw new ReportedException(crashreport);
////            }
////        } else
////            super.neighborChanged(pos, blockIn, fromPos);
////
////    }
//
////    @Override
////    public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType, boolean updateObservers) {
////        if (preventNeighborUpdate)
////            return;
////        super.notifyNeighborsOfStateChange(pos, blockType, updateObservers);
////    }
////
////    @Override
////    public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide) {
////        if (preventNeighborUpdate)
////            return;
////        super.notifyNeighborsOfStateExcept(pos, blockType, skipSide);
////    }
////
////    @Override
////    public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType, boolean p_175722_3_) {
////        if (preventNeighborUpdate)
////            return;
////        super.notifyNeighborsRespectDebug(pos, blockType, p_175722_3_);
////    }
//
//    public BlockPos transformToRealWorld(BlockPos pos) {
//        return new BlockPos(getOrigin().transformPointToWorld(new Vec3d(pos)));
//    }
//
////    @Override
////    public void playBroadcastSound(int id, BlockPos pos, int data) {
////        if (getOrigin() == null)
////            return;
////        getRealWorld().playBroadcastSound(id, transformToRealWorld(pos), data);
////    }
////
////    @Override
////    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
////        if (getOrigin() == null)
////            return;
////        Vector3d vec = new Vector3d(x, y, z);
////        getOrigin().transformPointToWorld(vec);
////        getRealWorld().playSound(vec.x, vec.y, vec.z, soundIn, category, volume, pitch, distanceDelay);
////    }
//
////    @Override
////    public void playSound(EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
////        if (getOrigin() == null)
////            return;
////        getRealWorld().playSound(player, transformToRealWorld(pos), soundIn, category, volume, pitch);
////    }
////
////    @Override
////    public void playSound(EntityPlayer player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
////        if (getOrigin() == null)
////            return;
////        Vector3d vec = new Vector3d(x, y, z);
////        getOrigin().transformPointToWorld(vec);
////        getRealWorld().playSound(player, x, y, z, soundIn, category, volume, pitch);
////    }
////
////    @Override
////    public void playRecord(BlockPos blockPositionIn, SoundEvent soundEventIn) {
////        if (getOrigin() == null)
////            return;
////        getRealWorld().playRecord(transformToRealWorld(blockPositionIn), soundEventIn);
////    }
//}
