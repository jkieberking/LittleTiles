package com.creativemd.littletiles.client.render;

import com.creativemd.littletiles.client.render.block.BakedQuadProxy;
import com.creativemd.littletiles.client.render.block.IVertexConsumer;
import com.creativemd.littletiles.client.render.entity.LittleRenderChunkProxy;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LittleRenderUtils {

    private static final VertexFormatProxy DEFAULT_FROM = VertexLighterFlatProxy.withNormal(DefaultVertexFormatsProxy.BLOCK);
    private static final VertexFormatProxy DEFAULT_TO = DefaultVertexFormatsProxy.ITEM;
    private static final int[] DEFAULT_MAPPING = generateMapping(DEFAULT_FROM, DEFAULT_TO);
    public static int[] mapFormat;
    private static final ConcurrentMap<Pair<VertexFormatProxy, VertexFormatProxy>, int[]> formatMaps = new ConcurrentHashMap<>();

    public static void putBakedQuad(IVertexConsumer consumer, BakedQuadProxy quad)
    {
        consumer.setTexture(quad.getSprite());
        consumer.setQuadOrientation(quad.getFace());
        if(quad.hasTintIndex())
        {
            consumer.setQuadTint(quad.getTintIndex());
        }
        consumer.setApplyDiffuseLighting(quad.shouldApplyDiffuseLighting());
        float[] data = new float[4];
        VertexFormatProxy formatFrom = consumer.getVertexFormat();
        VertexFormatProxy formatTo = quad.getFormat();
        int countFrom = formatFrom.getElements().size();
        int countTo = formatTo.getElements().size();
        int[] eMap = mapFormats(formatFrom, formatTo);
        for(int v = 0; v < 4; v++)
        {
            for(int e = 0; e < countFrom; e++)
            {
                if(eMap[e] != countTo)
                {
                    unpack(quad.getVertexData(), data, formatTo, v, eMap[e]);
                    consumer.put(e, data);
                }
                else
                {
                    consumer.put(e);
                }
            }
        }
    }

    public static int[] mapFormats(VertexFormatProxy from, VertexFormatProxy to)
    {
        //Speedup: in 99.99% this is the mapping, no need to go make a pair, and go through the slower hash map
        if (from.equals(DEFAULT_FROM) && to.equals(DEFAULT_TO))
            return DEFAULT_MAPPING;
        return formatMaps.computeIfAbsent(Pair.of(from, to), pair -> generateMapping(pair.getLeft(), pair.getRight()));
    }

    private static int[] generateMapping(VertexFormatProxy from, VertexFormatProxy to)
    {
        int fromCount = from.getElements().size();
        int toCount = to.getElements().size();
        int[] eMap = new int[fromCount];

        for(int e = 0; e < fromCount; e++)
        {
            VertexFormatElementProxy expected = from.getElements().get(e);
            int e2;
            for(e2 = 0; e2 < toCount; e2++)
            {
                VertexFormatElementProxy current = to.getElements().get(e2);
                if(expected.getUsage() == current.getUsage() && expected.getIndex() == current.getIndex())
                {
                    break;
                }
            }
            eMap[e] = e2;
        }
        return eMap;
    }

    public static void unpack(int[] from, float[] to, VertexFormatProxy formatFrom, int v, int e)
    {
        int length = 4 < to.length ? 4 : to.length;
        VertexFormatElementProxy element = formatFrom.getElements().get(e);
        int vertexStart = v * formatFrom.getNextOffset() + formatFrom.getOffset(e);
        int count = element.getElementCount();
        VertexFormatElementProxy.EnumType type = element.getType();
        int size = type.getSize();
        int mask = (256 << (8 * (size - 1))) - 1;
        for(int i = 0; i < length; i++)
        {
            if(i < count)
            {
                int pos = vertexStart + size * i;
                int index = pos >> 2;
                int offset = pos & 3;
                int bits = from[index];
                bits = bits >>> (offset * 8);
                if((pos + size - 1) / 4 != index)
                {
                    bits |= from[index + 1] << ((4 - offset) * 8);
                }
                bits &= mask;
                if(type == VertexFormatElementProxy.EnumType.FLOAT)
                {
                    to[i] = Float.intBitsToFloat(bits);
                }
                else if(type == VertexFormatElementProxy.EnumType.UBYTE || type == VertexFormatElementProxy.EnumType.USHORT)
                {
                    to[i] = (float)bits / mask;
                }
                else if(type == VertexFormatElementProxy.EnumType.UINT)
                {
                    to[i] = (float)((double)(bits & 0xFFFFFFFFL) / 0xFFFFFFFFL);
                }
                else if(type == VertexFormatElementProxy.EnumType.BYTE)
                {
                    to[i] = ((float)(byte)bits) / (mask >> 1);
                }
                else if(type == VertexFormatElementProxy.EnumType.SHORT)
                {
                    to[i] = ((float)(short)bits) / (mask >> 1);
                }
                else if(type == VertexFormatElementProxy.EnumType.INT)
                {
                    to[i] = (float)((double)(bits & 0xFFFFFFFFL) / (0xFFFFFFFFL >> 1));
                }
            }
            else
            {
                to[i] = 0;
            }
        }
    }

//    public static LittleRenderChunkProxy getRenderChunk(IOrientatedWorld world, BlockPos pos) {
//        if (world && ((CreativeWorld) world).renderChunkSupplier != null)
//            return (LittleRenderChunkProxy) ((CreativeWorld) world).renderChunkSupplier.getRenderChunk((World) world, pos);
//        return null;
//    }
}
