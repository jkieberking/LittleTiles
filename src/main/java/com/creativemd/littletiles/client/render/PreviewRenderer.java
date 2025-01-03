package com.creativemd.littletiles.client.render;

import java.util.ArrayList;
import java.util.List;

import com.cleanroommc.modularui.utils.Color;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.render.tile.LittleRenderBox;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.api.ILittlePlacer;
import com.creativemd.littletiles.common.api.ILittleTool;
import com.creativemd.littletiles.common.tile.place.PlacePreview;
import com.creativemd.littletiles.common.utils.place.IMarkMode;
import com.creativemd.littletiles.common.utils.place.PlacementMode;
import com.creativemd.littletiles.common.utils.place.PlacementPosition;
import com.creativemd.littletiles.common.utils.place.PlacementPreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.creativemd.creativecore.client.rendering.RenderHelper3D;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.CubeObject;
import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.common.packet.LittleFlipPacket;
import com.creativemd.littletiles.common.packet.LittleRotatePacket;
import com.creativemd.littletiles.common.utils.PlacementHelper;
import com.creativemd.littletiles.common.utils.small.LittleTileBox;
import com.creativemd.littletiles.common.utils.small.LittleTileVec;
import com.creativemd.littletiles.utils.PreviewTile;
import com.creativemd.littletiles.utils.ShiftHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;

@SideOnly(Side.CLIENT)
public class PreviewRenderer {

    public static final ResourceLocation WHITE_TEXTURE = new ResourceLocation(LittleTiles.modid, "textures/preview.png");

    public static IMarkMode marked;

    public static Minecraft mc = Minecraft.getMinecraft();

    // public static ForgeDirection direction = ForgeDirection.UP;
    // public static ForgeDirection direction2 = ForgeDirection.EAST;

    /*
     * public static void updateVertical() { if(direction == ForgeDirection.UNKNOWN) direction = ForgeDirection.DOWN;
     * else if(direction == ForgeDirection.DOWN) direction = ForgeDirection.UP; else direction = ForgeDirection.UNKNOWN;
     * } public static void updateHorizontal() { if(direction2 == ForgeDirection.WEST || direction2 ==
     * ForgeDirection.EAST) direction2 = ForgeDirection.NORTH; else direction2 = ForgeDirection.EAST; }
     */

    public void processKey(ForgeDirection direction) {
        LittleRotatePacket packet = new LittleRotatePacket(direction);
        packet.executeClient(mc.thePlayer);
        PacketHandler.sendPacketToServer(packet);
    }

    public static MovingObjectPosition markedHit = null;

    public static void moveMarkedHit(ForgeDirection direction) {
        int posX = (int) markedHit.hitVec.xCoord;
        int posY = (int) markedHit.hitVec.yCoord;
        int posZ = (int) markedHit.hitVec.zCoord;
        double move = 1D / 16D;
        if (GuiScreen.isCtrlKeyDown()) move = 1;
        switch (direction) {
            case EAST:
                markedHit.hitVec.xCoord += move;
                break;
            case WEST:
                markedHit.hitVec.xCoord -= move;
                break;
            case UP:
                markedHit.hitVec.yCoord += move;
                break;
            case DOWN:
                markedHit.hitVec.yCoord -= move;
                break;
            case SOUTH:
                markedHit.hitVec.zCoord += move;
                break;
            case NORTH:
                markedHit.hitVec.zCoord -= move;
                break;
            default:
                break;
        }
        if (posX != (int) markedHit.hitVec.xCoord) markedHit.blockX += ((int) markedHit.hitVec.xCoord) - posX;
        if (posY != (int) markedHit.hitVec.yCoord) markedHit.blockY += ((int) markedHit.hitVec.yCoord) - posY;
        if (posZ != (int) markedHit.hitVec.zCoord) markedHit.blockZ += ((int) markedHit.hitVec.zCoord) - posZ;
    }

    public void processMarkKey(EntityPlayer player, ILittleTool iTile, ItemStack stack, PlacementPreview preview ) {
        while (LittleTilesClient.mark.isPressed()) {
            if (marked == null) {
                // @TODO setup onmark for position/etc...
                PlacementHelper placementHelper = PlacementHelper.getInstance(player);
                marked = iTile.onMark(player, stack , placementHelper.getPosition(player.worldObj, mc.objectMouseOver, iTile.getPositionContext(stack), iTile, stack) , mc.objectMouseOver, preview );
                player.addChatMessage(new ChatComponentText("Marked!"));
                player.addChatMessage(new ChatComponentText("Marked position is: " + marked.getPosition().getPos().toString()));
//                if (GuiScreen.isCtrlKeyDown())
//                    FMLClientHandler.instance().displayGuiScreen(player, new GuiContainerSub(player, marked.getConfigurationGui(), new SubContainerEmpty(player)));
            } else {
//                if (GuiScreen.isCtrlKeyDown())
//                    FMLClientHandler.instance().displayGuiScreen(player, new GuiContainerSub(player, marked.getConfigurationGui(), new SubContainerEmpty(player)));
//                else {
                marked.done();
                marked = null;
                player.addChatMessage(new ChatComponentText("unMarked!"));

//                }
            }
        }
    }

    public static boolean isFixed(EntityPlayer player, ItemStack stack, ILittlePlacer iTile) {
        // @TODO implement snap to grid
//        if (iTile.snapToGridByDefault(stack))
//            return !LittleAction.isUsingSecondMode(player) && marked == null;
        return marked == null; //LittleTiles.CONFIG.building.invertStickToGrid != LittleAction.isUsingSecondMode(player) && marked == null;
    }

    public static boolean isCentered(EntityPlayer player, ItemStack stack, ILittlePlacer iTile) {
// @TODO implement snap to grid
        //        if (iTile.snapToGridByDefault(stack))
//            return LittleAction.isUsingSecondMode(player) && marked == null;
        return marked != null;// LittleTiles.CONFIG.building.invertStickToGrid == LittleAction.isUsingSecondMode(player) || marked != null;
    }

    @SubscribeEvent
    public void tick(RenderHandEvent event) {
        if (mc.thePlayer != null && mc.inGameHasFocus && !mc.gameSettings.hideGUI) {
            World world = mc.theWorld;
            EntityPlayer player = mc.thePlayer;
            ItemStack stack = mc.thePlayer.getHeldItem();

//                if (!LittleAction.canPlace(player))
//                    return;
//
//                handleUndoAndRedo(player);

            if (stack != null && stack.getItem() instanceof ILittleTool &&
                (marked != null || (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK /* @TODO don't think we need this?: && mc.objectMouseOver.sideHit != null */))
            ) {
                PlacementHelper helper = PlacementHelper.getInstance(mc.thePlayer);
                PlacementPosition position = helper.getPosition(world, mc.objectMouseOver, ((ILittleTool) stack.getItem()).getPositionContext(stack), (ILittleTool) stack.getItem(), stack); // marked != null ? marked.getPosition() : helper.getPosition(world, mc.objectMouseOver, ((ILittleTool) stack.getItem()).getPositionContext(stack), (ILittleTool) stack.getItem(), stack);

                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

                ((ILittleTool) stack.getItem()).tick(player, stack, position, mc.objectMouseOver);

                if (PlacementHelper.isLittlePlacer(stack)) {
                    ILittlePlacer iPlacer = PlacementHelper.getLittlePlacerInterface(stack);
                    PlacementMode mode = iPlacer.getPlacementMode(stack);

                    if (mode.getPreviewMode() == PlacementMode.PreviewMode.PREVIEWS) {
                        GL11.glEnable(GL11.GL_BLEND);

                        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        mc.renderEngine.bindTexture(WHITE_TEXTURE);
                        GL11.glDepthMask(false);

                        boolean allowLowResolution = marked != null ? marked.allowLowResolution() : true;
                        PlacementPreview result = PlacementHelper
                            .getPreviews(world, stack, position, isCentered(player, stack, iPlacer), isFixed(player, stack, iPlacer), allowLowResolution, mode);

                        if (result != null) {
                            processMarkKey(player, (ILittleTool) stack.getItem(), stack, result);
                            List<PlacePreview> placePreviews = result.getPreviews();

                            double posX = result.pos.getX() - TileEntityRendererDispatcher.staticPlayerX;
                            double posY = result.pos.getY() - TileEntityRendererDispatcher.staticPlayerY;
                            double posZ = result.pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ;

                            float alpha = (float) (Math.sin(System.nanoTime() / 200000000F) * 0.2F + 0.5F);

                            for (int i = 0; i < placePreviews.size(); i++) {
                                PlacePreview preview = placePreviews.get(i);
                                List<LittleRenderBox> cubes = preview.getPreviews(result.context);
                                for (LittleRenderBox cube : cubes)
                                    cube.renderPreview(posX, posY, posZ, (int) (alpha * iPlacer.getPreviewAlphaFactor() * 255));
                            }

                            if (position.positingCubes != null)
                                for (LittleRenderBox cube : position.positingCubes)
                                    cube.renderPreview(posX, posY, posZ, (int) (alpha * Color.getAlpha(cube.color) * iPlacer.getPreviewAlphaFactor() * 255));

                        }

                        GL11.glDepthMask(false);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_BLEND);
                    }


                } else {
                    processMarkKey(player, (ILittleTool) stack.getItem(), stack, null);
                }
                ((ILittleTool) stack.getItem()).render(player, stack, x, y, z);
                if (marked != null) {
                    marked.render(((ILittleTool) stack.getItem()).getPositionContext(stack), x, y, z);
                }
                } else {
                marked = null;
            }
        }
    }
}
