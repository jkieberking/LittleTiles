package com.creativemd.littletiles.client.render;

import com.creativemd.creativecore.common.utils.CubeObject;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public interface ITilesRenderer { //is client-side effective only!

	ArrayList<CubeObject> getRenderingCubes(ItemStack stack);

	boolean hasBackground(ItemStack stack);

}
