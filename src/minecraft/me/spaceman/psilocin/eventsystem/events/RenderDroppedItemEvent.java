package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.entity.item.EntityItem;

public class RenderDroppedItemEvent extends Event{

    private double x, y, z;

    private float entityYaw, partialTicks;
    private EntityItem item;

    protected RenderDroppedItemEvent(EntityItem item, double x, double y, double z, float entityYaw, float partialTicks) {
        super(RENDER_DROPPED_ITEM);
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
        this.item = item;
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

    public float getEntityYaw() {
        return entityYaw;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public EntityItem getItem() {
        return item;
    }
    // RenderEntityItem#doRender
    public static class Pre extends RenderDroppedItemEvent {

        public Pre(EntityItem item, double x, double y, double z, float entityYaw, float partialTicks) {
            super(item, x, y, z, entityYaw, partialTicks);
        }
    }
    // RenderEntityItem#doRender
    public static class Post extends RenderDroppedItemEvent {

        public Post(EntityItem item, double x, double y, double z, float entityYaw, float partialTicks) {
            super(item, x, y, z, entityYaw, partialTicks);
        }
    }
}

