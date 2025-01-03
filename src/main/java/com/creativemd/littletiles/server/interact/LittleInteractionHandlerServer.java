package com.creativemd.littletiles.server.interact;

import com.creativemd.littletiles.server.EnumActionResultProxy;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import com.creativemd.littletiles.common.action.interact.LittleInteraction;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;

public class LittleInteractionHandlerServer {

    public LittleInteractionHandlerServer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    protected HashMap<EntityPlayer, LittleInteraction> interactions = new HashMap<>();

    public void start(EntityPlayer player, int index, boolean rightclick) {
        interactions.put(player, new LittleInteraction(index, rightclick));
    }

    public void end(EntityPlayer player, int index) {
        LittleInteraction interaction = interactions.get(player);
        if (interaction != null && interaction.index <= index)
            interactions.remove(player);
    }

//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public void interact(PlayerInteractEvent event) {
//        if (!event.world.isRemote && interactions.containsKey(event.entityPlayer)) {
//            event.setCanceled(true);
//            event.setResult(EnumActionResultProxy.SUCCESS);
//        }
//    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        interactions.remove(event.player);
    }

}
