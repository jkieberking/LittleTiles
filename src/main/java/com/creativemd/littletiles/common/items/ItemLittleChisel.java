package com.creativemd.littletiles.common.items;

import java.util.List;

import com.cleanroommc.modularui.factory.ClientGUI;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.creativemd.littletiles.common.gui.GuiChisel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.gui.IGuiCreator;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.core.CreativeCore;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.gui.SubContainerChisel;
import com.creativemd.littletiles.common.gui.SubGuiChisel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemLittleChisel extends Item {
    private static final Logger log = LogManager.getLogger(ItemLittleChisel.class);

    public ItemLittleChisel() {
        setCreativeTab(CreativeTabs.tabTools);
        hasSubtypes = true;
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getIconString() {
        return LittleTiles.modid + ":LTChisel";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();

        if (stack.stackTagCompound.hasKey("x1")) list.add(
                "1: x=" + stack.stackTagCompound.getInteger("x1")
                        + ",y="
                        + stack.stackTagCompound.getInteger("y1")
                        + ",z="
                        + stack.stackTagCompound.getInteger("z1"));
        else list.add("1: undefinded");

        if (stack.stackTagCompound.hasKey("x2")) list.add(
                "2: x=" + stack.stackTagCompound.getInteger("x2")
                        + ",y="
                        + stack.stackTagCompound.getInteger("y2")
                        + ",z="
                        + stack.stackTagCompound.getInteger("z2"));
        else list.add("2: undefinded");

        list.add("creative mode only");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            ClientGUI.open(new GuiChisel());
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();

        if (!world.isRemote) {
            if (player.isSneaking()) {
                stack.stackTagCompound.setInteger("x2", x);
                stack.stackTagCompound.setInteger("y2", y);
                stack.stackTagCompound.setInteger("z2", z);
                player.addChatMessage(new ChatComponentText("Second position: x=" + x + ",y=" + y + ",z=" + z));
            } else {
                stack.stackTagCompound.setInteger("x1", x);
                stack.stackTagCompound.setInteger("y1", y);
                stack.stackTagCompound.setInteger("z1", z);
                player.addChatMessage(
                        new ChatComponentText(
                                "First position: x=" + x + ",y=" + y + ",z=" + z + " sneak to set the second pos!"));
            }
        }
        return true;
    }
}
