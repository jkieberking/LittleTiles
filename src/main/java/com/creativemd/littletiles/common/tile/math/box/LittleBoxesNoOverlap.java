package com.creativemd.littletiles.common.tile.math.box;

import com.creativemd.littletiles.common.tile.combine.BasicCombiner;
import com.creativemd.littletiles.common.tile.math.LittleUtils;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.type.HashMapListProxy;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class LittleBoxesNoOverlap extends LittleBoxes {

    protected HashMapListProxy<BlockPos, LittleBox> blockMap = new HashMapListProxy<>();
    protected HashMapListProxy<BlockPos, LittleBox> tempMap = new HashMapListProxy<>();
    protected List<LittleBox> cutOutTemp = new ArrayList<>();

    public LittleBoxesNoOverlap(BlockPos pos, LittleGridContext context, HashMapListProxy<BlockPos, LittleBox> map) {
        super(pos, context);
        this.blockMap = map;
    }

    public LittleBoxesNoOverlap(BlockPos pos, LittleGridContext context) {
        super(pos, context);
    }

    @Override
    public void add(LittleBox box) {
        tempMap.clear();
        box.split(context, pos, tempMap, null);
        for (Entry<BlockPos, ArrayList<LittleBox>> entry : tempMap.entrySet()) {
            List<LittleBox> existingBoxes = blockMap.getValues(entry.getKey());
            boolean missing = existingBoxes == null;

            if (missing)
                existingBoxes = new ArrayList<>();
            for (LittleBox splitted : entry.getValue()) {
                cutOutTemp.clear();
                existingBoxes.addAll(splitted.cutOut(existingBoxes, cutOutTemp/*, null*/));
            }

            BasicCombiner.combineBoxesOnlyLast(existingBoxes);
            if (missing)
                blockMap.add(entry.getKey(), existingBoxes);
        }
    }

    @Override
    public LittleBoxesNoOverlap copy() {
        return new LittleBoxesNoOverlap(pos, context, new HashMapListProxy<>(blockMap));
    }

    @Override
    public void convertTo(LittleGridContext to) {
        for (Iterator<LittleBox> iterator = blockMap.iterator(); iterator.hasNext();) {
            LittleBox box = iterator.next();
            box.convertTo(this.context, to);
        }
        this.context = to;
    }

    @Override
    public void convertToSmallest() {
        int size = LittleGridContext.minSize;
        for (Iterator<LittleBox> iterator = blockMap.iterator(); iterator.hasNext();) {
            LittleBox box = iterator.next();
            size = Math.max(size, box.getSmallestContext(context));
        }

        if (size < context.size)
            convertTo(LittleGridContext.get(size));
    }

    @Override
    public void clear() {
        blockMap.clear();
    }

    @Override
    public boolean isEmpty() {
        return blockMap.isEmpty();
    }

    @Override
    public int size() {
        return blockMap.sizeOfValues();
    }

    @Override
    public LittleBox getSurroundingBox() {
        if (isEmpty())
            return null;

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (Entry<BlockPos, ArrayList<LittleBox>> entry : blockMap.entrySet()) {
            int x = entry.getKey().getX() * context.size;
            int y = entry.getKey().getY() * context.size;
            int z = entry.getKey().getZ() * context.size;

            for (LittleBox box : entry.getValue()) {
                minX = Math.min(minX, x + box.minX);
                minY = Math.min(minY, y + box.minY);
                minZ = Math.min(minZ, z + box.minZ);
                maxX = Math.max(maxX, x + box.maxX);
                maxY = Math.max(maxY, y + box.maxY);
                maxZ = Math.max(maxZ, z + box.maxZ);
            }
        }

        return new LittleBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public HashMapListProxy<BlockPos, LittleBox> generateBlockWise() {
        return blockMap;
    }

    @Override
    public Iterable<LittleBox> all() {
        List<LittleBox> boxes = new ArrayList<>();
        LittleVec vec = new LittleVec(0, 0, 0);
        for (Entry<BlockPos, ArrayList<LittleBox>> entry : blockMap.entrySet()) {
            vec.set(context, LittleUtils.subtractVectors(entry.getKey(), new Vector3i(this.pos.x, this.pos.y, this.pos.z)));
            for (LittleBox box : entry.getValue()) {
                LittleBox toAdd = box.copy();
                toAdd.add(vec);
                boxes.add(toAdd);
            }
        }
        return boxes;
    }

//    @Override
//    public void flip(Axis axis, LittleAbsoluteBox absoluteBox) {
//        ensureContext(absoluteBox, () -> {
//            HashMapList<BlockPos, LittleBox> oldMap = blockMap;
//            blockMap = new HashMapList<>();
//            LittleVec center = absoluteBox.getDoubledCenter(pos);
//            for (LittleBox box : all()) {
//                box.flipBox(axis, center);
//                add(box);
//            }
//        });
//    }

    @Override
    public void combineBoxesBlocks() {
        // Already done when adding
    }

}
