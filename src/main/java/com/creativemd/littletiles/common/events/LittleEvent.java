package com.creativemd.littletiles.common.events;

import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.client.render.PreviewRenderer;
import com.creativemd.littletiles.common.action.block.LittleActionPlaceStack;
import com.creativemd.littletiles.common.api.ILittlePlacer;
import com.creativemd.littletiles.common.api.ILittleTool;
import com.creativemd.littletiles.common.utils.place.PlacementMode;
import com.creativemd.littletiles.common.utils.place.PlacementPosition;
import com.creativemd.littletiles.utils.EnumFacingProxy;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.utils.PlacementHelper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// this is not present in 1.12
@Deprecated
public class LittleEvent {

    @SideOnly(Side.CLIENT)
    public static int renderPass;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPreRenderWorld(RenderWorldEvent.Pre event) {
        renderPass = event.pass;
    }

    @SubscribeEvent
    public void openContainer(PlayerOpenContainerEvent event) {
        if (event.entityPlayer.openContainer instanceof ContainerWorkbench) event.setResult(Result.ALLOW);
    }



    @SideOnly(Side.CLIENT)
    public static PlacementPosition getPosition(World world, ILittleTool iTile, ItemStack stack, MovingObjectPosition movingObjectPosition, EntityPlayer player) {
        return PreviewRenderer.marked != null ? PreviewRenderer.marked.getPosition() : new PlacementHelper(player)
            .getPosition(world, movingObjectPosition, iTile.getPositionContext(stack), iTile, stack);
    }
}
