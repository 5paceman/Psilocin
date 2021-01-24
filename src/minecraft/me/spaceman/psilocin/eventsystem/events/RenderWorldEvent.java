package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;

public class RenderWorldEvent extends Event{

    private float partialTicks;
    private EntityRenderer entityRenderer;

    public RenderWorldEvent(float partialTicks, EntityRenderer entityRenderer) {
        super(RENDER_WORLD_EVENT);
        this.partialTicks = partialTicks;
        this.entityRenderer = entityRenderer;
    }

    public EntityRenderer getEntityRenderer()
    {
        return entityRenderer;
    }

    public float getPartialTicks()
    {
        return this.partialTicks;
    }
}
