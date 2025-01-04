package com.creativemd.littletiles.common.event;

import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.client.render.PreviewRenderer;
import com.creativemd.littletiles.common.action.block.LittleActionPlaceStack;
import com.creativemd.littletiles.common.api.ILittlePlacer;
import com.creativemd.littletiles.common.api.ILittleTool;
import com.creativemd.littletiles.common.utils.PlacementHelper;
import com.creativemd.littletiles.common.utils.place.PlacementMode;
import com.creativemd.littletiles.common.utils.place.PlacementPosition;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.WorldEvent.Unload;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.*;

@SideOnly(Side.CLIENT)
public class LittleEventHandler {

    public static ItemStack lastSelectedItem = null;
    public static ILittleTool tool = null;
    public static boolean onClickFirst = true;

    @SideOnly(Side.CLIENT)
    private boolean leftClicked;

//    @SubscribeEvent
//    @SideOnly(Side.CLIENT)
//    public void onMouseWheelClick(PickBlockEvent event) {
//        if (event.result != null && event.result.typeOfHit == Type.BLOCK) {
//            ItemStack stack = event.player.getHeldItemMainhand();
//            if (stack.getItem() instanceof ILittleTool && ((ILittleTool) stack.getItem())
//                .onMouseWheelClickBlock(event.world, event.player, stack, new PlacementPosition(event.result, ((ILittleTool) stack.getItem()).getPositionContext(stack)), event.result))
//                event.setCanceled(true);
//        }
//    }
//
//    @SubscribeEvent
//    public void onLeftClickAir(LeftClickEmpty event) {
//        if (event.getWorld().isRemote) {
//            ItemStack stack = event.getItemStack();
//
//            if (stack.getItem() instanceof ILittleTool)
//                ((ILittleTool) stack.getItem()).onClickAir(event.getEntityPlayer(), stack);
//        }
//    }
//
//    @SubscribeEvent
//    public void onLeftClick(LeftClickBlock event) {
//        if (event.getWorld().isRemote) {
//            if (!leftClicked) {
//                ItemStack stack = event.getItemStack();
//
//                if (event.getHitVec() == null)
//                    return;
//
//                RayTraceResult ray = new RayTraceResult(event.getHitVec(), event.getFace(), event.getPos());
//                if (lastSelectedItem != null && lastSelectedItem.getItem() != stack.getItem()) {
//                    tool.onClickAir(event.getEntityPlayer(), lastSelectedItem);
//                    lastSelectedItem = null;
//                }
//
//                if (stack.getItem() instanceof ILittleTool) {
//                    if (((ILittleTool) stack.getItem())
//                        .onClickBlock(event.getWorld(), event
//                            .getEntityPlayer(), stack, new PlacementPosition(ray, ((ILittleTool) stack.getItem()).getPositionContext(stack)), ray) && LittleTilesClient.INTERACTION
//                                .start(false))
//                        event.setCanceled(true);
//                    tool = (ILittleTool) stack.getItem();
//                    lastSelectedItem = stack;
//                }
//
//                leftClicked = true;
//            }
//        } else if (event.getItemStack().getItem() instanceof ILittleTool)
//            event.setCanceled(true);
//    }
//
//    @SideOnly(Side.CLIENT)
//    private static ResourceLocation RES_UNDERWATER_OVERLAY;
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void renderOverlay(RenderBlockOverlayEvent event) {
//        if (RES_UNDERWATER_OVERLAY == null)
//            RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
//
//        Minecraft mc = Minecraft.getMinecraft();
//        if (event.getOverlayType() == OverlayType.WATER) {
//            EntityPlayer player = event.getPlayer();
//            double d0 = player.posY + player.getEyeHeight();
//            BlockPos blockpos = new BlockPos(player.posX, d0, player.posZ);
//            TileEntity te = player.world.getTileEntity(blockpos);
//            if (te instanceof TileEntityLittleTiles) {
//                AxisAlignedBB bb = player.getEntityBoundingBox();
//                for (Pair<IParentTileList, LittleTile> pair : ((TileEntityLittleTiles) te).allTiles()) {
//                    LittleTile tile = pair.value;
//                    if (tile instanceof LittleTileColored && tile.isMaterial(Material.WATER) && tile.getBox().getBox(pair.key.getContext(), blockpos).intersects(bb)) {
//
//                        mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
//                        Tessellator tessellator = Tessellator.getInstance();
//                        BufferBuilder bufferbuilder = tessellator.getBuffer();
//                        float f = mc.player.getBrightness();
//                        Vec3d color = ColorUtils.IntToVec(((LittleTileColored) tile).color);
//                        GlStateManager.color(f * (float) color.x, f * (float) color.y, f * (float) color.z, 0.5F);
//                        GlStateManager.enableBlend();
//                        GlStateManager
//                            .tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//                        GlStateManager.pushMatrix();
//                        float f1 = 4.0F;
//                        float f2 = -1.0F;
//                        float f3 = 1.0F;
//                        float f4 = -1.0F;
//                        float f5 = 1.0F;
//                        float f6 = -0.5F;
//                        float f7 = -mc.player.rotationYaw / 64.0F;
//                        float f8 = mc.player.rotationPitch / 64.0F;
//                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
//                        bufferbuilder.pos(-1.0D, -1.0D, -0.5D).tex(4.0F + f7, 4.0F + f8).endVertex();
//                        bufferbuilder.pos(1.0D, -1.0D, -0.5D).tex(0.0F + f7, 4.0F + f8).endVertex();
//                        bufferbuilder.pos(1.0D, 1.0D, -0.5D).tex(0.0F + f7, 0.0F + f8).endVertex();
//                        bufferbuilder.pos(-1.0D, 1.0D, -0.5D).tex(4.0F + f7, 0.0F + f8).endVertex();
//                        tessellator.draw();
//                        GlStateManager.popMatrix();
//                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//                        GlStateManager.disableBlend();
//
//                        event.setCanceled(true);
//                        return;
//                    }
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public void breakSpeed(BreakSpeed event) {
//        ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
//        if (stack.getItem() instanceof ILittleTool)
//            event.setNewSpeed(0);
//    }
//
    @SubscribeEvent
    public void onMouseInput(MouseEvent event) {
        if (event.button == 0) {
            //left click
            return;
        }
        // only pass through right clicks
        if (event.button != 1) {
            return;
        }
        // if we are handling the second event from the tick, don't trigger the event again
        if (!onClickFirst) {
            onClickFirst = true;
            return;
        } else {
            onClickFirst = false;
        }
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        ItemStack stack = player.getHeldItem();
        if (stack == null) {
            return;
        }
        MovingObjectPosition movingObjectPosition = Minecraft.getMinecraft().objectMouseOver;
        if (stack.getItem() instanceof ILittleTool) {
//            if (mc.theWorld.isRemote)
                if (onRightInteractClient((ILittleTool) stack.getItem(), player, mc.theWorld, stack, movingObjectPosition)) {
                    event.setCanceled(true);
                }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean onRightInteractClient(ILittleTool iTile, EntityPlayer player, World world, ItemStack stack, MovingObjectPosition movingObjectPosition) {
        if (iTile instanceof ILittleTool) {
            PlacementPosition position = getPosition(world, iTile, stack, Minecraft.getMinecraft().objectMouseOver, player);
            if (iTile.onRightClick(world, player, stack, position.copy(), Minecraft.getMinecraft().objectMouseOver) && iTile instanceof ILittlePlacer && ((ILittlePlacer) iTile)
                .hasLittlePreview(stack)) {
                if (movingObjectPosition != null) {
                    BlockPos blockPos = new BlockPos(movingObjectPosition.blockX, movingObjectPosition.blockY, movingObjectPosition.blockZ);
                    // this block triggers the blocks to be updated in the game
                    if (true /* @TODO check that player can edit: player.canPlayerEdit(blockPos.x(), blockPos.y(), blockPos.z(), -999 *//* not used *//*, stack)*/ && LittleTilesClient.INTERACTION.start(true)) {
                        PlacementMode mode = ((ILittlePlacer) iTile).getPlacementMode(stack).place();
                        new LittleActionPlaceStack(stack, ((ILittlePlacer) iTile).getLittlePreview(stack, false), position, PreviewRenderer
                            .isCentered(player, stack, (ILittlePlacer) iTile), PreviewRenderer.isFixed(player, stack, (ILittlePlacer) iTile), mode).execute();

                        PreviewRenderer.marked = null;
                    }
                }
                iTile.onDeselect(world, stack, player);
                LittleTilesClient.INTERACTION.finish();
                return true;
            }
        }
        return false;
    }
//
//    private static Field setTileEntitiesField;
//
//    @SubscribeEvent
//    public synchronized void worldUnload(Unload event) {
//        if (event.getWorld().isRemote) {
//            MissingBlockHandler.unload();
//            ItemModelCache.unload();
//
//            for (int i = 0; i < RenderingThread.threads.size(); i++) {
//                RenderingThread thread = RenderingThread.threads.get(i);
//                if (thread == null)
//                    continue;
//                thread.interrupt();
//                thread.updateCoords.clear();
//                RenderingThread.threads.set(i, null);
//            }
//            RenderingThread.chunks.clear();
//
//            if (setTileEntitiesField == null)
//                setTileEntitiesField = ReflectionHelper.findField(RenderGlobal.class, new String[] { "setTileEntities", "field_181024_n" });
//
//            try {
//                Set<TileEntity> set = (Set<TileEntity>) setTileEntitiesField.get(Minecraft.getMinecraft().renderGlobal);
//                synchronized (set) {
//                    set.clear();
//                }
//            } catch (IllegalArgumentException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        } else {
//            queuedUpdateStructures.clear();
//            queuedStructures.clear();
//        }
//    }
//
    @SideOnly(Side.CLIENT)
    public static PlacementPosition getPosition(World world, ILittleTool iTile, ItemStack stack, MovingObjectPosition movingObjectPosition, EntityPlayer player) {
        return PreviewRenderer.marked != null ? PreviewRenderer.marked.getPosition() : new PlacementHelper(player)
            .getPosition(world, movingObjectPosition, iTile.getPositionContext(stack), iTile, stack);
    }

//
//    @SubscribeEvent
//    public void isSleepingLocationAllowed(SleepingLocationCheckEvent event) {
//        try {
//            LittleStructure bed = (LittleStructure) LittleBed.littleBed.get(event.getEntityPlayer());
//            if (bed instanceof LittleBed && ((LittleBed) bed).getSleepingPlayer() == event.getEntityPlayer())
//                event.setResult(Result.ALLOW);
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerLogout(PlayerLoggedOutEvent event) {
//        try {
//            LittleStructure bed = (LittleStructure) LittleBed.littleBed.get(event.player);
//            if (bed instanceof LittleBed)
//                ((LittleBed) bed).setSleepingPlayer(null);
//            LittleBed.littleBed.set(event.player, null);
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @SubscribeEvent
//    public void onWakeUp(PlayerWakeUpEvent event) {
//        try {
//            LittleStructure bed = (LittleStructure) LittleBed.littleBed.get(event.getEntityPlayer());
//            if (bed instanceof LittleBed)
//                ((LittleBed) bed).setSleepingPlayer(null);
//            LittleBed.littleBed.set(event.getEntityPlayer(), null);
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @SubscribeEvent
//    public void onPickup(EntityItemPickupEvent event) {
//        EntityPlayer player = event.getEntityPlayer();
//        EntityItem entityItem = event.getItem();
//        ItemStack stack = entityItem.getItem();
//
//        if (stack.getItem() instanceof ILittleIngredientInventory && ((ILittleIngredientInventory) stack.getItem()).shouldBeMerged()) {
//            LittleIngredients ingredients = ((ILittleIngredientInventory) stack.getItem()).getInventory(stack);
//            LittleInventory inventory = new LittleInventory(player);
//            inventory.allowDrop = false;
//
//            if (ingredients == null) {
//                entityItem.setDead();
//                event.setCanceled(true);
//                event.setResult(Result.DENY);
//                return;
//            }
//
//            try {
//                if (LittleAction.canGive(player, inventory, ingredients)) {
//                    LittleAction.give(player, inventory, ingredients);
//
//                    player.onItemPickup(entityItem, 1);
//                    entityItem.setDead();
//
//                    event.setCanceled(true);
//                    event.setResult(Result.DENY);
//                }
//            } catch (NotEnoughIngredientsException e1) {
//
//            }
//        }
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void onPlayerSleep(PlayerSleepInBedEvent event) {
//        if (event.getEntityPlayer().world.getBlockState(event.getPos()).getBlock() instanceof BlockTile) {
//            TileEntityLittleTiles te = BlockTile.loadTe(event.getEntityPlayer().world, event.getPos());
//            if (te != null) {
//                for (LittleStructure structure : te.loadedStructures()) {
//                    if (structure instanceof LittleBed && ((LittleBed) structure).hasBeenActivated) {
//                        try {
//                            ((LittleBed) structure).trySleep(event.getEntityPlayer(), structure.getHighestCenterVec());
//                            event.setResult(SleepResult.OK);
//                            ((LittleBed) structure).hasBeenActivated = false;
//                            return;
//                        } catch (CorruptedConnectionException | NotYetConnectedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @SideOnly(Side.CLIENT)
//    public static int transparencySortingIndex;
//
//    private static Field prevRenderSortX;
//    private static Field prevRenderSortY;
//    private static Field prevRenderSortZ;
//
//    @SubscribeEvent
//    @SideOnly(Side.CLIENT)
//    public void onRenderTick(RenderTickEvent event) {
//        if (event.phase == Phase.START) {
//            Minecraft mc = Minecraft.getMinecraft();
//
//            if (mc.player != null && mc.renderGlobal != null) {
//                if (prevRenderSortX == null) {
//                    prevRenderSortX = ReflectionHelper.findField(RenderGlobal.class, new String[] { "prevRenderSortX", "field_147596_f" });
//                    prevRenderSortY = ReflectionHelper.findField(RenderGlobal.class, new String[] { "prevRenderSortY", "field_147597_g" });
//                    prevRenderSortZ = ReflectionHelper.findField(RenderGlobal.class, new String[] { "prevRenderSortZ", "field_147602_h" });
//                }
//
//                Entity entityIn = mc.getRenderViewEntity();
//                if (entityIn == null)
//                    return;
//                try {
//                    double d0 = entityIn.posX - prevRenderSortX.getDouble(mc.renderGlobal);
//                    double d1 = entityIn.posY - prevRenderSortY.getDouble(mc.renderGlobal);
//                    double d2 = entityIn.posZ - prevRenderSortZ.getDouble(mc.renderGlobal);
//                    if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0D)
//                        transparencySortingIndex++;
//                } catch (IllegalArgumentException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//    @SideOnly(Side.CLIENT)
//    private static List<RenderChunk> queuedRenderChunksUpdate;
//
//    @SideOnly(Side.CLIENT)
//    public static void queueChunkUpdate(RenderChunk chunk) {
//        if (queuedRenderChunksUpdate == null)
//            queuedRenderChunksUpdate = new ArrayList<>();
//        synchronized (queuedRenderChunksUpdate) {
//            if (!queuedRenderChunksUpdate.contains(chunk))
//                queuedRenderChunksUpdate.add(chunk);
//        }
//    }
//
//    private static final HashSet<LittleStructure> queuedUpdateStructures = new HashSet<>();
//    private static final HashSet<LittleStructure> queuedStructures = new HashSet<>();
//
//    public static synchronized void queueStructureForUpdatePacket(LittleStructure structure) {
//        if (structure.getWorld().isRemote)
//            return;
//        queuedUpdateStructures.add(structure);
//    }
//
//    public static synchronized void queueStructureForNextTick(LittleStructure structure) {
//        if (structure.getWorld().isRemote)
//            return;
//        queuedStructures.add(structure);
//    }
//
//    @SubscribeEvent
//    public synchronized void serverTick(ServerTickEvent event) {
//        if (event.phase == Phase.START)
//            return;
//        if (!queuedUpdateStructures.isEmpty()) {
//            for (LittleStructure structure : queuedUpdateStructures)
//                structure.sendUpdatePacket();
//            queuedUpdateStructures.clear();
//        }
//        if (!queuedStructures.isEmpty()) {
//            for (Iterator<LittleStructure> iterator = queuedStructures.iterator(); iterator.hasNext();) {
//                LittleStructure structure = iterator.next();
//                if (!structure.queueTick())
//                    iterator.remove();
//            }
//        }
//        SignalTicker.serverTick();
//    }
//
//    @SubscribeEvent
//    @SideOnly(Side.CLIENT)
//    public void onClientTick(ClientTickEvent event) {
//        if (event.phase == Phase.END) {
//            Minecraft mc = Minecraft.getMinecraft();
//
//            if (queuedRenderChunksUpdate == null)
//                queuedRenderChunksUpdate = new ArrayList<>();
//            synchronized (queuedRenderChunksUpdate) {
//                if (!queuedRenderChunksUpdate.isEmpty()) {
//                    for (Iterator iterator = queuedRenderChunksUpdate.iterator(); iterator.hasNext();) {
//                        RenderChunk chunk = (RenderChunk) iterator.next();
//                        if (!chunk.needsUpdate()) {
//                            chunk.setNeedsUpdate(false);
//                            iterator.remove();
//                        }
//                    }
//                }
//            }
//
//            ItemModelCache.tick(mc.world);
//
//            if (leftClicked && !mc.gameSettings.keyBindAttack.isKeyDown()) {
//                leftClicked = false;
//            }
//
//            if (mc.player != null) {
//                ItemStack stack = mc.player.getHeldItemMainhand();
//
//                if (lastSelectedItem != null && lastSelectedItem.getItem() != stack.getItem()) {
//                    tool.onDeselect(mc.world, lastSelectedItem, mc.player);
//                    lastSelectedItem = null;
//                }
//
//                while (LittleTilesClient.configure.isPressed())
//                    if (stack.getItem() instanceof ILittleTool) {
//                        SubGui gui = ((ILittleTool) stack.getItem()).getConfigureGUI(mc.player, stack);
//                        if (gui != null)
//                            GuiHandler.openGui("configure", new NBTTagCompound());
//                    }
//
//                while (LittleTilesClient.configureAdvanced.isPressed())
//                    if (stack.getItem() instanceof ILittleTool) {
//                        SubGui gui = ((ILittleTool) stack.getItem()).getConfigureGUIAdvanced(mc.player, stack);
//                        if (gui != null)
//                            GuiHandler.openGui("configureadvanced", new NBTTagCompound());
//                    }
//            }
//        }
//    }
//
//    private static Field entitiesById = ReflectionHelper.findField(World.class, new String[] { "entitiesById", "field_175729_l" });
//
//    @SideOnly(Side.CLIENT)
//    public static boolean cancelEntitySpawn(WorldClient world, int entityID, Entity entity) {
//        if (entity instanceof EntityAnimation) {
//            ((EntityAnimation) entity).addDoor();
//
//            if (((EntityAnimation) entity).spawnedInWorld) {
//                entity.setEntityId(entityID);
//                try {
//                    ((IntHashMap<Entity>) entitiesById.get(world)).addKey(entityID, entity);
//                } catch (IllegalArgumentException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
//            ((EntityAnimation) entity).spawnedInWorld = true;
//            return false;
//        }
//        return false;
//    }
}
