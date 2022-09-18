package com.creativemd.littletiles.common.blocks;

import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.utils.LittleTilePreview;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public interface ILittleTile {

	ArrayList<LittleTilePreview> getLittlePreview(ItemStack stack);

	void rotateLittlePreview(ItemStack stack, ForgeDirection direction);

	void flipLittlePreview(ItemStack stack, ForgeDirection direction);

	LittleStructure getLittleStructure(ItemStack stack);

	//public ArrayList<LittleTile> getLittleTile(ItemStack stack);
}
