package com.creativemd.littletiles;

import com.creativemd.littletiles.client.interact.LittleInteractionHandlerClient;
import com.creativemd.littletiles.client.render.PreviewRenderer;
import com.creativemd.littletiles.common.action.block.LittleActionPlaceStack;
import com.creativemd.littletiles.common.event.LittleEventHandler;
import com.creativemd.littletiles.common.items.*;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.registry.LittleTileRegistry;
import com.creativemd.littletiles.common.utils.*;
import com.creativemd.littletiles.server.interact.LittleInteractionHandlerServer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.common.blocks.BlockLTColored;
import com.creativemd.littletiles.common.blocks.BlockTile;
import com.creativemd.littletiles.common.blocks.ItemBlockColored;
import com.creativemd.littletiles.common.events.LittleEvent;
import com.creativemd.littletiles.common.packet.LittleBlockPacket;
import com.creativemd.littletiles.common.packet.LittleFlipPacket;
import com.creativemd.littletiles.common.packet.LittlePlacePacket;
import com.creativemd.littletiles.common.packet.LittleRotatePacket;
import com.creativemd.littletiles.common.sorting.LittleTileSortingList;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesProxy;
import com.creativemd.littletiles.common.utils.LittleTile;
import com.creativemd.littletiles.server.LittleTilesServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = LittleTiles.modid, version = LittleTiles.version, name = "LittleTiles")
public class LittleTiles {

    private LittleBox box;

    @Instance(LittleTiles.modid)
    public static LittleTiles instance = new LittleTiles();

    @SidedProxy(
            clientSide = "com.creativemd.littletiles.client.LittleTilesClient",
            serverSide = "com.creativemd.littletiles.server.LittleTilesServer")
    public static LittleTilesServer proxy;

    public static final String modid = "littletiles";
    public static final String version = "GRADLETOKEN_VERSION";

    public static int maxNewTiles = 512;

    public static BlockTile blockTile = new BlockTile(Material.rock);
    public static Block coloredBlock = new BlockLTColored().setBlockName("LTBlocks");

    public static Item hammer = new ItemHammer().setUnlocalizedName("LTHammer");
    public static Item recipe = new ItemRecipe().setUnlocalizedName("LTRecipe");
    public static Item multiTiles = new ItemMultiTiles().setUnlocalizedName("LTMultiTiles");
    public static Item saw = new ItemLittleSaw().setUnlocalizedName("LTSaw");
    public static Item container = new ItemTileContainer().setUnlocalizedName("LTContainer");
    public static Item wrench = new ItemLittleWrench().setUnlocalizedName("LTWrench");
    public static Item chisel = new ItemLittleChisel().setUnlocalizedName("LTChisel");
    public static Item colorTube = new ItemColorTube().setUnlocalizedName("LTColorTube");
    public static Item rubberMallet = new ItemRubberMallet().setUnlocalizedName("LTRubberMallet");
    public static Item grabber = new ItemLittleGrabber().setUnlocalizedName("LTGrabber");

    public static boolean isAngelicaLoaded;

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
        LittleTileRegistry.initTiles();
        FMLCommonHandler.instance().bus().register(new PreviewRenderer());
        FMLCommonHandler.instance().bus().register(new LittleEventHandler());
        FMLCommonHandler.instance().bus().register(new LittleInteractionHandlerClient());
        FMLCommonHandler.instance().bus().register(new LittleInteractionHandlerServer());
        MinecraftForge.EVENT_BUS.register(new LittleEventHandler());

    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        ForgeModContainer.fullBoundingBoxLadders = true;

        // @TODO actually set up proper config loading
        LittleTilesConfig littleConfig = new LittleTilesConfig();
        littleConfig.core.configured();

        GameRegistry.registerItem(hammer, "hammer");
        GameRegistry.registerItem(recipe, "recipe");
        GameRegistry.registerItem(saw, "saw");
        GameRegistry.registerItem(container, "container");
        GameRegistry.registerItem(wrench, "wrench");
        GameRegistry.registerItem(chisel, "chisel");
        GameRegistry.registerItem(colorTube, "colorTube");
        GameRegistry.registerItem(rubberMallet, "rubberMallet");
        GameRegistry.registerItem(grabber, "grabber");

//        GameRegistry.registerBlock(coloredBlock, "LTColoredBlock");
        GameRegistry.registerBlock(blockTile, ItemBlockColored.class, "LTColoredBlock");
        GameRegistry.registerBlock(coloredBlock, ItemBlockTiles.class, "BlockLittleTiles");

        GameRegistry.registerItem(multiTiles, "multiTiles");

        GameRegistry.registerTileEntity(TileEntityLittleTilesProxy.class, "LittleTilesTileEntity");

        proxy.loadSide();

        LittleTile.registerLittleTile(LittleTileBlock.class, "BlockTileBlock");
        // LittleTile.registerLittleTile(LittleTileStructureBlock.class, "BlockTileStructure");
        LittleTile.registerLittleTile(LittleTileTileEntity.class, "BlockTileEntity");
        LittleTile.registerLittleTile(LittleTileColored.class, "BlockTileColored");

        CreativeCorePacket.registerPacket(LittlePlacePacket.class, "LittlePlace");
        CreativeCorePacket.registerPacket(LittleActionPlaceStack.class, "LittlePlaceStack");
        CreativeCorePacket.registerPacket(LittleBlockPacket.class, "LittleBlock");
        CreativeCorePacket.registerPacket(LittleRotatePacket.class, "LittleRotate");
        CreativeCorePacket.registerPacket(LittleFlipPacket.class, "LittleFlip");
        FMLCommonHandler.instance().bus().register(new LittleEvent());
        MinecraftForge.EVENT_BUS.register(new LittleEvent());

        LittleStructure.initStructures();

        proxy.loadSidePost();

        // Recipes
        GameRegistry.addRecipe(
                new ItemStack(hammer),
                new Object[] { "XXX", "ALA", "ALA", 'X', Items.iron_ingot, 'L', new ItemStack(Items.dye, 1, 4) });

        GameRegistry.addRecipe(
                new ItemStack(container),
                new Object[] { "XXX", "XHX", "XXX", 'X', Items.iron_ingot, 'H', hammer });

        GameRegistry.addRecipe(
                new ItemStack(saw),
                new Object[] { "AXA", "AXA", "ALA", 'X', Items.iron_ingot, 'L', new ItemStack(Items.dye, 1, 4) });

        GameRegistry.addRecipe(
                new ItemStack(wrench),
                new Object[] { "AXA", "ALA", "ALA", 'X', Items.iron_ingot, 'L', new ItemStack(Items.dye, 1, 4) });

        GameRegistry.addRecipe(
                new ItemStack(rubberMallet),
                new Object[] { "XXX", "XLX", "ALA", 'X', Blocks.wool, 'L', new ItemStack(Items.dye, 1, 4) });

        GameRegistry.addRecipe(
                new ItemStack(colorTube),
                new Object[] { "XXX", "XLX", "XXX", 'X', Items.dye, 'L', Items.iron_ingot });

        isAngelicaLoaded = Loader.isModLoaded("angelica");
    }

    public boolean canBeConvertedToVanilla() {
        if (!box.isSolid())
            return false;
//        if (hasSpecialBlockHandler())
//            return handler.canBeConvertedToVanilla(this);
        return true;
    }

    @EventHandler
    public void LoadComplete(FMLLoadCompleteEvent event) {
        LittleTileSortingList.initVanillaBlocks();
    }
}
