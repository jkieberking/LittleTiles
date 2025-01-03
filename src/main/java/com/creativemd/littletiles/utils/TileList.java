package com.creativemd.littletiles.utils;

import com.creativemd.littletiles.common.parent.IParentTileList;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.parent.ParentTileList;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesProxy;
import com.creativemd.littletiles.common.type.PairProxy;
import com.creativemd.littletiles.common.utils.LittleTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.nio.channels.NotYetConnectedException;
import java.util.*;

public class TileList extends ParentTileList {

    public TileList() {
        super();
    }
    public TileList(TileEntityLittleTilesProxy te) {
        super();
        this.te = te;
    }
    public TileEntityLittleTilesProxy te;

    @Override
    public LittleTile get(int paramInt) {
        if (paramInt < size()) {
            return super.get(paramInt);
        }
        return null;
    }

    public void fillUsedIds(BitSet usedIds) {
//        for (Integer id : structures.keySet())
//            if (id >= 0)
//                usedIds.set(id);
    }

    public Iterable<PairProxy<IParentTileList, LittleTile>> allTiles() {
        Iterator<IParentTileList> iterator = groups().iterator();
        return new Iterable<PairProxy<IParentTileList, LittleTile>>() {

            @Override
            public Iterator<PairProxy<IParentTileList, LittleTile>> iterator() {
                return new Iterator<PairProxy<IParentTileList, LittleTile>>() {

                    Iterator<LittleTile> inBlock = null;
                    PairProxy<IParentTileList, LittleTile> pair = null;

                    @Override
                    public boolean hasNext() {
                        while (inBlock == null || !inBlock.hasNext()) {
                            if (!iterator.hasNext())
                                return false;
                            IParentTileList list = iterator.next();
                            pair = new PairProxy<>(list, null);
                            inBlock = (Iterator<LittleTile>) list.iterator();
                        }
                        return true;
                    }

                    @Override
                    public PairProxy<IParentTileList, LittleTile> next() {
                        pair.setValue(inBlock.next());
                        return pair;
                    }
                };
            }
        };
    }


    public Iterable<IParentTileList> groups() {
        return new Iterable<IParentTileList>() {

            @Override
            public Iterator<IParentTileList> iterator() {
                return new Iterator<IParentTileList>() {

                    IParentTileList current = TileList.this;
//                    Iterator<IStructureTileList> children = structures().iterator();

                    @Override
                    public boolean hasNext() {
                        if (current != null)
                            return true;
//                        if (!children.hasNext())
//                            return false;
                        current = null; //children.next();
                        return false;
                    }

                    @Override
                    public IParentTileList next() {
                        IParentTileList result = current;
                        current = null;
                        return result;
                    }
                };
            }
        };
    }


    public boolean isCompletelyEmpty() {
        return super.isEmpty() /*&& structures.isEmpty()*/;
    }

//    @Override
    public LittleTile first() {
        return isEmpty() ? null : (LittleTile) super.get(0);
    }

//    @Override
    public int totalSize() {
        int size = size();
//        for (StructureTileList list : structures.values())
//            size += list.totalSize();
        return size;
    }


    public void clearEverything() {
        super.clear();
//        clearStructures();
    }

    @Override
    public LittleTile last() {
        return isEmpty() ? null : super.get(size() - 1);
    }

    @Override
    protected void readExtra(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("children", 10);
//        HashMap<Integer, StructureTileList> previous = new HashMap<>(structures);
//        structures.clear();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound childNBT = list.getCompoundTagAt(i);
//            StructureTileList child = previous.remove(childNBT.getInteger("index"));
//            if(child == null)
//                child = new StructureTileList(this, childNBT);
//            else
//                child.read(childNBT);
//            structures.put(child.getIndex(), child);
        }
//        for (StructureTileList child : previous.values())
//            child.unload();
//        reloadAttributes();
    }


    @Override
    protected void writeExtra(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
//        for (StructureTileList child : structures.values())
//            list.appendTag(child.write());
        nbt.setTag("children", list);
    }

    @Override

    public TileEntityLittleTilesProxy getTe() {
        return te;
    }

    @Override
    public boolean isStructure() {
        return false;
    }

    @Override
    public boolean isMain() {
        return false;
    }

    @Override
    public boolean isStructureChild(LittleStructure structure) throws CorruptedConnectionException, NotYetConnectedException {
        return false;
    }


//    public StructureTileList getStructure(int index) {
//        return structures.get(index);
//    }

    @Override
    public int getAttribute() {
        return 0;
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public void setAttribute(int attribute) {}
}
