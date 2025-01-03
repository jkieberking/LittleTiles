package com.creativemd.littletiles.common.utils.compression;

import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.tile.registry.LittleTileRegistry;
import com.creativemd.littletiles.common.type.HashMapListProxy;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LittleNBTCompressionTools {

    public static NBTTagList writeTiles(List<LittleTile> tiles) {
        HashMapListProxy<Class<? extends LittleTile>, LittleTile> groups = new HashMapListProxy<>();

        for (LittleTile tile : tiles)
            groups.add(tile.getClass(), tile);

        NBTTagList list = new NBTTagList();

        for (Iterator<ArrayList<LittleTile>> iterator = groups.values().iterator(); iterator.hasNext();) {
            ArrayList<LittleTile> classList = iterator.next();

            while (classList.size() > 0) {
                LittleTile grouping = classList.remove(0);
                NBTTagCompound groupNBT = null;

                for (Iterator iterator2 = classList.iterator(); iterator2.hasNext();) {
                    LittleTile littleTile = (LittleTile) iterator2.next();
                    if (grouping.canBeNBTGrouped(littleTile)) {
                        if (groupNBT == null)
                            groupNBT = grouping.startNBTGrouping();
                        grouping.groupNBTTile(groupNBT, littleTile);
                        iterator2.remove();
                    }
                }

                if (groupNBT == null) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    grouping.saveTile(nbt);
                    list.appendTag(nbt);
                } else
                    list.appendTag(groupNBT);
            }

        }

        return list;
    }

    public static List<LittleTile> readTiles(NBTTagList list) {
        ArrayList<LittleTile> tiles = new ArrayList<>();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            if (nbt.hasKey("boxes")) {
                LittleTile create = LittleTileRegistry.getTypeFromNBT(nbt).createTile();
                if (create != null) {
                    List<NBTTagCompound> nbts = create.extractNBTFromGroup(nbt);
                    for (int j = 0; j < nbts.size(); j++) {
                        LittleTile tile = LittleTileRegistry.loadTile(nbts.get(j));
                        if (tile != null)
                            tiles.add(tile);
                    }
                }
            } else {
                LittleTile tile = LittleTileRegistry.loadTile(nbt);
                if (tile != null)
                    tiles.add(tile);
            }
        }
        return tiles;
    }

    public static NBTTagList writePreviews(LittlePreviews previews) {
        HashMapListProxy<String, LittlePreview> groups = new HashMapListProxy<>();

        for (Iterator iterator = previews.iterator(); iterator.hasNext();) {
            LittlePreview preview = (LittlePreview) iterator.next();
            groups.add(preview.getTypeIdToSave(), preview);
        }

        NBTTagList list = new NBTTagList();

        for (Iterator<ArrayList<LittlePreview>> iterator = groups.values().iterator(); iterator.hasNext();) {
            ArrayList<LittlePreview> classList = iterator.next();

            while (classList.size() > 0) {
                LittlePreview grouping = classList.remove(0);
                NBTTagCompound groupNBT = null;

                for (Iterator iterator2 = classList.iterator(); iterator2.hasNext();) {
                    LittlePreview preview = (LittlePreview) iterator2.next();
                    if (grouping.canBeNBTGrouped(preview)) {
                        if (groupNBT == null)
                            groupNBT = grouping.startNBTGrouping();
                        grouping.groupNBTTile(groupNBT, preview);
                        iterator2.remove();
                    }
                }

                if (groupNBT == null) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    grouping.writeToNBT(nbt);
                    list.appendTag(nbt);
                } else
                    list.appendTag(groupNBT);
            }
        }

        return list;
    }

    public abstract static class PreviewCompressionHandler {

        public abstract List<NBTTagCompound> extractNBTFromGroup(NBTTagCompound nbt);

    }

    public static void registerPreviewCompressionHandler(String id, PreviewCompressionHandler handler) {
        if (id.equals(""))
            defaultHandler = handler;
        handlers.put(id, handler);
    }

    private static PreviewCompressionHandler defaultHandler = null;
    private static HashMap<String, PreviewCompressionHandler> handlers = new HashMap<>();

    public static PreviewCompressionHandler ordinaryPreviewHandler = new PreviewCompressionHandler() {

        @Override
        public List<NBTTagCompound> extractNBTFromGroup(NBTTagCompound nbt) {
            List<NBTTagCompound> tags = new ArrayList<>();
            NBTTagList list = nbt.getTagList("boxes", 11);
            NBTTagCompound copy = new NBTTagCompound();
            // function func_150296_c() = NBTTagCompound.getKeySet
            for (Iterator<String> iterator = nbt.func_150296_c().iterator(); iterator.hasNext();) {
                String key = iterator.next();
                if (!key.equals("boxes") && !key.equals("group"))
                    copy.setTag(key, nbt.getTag(key).copy());
            }

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound second = (NBTTagCompound) copy.copy();
                // func_150306_c = getIntArrayAt()
                second.setIntArray("bBox", list.func_150306_c(i));
                tags.add(second);
            }
            return tags;
        }
    };

    static {
        registerPreviewCompressionHandler("", ordinaryPreviewHandler);
    }

    public static LittlePreviews readPreviews(LittlePreviews previews, NBTTagList list) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            if (nbt.hasKey("boxes")) {
                PreviewCompressionHandler handler = handlers.get(nbt.getString("tID"));
                if (handler == null)
                    handler = defaultHandler;

                List<NBTTagCompound> nbts = handler.extractNBTFromGroup(nbt);
                for (int j = 0; j < nbts.size(); j++) {
                    LittlePreview preview = LittleTileRegistry.loadPreview(nbts.get(j));
                    if (preview != null)
                        previews.addWithoutCheckingPreview(preview);
                }
            } else {
                LittlePreview preview = LittleTileRegistry.loadPreview(nbt);
                if (preview != null)
                    previews.addWithoutCheckingPreview(preview);
            }
        }
        return previews;
    }

    public static LittlePreviews readPreviews(LittleGridContext context, NBTTagList list) {
        return readPreviews(new LittlePreviews(context), list);
    }

}
