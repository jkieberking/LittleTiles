package com.creativemd.littletiles.common.event;

import com.creativemd.littletiles.common.action.LittleAction;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

public class ActionEvent extends Event {

    public final LittleAction action;

    public final ActionType type;

    public final EntityPlayer player;

    public ActionEvent(LittleAction action, ActionType type, EntityPlayer player) {
        this.action = action;
        this.type = type;
        this.player = player;
    }

    public static enum ActionType {
        normal,
        undo,
        redo;
    }
}
