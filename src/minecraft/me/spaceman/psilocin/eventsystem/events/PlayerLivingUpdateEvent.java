package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.client.entity.EntityPlayerSP;

public class PlayerLivingUpdateEvent extends Event{

    private EntityPlayerSP thePlayer;

    // EntityPlayerSP#onLivingUpdate()
    public PlayerLivingUpdateEvent(EntityPlayerSP thePlayer) {
        super(PLAYER_LIVING_UPDATE);
        this.thePlayer = thePlayer;
    }

    public EntityPlayerSP getPlayer()
    {
        return this.thePlayer;
    }
}
