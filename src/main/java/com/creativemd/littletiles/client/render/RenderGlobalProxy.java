package com.creativemd.littletiles.client.render;

import com.cleanroommc.modularui.drawable.BufferBuilder;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

/**
 * This class is meant to proxy 1.12 RenderGlobal calls that aren't in 1.7
 */
public class RenderGlobalProxy {
    public static void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha)
    {
        drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
    }

    public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_TRIANGLE_FAN);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.addVertex(maxX, minY, maxZ);
        tessellator.addVertex(minX, minY, maxZ);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(minX, maxY, minZ);
        tessellator.addVertex(maxX, maxY, minZ);
        tessellator.addVertex(maxX, maxY, maxZ);
        tessellator.addVertex(minX, maxY, maxZ);
        tessellator.addVertex(minX, maxY, minZ);
        tessellator.addVertex(minX, maxY, maxZ);
        tessellator.addVertex(minX, minY, maxZ);
        tessellator.addVertex(maxX, maxY, maxZ);
        tessellator.addVertex(maxX, minY, maxZ);
        tessellator.addVertex(maxX, maxY, minZ);
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.draw();

//        buffer.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
//        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(minX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
//        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
//        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
//        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
//        buffer.pos(maxX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
    }
}
