package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class RenderNametagsEvent extends Event{

    private double x, y, z;
    private EntityLivingBase entity;
    // RendererLivingEntity#renderName
    public RenderNametagsEvent(EntityLivingBase entity, double x, double y, double z) {
        super(RENDER_NAMETAGS);
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.isCancellable = true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }
}
