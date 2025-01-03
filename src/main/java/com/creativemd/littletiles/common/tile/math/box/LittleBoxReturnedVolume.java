package com.creativemd.littletiles.common.tile.math.box;

import com.creativemd.creativecore.common.utils.RotationUtils.Axis;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;

public class LittleBoxReturnedVolume {

    private int volume;

    public LittleBoxReturnedVolume() {

    }

    public void addPixel() {
        volume++;
    }

    public void addDifBox(LittleBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        int sizeX = (maxX - minX) - box.getSize(Axis.Xaxis);
        int sizeY = (maxY - minY) - box.getSize(Axis.Yaxis);
        int sizeZ = (maxZ - minZ) - box.getSize(Axis.Zaxis);
        volume += sizeX * sizeY * sizeZ;
    }

    public void addBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        volume += (maxX - minX) * (maxY - minY) * (maxZ - minZ);
    }

    public boolean has() {
        return volume > 0;
    }

    public int getVolume() {
        return volume;
    }

    public double getPercentVolume(LittleGridContext context) {
        return volume / (double) context.maxTilesPerBlock;
    }

    public void clear() {
        volume = 0;
    }

    public LittlePreview createFakePreview(LittlePreview preview) {
        LittlePreview copy = preview.copy();
        copy.setBox(new LittleBox(0, 0, 0, volume, 1, 1));
        return copy;
    }

//    public LittleTile createFakeTile(LittleTile tile) {
//        LittleTile copy = tile.copy();
//        copy.setBox(new LittleBox(0, 0, 0, volume, 1, 1));
//        return copy;
//    }

}
