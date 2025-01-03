package com.creativemd.littletiles.common.utils;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.Vec3;

import com.creativemd.creativecore.common.utils.ColorUtils;
import com.creativemd.creativecore.common.utils.CubeObject;

public class LittleTileColored extends LittleTile {

    public int color;

    public LittleTileColored(Block block, int meta, Vec3 color) {
        super(block, meta);
        this.color = ColorUtils.RGBToInt(color);
    }

    public LittleTileColored() {
        super();
    }

    @Override
    public void updatePacket(NBTTagCompound nbt) {
        super.updatePacket(nbt);
        nbt.setInteger("color", color);
    }

    @Override
    public void receivePacket(NBTTagCompound nbt, NetworkManager net) {
        super.receivePacket(nbt, net);
        color = nbt.getInteger("color");
    }

//    @Override
//    public ArrayList<CubeObject> getRenderingCubes() {
//        ArrayList<CubeObject> cubes = super.getRenderingCubes();
//        int color = this.color;
//        for (CubeObject cube : cubes) {
//            cube.color = color;
//        }
//        return cubes;
//    }

    @Override
    public void copyExtra(LittleTile tile) {
        super.copyExtra(tile);
        if (tile instanceof LittleTileColored) {
            LittleTileColored thisTile = (LittleTileColored) tile;
            thisTile.color = color;
        }
    }

    @Override
    public void saveTileExtra(NBTTagCompound nbt) {
        super.saveTileExtra(nbt);
        nbt.setInteger("color", color);
    }

    @Override
    public void loadTileExtra(NBTTagCompound nbt) {
        super.loadTileExtra(nbt);
        color = nbt.getInteger("color");
    }

    @Override
    public boolean canBeCombined(LittleTile tile) {
        if (tile instanceof LittleTileColored && super.canBeCombined(tile)) {
            int color1 = ((LittleTileColored) tile).color;
            int color2 = this.color;
            return color1 == color2;
        }
        return false;
    }

    public static LittleTile setColor(LittleTile tile, int color) {
        if (color == ColorUtils.WHITE) return removeColor(tile);
        if (tile instanceof LittleTileColored) {
            ((LittleTileColored) tile).color = color;
        } else {
            LittleTileColored newTile = new LittleTileColored();
            tile.assign(newTile);
            newTile.color = color;
            return newTile;
        }
        return null;
    }

    public static LittleTile removeColor(LittleTile tile) {
        if (tile instanceof LittleTileColored) {
            LittleTile newTile = new LittleTile();
            tile.assign(newTile);
            return newTile;
        }
        return null;
    }

}
