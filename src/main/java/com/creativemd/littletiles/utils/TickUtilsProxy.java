package com.creativemd.littletiles.utils;

import com.creativemd.littletiles.client.CreativeCoreClientProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TickUtilsProxy {

    @SideOnly(Side.CLIENT)
    private static float getPartialTickTimeClient() {
        return CreativeCoreClientProxy.getRenderPartialTicks();
    }

    public static float getPartialTickTime() {
        if (FMLCommonHandler.instance().getSide().isClient())
            return getPartialTickTimeClient();
        return 1.0F;
    }

}
