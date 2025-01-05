package com.creativemd.littletiles.client.render;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.creativemd.creativecore.client.block.IBlockAccessFake;
import com.creativemd.littletiles.client.render.BlockRenderLayerProxy;
import com.creativemd.littletiles.client.render.DefaultVertexFormatsProxy;
import com.creativemd.littletiles.client.render.LittleRenderUtils;
import com.creativemd.littletiles.client.render.VertexFormatProxy;
import com.creativemd.littletiles.client.render.block.BakedQuadProxy;
import com.creativemd.littletiles.client.render.tile.LittleRenderBox;
import com.creativemd.littletiles.client.render.tile.RenderBox;
//import com.creativemd.littletiles.client.render.world.LittleChunkDispatcherProxy;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesProxy;
import com.creativemd.littletiles.common.utils.SingletonListProxy;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import com.creativemd.littletiles.LittleTiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderingThread extends Thread {

    private static final String[] fakeWorldMods = new String[] { "chisel" };

    public static List<RenderingThread> threads;

    private static int threadIndex;

    public static synchronized RenderingThread getNextThread() {
        synchronized (threads) {
            RenderingThread thread = threads.get(threadIndex);
            if (thread == null)
                threads.set(threadIndex, thread = new RenderingThread(threadIndex));
            threadIndex++;
            if (threadIndex >= threads.size())
                threadIndex = 0;

            return thread;
        }
    }

    public static void initThreads(int count) {
        if (count <= 0)
            throw new IllegalArgumentException("count has to be at least equal or greater than one");
        if (threads != null) {
            for (RenderingThread thread : threads)
                if (thread != null)
                    thread.interrupt();

//            for (RenderingThread thread : threads)
//                while (thread != null && thread.updateCoords.size() > 0)
//                    thread.updateCoords.poll().te.render.resetRenderingState();
        }
        threadIndex = 0;
        threads = new ArrayList<>();
        for (int i = 0; i < count; i++)
            threads.add(null);
    }

    public static final HashMap<Object, Integer> chunks = new HashMap<>();
    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean addCoordToUpdate(TileEntityLittleTilesProxy te) {
        RenderingThread renderer = getNextThread();

        Object chunk;
        // @TODO what is orientated world?
//        if (te.getWorldObj() instanceof IOrientatedWorld)
//            chunk = RenderUtils.getRenderChunk((IOrientatedWorld) te.getWorldObj(), te.getPos());
//        else
//            chunk = LittleRenderUtils.getRenderChunk(LittleRenderUtils.getViewFrustum(), te.getPos());

//        if (chunk == null) {
//            System.out.println("Invalid tileentity with no rendering chunk! pos: " + te.getPos() + ", world: " + te.getWorldObj());
//            return false;
//        }
//
         // @TODO cache
//        if (te.isRenderingEmpty()) {
//            int index = te.render.startBuildingCache();
//            te.render.getBoxCache().clear();
//            synchronized (te.render) {
//                te.render.getBufferCache().setEmpty();
//            }
//            if (!te.render.finishBuildingCache(index, LittleChunkDispatcher.currentRenderState, true))
//                return addCoordToUpdate(te);
//            return false;
//        }

        synchronized (chunks) {
//            Integer count = RenderingThread.chunks.get(chunk);
//            if (count == null)
//                count = 0;
//            RenderingThread.chunks.put(chunk, count + 1);
        }
//        renderer.updateCoords.add(new RenderingData(te, chunk));
        return true;
    }

    static {
        initThreads(1/* @TODO config LittleTiles.CONFIG.rendering.renderingThreadCount*/);
    }

    public ConcurrentLinkedQueue<RenderingData> updateCoords = new ConcurrentLinkedQueue<>();

    final int index;

    public RenderingThread(int index) {
        this.index = index;
        start();
    }

    public int getThreadIndex() {
        return index;
    }

    private final SingletonListProxy<BakedQuadProxy> bakedQuadWrapper = new SingletonListProxy<>(null);
    private final IBlockAccessFake fakeAccess = new IBlockAccessFake(mc.theWorld);
    public boolean active = true;

    @Override
    public void run() {
//        try {
//            while (active) {
//                IBlockAccess world = mc.theWorld;
//                long duration = 0;
//
//                if (world != null && !updateCoords.isEmpty()) {
//                    RenderingData data = updateCoords.poll();
//
//                    try {
//                        // @TODO profiler
////                        if (LittleTilesProfilerOverlay.isActive())
////                            duration = System.nanoTime();
//
//                        if (data.te.isInvalid())
//                            throw new InvalidTileEntityException(data.te.getPos() + "");
//
//                        data.index = data.te.render.startBuildingCache();
//
//                        BlockPos pos = data.te.getPos();
//                        LayeredRenderBoxCache cubeCache = data.te.render.getBoxCache();
//
//                        if (cubeCache == null)
//                            throw new InvalidTileEntityException(data.te.getPos() + "");
//
//                        if (data.te.getWorldObj() == null || !data.te.hasLoaded())
//                            throw new RenderingException("Tileentity is not loaded yet");
//
//                        for (BlockRenderLayerProxy layer : BlockRenderLayerProxy.values()) {
//                            cubeCache.set(BlockTile.getRenderingCubes(data.state, data.te, null, layer), layer);
//
//                            List<LittleRenderBox> cubes = cubeCache.get(layer);
//                            // UPDATE QUADS IN EACH CUBE
//                            for (int j = 0; j < cubes.size(); j++) {
//                                RenderBox cube = cubes.get(j);
//                                if (cube.doesNeedQuadUpdate) {
//                                    if (ArrayUtils.contains(fakeWorldMods, cube.block.getRegistryName().getResourceDomain())) {
//                                        fakeAccess.set(data.te.getWorldObj(), pos, cube.getBlockState());
//                                        world = fakeAccess;
//                                    } else
//                                        world = data.te.getWorldObj();
//
//                                    IBlockState modelState = cube.getBlockState().getActualState(world, pos);
//                                    IBakedModel blockModel = OptifineHelper.getRenderModel(mc.getBlockRendererDispatcher().getModelForState(modelState), world, modelState, pos);
//                                    modelState = cube.getModelState(modelState, world, pos);
//                                    BlockPos offset = cube.getOffset();
//                                    for (int h = 0; h < EnumFacingProxy.VALUES.length; h++) {
//                                        EnumFacingProxy facing = EnumFacingProxy.VALUES[h];
//                                        if (cube.renderSide(facing)) {
//                                            if (cube.getQuad(facing) == null)
//                                                cube.setQuad(facing, CreativeBakedModel
//                                                    .getBakedQuad(world, cube, pos, offset, modelState, blockModel, layer, facing, MathHelper.getPositionRandom(pos), false));
//                                        } else
//                                            cube.setQuad(facing, null);
//                                    }
//                                    cube.doesNeedQuadUpdate = false;
//                                }
//                            }
//                        }
//
//                        cubeCache.sort();
//                        fakeAccess.set(null, null, null);
//                        if (data.te.isInvalid())
//                            throw new InvalidTileEntityException(data.te.getPos() + "");
//
//                        int renderState = LittleChunkDispatcherProxy.currentRenderState;
//                        LayeredRenderBufferCache layerBuffer = data.te.render.getBufferCache();
//                        VertexFormatProxy format = DefaultVertexFormatsProxy.BLOCK;
//                        try {
//                            World renderWorld = data.te.getWorldObj();
//                            if (renderWorld instanceof SubWorldProxy && !((SubWorldProxy) renderWorld).shouldRender)
//                                renderWorld = ((SubWorldProxyProxy) renderWorld).getRealWorld();
//
//                            // Render vertex buffer
//                            for (int i = 0; i < BlockRenderLayer.values().length; i++) {
//                                BlockRenderLayer layer = BlockRenderLayer.values()[i];
//
//                                net.minecraftforge.client.ForgeHooksClient.setRenderLayer(layer);
//
//                                List<LittleRenderBox> cubes = cubeCache.get(layer);
//                                BufferBuilder buffer = null;
//
//                                if (cubes != null && cubes.size() > 0)
//                                    buffer = LayeredRenderBufferCache.createVertexBuffer(format, cubes);
//
//                                if (buffer != null) {
//                                    buffer.begin(7, format);
//                                    if (FMLClientHandler.instance().hasOptifine() && OptifineHelper.isRenderRegions() && !data.SubWorldProxy) {
//                                        int bits = 8;
//                                        RenderChunk chunk = (RenderChunk) data.chunk;
//                                        int dx = chunk.getPosition().getX() >> bits << bits;
//                                        int dy = chunk.getPosition().getY() >> bits << bits;
//                                        int dz = chunk.getPosition().getZ() >> bits << bits;
//
//                                        dx = OptifineHelper.getRenderChunkRegionX(chunk);
//                                        dz = OptifineHelper.getRenderChunkRegionZ(chunk);
//
//                                        buffer.setTranslation(-dx, -dy, -dz);
//                                    } else {
//                                        int chunkX = MathHelper.intFloorDiv(pos.getX(), 16);
//                                        int chunkY = MathHelper.intFloorDiv(pos.getY(), 16);
//                                        int chunkZ = MathHelper.intFloorDiv(pos.getZ(), 16);
//                                        buffer.setTranslation(-chunkX * 16, -chunkY * 16, -chunkZ * 16);
//                                    }
//
//                                    boolean smooth = Minecraft.isAmbientOcclusionEnabled() && data.state.getLightValue(renderWorld, pos) == 0; //&& modelIn.isAmbientOcclusion(stateIn);
//
//                                    BitSet bitset = null;
//                                    float[] afloat = null;
//                                    Object ambientFace = null;
//                                    if (FMLClientHandler.instance().hasOptifine())
//                                        ambientFace = OptifineHelper.getEnv(buffer, renderWorld, data.state, pos);
//                                    else if (smooth) {
//                                        bitset = new BitSet(3);
//                                        afloat = new float[EnumFacingProxy.VALUES.length * 2];
//                                        ambientFace = CreativeModelPipeline.createAmbientOcclusionFace();
//                                    }
//
//                                    for (int j = 0; j < cubes.size(); j++) {
//                                        RenderBox cube = cubes.get(j);
//                                        IBlockState state = cube.getBlockState();
//
//                                        if (FMLClientHandler.instance().hasOptifine() && OptifineHelper.isShaders()) {
//                                            if (state.getBlock() instanceof IFakeRenderingBlock)
//                                                state = ((IFakeRenderingBlock) state.getBlock()).getFakeState(state);
//                                            SVertexBuilder.pushEntity(state, pos, data.te.getWorldObj(), buffer);
//                                        }
//
//                                        for (int h = 0; h < EnumFacingProxy.VALUES.length; h++) {
//                                            EnumFacingProxy facing = EnumFacingProxy.VALUES[h];
//                                            Object quadObject = cube.getQuad(facing);
//                                            List<BakedQuad> quads = null;
//                                            if (quadObject instanceof List) {
//                                                quads = (List<BakedQuad>) quadObject;
//                                            } else if (quadObject instanceof BakedQuad) {
//                                                bakedQuadWrapper.setElement((BakedQuad) quadObject);
//                                                quads = bakedQuadWrapper;
//                                            }
//                                            if (quads != null && !quads.isEmpty())
//                                                if (smooth)
//                                                    CreativeModelPipeline
//                                                        .renderBlockFaceSmooth(renderWorld, state, pos, buffer, layer, quads, afloat, facing, bitset, ambientFace, cube);
//                                                else
//                                                    CreativeModelPipeline.renderBlockFaceFlat(renderWorld, state, pos, buffer, layer, quads, facing, bitset, cube, ambientFace);
//                                        }
//
//                                        bakedQuadWrapper.setElement(null);
//
//                                        if (FMLClientHandler.instance().hasOptifine() && OptifineHelper.isShaders())
//                                            SVertexBuilder.popEntity(buffer);
//
//                                        if (!LittleTiles.CONFIG.rendering.useQuadCache)
//                                            cube.deleteQuadCache();
//                                    }
//
//                                    if (FMLClientHandler.instance().hasOptifine() && OptifineHelper.isShaders())
//                                        SVertexBuilder.calcNormalChunkLayer(buffer);
//
//                                    buffer.finishDrawing();
//
//                                    synchronized (data.te.render) {
//                                        layerBuffer.set(layer.ordinal(), buffer);
//                                    }
//                                } else
//                                    synchronized (data.te.render) {
//                                        layerBuffer.set(layer.ordinal(), null);
//                                    }
//                            }
//
//                            net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
//
//                            if (!LittleTiles.CONFIG.rendering.useCubeCache)
//                                cubeCache.clear();
//                            if (!finish(data, renderState, false))
//                                updateCoords.add(data);
//
//                            if (LittleTilesProfilerOverlay.isActive())
//                                LittleTilesProfilerOverlay.finishBuildingCache(System.nanoTime() - duration);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            if (!finish(data, -1, false))
//                                updateCoords.add(data);
//                        }
//                    } catch (InvalidTileEntityException e) {
//                        finish(data, -1, true);
//                    } catch (Exception e) {
//                        if (!(e instanceof RenderingException))
//                            e.printStackTrace();
//                        //updateCoords.add(data);
//                    } catch (OutOfMemoryError error) {
//                        updateCoords.add(data);
//                        error.printStackTrace();
//                    }
//                    data = null;
//                } else if (world == null && (!updateCoords.isEmpty() || !chunks.isEmpty())) {
//                    updateCoords.clear();
//                    chunks.clear();
//                }
//
//                if (updateCoords.isEmpty())
//                    sleep(1);
//                if (Thread.currentThread().isInterrupted())
//                    throw new InterruptedException();
//            }
//        } catch (InterruptedException e) {}
    }

//    public static final Field compileTaskField = ReflectionHelper.findField(RenderChunk.class, new String[] { "compileTask", "field_178599_i" });
//
//    public static boolean finish(RenderingData data, int renderState, boolean force) {
//        if (!data.te.render.finishBuildingCache(data.index, renderState, force))
//            return false;
//
//        boolean complete = false;
//
//        synchronized (chunks) {
//            Integer count = chunks.get(data.chunk);
//            if (count != null)
//                if (count <= 1) {
//                    chunks.remove(data.chunk);
//                    complete = true;
//                } else
//                    chunks.put(data.chunk, count - 1);
//
//            /*boolean finished = true;
//            for (RenderingThread thread : threads) {
//            	if (thread != null && !thread.updateCoords.isEmpty()) {
//            		finished = false;
//            		break;
//            	}
//            }*/
//            //if (finished && !chunks.isEmpty())
//            //chunks.clear();
//        }
//
//        if (data.SubWorldProxy)
//            ((LittleRenderChunk) data.chunk).addRenderData(data.te);
//
//        if (complete) {
//            if (data.SubWorldProxy) {
//                LittleTilesProfilerOverlay.ltChunksUpdates++;
//                ((LittleRenderChunk) data.chunk).markCompleted();
//            } else {
//                LittleTilesProfilerOverlay.vanillaChunksUpdates++;
//                markRenderUpdate((RenderChunk) data.chunk);
//            }
//        }
//
//        return true;
//
//    }
//
//    public static void markRenderUpdate(RenderChunk chunk) {
//        try {
//            chunk.getLockCompileTask().lock();
//
//            if (isChunkCurrentlyUpdating(chunk))
//                LittleEventHandler.queueChunkUpdate(chunk);
//            else
//                chunk.setNeedsUpdate(false);
//
//        } finally {
//            chunk.getLockCompileTask().unlock();
//        }
//    }
//
//    public static boolean isChunkCurrentlyUpdating(RenderChunk chunk) {
//        try {
//            ChunkCompileTaskGenerator compileTask = (ChunkCompileTaskGenerator) compileTaskField.get(chunk);
//            return chunk.needsUpdate() || (compileTask != null && compileTask
//                .getType() == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK && (compileTask.getStatus() != Status.COMPILING || compileTask.getStatus() != Status.UPLOADING));
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public static class InvalidTileEntityException extends Exception {

        public InvalidTileEntityException(String arg0) {
            super(arg0);
        }
    }

    public static class RenderingException extends Exception {

        public RenderingException(String arg0) {
            super(arg0);
        }
    }

    private static class RenderingData {

        public final TileEntityLittleTilesProxy te;
//        public final IBlockState state;
        public final Object chunk;
//        public final boolean SubWorldProxy;
        public int index;

        public RenderingData(TileEntityLittleTilesProxy te, Object chunk) {
            this.te = te;
//            this.state = te.getBlockTileState();
            this.chunk = chunk;
//            this.SubWorldProxy = !(chunk instanceof RenderChunk);
        }
    }
}
