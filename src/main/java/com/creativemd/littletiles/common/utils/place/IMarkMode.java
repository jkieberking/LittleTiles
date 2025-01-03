package com.creativemd.littletiles.common.utils.place;

//import com.creativemd.creativecore.common.gui.container.SubGui;
//import com.creativemd.littletiles.common.util.grid.LittleGridContext;
//import net.minecraft.util.EnumFacing;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;

import com.creativemd.littletiles.common.utils.grid.LittleGridContext;

// @TODO implement markmode interface
public interface IMarkMode {

    public boolean allowLowResolution();
//
    public PlacementPosition getPosition();
//
//    @SideOnly(Side.CLIENT)
//    public SubGui getConfigurationGui();
//
    public void render(LittleGridContext positionContext, double x, double y, double z);
//
//    public void move(LittleGridContext positionContext, EnumFacing facing);
//
    public void done();
//
}
