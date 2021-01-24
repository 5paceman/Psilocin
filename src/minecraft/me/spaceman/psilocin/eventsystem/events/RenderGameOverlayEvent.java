package me.spaceman.psilocin.eventsystem.events;

import me.spaceman.psilocin.eventsystem.EventHandler;
import net.minecraft.client.gui.FontRenderer;

public class RenderGameOverlayEvent extends Event {

    private float partialTicks;
    public FontRenderer fontRenderer;
    // PsyGuiIngame#renderGameOverlay
    public RenderGameOverlayEvent(float partialTicks, FontRenderer fontRenderer) {
        super(RENDER_GAME_OVERLAY_EVENT);
        this.partialTicks = partialTicks;
        this.fontRenderer = fontRenderer;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

}
