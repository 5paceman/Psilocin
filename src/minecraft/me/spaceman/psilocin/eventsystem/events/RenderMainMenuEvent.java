package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.client.gui.FontRenderer;

public class RenderMainMenuEvent extends RenderGameOverlayEvent{
    // GuiMainMenu#drawScreen
    public RenderMainMenuEvent(float partialTicks, FontRenderer fontRenderer) {
        super(partialTicks, fontRenderer);
    }
}
