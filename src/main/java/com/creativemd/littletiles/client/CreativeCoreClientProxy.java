package com.creativemd.littletiles.client;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
public class CreativeCoreClientProxy {

    public static Minecraft mc = Minecraft.getMinecraft();
    private static Field fieldTimer;
    private static Timer timer;

    public static float getRenderPartialTicks() {
        if(fieldTimer == null) {
            fieldTimer = ReflectionHelper.findField(Minecraft.class, new String[]{"timer", "field_71428_T", "Q"});

            try {
                Minecraft ex = Minecraft.getMinecraft();
                if(timer == null) {
                    timer = (Timer)fieldTimer.get(ex);
                }
            } catch (IllegalAccessException var1) {
                ;
            }
        }

        return timer != null?timer.renderPartialTicks:0.0F;
    }

//    public static void doClientThings() {
//        if (CreativeCore.config.useStencil && !mc.getFramebuffer().isStencilEnabled())
//            mc.getFramebuffer().enableStencil();
//
//        ModelLoaderRegistry.registerLoader(new CreativeCustomModelLoader());
//    }
//
//    public static void doClientThingsLate() {
//        CreativeBakedModel.lateInit();
//    }
//
//    public static ItemMeshDefinition mesh = new ItemMeshDefinition() {
//
//        @Override
//        public ModelResourceLocation getModelLocation(ItemStack stack) {
//            return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory");
//        }
//    };
//
//    public static void registerBlockItem(Block toRegister) {
//        Item item = Item.getItemFromBlock(toRegister);
//        ModelLoader.setCustomMeshDefinition(item, mesh);
//        // Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,
//        // mesh);
//    }
//
//    public static void registerItemRenderer(Item item) {
//        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
//        // Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,
//        // 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
//    }
//
//    public static IItemColor itemColor = new IItemColor() {
//
//        @Override
//        public int colorMultiplier(ItemStack stack, int tintIndex) {
//            return tintIndex;
//        }
//    };
//
//    public static void registerBlockModels(Block block, String modID, String prefix, Enum<? extends IStringSerializable>[] enumtype) {
//        ResourceLocation[] locations = new ResourceLocation[enumtype.length];
//        Item item = Item.getItemFromBlock(block);
//        for (int i = 0; i < enumtype.length; i++) {
//
//            ResourceLocation location = new ResourceLocation(modID + ":" + prefix + ((IStringSerializable) enumtype[i]).getName());
//            locations[i] = location;
//            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(location, "inventory"));
//            // mc.getRenderItem().getItemModelMesher().register(item, i, new
//            // ModelResourceLocation(location, "inventory"));
//        }
//
//        ModelBakery.registerItemVariants(item, locations);
//    }
//
//    public static IBlockColor blockColor = new IBlockColor() {
//
//        @Override
//        public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
//            CreativeBakedQuad quad = CreativeBakedQuad.lastRenderedQuad.get();
//            if (quad != null && quad.cube != null && quad.cube.block != null && quad.cube.block.getBlockLayer() == BlockRenderLayer.CUTOUT_MIPPED) {
//                IBlockState newState = quad.cube.getBlockState(quad.cube.block);
//                return mc.getBlockColors().colorMultiplier(newState, worldIn, pos, tintIndex);
//                // return ColorUtils.WHITE;
//            }
//
//            return tintIndex;
//        }
//    };
//
//    public static void registerItemColorHandler(Item item) {
//        mc.getItemColors().registerItemColorHandler(itemColor, item);
//    }
//
//    public static void registerBlockColorHandler(Block block) {
//        mc.getItemColors().registerItemColorHandler(itemColor, block);
//        // mc.getBlockColors().registerBlockColorHandler(blockColor, block);
//    }
}
