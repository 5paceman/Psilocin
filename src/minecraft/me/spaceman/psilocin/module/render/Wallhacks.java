package me.spaceman.psilocin.module.render;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.RenderDroppedItemEvent;
import me.spaceman.psilocin.eventsystem.events.RenderPlayerEvent;
import me.spaceman.psilocin.module.Module;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Wallhacks extends Module {

    public Wallhacks() {
        super("Wallhacks", Keyboard.KEY_N, Category.RENDER, 0xFFD7E800);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
    }

    @EventSubscriber
    public void onPostRenderItem(final RenderDroppedItemEvent.Post event)
    {
        if(!this.isEnabled())
            return;

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1, 1000000F);
    }

    @EventSubscriber
    public void onPreRenderItem(final RenderDroppedItemEvent.Pre event)
    {
        if(!this.isEnabled())
            return;

        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1, -1000000F);
    }

    @EventSubscriber
    public void onPreRenderPlayer(final RenderPlayerEvent.Pre event)
    {
        if(!this.isEnabled())
            return;
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1, -1000000F);
    }

    @EventSubscriber
    public void onPostRenderPlayer(final RenderPlayerEvent.Post event)
    {
        if(!this.isEnabled())
            return;
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(1, 1000000F);
    }
}
