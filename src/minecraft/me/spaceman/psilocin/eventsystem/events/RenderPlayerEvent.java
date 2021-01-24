package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.entity.player.EntityPlayer;

public class RenderPlayerEvent extends Event{

    float lastPartialTicks, entityYaw;
    double x, y, z;
    EntityPlayer player;
    protected RenderPlayerEvent(float lastPartialTicks, EntityPlayer player, double x, double y, double z, float entityYaw) {
        super(RENDER_PLAYER_EVENT);
        this.entityYaw = entityYaw;
        this.lastPartialTicks = lastPartialTicks;
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
    }

    public static class Pre extends RenderPlayerEvent{
        // RenderPlayer#doRender
        public Pre(float lastPartialTicks, EntityPlayer player, double x, double y, double z, float entityYaw) {
            super(lastPartialTicks, player, x, y, z, entityYaw);
        }
    }

    public static class Post extends RenderPlayerEvent {
        // RenderPlayer#doRender
        public Post(float lastPartialTicks, EntityPlayer player, double x, double y, double z, float entityYaw) {
            super(lastPartialTicks, player, x, y, z, entityYaw);
        }
    }
}
