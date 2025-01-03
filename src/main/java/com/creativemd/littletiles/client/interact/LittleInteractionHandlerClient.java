package com.creativemd.littletiles.client.interact;

import com.creativemd.creativecore.common.packet.PacketHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import com.creativemd.littletiles.common.action.interact.LittleInteraction;
import com.creativemd.littletiles.common.packet.LittleInteractionPacket;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;

public class LittleInteractionHandlerClient {

    private int index;
    private LittleInteraction interaction;

    public LittleInteractionHandlerClient() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean start(boolean rightclick) {
        if (this.interaction == null) {
            this.interaction = new LittleInteraction(index++, rightclick);
            PacketHandler.sendPacketToServer(new LittleInteractionPacket(interaction, true));
            return true;
        }
        return false;
    }

    public void finish() {
        PacketHandler.sendPacketToServer(new LittleInteractionPacket(interaction, false));
        interaction = null;
    }

    @SubscribeEvent
    public void clientTick(TickEvent.RenderTickEvent event) {
        if (interaction != null)
            finish();
    }
}
