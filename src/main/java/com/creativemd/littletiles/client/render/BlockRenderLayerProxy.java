package com.creativemd.littletiles.client.render;

public enum BlockRenderLayerProxy
{
    SOLID("Solid"),
    CUTOUT_MIPPED("Mipped Cutout"),
    CUTOUT("Cutout"),
    TRANSLUCENT("Translucent");

    private final String layerName;

    private BlockRenderLayerProxy(String layerNameIn)
    {
        this.layerName = layerNameIn;
    }

    public String toString()
    {
        return this.layerName;
    }
}
